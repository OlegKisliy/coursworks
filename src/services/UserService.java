package services;

import model.UserModel;

public interface UserService {

    UserModel login(UserModel userModel);
    UserModel register(UserModel userModel);
    boolean isFree(String login);
}
