import controller.HotelController;
import model.Hotel;
import view.DisplayHotel;

import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Created by synerzip on 7/2/17.
 */
public class HotelApp {
    /**
     * Main Function that starts calling all methods.
     *
     * @throws FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException {
        HotelController hotelController = new HotelController();
        Scanner sc = new Scanner(System.in);
        String input;
        System.out.println("Enter Information \n<customer_type>: <date1>, <date2>, <date3>,...");
        input = sc.nextLine().trim();

        Hotel cheapHotel = hotelController.getCheapestHotel(input);
        if (cheapHotel != null)
            DisplayHotel.display(cheapHotel);
        else System.out.println("Hotel Not Found for your Input");

    }
}
/*
INPUT FORMAT: <customer_type>: <date1>, <date2>, <date3>, ...
OUTPUT FORMAT: <name_of_the_cheapest_hotel>

INPUT 1:
Regular: 16-Mar-2009 mon, 17-Mar-2009 tues , 18-Mar-2009 wed
OUTPUT 1: <model.Hotel Name>

INPUT 2:
Regular: 20-Mar-2009 fri , 21-Mar-2009 sat, 22-Mar-2009 sun
OUTPUT 2: <model.Hotel Name>

INPUT 3:
Rewards: 26-Mar-2009 thur, 27-Mar-2009 fri , 28-Mar-2009 sat
OUTPUT 3: <model.Hotel Name>
*/