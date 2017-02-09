package view;

import model.Hotel;

/**
 * Created by synerzip on 19/1/17.
 */
public class DisplayHotel {

    public static void display(Hotel hotel) {
        System.out.println("Hotel Type : " + hotel.getName() + "\nTotal Rate : " + hotel.getTotalRate());
    }
}
