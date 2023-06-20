package com.example.coen_mp_concordiatravelwebapplication.dataaccess;

import com.example.coen_mp_concordiatravelwebapplication.config.CONFIG;
import com.example.coen_mp_concordiatravelwebapplication.models.packageModels.Activity;
import com.example.coen_mp_concordiatravelwebapplication.models.packageModels.Flight;
import com.example.coen_mp_concordiatravelwebapplication.models.packageModels.Hotel;
import com.example.coen_mp_concordiatravelwebapplication.models.packageModels.TravelPackage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomPackageDAOImpl implements CustomPackageDAO {
    @Override
    public List<TravelPackage> retrieveUserPackages(String userId) {
        List<TravelPackage> userPackages = new ArrayList<>();

        try {
            Connection conn = DriverManager.getConnection(CONFIG.SQLURL, CONFIG.SQLUSER, CONFIG.SQLPASS);

            String query = "SELECT * FROM user_packages WHERE user_id = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, userId);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int packageId = resultSet.getInt("package_id")-499;
                String activityIds = resultSet.getString("activity_ids");
                String flightIds = resultSet.getString("flight_ids");
                String hotelIds = resultSet.getString("hotel_ids");

                List<Activity> activities = fetchActivities(activityIds);
                List<Flight> flights = fetchFlights(flightIds);
                List<Hotel> hotels = fetchHotels(hotelIds);

                TravelPackage userPackage = new TravelPackage(packageId, activities, flights, hotels);
                userPackages.add(userPackage);
            }

            resultSet.close();
            statement.close();
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userPackages;
    }

    @Override
    public boolean deletePackage(String packageId) {
        if (packageId != null && !packageId.isEmpty()) {
            Connection conn = null;
            PreparedStatement stmt = null;

            try {
                // Establish a connection to the database
                conn = DriverManager.getConnection(CONFIG.SQLURL, CONFIG.SQLUSER, CONFIG.SQLPASS);

                // Prepare the SQL statement to delete the row with the provided package ID
                String sql = "DELETE FROM user_packages WHERE package_id = ?";
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, packageId);

                // Execute the SQL statement
                int rowsAffected = stmt.executeUpdate();

                if (rowsAffected > 0) {
                    return true;
                } else {
                    return false;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                // Close the database resources
                try {
                    if (stmt != null) {
                        stmt.close();
                    }
                    if (conn != null) {
                        conn.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public List<Activity> fetchActivities(String activityIds) {
        List<Activity> activities = new ArrayList<>();
        String[] activityIdArray = activityIds.split(",");
        for (String activityId : activityIdArray) {
            Activity activity = null;

            try {
                Connection conn = DriverManager.getConnection(CONFIG.SQLURL, CONFIG.SQLUSER, CONFIG.SQLPASS);

                String query = "SELECT * FROM activity WHERE activity_id = ?";
                PreparedStatement statement = conn.prepareStatement(query);
                statement.setString(1, activityId);

                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    String name = resultSet.getString("name");
                    String description = resultSet.getString("description");
                    double price = resultSet.getDouble("price");

                    activity = new Activity(activityId, name, description, price);
                }

                resultSet.close();
                statement.close();
                conn.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }

            if (activity != null) {
                activities.add(activity);
            }
        }

        return activities;
    }


    public List<Flight> fetchFlights(String flightIds) {
        List<Flight> flights = new ArrayList<>();

        // Split the flight_ids string and query the flights table for each flight_id
        String[] flightIdArray = flightIds.split(",");
        for (String flightId : flightIdArray) {
            // Query the flights table based on flight_id and retrieve corresponding flight
            Flight flight = null;

            try {
                Connection conn = DriverManager.getConnection(CONFIG.SQLURL, CONFIG.SQLUSER, CONFIG.SQLPASS);

                String query = "SELECT * FROM flight WHERE flight_id = ?";
                PreparedStatement statement = conn.prepareStatement(query);
                statement.setString(1, flightId);

                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    String airline = resultSet.getString("airline");
                    Timestamp departure = resultSet.getTimestamp("departure");
                    Timestamp arrival = resultSet.getTimestamp("arrival");
                    double price = resultSet.getDouble("price");

                    flight = new Flight(flightId, airline, departure, arrival, price);
                }

                resultSet.close();
                statement.close();
                conn.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }

            if (flight != null) {
                flights.add(flight);
            }
        }

        return flights;
    }



    public List<Hotel> fetchHotels(String hotelIds) {
        List<Hotel> hotels = new ArrayList<>();

        // Split the hotel_ids string and query the hotels table for each hotel_id
        String[] hotelIdArray = hotelIds.split(",");
        for (String hotelId : hotelIdArray) {
            // Query the hotels table based on hotel_id and retrieve corresponding hotel
            Hotel hotel = null;

            try {
                Connection conn = DriverManager.getConnection(CONFIG.SQLURL, CONFIG.SQLUSER, CONFIG.SQLPASS);

                String query = "SELECT * FROM hotel WHERE hotel_id = ?";
                PreparedStatement statement = conn.prepareStatement(query);
                statement.setString(1, hotelId);

                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    String name = resultSet.getString("name");
                    String location = resultSet.getString("location");
                    double price = resultSet.getDouble("price");

                    hotel = new Hotel(hotelId, name, location, price);
                }

                resultSet.close();
                statement.close();
                conn.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }

            if (hotel != null) {
                hotels.add(hotel);
            }
        }

        return hotels;
    }
}