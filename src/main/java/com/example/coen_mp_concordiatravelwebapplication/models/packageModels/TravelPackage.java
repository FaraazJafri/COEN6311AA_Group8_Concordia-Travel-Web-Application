package com.example.coen_mp_concordiatravelwebapplication.models.packageModels;

import java.util.ArrayList;
import java.util.List;

public class TravelPackage {
    private String packageId;
    private String name;
    private String description;
    private double price;
    private List<Flight> flights;
    private List<Hotel> hotels;
    private List<Activity> activities;


    public TravelPackage(String packageId, String name, String description, double price, List<Flight> flights, List<Hotel> hotels, List<Activity> activities) {
        this.packageId = packageId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.flights = flights;
        this.hotels = hotels;
        this.activities = activities;
    }

    public TravelPackage() {
        this.packageId = "";
        this.name = "";
        this.description = "";
        this.price = 0.0;
        this.flights = new ArrayList<>();
        this.hotels = new ArrayList<>();
        this.activities = new ArrayList<>();
    }


    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<Flight> getFlights() {
        return flights;
    }

    public List<Hotel> getHotels() {
        return hotels;
    }

    public List<Activity> getActivities() {
        return activities;
    }

    // Methods to add flights, hotels, and activities to the package

    public void addFlight(Flight flight) {
        flights.add(flight);
    }

    public void addHotel(Hotel hotel) {
        hotels.add(hotel);
    }

    public void addActivity(Activity activity) {
        activities.add(activity);
    }

    @Override
    public String toString() {
        return "TravelPackage{" +
                "packageId='" + packageId + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", flights=" + flights +
                ", hotels=" + hotels +
                ", activities=" + activities +
                '}';
    }
}

