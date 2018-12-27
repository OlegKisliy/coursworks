package model;
// Енам який вказує статус операції
public enum OperationStatus {
    REGISTER_SUCESS("Successfully registered!"),REGISTER_FAILURE("Registration failed!"), LOGIN_SUCESS("The entry is complete"), LOGIN_FAILURE("Login failed, invalid password, or such user does not exist"),  CHECKIN_SUCESS("You have successfully booked a place"), CHECKIN_FAILURE("Room is already booked, choose the correct option"),
    CHECKIN_FAILURE_END_OF_ROOMS("Rooms are already booked, unfortunately there are no vacancies"),    CHECKOUT_SUCESS("You have successfully left the room"),CHECKOUT_FAILURE("Discharge error"), GETFREE_SUCESS("Successfully."),  LOGOUT_SUCESS("Successfully."),ERROR("Error, something went wrong");
    private String string;

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    OperationStatus(String string) {

        this.string = string;
    }
}
