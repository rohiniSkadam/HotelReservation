package view;

import model.Hotel;

/**
 * Created by synerzip on 19/1/17.
 */
public class DisplayHotel {
    public static void display(Hotel cheapestHotel) {
        System.out.println("Hotel Type : " + cheapestHotel.getName() + "\nTotal Rate : " + cheapestHotel.getTotalRate());
    }
}
