package hu.szte.mobilalk.buszjegy_mobilalkfejl_2024;

import androidx.annotation.NonNull;

public class BillingAddress {
    private int postalCode;
    private String city;
    private String street;
    private int houseNumber;

    public BillingAddress() {
    }

    public BillingAddress(int postalCode, String city, String street, int houseNumber) {
        this.postalCode = postalCode;
        this.city = city;
        this.street = street;
        this.houseNumber = houseNumber;
    }

    public int getPostalCode() {
        return postalCode;
    }

    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

    public int getHouseNumber() {
        return houseNumber;
    }

    public void setPostalCode(int postalCode) {
        this.postalCode = postalCode;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setHouseNumber(int houseNumber) {
        this.houseNumber = houseNumber;
    }

    @NonNull
    @Override
    public String toString() {
        return getPostalCode()+" "+getCity()+" "+getStreet()+" "+getHouseNumber();
    }
}
