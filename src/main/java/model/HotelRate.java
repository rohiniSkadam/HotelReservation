package model;

import java.util.List;

public class HotelRate {

    private Customer customer;
    private List<Rate> rate;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<Rate> getRate() {
        return rate;
    }

    public void setRate(List<Rate> rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return customer + "  " + rate + "\n";
    }


}
