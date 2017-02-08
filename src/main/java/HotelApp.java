import controller.HotelController;
import model.Customer;
import model.Hotel;
import org.apache.log4j.Logger;
import org.yaml.snakeyaml.Yaml;
import view.DisplayHotel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by synerzip on 7/2/17.
 */
public class HotelApp {

    private static Logger logger = Logger.getLogger(HotelApp.class);
    StringTokenizer token;
    private String customerType;
    private List<Hotel> hotelList = null;
    private List<Customer> customerList = null;
    private ArrayList<Date> datelist = new ArrayList<>();
    private HotelController hotelController = new HotelController();

    public static void main(String[] args) throws FileNotFoundException {
        Scanner sc = new Scanner(System.in);
        String input;
        System.out.println("Enter Information \n<customer_type>: <date1>, <date2>, <date3>,...");
        input = sc.nextLine().trim();

        HotelApp hotelAppObj = new HotelApp();
        hotelAppObj.loadHotels();
        hotelAppObj.loadCustomer();
        hotelAppObj.validateInput(input);

    }

    /**
     * Function to take input from user
     */
    private void validateInput(String input) {
        validateDate(input);
        boolean flag = false;

        for (Customer cust : customerList) {
            if (cust.getCustType().equals(customerType) && (!datelist.isEmpty()))
                flag = true;
        }
        if (flag) {
            Hotel cheapHotel = hotelController.searchHotel(customerType, datelist, (ArrayList<Hotel>) hotelList);
            DisplayHotel.display(cheapHotel);
        } else
            System.out.println("Please Enter Correct Input");
    }

    /**
     * Function to validate date format
     *
     * @param info - user input which contain customer type & list of dates
     */
    private ArrayList<Date> validateDate(String info) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MMM-yyyy EEE");
        token = new StringTokenizer(info, ":,");
        customerType = token.nextToken().toUpperCase();

        try {
            while (token.hasMoreTokens()) {
                String addDate = token.nextToken();
                Date date = dateFormatter.parse(addDate);
                dateFormatter.format(date);
                datelist.add(date);

            }
            logger.info("List of Date is updated successfully.");
        } catch (ParseException p) {
            System.out.println("Enter Dates in DD-MM-YYYY Format.(eg.18-Mar-2010 wed)");
            return null;
        }
        return datelist;
    }

    /**
     * Function to load list of hotels from hotels.yml file
     *
     * @return - returns list of available hotels
     * @throws FileNotFoundException
     */
    private List<Hotel> loadHotels() throws FileNotFoundException {
        Yaml obj = new Yaml();
        String path = "/home/synerzip/HotelRoomReservationSystem/src/main/java/inputFiles/hotels.yml";
        InputStream inputStream = new FileInputStream(new File(path));
        hotelList = (List<Hotel>) obj.load(inputStream);
        logger.info("hotels.yml file loaded successfully");
        return hotelList;
    }

    /**
     * @return - returns list of customer types
     * @throws FileNotFoundException
     */
    private List<Customer> loadCustomer() throws FileNotFoundException {
        Yaml obj = new Yaml();
        String path = "/home/synerzip/HotelRoomReservationSystem/src/main/java/inputFiles/customers.yml";
        InputStream inputStream = new FileInputStream(new File(path));
        customerList = (List<Customer>) obj.load(inputStream);
        logger.info("customers.yml file loaded successfully");
        return customerList;
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