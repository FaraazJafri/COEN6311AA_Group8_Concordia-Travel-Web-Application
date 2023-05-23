package com.example.coen_mp_concordiatravelwebapplication.models.packageModels;

import java.sql.Timestamp;
// Flight class representing a flight component
public class Flight {
    private String flightId;
    private String airline;
    private Timestamp departure;
    private Timestamp arrival;
    private double price;

    public Flight(String flightId, String airline, Timestamp departure, Timestamp arrival, double price) {
        this.flightId = flightId;
        this.airline = airline;
        this.departure = departure;
        this.arrival = arrival;
        this.price = price;
    }

    public Flight() {
        this.flightId = "";
        this.airline = "";
        this.departure = Timestamp.valueOf("");
        this.arrival = Timestamp.valueOf("");
        this.price = 0.0;
    }

    public String getFlightId() {
        return flightId;
    }

    public void setFlightId(String flightId) {
        this.flightId = flightId;
    }

    public String getAirline() {
        return airline;
    }

    public void setAirline(String airline) {
        this.airline = airline;
    }

    public Timestamp getDeparture() {
        return departure;
    }

    public void setDeparture(Timestamp departure) {
        this.departure = departure;
    }

    public Timestamp getArrival() {
        return arrival;
    }

    public void setArrival(Timestamp arrival) {
        this.arrival = arrival;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Flight{" +
                "flightId='" + flightId + '\'' +
                ", airline='" + airline + '\'' +
                ", departure='" + departure + '\'' +
                ", arrival='" + arrival + '\'' +
                ", price=" + price +
                '}';
    }
}

