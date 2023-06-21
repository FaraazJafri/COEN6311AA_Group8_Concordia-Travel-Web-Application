package com.example.coen_mp_concordiatravelwebapplication.dataaccess;

import com.example.coen_mp_concordiatravelwebapplication.models.bookingModels.Booking;
import com.example.coen_mp_concordiatravelwebapplication.models.packageModels.Activity;
import com.example.coen_mp_concordiatravelwebapplication.models.packageModels.Flight;
import com.example.coen_mp_concordiatravelwebapplication.models.packageModels.Hotel;
import com.example.coen_mp_concordiatravelwebapplication.models.packageModels.TravelPackage;

import java.util.List;

public interface CustomPackageDAO {

    List<Activity> fetchActivities(String activityIds);

    List<Flight> fetchFlights(String flightIds);

    List<Hotel> fetchHotels(String hotelIds);

    List<TravelPackage> retrieveUserPackages(String userID);

    boolean deletePackage(String packageId);

    boolean addCustomPackage(String userID, String activityIds, String flightIds, String hotelIds);

    boolean modifyCustomPackage(String userID, String packageId, String activityIds, String flightIds, String hotelIds);
    TravelPackage retrieveUserPackagesForCart(String userId, String customPackageId);


}
