package com.example.coen_mp_concordiatravelwebapplication.models.packageModels;

// Hotel class representing a hotel component
public class Hotel {
    private String hotelId;
    private String name;
    private String location;
    private double price;

    public Hotel(String hotelId, String name, String location, double price) {
        this.hotelId = hotelId;
        this.name = name;
        this.location = location;
        this.price = price;
    }

    public String getHotelId() {
        return hotelId;
    }

    public void setHotelId(String hotelId) {
        this.hotelId = hotelId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Hotel{" +
                "hotelId='" + hotelId + '\'' +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", price=" + price +
                '}';
    }
}

