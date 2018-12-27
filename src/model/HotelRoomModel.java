package model;

import java.io.Serializable;
import java.util.Objects;
// Model room in the hotel
public class HotelRoomModel implements Serializable {


    private int roomNumber;
    private int guestsNumber;
    private boolean isCharged;

    @Override
    public String toString() {

        return "\n------------------------\n"+ "Room №" + roomNumber + "\n"+
                "Number of guests - " + guestsNumber +"\n------------------------\n";

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HotelRoomModel that = (HotelRoomModel) o;
        return roomNumber == that.roomNumber &&
                guestsNumber == that.guestsNumber &&
                isCharged == that.isCharged;
    }

    @Override
    public int hashCode() {

        return Objects.hash(roomNumber, guestsNumber, isCharged);
    }

    public HotelRoomModel(int roomNumber, int guestsNumber, boolean isCharged) {

        this.roomNumber = roomNumber;
        this.guestsNumber = guestsNumber;
        this.isCharged = isCharged;
    }

    public int getRoomNumber() {

        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public int getGuestsNumber() {
        return guestsNumber;
    }

    public void setGuestsNumber(int guestsNumber) {
        this.guestsNumber = guestsNumber;
    }

    public boolean isCharged() {
        return isCharged;
    }

    public void setCharged(boolean charged) {
        isCharged = charged;
    }
}
