package model;

public class Customer {
    private String custType;

    public String getCustType() {
        return custType;
    }

    public void setCustType(String custType) {
        this.custType = custType;
    }

    @Override
    public String toString() {
        return custType;
    }

}
