package services.impl;

import model.UserModel;
import services.UserService;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
//Implementation of the service for work with user objects
public class UserServiceImpl implements UserService {
    private static volatile ArrayList<UserModel> users = new ArrayList<>();
    private  static UserServiceImpl userService = new UserServiceImpl();

    public static UserServiceImpl getInstance() {
        return userService;
    }
    //The static user initialisation block is similar to the hotel room service
    static {
        try {
            ObjectInputStream is = new ObjectInputStream(new FileInputStream(new File("users.db")));
            users = (ArrayList<UserModel>) is.readObject();
            is.close();
        } catch (ClassNotFoundException | IOException e) {
            try {
                users.addAll(Arrays.asList(new UserModel("Oleg", "Vasiliy", "+972532856626", "24121996"), new UserModel("Elad", "Lavi", "+972532856626", "0123456789")));
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("users.db")));
                oos.writeObject(users);
                oos.close();
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
                e.printStackTrace();
            }
        }
    }

    //A method that verifies whether the user data is correct and returns the result of the logon
    @Override
    public UserModel login(UserModel userModel) {
        if (users.stream().anyMatch(user -> user.equals(userModel))){
            return users.get(users.indexOf(userModel));
        }else {
            return null;
        }

    }
  //New user registration
    @Override
    public UserModel register(UserModel userModel) {
        if (login(userModel)==null){
            users.add(userModel);
            return userModel;
        }else {
            return null;
        }

    }
// Checks if there is a user with this login in the user list
    @Override
    public boolean isFree(String login) {
        return users.stream().noneMatch(user -> user.getTelephoneNumber().equals(login));
    }

    public  ArrayList<UserModel> getUsers() {
        return users;
    }

    public static void saveChanges() {

        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("users.db")));
            oos.writeObject(users);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
