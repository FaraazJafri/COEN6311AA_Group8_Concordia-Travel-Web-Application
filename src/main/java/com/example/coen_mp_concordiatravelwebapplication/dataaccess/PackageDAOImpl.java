package com.example.coen_mp_concordiatravelwebapplication.dataaccess;

import com.example.coen_mp_concordiatravelwebapplication.config.CONFIG;
import com.example.coen_mp_concordiatravelwebapplication.models.packageModels.Activity;
import com.example.coen_mp_concordiatravelwebapplication.models.packageModels.Flight;
import com.example.coen_mp_concordiatravelwebapplication.models.packageModels.Hotel;
import com.example.coen_mp_concordiatravelwebapplication.models.packageModels.TravelPackage;
import jakarta.servlet.ServletException;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class PackageDAOImpl implements PackageDAO {
    @Override
    public List<TravelPackage> getAllPackages() {
        List<TravelPackage> travelPackages = new ArrayList<>();

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        try (Connection connection = DriverManager.getConnection(CONFIG.SQLURL, CONFIG.SQLUSER, CONFIG.SQLPASS)) {
            Statement statement = connection.createStatement();

            String query = "SELECT * FROM travel_package";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String packageId = resultSet.getString("package_id");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                double price = resultSet.getDouble("price");

                TravelPackage travelPackage = new TravelPackage(packageId, name, description, price,
                        new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

                travelPackages.add(travelPackage);
            }

            for (TravelPackage travelPackage : travelPackages) {
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
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return travelPackages;
    }

    @Override
    public void savePackage(String packageId, String packageName, String packageDescription, double packagePrice, List<Flight> flights, List<Hotel> hotels, List<Activity> activities) {
        try {
            // Establish a database connection
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection(CONFIG.SQLURL, CONFIG.SQLUSER, CONFIG.SQLPASS);

            // Insert the package information
            String insertPackageQuery = "INSERT INTO travel_package (package_id, name, description, price) VALUES (?, ?, ?, ?)";
            PreparedStatement packageStatement = connection.prepareStatement(insertPackageQuery);
            packageStatement.setString(1, packageId);
            packageStatement.setString(2, packageName);
            packageStatement.setString(3, packageDescription);
            packageStatement.setDouble(4, packagePrice);
            packageStatement.executeUpdate();

            // Insert the flight information and establish relationships
            for (Flight flight : flights) {
                String insertFlightQuery = "INSERT INTO flight (flight_id, airline, departure, arrival, price) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement flightStatement = connection.prepareStatement(insertFlightQuery);
                flightStatement.setString(1, flight.getFlightId());
                flightStatement.setString(2, flight.getAirline());
                flightStatement.setTimestamp(3, flight.getDeparture());
                flightStatement.setTimestamp(4, flight.getArrival());
                flightStatement.setDouble(5, flight.getPrice());
                flightStatement.executeUpdate();

                // Establish relationship between package and flight
                String insertPackageFlightQuery = "INSERT INTO package_flight (package_id, flight_id) VALUES (?, ?)";
                PreparedStatement packageFlightStatement = connection.prepareStatement(insertPackageFlightQuery);
                packageFlightStatement.setString(1, packageId);
                packageFlightStatement.setString(2, flight.getFlightId());
                packageFlightStatement.executeUpdate();
            }

            // Insert the hotel information and establish relationships
            for (Hotel hotel : hotels) {
                String insertHotelQuery = "INSERT INTO hotel (hotel_id, name, location, price) VALUES (?, ?, ?, ?)";
                PreparedStatement hotelStatement = connection.prepareStatement(insertHotelQuery);
                hotelStatement.setString(1, hotel.getHotelId());
                hotelStatement.setString(2, hotel.getName());
                hotelStatement.setString(3, hotel.getLocation());
                hotelStatement.setDouble(4, hotel.getPrice());
                hotelStatement.executeUpdate();

                // Establish relationship between package and hotel
                String insertPackageHotelQuery = "INSERT INTO package_hotel (package_id, hotel_id) VALUES (?, ?)";
                PreparedStatement packageHotelStatement = connection.prepareStatement(insertPackageHotelQuery);
                packageHotelStatement.setString(1, packageId);
                packageHotelStatement.setString(2, hotel.getHotelId());
                packageHotelStatement.executeUpdate();
            }

            // Insert the activity information and establish relationships
            for (Activity activity : activities) {
                String insertActivityQuery = "INSERT INTO activity (activity_id, name, description, price) VALUES (?, ?, ?, ?)";
                PreparedStatement activityStatement = connection.prepareStatement(insertActivityQuery);
                activityStatement.setString(1, activity.getActivityId());
                activityStatement.setString(2, activity.getName());
                activityStatement.setString(3, activity.getDescription());
                activityStatement.setDouble(4, activity.getPrice());
                activityStatement.executeUpdate();

                // Establish relationship between package and activity
                String insertPackageActivityQuery = "INSERT INTO package_activity (package_id, activity_id) VALUES (?, ?)";
                PreparedStatement packageActivityStatement = connection.prepareStatement(insertPackageActivityQuery);
                packageActivityStatement.setString(1, packageId);
                packageActivityStatement.setString(2, activity.getActivityId());
                packageActivityStatement.executeUpdate();
            }

            // Close the database connection
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception appropriately
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<TravelPackage> searchByPrice(int minPrice, int maxPrice) throws ServletException {
        List<TravelPackage> searchResults = new ArrayList<>();
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        // Establish a connection to the database
        try (Connection connection = DriverManager.getConnection(CONFIG.SQLURL, CONFIG.SQLUSER, CONFIG.SQLPASS)) {
            // Construct the SQL query
            String query = "SELECT tp.package_id, tp.name, tp.description, tp.price " +
                    "FROM travel_package tp " +
                    "WHERE tp.price >= ? AND tp.price <= ?";

            // Create a prepared statement
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, minPrice);
            statement.setInt(2, maxPrice);

            // Execute the query
            ResultSet resultSet = statement.executeQuery();

            // Process the result set
            while (resultSet.next()) {
                String packageId = resultSet.getString("package_id");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                double price = resultSet.getDouble("price");

                // Create a travel package object
                TravelPackage travelPackage = new TravelPackage(packageId, name, description, price,
                        new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

                // Add the travel package to the search results
                searchResults.add(travelPackage);
            }

            // Retrieve flights, hotels, and activities for each travel package
            for (TravelPackage travelPackage : searchResults) {
                // Retrieve flights
                query = "SELECT f.flight_id, f.airline, f.departure, f.arrival, f.price " +
                        "FROM flight f " +
                        "INNER JOIN package_flight pf ON f.flight_id = pf.flight_id " +
                        "WHERE pf.package_id = ?";
                statement = connection.prepareStatement(query);
                statement.setString(1, travelPackage.getPackageId());
                resultSet = statement.executeQuery();
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
                query = "SELECT h.hotel_id, h.name, h.location, h.price " +
                        "FROM hotel h " +
                        "INNER JOIN package_hotel ph ON h.hotel_id = ph.hotel_id " +
                        "WHERE ph.package_id = ?";
                statement = connection.prepareStatement(query);
                statement.setString(1, travelPackage.getPackageId());
                resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    String hotelId = resultSet.getString("hotel_id");
                    String hotelName = resultSet.getString("name");
                    String hotelLocation = resultSet.getString("location");
                    double hotelPrice = resultSet.getDouble("price");

                    Hotel hotel = new Hotel(hotelId, hotelName, hotelLocation, hotelPrice);
                    travelPackage.addHotel(hotel);
                }

                // Retrieve activities
                query = "SELECT a.activity_id, a.name, a.description, a.price " +
                        "FROM activity a " +
                        "INNER JOIN package_activity pa ON a.activity_id = pa.activity_id " +
                        "WHERE pa.package_id = ?";
                statement = connection.prepareStatement(query);
                statement.setString(1, travelPackage.getPackageId());
                resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    String activityId = resultSet.getString("activity_id");
                    String activityName = resultSet.getString("name");
                    String activityDescription = resultSet.getString("description");
                    double activityPrice = resultSet.getDouble("price");

                    Activity activity = new Activity(activityId, activityName, activityDescription, activityPrice);
                    travelPackage.addActivity(activity);
                }
            }
        } catch (SQLException e) {
            throw new ServletException("Unable to connect to the database.", e);
        }
        return searchResults;
    }

    @Override
    public List<TravelPackage> searchByLocation(String location) throws ServletException {
        List<TravelPackage> searchResults = new ArrayList<>();
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        // Establish a connection to the database
        try (Connection connection = DriverManager.getConnection(CONFIG.SQLURL, CONFIG.SQLUSER, CONFIG.SQLPASS)) {
            // Construct the SQL query
            String query = "SELECT tp.package_id, tp.name, tp.description, tp.price " +
                    "FROM travel_package tp " +
                    "INNER JOIN package_hotel ph ON tp.package_id = ph.package_id " +
                    "INNER JOIN hotel h ON ph.hotel_id = h.hotel_id " +
                    "WHERE h.location = ?";

            // Create a prepared statement
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, location);

            // Execute the query
            ResultSet resultSet = statement.executeQuery();

            // Process the result set
            while (resultSet.next()) {
                String packageId = resultSet.getString("package_id");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                double price = resultSet.getDouble("price");

                // Create a travel package object
                TravelPackage travelPackage = new TravelPackage(packageId, name, description, price,
                        new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

                // Add the travel package to the search results
                searchResults.add(travelPackage);
            }

            // Retrieve flights, hotels, and activities for each travel package
            for (TravelPackage travelPackage : searchResults) {
                // Retrieve flights
                query = "SELECT f.flight_id, f.airline, f.departure, f.arrival, f.price " +
                        "FROM flight f " +
                        "INNER JOIN package_flight pf ON f.flight_id = pf.flight_id " +
                        "WHERE pf.package_id = ?";
                statement = connection.prepareStatement(query);
                statement.setString(1, travelPackage.getPackageId());
                resultSet = statement.executeQuery();
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
                query = "SELECT h.hotel_id, h.name, h.location, h.price " +
                        "FROM hotel h " +
                        "INNER JOIN package_hotel ph ON h.hotel_id = ph.hotel_id " +
                        "WHERE ph.package_id = ?";
                statement = connection.prepareStatement(query);
                statement.setString(1, travelPackage.getPackageId());
                resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    String hotelId = resultSet.getString("hotel_id");
                    String hotelName = resultSet.getString("name");
                    String hotelLocation = resultSet.getString("location");
                    double hotelPrice = resultSet.getDouble("price");

                    Hotel hotel = new Hotel(hotelId, hotelName, hotelLocation, hotelPrice);
                    travelPackage.addHotel(hotel);
                }

                // Retrieve activities
                query = "SELECT a.activity_id, a.name, a.description, a.price " +
                        "FROM activity a " +
                        "INNER JOIN package_activity pa ON a.activity_id = pa.activity_id " +
                        "WHERE pa.package_id = ?";
                statement = connection.prepareStatement(query);
                statement.setString(1, travelPackage.getPackageId());
                resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    String activityId = resultSet.getString("activity_id");
                    String activityName = resultSet.getString("name");
                    String activityDescription = resultSet.getString("description");
                    double activityPrice = resultSet.getDouble("price");

                    Activity activity = new Activity(activityId, activityName, activityDescription, activityPrice);
                    travelPackage.addActivity(activity);
                }
            }
        } catch (SQLException e) {
            throw new ServletException("Unable to connect to the database.", e);
        }

        // Remove duplicate packages based on package ID
        Set<String> packageIds = new HashSet<>();
        searchResults.removeIf(travelPackage -> !packageIds.add(travelPackage.getPackageId()));

        return searchResults;
    }
}
