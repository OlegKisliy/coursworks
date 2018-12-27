package server;

import model.HotelRoomModel;
import model.OperationStatus;
import model.UserModel;
import services.impl.RoomServiceImpl;
import services.impl.UserServiceImpl;
import services.router.Router;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;
//Сервер який підтримує велику кіллькість одночасних підключень
public class SocketServer implements Runnable {

    public static final int PORT_NUMBER = 8081;
    //We store information about the user's session
    private UserModel currentUser;
    protected Socket socket;
    //Variable for data exchange on numbers for each socket
    private ArrayList<HotelRoomModel> freeRooms = new ArrayList<>();

    public void setFreeRooms(ArrayList<HotelRoomModel> freeRooms) {
        this.freeRooms = freeRooms;
    }
    //Constructor
    private SocketServer(Socket socket) {
        this.socket = socket;
        System.out.println("New connection from address - " + socket.getInetAddress().getHostAddress());
        run();
    }

    public void setCurrentUser(UserModel currentUser) {
        this.currentUser = currentUser;
    }

    public UserModel getCurrentUser() {
        return currentUser;
    }
    //Run the main stream of the session
    public void run() {
        InputStream in = null;
        OutputStream out = null;
        try {
            in = socket.getInputStream();
            out = socket.getOutputStream();
            ObjectInputStream ois = new ObjectInputStream(in);
            ObjectOutputStream oos = new ObjectOutputStream(out);
            Map<String, Object> request;
            while ((request = (Map<String, Object>) ois.readObject()) != null) {
                System.out.println(request);
                System.out.println("Message Sent. Method :" + request.keySet().stream().findFirst().get());
                //We call the routing method by sending data about our connection to it
                OperationStatus operationStatus = Router.getInstance().route(request, this);
                System.out.println(operationStatus.getString());
                switch (operationStatus) {
                    case REGISTER_SUCESS: {
                        oos.writeObject(currentUser);
                        UserServiceImpl.saveChanges();
                    }
                        break;
                    case CHECKIN_SUCESS:
                        oos.writeObject(operationStatus.getString());
                        UserServiceImpl.saveChanges();
                        RoomServiceImpl.saveChanges();
                        break;
                    case GETFREE_SUCESS: {
                        oos.writeObject(freeRooms);
                    }
                    break;
                    case LOGIN_SUCESS: {
                        oos.writeObject(currentUser);
                    }break;
                    case LOGIN_FAILURE: {
                        oos.writeObject(operationStatus.getString());
                    }break;
                        default:{
                            oos.writeObject(operationStatus.getString());
                        } break;
                }

            }
        } catch (IOException ex) {
            System.out.println("Unable to retrieve streaming data!");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                out.close();
                socket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("Booking server at the hotel 'Ukraine'");
        ServerSocket server = null;
        try {
            server = new ServerSocket(PORT_NUMBER);
            while (true) {
                /**
                 * creates {@link SocketServer} object for each connection
                 * (is done so that several connections can be made at once)
                 */
                new SocketServer(server.accept());
            }
        } catch (IOException ex) {
            System.out.println("Something went wrong! Error starting the server!");
        } finally {
            try {
                if (server != null)
                    server.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}