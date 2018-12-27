package services.router;

import model.HotelRoomModel;
import model.OperationStatus;
import model.UserModel;
import server.SocketServer;
import services.impl.RoomServiceImpl;
import services.impl.UserServiceImpl;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;
// Singleton who routinely works with the server, depending on the queries, certain actions are taken
public class Router {
    private static Router router = new Router();

    public static Router getInstance() {
        return router;
    }
//  method of routing the main actions
    public OperationStatus route(Map<String, Object> routeMethodAndBody, SocketServer socketServer){
        String method = routeMethodAndBody.keySet().stream().findFirst().get();
        switch (method){
//            registration
            case "register" :{
                Object obj = routeMethodAndBody.get(method);
                if (obj instanceof UserModel){
                   if(UserServiceImpl.getInstance().isFree(((UserModel) obj).getTelephoneNumber())){
                       socketServer.setCurrentUser(UserServiceImpl.getInstance().register((UserModel) obj));
                       return OperationStatus.REGISTER_SUCESS;
                   } else {
                       return OperationStatus.REGISTER_FAILURE;
                   }

                }
                return OperationStatus.REGISTER_FAILURE;
                }
//                login
            case "login" :{
                Object obj = routeMethodAndBody.get(method);
                if (obj instanceof UserModel){
                        socketServer.setCurrentUser(UserServiceImpl.getInstance().login((UserModel) obj));
                        if (socketServer.getCurrentUser()!=null){
                            return OperationStatus.LOGIN_SUCESS;}
                            else {
                            return OperationStatus.LOGIN_FAILURE;
                        }

                } else {
                    return OperationStatus.LOGIN_FAILURE;
                    }
            }
//            removing the number
            case "checkin" :{
                Object obj = routeMethodAndBody.get(method);
                System.out.println(obj);
                if (obj instanceof Integer) {
                    if (socketServer.getCurrentUser() != null) {
                        if (RoomServiceImpl.getInstance().chargeHotelRoomByNumber(((Integer) obj).intValue(),socketServer.getCurrentUser())==null){
                            return OperationStatus.CHECKIN_FAILURE;
                        }
                        return OperationStatus.CHECKIN_SUCESS;
                    } else {
                        return OperationStatus.CHECKIN_FAILURE;
                    }
                } return OperationStatus.CHECKIN_FAILURE;
            }
//            getting free rooms
            case "getfree" :{
                if (socketServer.getCurrentUser()!=null){
                    ArrayList<HotelRoomModel> rooms = new ArrayList<>(RoomServiceImpl.getInstance().getRooms().stream().filter(room -> !RoomServiceImpl.getInstance().isCharged(room.getRoomNumber())).collect(Collectors.toList()));

                    if (rooms.size()==0){
                        return OperationStatus.CHECKIN_FAILURE_END_OF_ROOMS;
                    }

                    socketServer.setFreeRooms(rooms);

                    return OperationStatus.GETFREE_SUCESS;
                }
                else {
                    return OperationStatus.ERROR;
                }
            }
//            receive rooms booked by the current user
            case "getyour" :{
                UserModel userModel =socketServer.getCurrentUser();
                if (socketServer.getCurrentUser()!=null){
                    socketServer.setFreeRooms(RoomServiceImpl.getInstance().getReservedNumbers().get(userModel));
                    return OperationStatus.GETFREE_SUCESS;
                }
                else {
                    return OperationStatus.LOGIN_FAILURE;
                }
            }
//            exit and clean up session
            case "logout" :{
                    socketServer.setCurrentUser(null);
                    socketServer.setFreeRooms(new ArrayList<>());
                    return OperationStatus.LOGOUT_SUCESS;
            }

            default: return OperationStatus.ERROR;
        }

    }
}
