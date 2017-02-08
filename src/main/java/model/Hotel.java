package model;

import java.util.List;

public class Hotel {
    private int rating;
    private String name;
    private List<HotelRate> rates;
    private int totalRate;

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<HotelRate> getRates() {
        return rates;
    }

    public void setRates(List<HotelRate> rates) {
        this.rates = rates;
    }

    public int getTotalRate() {
        return totalRate;
    }

    public void setTotalRate(int totalRate) {
        this.totalRate = totalRate;
    }

    @Override
    public String toString() {
        return "\n Rating : " + rating + "\n Name : '" + name + '\'' + "\n Total Rate : " + totalRate + "\n Rate : \n" + rates;
    }

}
