package controller;

import model.Hotel;
import model.HotelRate;
import model.Rate;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * Created by synerzip on 7/2/17.
 */

public class HotelController {

    private static Logger logger = Logger.getLogger(HotelController.class);

    /**
     * Function to return cheapest Hotel
     *
     * @param customerType - Type of Customer
     * @param listOfDates  - List of dates entered by user
     * @param hotelList    - List of hotels available
     */
    public Hotel searchHotel(String customerType, ArrayList<Date> listOfDates, ArrayList<Hotel> hotelList) {
        return getCheapestHotel(customerType, listOfDates, hotelList);
    }

    /**
     * Function to find cheapest Hotel from List of hotel
     *
     * @param customerType -Type of Customer
     * @param listOfDates  - List of dates
     * @param hotels       - List of available Hotels
     */
    private Hotel getCheapestHotel(String customerType, ArrayList<Date> listOfDates, ArrayList<Hotel> hotels) {
        getTotalRoomRate(customerType, hotels, listOfDates);
        Collections.sort(hotels, new Comparator<Hotel>() {
            @Override
            public int compare(Hotel o1, Hotel o2) {
                if (o1.getTotalRate() != o2.getTotalRate())
                    return o1.getTotalRate() - o2.getTotalRate();
                else
                    return o2.getRating() - o1.getRating();
            }
        });
        logger.debug("Hotel : " + hotels.get(0));
        return hotels.get(0);
    }

    /**
     * Function to get total room rate for each individual hotel
     *
     * @param customerType - Type of Customer
     * @param hotels       - List Of hotels available
     * @param listOfDates  - date list
     */
    private void getTotalRoomRate(String customerType, ArrayList<Hotel> hotels, ArrayList<Date> listOfDates) {
        for (Hotel h : hotels) {
            HotelRate regularRate = h.getRates().get(0);
            HotelRate rewardsRate = h.getRates().get(1);
            if (customerType.equals(regularRate.getCustomer().getCustType()))
                getRateValues(h, regularRate, listOfDates);
            else
                getRateValues(h, rewardsRate, listOfDates);
        }
    }

    /**
     * Function to get rate values of a hotel
     *
     * @param hotel       - individual Hotel
     * @param hotelRate   - individual Hotel Rate
     * @param listOfDates - date list
     */
    private void getRateValues(Hotel hotel, HotelRate hotelRate, ArrayList<Date> listOfDates) {
        //  int totalRate;
        Rate weekdayRate = hotelRate.getRate().get(0);
        Rate weekendRate = hotelRate.getRate().get(1);
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
}
