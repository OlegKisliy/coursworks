package services;

import model.HotelRoomModel;
import model.UserModel;

import java.util.ArrayList;

public interface RoomService {

    ArrayList<HotelRoomModel> getRooms();
    boolean isCharged(int nomerNumber);
    HotelRoomModel chargeHotelRoomByNumber (int nomerNumber, UserModel userModel);

}
