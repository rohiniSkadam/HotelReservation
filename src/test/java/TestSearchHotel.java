import controller.HotelController;
import model.Hotel;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * Created by synerzip on 7/2/17.
 */

public class TestSearchHotel {
    HotelController searchHotel = new HotelController();
    HotelApp hotelMain = new HotelApp();

    @Test
    public void testCalculateRate() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        int a = 1000;
        int b = 1500;

        Date date = new Date("21-Mar-2009");
        Date date1 = new Date("22-Mar-2009");

        ArrayList<Date> datelist = new ArrayList<>();
        datelist.add(date);
        datelist.add(date1);

        Method calculateRateTest = HotelController.class.getDeclaredMethod("getCalculateRate", int.class, int.class, ArrayList.class);
        calculateRateTest.setAccessible(true);
        int calculateRateResult = (int) calculateRateTest.invoke(searchHotel, a, b, datelist);
        int expectedResult = 3000;

        System.out.println("Expected : " + expectedResult + "\nActual   : " + calculateRateResult);
        assertEquals("true", expectedResult, calculateRateResult);
    }

    @Test
    public void testFindCheapestHotel() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Date date = new Date("21-Mar-2009");
        Date date1 = new Date("22-Mar-2009");

        ArrayList<Date> datelist = new ArrayList<>();
        datelist.add(date);
        datelist.add(date1);


        Method listOfHotelTest = HotelApp.class.getDeclaredMethod("loadDetails");
        listOfHotelTest.setAccessible(true);
        ArrayList<Hotel> hotelList = (ArrayList<Hotel>) listOfHotelTest.invoke(hotelMain);

        Method cheapestHotelTest = HotelController.class.getDeclaredMethod("getCheapestHotel", String.class, ArrayList.class, ArrayList.class);
        cheapestHotelTest.setAccessible(true);
        Hotel nameOfHotel = (Hotel) cheapestHotelTest.invoke(searchHotel, "REGULAR", datelist, hotelList);

        String nm = nameOfHotel.getName();
        String expectedResult = "SMN Deluxe";

        System.out.println("Expected : " + expectedResult + "\nActual   : " + nm);
        assertEquals("Result  : ", expectedResult, nm);
    }
}
