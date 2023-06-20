package com.example.coen_mp_concordiatravelwebapplication.dataaccess;

import com.example.coen_mp_concordiatravelwebapplication.models.packageModels.Flight;

import java.util.List;

public interface FlightDAO {
    List<Flight> getAllFlights();
}
