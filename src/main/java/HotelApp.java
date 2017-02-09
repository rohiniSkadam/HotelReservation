import controller.HotelController;
import model.Customer;
import model.Hotel;
import model.HotelRate;
import model.Rate;
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
    private static boolean flag;
    private static String customerType;
    private static List<Hotel> hotelList = null;
    private static ArrayList<Date> datelist = new ArrayList<>();
    private static HotelController hotelController = new HotelController();
    StringTokenizer token;
    private List<Customer> customerList = null;

    /**
     * Main Function that starts calling all methods.
     *
     * @throws FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException {
        Scanner sc = new Scanner(System.in);
        String input;
        System.out.println("Enter Information \n<customer_type>: <date1>, <date2>, <date3>,...");
        input = sc.nextLine().trim();

        HotelApp hotelAppObj = new HotelApp();

        hotelAppObj.loadDetails();
        hotelAppObj.validateInput(input);

        if (flag) {
            Hotel cheapHotel = hotelController.getCheapestHotel(customerType, datelist, (ArrayList<Hotel>) hotelList);
            DisplayHotel.display(cheapHotel);
        } else
            System.out.println("Please Enter Correct Input");
    }

    /**
     * Function to take & validate input from user
     */
    private void validateInput(String input) {
        validateDate(input);
        for (Customer cust : customerList) {
            if (cust.getCustType().equals(customerType) && (!datelist.isEmpty()))
                flag = true;
        }
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
     * Function to load hotels,customer types & hotel rates
     *
     * @return
     * @throws FileNotFoundException
     */
    public List<Hotel> loadDetails() throws FileNotFoundException {
        loadHotels();
        loadCustomer();
        loadHotelRates();
        return hotelList;
    }

    /**
     * Function to load hotel rates
     */
    private void loadHotelRates() {

        for (Hotel h : hotelList) {
            HashMap<String, List<Rate>> listofHotelRates = new HashMap<>();
            List<HotelRate> rates = h.getRates();
            for (HotelRate cust : rates)
                listofHotelRates.put(cust.getCustomer().getCustType(), cust.getRate());
            h.setCustHotalRates(listofHotelRates);
        }
    }

    /**
     * Function to load list of hotels from hotels.yml file
     *
     * @return - returns list of available hotels
     * @throws FileNotFoundException
     */
    private List<Hotel> loadHotels() throws FileNotFoundException {
        Yaml obj = new Yaml();
        String path = "/home/synerzip/HotelRoomReservation/src/main/java/inputFiles/hotels.yml";
        InputStream inputStream = new FileInputStream(new File(path));
        hotelList = (List<Hotel>) obj.load(inputStream);
        logger.info("hotels.yml file loaded successfully");
        return hotelList;
    }

    /**
     * Function to load list of customers from customers.yml file
     *
     * @return - returns list of customer types
     * @throws FileNotFoundException
     */
    private List<Customer> loadCustomer() throws FileNotFoundException {
        Yaml obj = new Yaml();
        String path = "/home/synerzip/HotelRoomReservation/src/main/java/inputFiles/customers.yml";
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