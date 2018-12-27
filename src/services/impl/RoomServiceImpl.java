package services.impl;

import model.HotelRoomModel;
import model.UserModel;
import services.RoomService;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
//Implementation of the interface for working with hotel rooms
public class RoomServiceImpl implements RoomService {
    // Create singleton to provide multithreading
    private static RoomServiceImpl roomService = new RoomServiceImpl();

    public static RoomServiceImpl getInstance() {
        return roomService;
    }
    // Base of all rooms
    private static volatile ArrayList<HotelRoomModel> numbers = new ArrayList<>();
    // Base of all users who took pictures
    private static volatile HashMap<UserModel, ArrayList<HotelRoomModel>> reservedNumbers = new HashMap<>();

    public HashMap<UserModel, ArrayList<HotelRoomModel>> getReservedNumbers() {
        return reservedNumbers;
    }
    //The static block of initialization, in the absence of files, creates them, and stores the corresponding files
    static {
        try {
            ObjectInputStream is = new ObjectInputStream(new FileInputStream(new File("hotel.db")));
            ObjectInputStream is2 = new ObjectInputStream(new FileInputStream(new File("hotel_free.db")));
            reservedNumbers = (HashMap<UserModel, ArrayList<HotelRoomModel>>) is.readObject();
            numbers = (ArrayList<HotelRoomModel>) is2.readObject();
            is.close();
            is2.close();
        } catch (ClassNotFoundException | IOException e) {
            ArrayList numberList = new ArrayList<>(Arrays.asList(new HotelRoomModel[]{new HotelRoomModel(1, 2, true)}));
            reservedNumbers.put(new UserModel("Oleg", "Vasiliy", "+972532856626", "24121996")
                    , numberList);
            ArrayList numberListForSecondReservation = new ArrayList<>(Arrays.asList(new HotelRoomModel[]{new HotelRoomModel(2, 1, true)}));
            reservedNumbers.put(new UserModel("Elad", "Lavi", "+972532856626", "0123456789")
                    , numberListForSecondReservation);
            numbers.addAll(numberList);
            numbers.addAll(numberListForSecondReservation);
            numbers.addAll(Arrays.asList(new HotelRoomModel(3, 3, false), new HotelRoomModel(4, 1, false)));
            try {
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("hotel.db")));
                ObjectOutputStream oos2 = new ObjectOutputStream(new FileOutputStream(new File("hotel_free.db")));
                oos.writeObject(reservedNumbers);
                oos2.writeObject(numbers);
                oos.close();
                oos2.close();
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
                e.printStackTrace();
            }
        }
    }

    @Override
    public ArrayList<HotelRoomModel> getRooms() {
        return numbers;
    }

    @Override
    public boolean isCharged(int nomerNumber) {
        return numbers.stream().filter(i-> i.getRoomNumber()==nomerNumber).findFirst().orElse(new HotelRoomModel(0,1,true)).isCharged();
    }
    //метод зняття кімнати, який змінює стан кімнат, та резервацію.
    @Override
    public HotelRoomModel chargeHotelRoomByNumber(int nomerNumber, UserModel userModel) {
        System.out.println(nomerNumber + " " + userModel);
        if(isCharged(nomerNumber)){
            System.out.println("Number is already occupied, try the other one");
            return null;
        } else {
            HotelRoomModel number = numbers.stream().filter(i-> i.getRoomNumber()==nomerNumber).findFirst().get();
            number.setCharged(true);
            ArrayList<HotelRoomModel> rooms = reservedNumbers.get(userModel);
            if(rooms==null) {
                rooms = new ArrayList<>();
            }
            rooms.add(number);
            System.out.println(rooms);
            numbers.get(numbers.indexOf(number)).setCharged(true);
            reservedNumbers.put(userModel,rooms);
            System.out.println(reservedNumbers);
            saveChanges();
            return number;
        }
    }
    //saving changes
    public static void saveChanges() {

        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("hotel.db")));
            ObjectOutputStream oos2 = new ObjectOutputStream(new FileOutputStream(new File("hotel_free.db")));
            oos.writeObject(reservedNumbers);
            oos2.writeObject(numbers);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
