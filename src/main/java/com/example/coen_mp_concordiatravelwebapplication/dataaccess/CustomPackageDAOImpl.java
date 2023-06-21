package com.example.coen_mp_concordiatravelwebapplication.dataaccess;

import com.example.coen_mp_concordiatravelwebapplication.config.CONFIG;
import com.example.coen_mp_concordiatravelwebapplication.models.packageModels.Activity;
import com.example.coen_mp_concordiatravelwebapplication.models.packageModels.Flight;
import com.example.coen_mp_concordiatravelwebapplication.models.packageModels.Hotel;
import com.example.coen_mp_concordiatravelwebapplication.models.packageModels.TravelPackage;
import com.mysql.cj.PreparedQuery;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomPackageDAOImpl implements CustomPackageDAO {
    @Override
    public boolean addCustomPackage(String userID, String activityIds, String flightIds, String hotelIds) {
        try {
            Connection conn = DriverManager.getConnection(CONFIG.SQLURL, CONFIG.SQLUSER, CONFIG.SQLPASS);

            String query = "INSERT INTO user_packages (user_id, activity_ids, flight_ids, hotel_ids) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(query);

            statement.setInt(1, Integer.parseInt(userID));
            statement.setString(2, activityIds);
            statement.setString(3, flightIds);
            statement.setString(4, hotelIds);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Data inserted successfully");
                statement.close();
                conn.close();
                return true;
            } else {
                System.out.println("Failed to insert data");
                statement.close();
                conn.close();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean modifyCustomPackage(String userID, String packageId, String activityIds, String flightIds, String hotelIds) {
        try (Connection connection = DriverManager.getConnection(CONFIG.SQLURL, CONFIG.SQLUSER, CONFIG.SQLPASS)) {
            // Disable auto-commit to allow transaction handling
            connection.setAutoCommit(false);

            String updateQuery = "UPDATE user_packages SET activity_ids=?, flight_ids=?, hotel_ids=? WHERE user_id=? AND package_id=?";

            try (PreparedStatement statement = connection.prepareStatement(updateQuery)) {
                // Set the parameter values for the update statement
                statement.setString(1, activityIds);
                statement.setString(2, flightIds);
                statement.setString(3, hotelIds);
                statement.setString(4, userID);
                statement.setString(5, packageId);

                // Execute the update statement
                int rowsAffected = statement.executeUpdate();

                if (rowsAffected > 0) {
                    // If the update was successful, commit the changes
                    connection.commit();
                    return true;
                } else {
                    // If no rows were affected, rollback the transaction
                    connection.rollback();
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return false;
    }

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

                double totalPrice = 0.0;

                for (Activity activity : activities) {
                    totalPrice += activity.getPrice();
                }

                for (Flight flight : flights) {
                    totalPrice += flight.getPrice();
                }

                for (Hotel hotel : hotels) {
                    totalPrice += hotel.getPrice();
                }

                TravelPackage userPackage = new TravelPackage(packageId, activities, flights, hotels);
                userPackage.setPrice(totalPrice);
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
    public TravelPackage retrieveUserPackagesForCart(String userId, String customPackageId) {

        try {
            Connection conn = DriverManager.getConnection(CONFIG.SQLURL, CONFIG.SQLUSER, CONFIG.SQLPASS);

            String query = "SELECT * FROM user_packages WHERE user_id = ? AND package_id = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, userId);
            statement.setString(2, customPackageId);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int packageId = resultSet.getInt("package_id") - 499;
                String activityIds = resultSet.getString("activity_ids");
                String flightIds = resultSet.getString("flight_ids");
                String hotelIds = resultSet.getString("hotel_ids");

                List<Activity> activities = fetchActivities(activityIds);
                List<Flight> flights = fetchFlights(flightIds);
                List<Hotel> hotels = fetchHotels(hotelIds);

                double totalPrice = 0.0;

                for (Activity activity : activities) {
                    totalPrice += activity.getPrice();
                }

                for (Flight flight : flights) {
                    totalPrice += flight.getPrice();
                }

                for (Hotel hotel : hotels) {
                    totalPrice += hotel.getPrice();
                }

                TravelPackage userPackage = new TravelPackage(packageId, activities, flights, hotels);
                userPackage.setPrice(totalPrice);
                return userPackage;
            }

            resultSet.close();
            statement.close();
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
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
