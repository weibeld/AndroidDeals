package org.latefire.deals.models;

/**
 * Created by dw on 19/03/17.
 */

public class Business extends User {

    private String businessName;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private double locationLat;
    private double locationLong;

    public Business() {
    }

    public Business(String email, String phone, String businessName, String addressLine1, String addressLine2, String city, double locationLat, double locationLong) {
        super(email, phone);
        this.businessName = businessName;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.city = city;
        this.locationLat = locationLat;
        this.locationLong = locationLong;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public double getLocationLat() {
        return locationLat;
    }

    public void setLocationLat(double locationLat) {
        this.locationLat = locationLat;
    }

    public double getLocationLong() {
        return locationLong;
    }

    public void setLocationLong(double locationLong) {
        this.locationLong = locationLong;
    }
}
