package com.example.coen_mp_concordiatravelwebapplication.dataaccess;

import com.example.coen_mp_concordiatravelwebapplication.config.CONFIG;
import com.example.coen_mp_concordiatravelwebapplication.models.packageModels.Activity;
import com.example.coen_mp_concordiatravelwebapplication.models.packageModels.Flight;
import com.example.coen_mp_concordiatravelwebapplication.models.packageModels.Hotel;
import com.example.coen_mp_concordiatravelwebapplication.models.packageModels.TravelPackage;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartDAOImpl implements CartDAO {

    @Override
    public void addToCart(String userId, String packageId, String agentId) {


        try (Connection connection = DriverManager.getConnection(CONFIG.SQLURL, CONFIG.SQLUSER, CONFIG.SQLPASS)) {
            String query = "INSERT INTO cart (userId, package_id, agentid) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, userId);
            statement.setString(2, packageId);
            if (agentId != null) {
                statement.setString(3, agentId);
            } else {
                statement.setNull(3, Types.INTEGER);
            }
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<TravelPackage> getPackagesInCartForCustomer(String userId) {

        List<String> packageIds = new ArrayList<>();

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        try (Connection connection = DriverManager.getConnection(CONFIG.SQLURL, CONFIG.SQLUSER, CONFIG.SQLPASS)) {
            String cartQuery = "SELECT package_id FROM cart WHERE userId = ? AND agentId IS NULL";
            PreparedStatement cartStatement = connection.prepareStatement(cartQuery);
            cartStatement.setString(1, userId);

            ResultSet cartResultSet = cartStatement.executeQuery();

            while (cartResultSet.next()) {
                String packageId = cartResultSet.getString("package_id");
                packageIds.add(packageId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        List<TravelPackage> travelPackages = new ArrayList<>();

        if (!packageIds.isEmpty()) {

            try (Connection connection = DriverManager.getConnection(CONFIG.SQLURL, CONFIG.SQLUSER, CONFIG.SQLPASS)) {
                String packageQuery = "SELECT * FROM travel_package WHERE package_id IN (";
                for (int i = 0; i < packageIds.size(); i++) {
                    packageQuery += "?";
                    if (i < packageIds.size() - 1) {
                        packageQuery += ",";
                    }
                }
                packageQuery += ")";

                PreparedStatement packageStatement = connection.prepareStatement(packageQuery);
                for (int i = 0; i < packageIds.size(); i++) {
                    packageStatement.setString(i + 1, packageIds.get(i));
                }

                ResultSet packageResultSet = packageStatement.executeQuery();

                while (packageResultSet.next()) {
                    String packageId = packageResultSet.getString("package_id");
                    String name = packageResultSet.getString("name");
                    String description = packageResultSet.getString("description");
                    double price = packageResultSet.getDouble("price");

                    TravelPackage travelPackage = new TravelPackage(packageId, name, description, price,
                            new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

                    travelPackages.add(travelPackage);
                }

                for (TravelPackage travelPackage : travelPackages) {
                    String flightQuery = "SELECT * FROM flight INNER JOIN package_flight ON flight.flight_id = package_flight.flight_id " +
                            "WHERE package_flight.package_id = ?";
                    PreparedStatement flightStatement = connection.prepareStatement(flightQuery);
                    flightStatement.setString(1, travelPackage.getPackageId());
                    ResultSet flightResultSet = flightStatement.executeQuery();

                    while (flightResultSet.next()) {
                        String flightId = flightResultSet.getString("flight_id");
                        String airline = flightResultSet.getString("airline");
                        Timestamp departure = flightResultSet.getTimestamp("departure");
                        Timestamp arrival = flightResultSet.getTimestamp("arrival");
                        double flightPrice = flightResultSet.getDouble("price");

                        Flight flight = new Flight(flightId, airline, departure, arrival, flightPrice);
                        travelPackage.addFlight(flight);
                    }

                    String hotelQuery = "SELECT * FROM hotel INNER JOIN package_hotel ON hotel.hotel_id = package_hotel.hotel_id " +
                            "WHERE package_hotel.package_id = ?";
                    PreparedStatement hotelStatement = connection.prepareStatement(hotelQuery);
                    hotelStatement.setString(1, travelPackage.getPackageId());
                    ResultSet hotelResultSet = hotelStatement.executeQuery();

                    while (hotelResultSet.next()) {
                        String hotelId = hotelResultSet.getString("hotel_id");
                        String hotelName = hotelResultSet.getString("name");
                        String location = hotelResultSet.getString("location");
                        double hotelPrice = hotelResultSet.getDouble("price");

                        Hotel hotel = new Hotel(hotelId, hotelName, location, hotelPrice);
                        travelPackage.addHotel(hotel);
                    }

                    String activityQuery = "SELECT * FROM activity INNER JOIN package_activity ON activity.activity_id = package_activity.activity_id " +
                            "WHERE package_activity.package_id = ?";
                    PreparedStatement activityStatement = connection.prepareStatement(activityQuery);
                    activityStatement.setString(1, travelPackage.getPackageId());
                    ResultSet activityResultSet = activityStatement.executeQuery();

                    while (activityResultSet.next()) {
                        String activityId = activityResultSet.getString("activity_id");
                        String activityName = activityResultSet.getString("name");
                        String activityDescription = activityResultSet.getString("description");
                        double activityPrice = activityResultSet.getDouble("price");

                        Activity activity = new Activity(activityId, activityName, activityDescription, activityPrice);
                        travelPackage.addActivity(activity);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return travelPackages;

    }

    @Override
    public List<TravelPackage> getPackagesInCartForAgent(String userId, String agentId) {
        List<String> packageIds = new ArrayList<>();

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        try (Connection connection = DriverManager.getConnection(CONFIG.SQLURL, CONFIG.SQLUSER, CONFIG.SQLPASS)) {
            String cartQuery = "SELECT package_id FROM cart WHERE userId = ? AND agentId = ?";
            PreparedStatement cartStatement = connection.prepareStatement(cartQuery);
            cartStatement.setString(1, userId);
            cartStatement.setString(2, agentId);

            ResultSet cartResultSet = cartStatement.executeQuery();

            while (cartResultSet.next()) {
                String packageId = cartResultSet.getString("package_id");
                packageIds.add(packageId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        List<TravelPackage> travelPackages = new ArrayList<>();

        if (!packageIds.isEmpty()) {

            try (Connection connection = DriverManager.getConnection(CONFIG.SQLURL, CONFIG.SQLUSER, CONFIG.SQLPASS)) {
                String packageQuery = "SELECT * FROM travel_package WHERE package_id IN (";
                for (int i = 0; i < packageIds.size(); i++) {
                    packageQuery += "?";
                    if (i < packageIds.size() - 1) {
                        packageQuery += ",";
                    }
                }
                packageQuery += ")";

                PreparedStatement packageStatement = connection.prepareStatement(packageQuery);
                for (int i = 0; i < packageIds.size(); i++) {
                    packageStatement.setString(i + 1, packageIds.get(i));
                }

                ResultSet packageResultSet = packageStatement.executeQuery();

                while (packageResultSet.next()) {
                    String packageId = packageResultSet.getString("package_id");
                    String name = packageResultSet.getString("name");
                    String description = packageResultSet.getString("description");
                    double price = packageResultSet.getDouble("price");

                    TravelPackage travelPackage = new TravelPackage(packageId, name, description, price,
                            new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

                    travelPackages.add(travelPackage);
                }

                for (TravelPackage travelPackage : travelPackages) {
                    String flightQuery = "SELECT * FROM flight INNER JOIN package_flight ON flight.flight_id = package_flight.flight_id " +
                            "WHERE package_flight.package_id = ?";
                    PreparedStatement flightStatement = connection.prepareStatement(flightQuery);
                    flightStatement.setString(1, travelPackage.getPackageId());
                    ResultSet flightResultSet = flightStatement.executeQuery();

                    while (flightResultSet.next()) {
                        String flightId = flightResultSet.getString("flight_id");
                        String airline = flightResultSet.getString("airline");
                        Timestamp departure = flightResultSet.getTimestamp("departure");
                        Timestamp arrival = flightResultSet.getTimestamp("arrival");
                        double flightPrice = flightResultSet.getDouble("price");

                        Flight flight = new Flight(flightId, airline, departure, arrival, flightPrice);
                        travelPackage.addFlight(flight);
                    }

                    String hotelQuery = "SELECT * FROM hotel INNER JOIN package_hotel ON hotel.hotel_id = package_hotel.hotel_id " +
                            "WHERE package_hotel.package_id = ?";
                    PreparedStatement hotelStatement = connection.prepareStatement(hotelQuery);
                    hotelStatement.setString(1, travelPackage.getPackageId());
                    ResultSet hotelResultSet = hotelStatement.executeQuery();

                    while (hotelResultSet.next()) {
                        String hotelId = hotelResultSet.getString("hotel_id");
                        String hotelName = hotelResultSet.getString("name");
                        String location = hotelResultSet.getString("location");
                        double hotelPrice = hotelResultSet.getDouble("price");

                        Hotel hotel = new Hotel(hotelId, hotelName, location, hotelPrice);
                        travelPackage.addHotel(hotel);
                    }

                    String activityQuery = "SELECT * FROM activity INNER JOIN package_activity ON activity.activity_id = package_activity.activity_id " +
                            "WHERE package_activity.package_id = ?";
                    PreparedStatement activityStatement = connection.prepareStatement(activityQuery);
                    activityStatement.setString(1, travelPackage.getPackageId());
                    ResultSet activityResultSet = activityStatement.executeQuery();

                    while (activityResultSet.next()) {
                        String activityId = activityResultSet.getString("activity_id");
                        String activityName = activityResultSet.getString("name");
                        String activityDescription = activityResultSet.getString("description");
                        double activityPrice = activityResultSet.getDouble("price");

                        Activity activity = new Activity(activityId, activityName, activityDescription, activityPrice);
                        travelPackage.addActivity(activity);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return travelPackages;
    }

    @Override
    public Boolean removeFromCartForAgent(String package_id, String userId, String agentId) {
        try (Connection connection = DriverManager.getConnection(CONFIG.SQLURL, CONFIG.SQLUSER, CONFIG.SQLPASS)) {
            // Construct the SQL query
            String query = "DELETE FROM cart WHERE package_id = ? AND userId = ? AND agentid = ?";

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                // Set the parameter values
                statement.setString(1, package_id);
                statement.setString(2, userId);
                statement.setString(3, agentId);

                // Execute the query
                int rowsAffected = statement.executeUpdate();

                if (rowsAffected > 0) {
                    // Cart item removed successfully
                    return true;
                } else {
                    // Failed to remove cart item
                    return false;
                }
            } catch (SQLException e) {
                // Handle any potential exceptions
                e.printStackTrace();
                return false;
            }
        } catch (SQLException e) {
            // Handle connection-related exceptions
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean removeFromCartForCustomer(String package_id, String userId) {
        try (Connection connection = DriverManager.getConnection(CONFIG.SQLURL, CONFIG.SQLUSER, CONFIG.SQLPASS)) {
            // Construct the SQL query
            String query = "DELETE FROM cart WHERE package_id = ? AND userId = ? AND agentid IS NULL";

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                // Set the parameter values
                statement.setString(1, package_id);
                statement.setString(2, userId);

                // Execute the query
                int rowsAffected = statement.executeUpdate();

                if (rowsAffected > 0) {
                    // Cart item removed successfully
                    return true;
                } else {
                    // Failed to remove cart item
                    return false;
                }
            } catch (SQLException e) {
                // Handle any potential exceptions
                e.printStackTrace();
                return false;
            }
        } catch (SQLException e) {
            // Handle connection-related exceptions
            e.printStackTrace();
            return false;
        }
    }

}
