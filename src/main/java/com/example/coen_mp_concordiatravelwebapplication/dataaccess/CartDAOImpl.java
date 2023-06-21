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
    public void addToCart(String userId, String packageId, String agentId, String customPackageId, Timestamp departureDate) {

        try (Connection connection = DriverManager.getConnection(CONFIG.SQLURL, CONFIG.SQLUSER, CONFIG.SQLPASS)) {
            String query = "INSERT INTO cart (userId, package_id, custom_package_id, agentid, departure_date) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, Integer.parseInt(userId));
            statement.setString(2, packageId);
            if (customPackageId != null) {
                statement.setInt(3, Integer.parseInt(customPackageId));
            } else {
                statement.setNull(3, Types.INTEGER);
            }
            if (agentId != null) {
                statement.setInt(4, Integer.parseInt(agentId));
            } else {
                statement.setNull(4, Types.INTEGER);
            }
            statement.setTimestamp(5, departureDate);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<TravelPackage> getPackagesInCartForCustomer(String userId) {

        List<String> packageIds = this.getPackageIdsInCartForCustomer(userId);

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

        List<String> packageIds = getPackageIdsInCartForAgent(userId, agentId);

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
    public List<String> getPackageIdsInCartForCustomer(String userId) {
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
        return packageIds;
    }

    @Override
    public List<String> getCustomPackageIdsInCartForCustomer(String userId) {
        List<String> customPackageIds = new ArrayList<>();

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        try (Connection connection = DriverManager.getConnection(CONFIG.SQLURL, CONFIG.SQLUSER, CONFIG.SQLPASS)) {
            String cartQuery = "SELECT custom_package_id FROM cart WHERE userId = ? AND agentId IS NULL";
            PreparedStatement cartStatement = connection.prepareStatement(cartQuery);
            cartStatement.setString(1, userId);

            ResultSet cartResultSet = cartStatement.executeQuery();

            while (cartResultSet.next()) {
                String customPackageId = cartResultSet.getString("custom_package_id");
                customPackageIds.add(customPackageId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customPackageIds;
    }

    @Override
    public List<String> getPackageIdsInCartForAgent(String userId, String agentId) {
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
        return packageIds;
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

    @Override
    public Boolean removeFromCartForUserPackage(String customPackageId, String userId) {
        try (Connection connection = DriverManager.getConnection(CONFIG.SQLURL, CONFIG.SQLUSER, CONFIG.SQLPASS)) {
            // Construct the SQL query
            String query = "DELETE FROM cart WHERE custom_package_id = ? AND userId = ? AND agentid IS NULL";

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                // Set the parameter values
                statement.setString(1, customPackageId);
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


    @Override
    public boolean checkDuplicatePackage(String customerId, String packageId, String agentId) {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        try (Connection connection = DriverManager.getConnection(CONFIG.SQLURL, CONFIG.SQLUSER, CONFIG.SQLPASS)) {
            String query;
            PreparedStatement statement;

            if (agentId == null) {
                query = "SELECT COUNT(*) AS count FROM cart WHERE userId = ? AND package_id = ?";
                statement = connection.prepareStatement(query);
                statement.setString(1, customerId);
                statement.setString(2, packageId);
            } else {
                query = "SELECT COUNT(*) AS count FROM cart WHERE userId = ? AND package_id = ? AND agentId = ?";
                statement = connection.prepareStatement(query);
                statement.setString(1, customerId);
                statement.setString(2, packageId);
                statement.setString(3, agentId);
            }

            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt("count");
            return count > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Timestamp getDepartureDate(String userId, String packageId, String customPackageId, String agentId) {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        try (Connection connection = DriverManager.getConnection(CONFIG.SQLURL, CONFIG.SQLUSER, CONFIG.SQLPASS)) {
            String query;
            PreparedStatement statement;

            if (agentId != null) {
                query = "SELECT departure_date FROM cart WHERE userId = ? AND (package_id = ? OR custom_package_id = ?) and agentId = ?";
                statement = connection.prepareStatement(query);
                statement.setString(1, userId);
                statement.setString(2, packageId);
                statement.setString(3, customPackageId);
                statement.setString(4, agentId);
            } else {
                query = "SELECT departure_date FROM cart WHERE userId = ? AND (package_id = ? OR custom_package_id = ?) AND agentId is NULL";
                statement = connection.prepareStatement(query);
                statement.setString(1, userId);
                statement.setString(2, packageId);
                statement.setString(3, customPackageId);
            }

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getTimestamp("departure_date");
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
