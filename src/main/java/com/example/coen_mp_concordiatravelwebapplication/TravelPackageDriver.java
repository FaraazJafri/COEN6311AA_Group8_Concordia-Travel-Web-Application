package com.example.coen_mp_concordiatravelwebapplication;

import com.example.coen_mp_concordiatravelwebapplication.models.packageModels.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class TravelPackageDriver {

    public static void main(String[] args) {
        // JDBC connection details
        String url = "jdbc:mysql://localhost:3306/travelsystem";
        String username = "root";
        String password = "Zeba_b38";

        // Create an empty list to store travel packages
        List<TravelPackage> travelPackages = new ArrayList<>();

        // Establish a connection to the database
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            // Create a statement
            Statement statement = connection.createStatement();

            // Retrieve travel packages
            String query = "SELECT * FROM travel_package";
            ResultSet resultSet = statement.executeQuery(query);

            // Process the result set
            while (resultSet.next()) {
                String packageId = resultSet.getString("package_id");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                double price = resultSet.getDouble("price");

                // Create a travel package object
                TravelPackage travelPackage = new TravelPackage(packageId, name, description, price,
                        new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

                // Add the travel package to the list
                travelPackages.add(travelPackage);
            }

            // Retrieve flights, hotels, and activities for each travel package
            for (TravelPackage travelPackage : travelPackages) {
                // Retrieve flights
                query = "SELECT * FROM flight INNER JOIN package_flight ON flight.flight_id = package_flight.flight_id " +
                        "WHERE package_flight.package_id = '" + travelPackage.getPackageId() + "'";
                resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    String flightId = resultSet.getString("flight_id");
                    String airline = resultSet.getString("airline");
                    Timestamp departure = resultSet.getTimestamp("departure");
                    Timestamp arrival = resultSet.getTimestamp("arrival");
                    double flightPrice = resultSet.getDouble("price");

                    Flight flight = new Flight(flightId, airline, departure, arrival, flightPrice);
                    travelPackage.addFlight(flight);
                }

                // Retrieve hotels
                query = "SELECT * FROM hotel INNER JOIN package_hotel ON hotel.hotel_id = package_hotel.hotel_id " +
                        "WHERE package_hotel.package_id = '" + travelPackage.getPackageId() + "'";
                resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    String hotelId = resultSet.getString("hotel_id");
                    String hotelName = resultSet.getString("name");
                    String location = resultSet.getString("location");
                    double hotelPrice = resultSet.getDouble("price");

                    Hotel hotel = new Hotel(hotelId, hotelName, location, hotelPrice);
                    travelPackage.addHotel(hotel);
                }

                // Retrieve activities
                query = "SELECT * FROM activity INNER JOIN package_activity ON activity.activity_id = package_activity.activity_id " +
                        "WHERE package_activity.package_id = '" + travelPackage.getPackageId() + "'";
                resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    String activityId = resultSet.getString("activity_id");
                    String activityName = resultSet.getString("name");
                    String activityDescription = resultSet.getString("description");
                    double activityPrice = resultSet.getDouble("price");

                    Activity activity = new Activity(activityId, activityName, activityDescription, activityPrice);
                    travelPackage.addActivity(activity);
                }
            }

            // Print the travel package information
            for (TravelPackage travelPackage : travelPackages) {
                System.out.println(travelPackage);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
