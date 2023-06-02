package com.example.coen_mp_concordiatravelwebapplication.models.bookingModels;

import java.sql.Timestamp;

public class Booking {

    private String bookingId;

    private String packageId;

    private String customerId;

    private Timestamp departureDate;

    public Booking(String bookingId, String packageId, String customerId, Timestamp departureDate){
        this.bookingId = bookingId;
        this.packageId = packageId;
        this.customerId = customerId;
        this.departureDate = departureDate;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public Timestamp getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(Timestamp departureDate) {
        this.departureDate = departureDate;
    }

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
}
