package model;

public class Customer {
    public String getCustType() {
        return custType;
    }

    public void setCustType(String custType) {
        this.custType = custType;
    }

    private String custType;



    @Override
    public String toString() {
        return custType;
    }

}
