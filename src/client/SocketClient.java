package client;

import model.HotelRoomModel;
import model.UserModel;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class SocketClient {
    //The main socket for our connection
    private Socket echoSocket;
    //Output flow of the session
    private  ObjectOutputStream oos;
    //Incoming stream of the session
    private  ObjectInputStream ois;
    //Customer Designer
    public SocketClient(String host, int port) {
//We create a connection, in case of error we complete the work
        try {
            String serverHostname = new String("127.0.0.1");
            System.out.println("Connect with " + serverHostname + " at the port " + port + ".");
            echoSocket = null;
            PrintWriter out = null;
            BufferedReader in = null;
            try {
                echoSocket = new Socket(serverHostname, 8081);
                out = new PrintWriter(echoSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
                oos = new ObjectOutputStream(echoSocket.getOutputStream());
                ois = new ObjectInputStream(echoSocket.getInputStream());
            } catch (UnknownHostException e) {
                System.err.println("Unknown host: " + serverHostname);
                System.exit(1);
            } catch (IOException e) {
                System.err.println("Unable to receive message!");
                System.exit(1);
            }
            /** {@link UnknownHost} Read from the console */
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
            //until the user completes the job
            while (createSession()) {
            }
            /** Close all threads*/
            out.close();
            in.close();
            stdIn.close();
            echoSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //The method of login, accepts two parameters and launches the main job cycle, if the answer comes from the server UserModel
    private void login(String login, String password){
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        try {
            int choice;
            //send a request login
           oos.writeObject(createRequest("login", new UserModel(login,password)));

           Object obj =ois.readObject();

           SimpleDateFormat format = new SimpleDateFormat("hh:mm dd.MM.yyyy");
            if(obj instanceof UserModel){

                UserModel userModel = (UserModel) obj;
                System.out.println( "Welcome, " +userModel.getName() + " " + userModel.getSurname() + "! " + format.format(new Date()));
                System.out.println("Select the operation: ");
                System.out.println("1 -List of all available rooms\n2 -Book a room\n3 -My numbers\n4 -Exit");
                ArrayList<HotelRoomModel> freeRooms = new ArrayList<>();
                ArrayList<HotelRoomModel> yourRooms = new ArrayList<>();
                choice=Integer.parseInt(stdIn.readLine());
                while (choice!=4){
                    switch (choice){
                        case 1: {
                            oos.writeObject(createRequest("getfree", ""));
                            Object object = ois.readObject();
                            if (object instanceof ArrayList) {
                                freeRooms = (ArrayList<HotelRoomModel>) object;
                                freeRooms.forEach(System.out::println);
                            } else
                            {
                                System.out.println(object);
                            }

                            System.out.println("1 -List of all available rooms\n2 -Book a room\n3 -My numbers\n4 -Exit");
                            choice=Integer.parseInt(stdIn.readLine());
                        } break;
                        case 2:{
                            int roomChoice =0;
                            boolean flag =true;
                            while (flag){
                                System.out.println("Select a room, if you want to leave enter 0 :");
                                oos.writeObject(createRequest("getfree", ""));
                                freeRooms   = (ArrayList<HotelRoomModel>) ois.readObject();
                                freeRooms.forEach(System.out::println);
                                try {
                                    roomChoice = Integer.parseInt(stdIn.readLine());
                                    if (roomChoice==0){
                                        System.out.println("1 -List of all available rooms\n2 -Book a room\n3 -My numbers\n4 -Exit");
                                        choice=Integer.parseInt(stdIn.readLine());
                                        flag = false;
                                    }
                                    for (HotelRoomModel room: freeRooms) {
                                        if(room.getRoomNumber()==roomChoice){
                                            flag = false;
                                        }
                                    }
                                } catch (Exception e) {
                                    System.out.println("Correctly enter the room number, if you want to leave enter: 0");
                                }
                            }
                            if (roomChoice==0){
                                break;
                            }
                            oos.writeObject(createRequest("checkin",new Integer(roomChoice)));
                            String str = (String) ois.readObject();
                            System.out.println(str);
                            System.out.println("1 -List of all available rooms\n2 -Book a room\n3 -My numbers\n4 -Exit");
                            choice=Integer.parseInt(stdIn.readLine());

                        }  break;

                        case 3: {

                            oos.writeObject(createRequest("getyour", ""));
                            yourRooms = (ArrayList<HotelRoomModel>) ois.readObject();
                            yourRooms.forEach(System.out::println);
                            System.out.println("1 -List of all available rooms\n2 -Book a room\n3 -My numbers\n4 -Exit");
                            choice=Integer.parseInt(stdIn.readLine());

                        }break;

                            case 4:


                            return;

                        default:{
                            System.out.println("Select the operation: ");
                            System.out.println("1 -List of all available rooms\n2 -Book a room\n3 -My numbers\n4 -Exit");
                            choice = Integer.parseInt(stdIn.readLine());
                        } continue;
                    }
                }


            } else {
                System.out.println( obj.toString());
                createSession();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    //Create a session of the user until he decides to exit
    private boolean createSession() throws IOException {
        int choice;
        System.out.println("Select the operation: ");
        System.out.println("1 -Sign in\n2 -Registration\n3 -Finish work");
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        while (true){
            try {
                choice=Integer.parseInt(stdIn.readLine());
                break;
            }catch (Exception e){
                System.out.println("Invalid input, try again");
            }

        }

        while (choice!=3){
            switch (choice){
                case 1:{
                    System.out.println("Enter a login");
                    String log = stdIn.readLine();
                    System.out.println("Enter the password");
                    String pass= stdIn.readLine();
                    login(log,pass);
                } break;
                case 2:
                    System.out.println("Enter a login");
                    String log = stdIn.readLine();
                    System.out.println("Enter the password");
                    String pass= stdIn.readLine();
                    System.out.println("Enter a name");
                    String name = stdIn.readLine();
                    System.out.println("Enter middle name");
                    String surname= stdIn.readLine();
                    register(new UserModel(name,surname,log,pass));
                case 3:{

                } return false;
                default:{System.out.println("Select the operation:\n ");
                    System.out.println("1 -Sign in\n2 -Registration\n3 -Finish work"); choice=Integer.parseInt(stdIn.readLine());} continue;
            }
        }
        return false;
    }
    //The registration method takes 4 parameters and launches the main job cycle if the server arrives in response UserModel
    private void register(UserModel userModel) {
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        try {
            int choice;

            oos.writeObject(createRequest("register", userModel));
            Object obj =ois.readObject();
            SimpleDateFormat format = new SimpleDateFormat("hh:mm dd.MM.yyyy");
            if(obj instanceof UserModel){
                userModel = (UserModel) obj;
                System.out.println( "Welcome, " +userModel.getName() + " " + userModel.getSurname() + "! " + format.format(new Date()));
                System.out.println("Select the operation: ");
                System.out.println("1 -List of all available rooms\n2 -Book a room\n3 -My numbers\n4 -Exit");
                ArrayList<HotelRoomModel> freeRooms = new ArrayList<>();
                ArrayList<HotelRoomModel> yourRooms = new ArrayList<>();
                choice=Integer.parseInt(stdIn.readLine());
                while (choice!=4){
                    switch (choice){
                        case 1: {
                            oos.writeObject(createRequest("getfree", ""));
                            Object object = ois.readObject();
                            if (object instanceof ArrayList) {
                                freeRooms = (ArrayList<HotelRoomModel>) object;
                                freeRooms.forEach(System.out::println);
                            } else
                            {
                                System.out.println(object);
                            }

                            System.out.println("1 -List of all available rooms\n2 -Book a room\n3 -My numbers\n4 -Exit");
                            choice=Integer.parseInt(stdIn.readLine());
                        } break;
                        case 2:{
                            int roomChoice =0;
                            boolean flag =true;
                            while (flag){
                                System.out.println("Select a room, if you want to leave enter 0 :");
                                oos.writeObject(createRequest("getfree", ""));
                                freeRooms   = (ArrayList<HotelRoomModel>) ois.readObject();
                                freeRooms.forEach(System.out::println);
                                try {
                                    roomChoice = Integer.parseInt(stdIn.readLine());
                                    if (roomChoice==0){
                                        System.out.println("1 -List of all available rooms\n2 -Book a room\n3 -My number\n4 -Exit");
                                        choice=Integer.parseInt(stdIn.readLine());
                                        flag = false;
                                    }
                                    for (HotelRoomModel room: freeRooms) {
                                        if(room.getRoomNumber()==roomChoice){
                                            flag = false;
                                        }
                                    }
                                } catch (Exception e) {
                                    System.out.println("Correctly enter the room number, if you want to leave enter: 0");
                                }
                            }
                            if (roomChoice==0){
                                break;
                            }
                            oos.writeObject(createRequest("checkin",new Integer(roomChoice)));
                            String str = (String) ois.readObject();
                            System.out.println(str);
                            System.out.println("1 -List of all available rooms\n2 -Boo a room\n3 -My numbers\n4 -Exit");
                            choice=Integer.parseInt(stdIn.readLine());

                        }  break;

                        case 3: {

                            oos.writeObject(createRequest("getyour", ""));
                            yourRooms = (ArrayList<HotelRoomModel>) ois.readObject();
                            yourRooms.forEach(System.out::println);
                            System.out.println("1 -List of all available rooms\n2 -Book a room\n3 -My numbers\n4 -Exit");
                            choice=Integer.parseInt(stdIn.readLine());

                        }break;

                        case 4:


                            return;

                        default:{
                            System.out.println("Select the operation: ");
                            System.out.println("1 -List of all available rooms\n2 -Book a room\n3 -My numbers\n4 -Exit");
                            choice = Integer.parseInt(stdIn.readLine());
                        } continue;
                    }
                }


            } else {
                System.out.println( obj.toString());
                createSession();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    //generates basic requests received by the router on the server
    private HashMap<String,Object> createRequest(String name, Object object){
    HashMap<String,Object> map = new HashMap<>();
    map.put(name,object);
    return map;

    }
//    private void mainSession(Object obj){
//        if(obj instanceof UserModel){
//            UserModel userModel = (UserModel) obj;
//            System.out.println( "Welcome, " +userModel.getName() + " " + userModel.getSurname() + "! " + format.format(new Date()));
//            System.out.println("Select the operation: ");
//            System.out.println("1 -List of all available rooms\n2 -Book a number\n3 -My numbers\n4 -Exit");
//            oos.writeObject(createRequest("getfree", ""));
//            ArrayList<HotelRoomModel> freeRooms   = (ArrayList<HotelRoomModel>) ois.readObject();
//            oos.writeObject(createRequest("getyour", ""));
//            ArrayList<HotelRoomModel> yourRooms = (ArrayList<HotelRoomModel>) ois.readObject();
//            choice=Integer.parseInt(stdIn.readLine());
//            while (choice!=4){
//                switch (choice){
//                    case 1:{
//                        oos.writeObject(createRequest("getfree", ""));
//                        freeRooms   = (ArrayList<HotelRoomModel>) ois.readObject();
//                        freeRooms.forEach(System.out::println);
//                        System.out.println("1 -List of all available rooms\n2 -Book a number\n3 -My numbers\n4 -Exit");
//                        choice=Integer.parseInt(stdIn.readLine());
//                    } break;
//                    case 2:{
//                        int roomChoice;
//                        while (true){
//                            try {
//                                roomChoice = Integer.parseInt(stdIn.readLine());
//                                if (roomChoice==-1){
//                                    break;
//                                }
//                            } catch (Exception e){
//                                System.out.println("Correctly enter the room number, if you want to leave enter -1");
//                                continue;
//                            }
//                            System.out.println("Select a room, if you want to leave enter -1 :");
//                            oos.writeObject(createRequest("getfree", ""));
//                            freeRooms   = (ArrayList<HotelRoomModel>) ois.readObject();
//                            freeRooms.forEach(System.out::println);
//                            for (HotelRoomModel room: freeRooms) {
//                                if(room.getRoomNumber()==roomChoice){
//                                    break;
//                                }
//                            }
//                        }
//                        if (roomChoice==-1){
//                            break;
//                        }
//                        oos.writeObject(createRequest("checkin",new Integer(choice)));
//                        String str = (String) ois.readObject();
//                        System.out.println(str);
//                        System.out.println("1 -List of all available rooms\n2 -Book a number\n3 -My numbers\n4 -Exit");
//                        choice=Integer.parseInt(stdIn.readLine());
//
//                    }  break;
//
//                    case 3: {
//
//                        oos.writeObject(createRequest("getyour", ""));
//                        yourRooms = (ArrayList<HotelRoomModel>) ois.readObject();
//                        yourRooms.forEach(System.out::println);
//                        System.out.println("1 -List of all available rooms\n2 -Book a number\n3 -My numbers\n4 -Exit");
//                        choice=Integer.parseInt(stdIn.readLine());
//
//                    }break;
//
//                    case 4:
//
//
//                        return;
//
//                    default:{
//                        System.out.println("Select the operation: ");
//                        System.out.println("1 -List of all available rooms\n2 -Book a number\n3 -My numbers\n4 -Exit");
//                        choice = Integer.parseInt(stdIn.readLine());
//                    } continue;
//                }
//            }
//
//
//        } else {
//            System.out.println( obj.toString());
//            createSession();
//        }
//    }
}
