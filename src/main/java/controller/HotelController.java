package controller;

import model.Customer;
import model.Hotel;
import model.HotelRate;
import model.Rate;
import org.apache.log4j.Logger;
import org.yaml.snakeyaml.Yaml;

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
public class HotelController {

    private static Logger logger = Logger.getLogger(HotelController.class);

    private String customerType;
    private List<Hotel> hotelList = null;
    private ArrayList<Date> datelist = new ArrayList<>();
    private List<Customer> customerList = null;

    /**
     * Function to find cheapest Hotel from List of hotel
     *
     * @param input - user input which contain customer type & list of dates
     * @return - cheapest hotel details
     * @throws FileNotFoundException
     */
    public Hotel getCheapestHotel(String input) throws FileNotFoundException {
        loadDetails();
        boolean flag=validateInput(input);

        if(flag) {
            setHotelTotalRate(customerType, hotelList, datelist);
            Collections.sort(hotelList, new Comparator<Hotel>() {
                @Override
                public int compare(Hotel o1, Hotel o2) {
                    if (o1.getTotalRate() != o2.getTotalRate())
                        return o1.getTotalRate() - o2.getTotalRate();
                    else
                        return o2.getRating() - o1.getRating();
                }
            });

            logger.debug("Hotel : " + hotelList.get(0));
            return hotelList.get(0);
        }
        else {
            System.out.println("Invalid Input. ");
            return null;
        }
    }

    /**
     * Function to get total room rate for each individual hotel
     *
     * @param customerType - Type of Customer
     * @param hotels       - List Of hotels available
     * @param listOfDates  - date list
     */
    private void setHotelTotalRate(String customerType, List<Hotel> hotels, ArrayList<Date> listOfDates) {
        for (Hotel h : hotels) {
            List<Rate> hotelRate = h.getCustHotalRates().get(customerType);
            setRateValues(h, hotelRate, listOfDates);
        }
    }

    /**
     * Function to get rate values of a hotel
     *
     * @param hotel       - individual Hotel
     * @param hotelRate   - individual Hotel Rate
     * @param listOfDates - date list
     */
    private void setRateValues(Hotel hotel, List<Rate> hotelRate, ArrayList<Date> listOfDates) {
        Rate weekdayRate = hotelRate.get(0);
        Rate weekendRate = hotelRate.get(1);
        int weekday = weekdayRate.getRateValue();
        int weekend = weekendRate.getRateValue();
        int totalRate = getCalculateRate(weekday, weekend, listOfDates);
        hotel.setTotalRate(totalRate);
        logger.debug("Setting total rate for Hotel : " + hotel.getName() + "  Total Rate :" + totalRate);
    }

    /**
     * Function to calculate total rate
     *
     * @param weekdayRate - weekday rate of hotel
     * @param weekendRate - weekend rate of hotel
     * @param listOfDates - date list
     * @return- calculated total rate
     */
    private int getCalculateRate(int weekdayRate, int weekendRate, ArrayList<Date> listOfDates) {
        int weekDayCount = 0, weekEndCount = 0, totalRate = 0;

        for (Date dt : listOfDates) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(dt);
            int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
            if (dayOfWeek == Calendar.SUNDAY || dayOfWeek == Calendar.SATURDAY)
                weekEndCount += 1;
            else
                weekDayCount += 1;
        }
        logger.debug("Weekday count : " + weekDayCount + "\t Weekend count : " + weekEndCount);
        int weekdayrate = weekDayCount * weekdayRate;
        int weekEndRate = weekEndCount * weekendRate;
        totalRate = weekdayrate + weekEndRate;
        logger.debug("Total Rate is : " + totalRate);
        return totalRate;
    }

    /**
     * Function to validate input
     *
     * @param input - user input which contain customer type & list of dates
     * @return - boolean flag for valid customer type & Date
     */
    private boolean validateInput(String input) {
        validateDate(input);
        boolean flag = false;
        for (Customer cust : customerList) {
            if (cust.getCustType().equals(customerType) && (!datelist.isEmpty()))
                flag = true;
        }
        if (flag) return true;
        else return false;
    }

    /**
     * Function to validate date format
     *
     * @param input - user input which contain customer type & list of dates
     */
    private void validateDate(String input) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MMM-yyyy EEE");
        StringTokenizer token = new StringTokenizer(input, ":,");
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
            return;
        }
    }

    /**
     * Function to load hotels,customer types & hotel rates
     *
     * @throws FileNotFoundException
     */
    public void loadDetails() throws FileNotFoundException {
        loadHotels();
        loadCustomer();
        loadHotelRates();
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
     * @throws FileNotFoundException
     */
    private void loadHotels() throws FileNotFoundException {
        Yaml obj = new Yaml();
        String path = "/home/synerzip/HotelRoomReservation/src/main/java/inputFiles/hotels.yml";
        InputStream inputStream = new FileInputStream(new File(path));
        hotelList = (List<Hotel>) obj.load(inputStream);
        logger.info("hotels.yml file loaded successfully");
    }

    /**
     * Function to load list of customers from customers.yml file
     *
     * @throws FileNotFoundException
     */
    private void loadCustomer() throws FileNotFoundException {
        Yaml obj = new Yaml();
        String path = "/home/synerzip/HotelRoomReservation/src/main/java/inputFiles/customers.yml";
        InputStream inputStream = new FileInputStream(new File(path));
        customerList = (List<Customer>) obj.load(inputStream);
        logger.info("customers.yml file loaded successfully");
    }
}
