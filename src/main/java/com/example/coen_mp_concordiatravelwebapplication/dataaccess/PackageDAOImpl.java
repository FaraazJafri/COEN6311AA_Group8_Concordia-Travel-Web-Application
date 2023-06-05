package com.example.coen_mp_concordiatravelwebapplication.dataaccess;

import com.example.coen_mp_concordiatravelwebapplication.config.CONFIG;
import com.example.coen_mp_concordiatravelwebapplication.models.packageModels.Activity;
import com.example.coen_mp_concordiatravelwebapplication.models.packageModels.Flight;
import com.example.coen_mp_concordiatravelwebapplication.models.packageModels.Hotel;
import com.example.coen_mp_concordiatravelwebapplication.models.packageModels.TravelPackage;
import jakarta.servlet.ServletException;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;


public class PackageDAOImpl implements PackageDAO {
    @Override
    public List<TravelPackage> getAllPackages() {
        List<TravelPackage> travelPackages = new ArrayList<>();

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
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
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
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
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
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
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
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

    @Override
    public TravelPackage getSelectedTravelPackage(String packageId) throws ServletException {

        TravelPackage travelPackage = new TravelPackage();
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        // JDBC connection details
        String url = CONFIG.SQLURL;
        String username = CONFIG.SQLUSER;
        String password = CONFIG.SQLPASS;


        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            // Create a prepared statement to retrieve the package information
            String query = "SELECT * FROM travel_package WHERE package_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, packageId);

            // Execute the query
            ResultSet resultSet = statement.executeQuery();

            // Check if a package with the given ID exists
            if (resultSet.next()) {
                // Retrieve the package details from the result set
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                double price = resultSet.getDouble("price");

                // Create a travel package object
                travelPackage = new TravelPackage(packageId, name, description, price,
                        new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

                // Retrieve flights associated with the package
                query = "SELECT * FROM flight INNER JOIN package_flight ON flight.flight_id = package_flight.flight_id " +
                        "WHERE package_flight.package_id = ?";
                statement = connection.prepareStatement(query);
                statement.setString(1, packageId);
                resultSet = statement.executeQuery();

                // Process the flights and add them to the travel package
                while (resultSet.next()) {
                    // Retrieve flight details
                    String flightId = resultSet.getString("flight_id");
                    String airline = resultSet.getString("airline");
                    Timestamp departure = resultSet.getTimestamp("departure");
                    Timestamp arrival = resultSet.getTimestamp("arrival");
                    double flightPrice = resultSet.getDouble("price");

                    // Create a Flight object
                    Flight flight = new Flight(flightId, airline, departure, arrival, flightPrice);
                    travelPackage.addFlight(flight);
                }

                // Retrieve hotels associated with the package
                query = "SELECT * FROM hotel INNER JOIN package_hotel ON hotel.hotel_id = package_hotel.hotel_id " +
                        "WHERE package_hotel.package_id = ?";
                statement = connection.prepareStatement(query);
                statement.setString(1, packageId);
                resultSet = statement.executeQuery();

                // Process the hotels and add them to the travel package
                while (resultSet.next()) {
                    // Retrieve hotel details
                    String hotelId = resultSet.getString("hotel_id");
                    String hotelName = resultSet.getString("name");
                    String location = resultSet.getString("location");
                    double hotelPrice = resultSet.getDouble("price");

                    // Create a Hotel object
                    Hotel hotel = new Hotel(hotelId, hotelName, location, hotelPrice);
                    travelPackage.addHotel(hotel);
                }

                // Retrieve activities associated with the package
                query = "SELECT * FROM activity INNER JOIN package_activity ON activity.activity_id = package_activity.activity_id " +
                        "WHERE package_activity.package_id = ?";
                statement = connection.prepareStatement(query);
                statement.setString(1, packageId);
                resultSet = statement.executeQuery();

                // Process the activities and add them to the travel package
                while (resultSet.next()) {
                    // Retrieve activity details
                    String activityId = resultSet.getString("activity_id");
                    String activityName = resultSet.getString("name");
                    String activityDescription = resultSet.getString("description");
                    double activityPrice = resultSet.getDouble("price");

                    // Create an Activity object
                    Activity activity = new Activity(activityId, activityName, activityDescription, activityPrice);
                    travelPackage.addActivity(activity);
                }
            }

            // Close the result set and statement
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return travelPackage;
    }

    @Override
    public Boolean modifyPackageDetails(String packageId, TravelPackage travelPackage) throws ServletException {

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }


        try (Connection connection = DriverManager.getConnection(CONFIG.SQLURL, CONFIG.SQLUSER, CONFIG.SQLPASS)) {
            // Update the travel package information
            String updatePackageQuery = "UPDATE travel_package SET name=?, description=?, price=? WHERE package_id=?";
            PreparedStatement updatePackageStatement = connection.prepareStatement(updatePackageQuery);
            updatePackageStatement.setString(1, travelPackage.getName());
            updatePackageStatement.setString(2, travelPackage.getDescription());
            updatePackageStatement.setDouble(3, travelPackage.getPrice());
            updatePackageStatement.setString(4, packageId);
            updatePackageStatement.executeUpdate();

            // Update the flights information
            String updateFlightQuery = "UPDATE flight SET airline=?, departure=?, arrival=?, price=? WHERE flight_id=?";
            PreparedStatement updateFlightStatement = connection.prepareStatement(updateFlightQuery);
            List<Flight> flights = travelPackage.getFlights();
            for (int i = 0; i < flights.size(); i++) {
                updateFlightStatement.setString(1, flights.get(i).getAirline());

                updateFlightStatement.setTimestamp(2, flights.get(i).getDeparture());
                updateFlightStatement.setTimestamp(3, flights.get(i).getArrival());
                updateFlightStatement.setDouble(4, flights.get(i).getPrice());
                updateFlightStatement.setString(5, flights.get(i).getFlightId());
                updateFlightStatement.executeUpdate();
            }


            // Update the hotels information
            String updateHotelQuery = "UPDATE hotel SET name=?, location=?, price=? WHERE hotel_id=?";
            PreparedStatement updateHotelStatement = connection.prepareStatement(updateHotelQuery);
            List<Hotel> hotels = travelPackage.getHotels();
            for (int i = 0; i < hotels.size(); i++) {
                updateHotelStatement.setString(1, hotels.get(i).getName());
                updateHotelStatement.setString(2, hotels.get(i).getLocation());
                updateHotelStatement.setDouble(3, hotels.get(i).getPrice());
                updateHotelStatement.setString(4, hotels.get(i).getHotelId());
                updateHotelStatement.executeUpdate();
            }

            // Update the activities information
            String updateActivityQuery = "UPDATE activity SET name=?, description=?, price=? WHERE activity_id=?";
            PreparedStatement updateActivityStatement = connection.prepareStatement(updateActivityQuery);
            List<Activity> activities = travelPackage.getActivities();
            for (int i = 0; i < activities.size(); i++) {
                updateActivityStatement.setString(1, activities.get(i).getName());
                updateActivityStatement.setString(2, activities.get(i).getDescription());
                updateActivityStatement.setDouble(3, activities.get(i).getPrice());
                updateActivityStatement.setString(4, activities.get(i).getActivityId());
                updateActivityStatement.executeUpdate();
            }

            connection.commit();

        } catch (SQLException e) {
            throw new ServletException(e);
        }

        return true;
    }

    @Override
    public Boolean removePackage(String packageId) throws ServletException {
        // JDBC connection details
        String url = CONFIG.SQLURL;
        String username = CONFIG.SQLUSER;
        String password = CONFIG.SQLPASS;

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }


        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            // Disable auto-commit to create a transaction
            connection.setAutoCommit(false);

            try (PreparedStatement retrieveFlightIdsStmt = connection.prepareStatement(
                    "SELECT flight_id FROM package_flight WHERE package_id = ?")) {
                retrieveFlightIdsStmt.setString(1, packageId);
                try (ResultSet flightIdsResultSet = retrieveFlightIdsStmt.executeQuery()) {
                    // Retrieve the flight IDs related to the package
                    List<String> flightIds = new ArrayList<>();

                    // Collect flight IDs
                    while (flightIdsResultSet.next()) {
                        String flightId = flightIdsResultSet.getString("flight_id");
                        flightIds.add(flightId);
                    }

                    // Close the ResultSet for flight IDs
                    flightIdsResultSet.close();

                    // Delete the package_flight entries related to the package
                    try (PreparedStatement deletePackageFlightStmt = connection.prepareStatement(
                            "DELETE FROM package_flight WHERE package_id = ?")) {
                        deletePackageFlightStmt.setString(1, packageId);
                        deletePackageFlightStmt.executeUpdate();
                    }

                    try (PreparedStatement retrieveHotelIdsStmt = connection.prepareStatement(
                            "SELECT hotel_id FROM package_hotel WHERE package_id = ?")) {
                        retrieveHotelIdsStmt.setString(1, packageId);
                        try (ResultSet hotelIdsResultSet = retrieveHotelIdsStmt.executeQuery()) {
                            // Retrieve the hotel IDs related to the package
                            List<String> hotelIds = new ArrayList<>();

                            // Collect hotel IDs
                            while (hotelIdsResultSet.next()) {
                                String hotelId = hotelIdsResultSet.getString("hotel_id");
                                hotelIds.add(hotelId);
                            }

                            // Close the ResultSet for hotel IDs
                            hotelIdsResultSet.close();

                            // Delete the package_hotel entries related to the package
                            try (PreparedStatement deletePackageHotelStmt = connection.prepareStatement(
                                    "DELETE FROM package_hotel WHERE package_id = ?")) {
                                deletePackageHotelStmt.setString(1, packageId);
                                deletePackageHotelStmt.executeUpdate();
                            }

                            try (PreparedStatement retrieveActivityIdsStmt = connection.prepareStatement(
                                    "SELECT activity_id FROM package_activity WHERE package_id = ?")) {
                                retrieveActivityIdsStmt.setString(1, packageId);
                                try (ResultSet activityIdsResultSet = retrieveActivityIdsStmt.executeQuery()) {
                                    // Retrieve the activity IDs related to the package
                                    List<String> activityIds = new ArrayList<>();

                                    // Collect activity IDs
                                    while (activityIdsResultSet.next()) {
                                        String activityId = activityIdsResultSet.getString("activity_id");
                                        activityIds.add(activityId);
                                    }

                                    // Close the ResultSet for activity IDs
                                    activityIdsResultSet.close();

                                    // Delete the package_activity entries related to the package
                                    try (PreparedStatement deletePackageActivityStmt = connection.prepareStatement(
                                            "DELETE FROM package_activity WHERE package_id = ?")) {
                                        deletePackageActivityStmt.setString(1, packageId);
                                        deletePackageActivityStmt.executeUpdate();
                                    }

                                    // Delete the package entry
                                    try (PreparedStatement deletePackageStmt = connection.prepareStatement(
                                            "DELETE FROM travel_package WHERE package_id = ?")) {
                                        deletePackageStmt.setString(1, packageId);
                                        deletePackageStmt.executeUpdate();
                                    }

                                    // Commit the transaction
                                    connection.commit();

                                    // Delete the entries from hotels, flights, and activities
                                    deleteEntriesFromHotels(connection, hotelIds);
                                    deleteEntriesFromFlights(connection, flightIds);
                                    deleteEntriesFromActivities(connection, activityIds);

                                    connection.commit();
                                    return true;
                                }
                            }
                        }
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void deleteEntriesFromHotels(Connection connection, List<String> hotelIds) throws SQLException {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try (PreparedStatement deleteHotelStmt = connection.prepareStatement(
                "DELETE FROM hotel WHERE hotel_id = ?")) {
            for (String hotelId : hotelIds) {
                deleteHotelStmt.setString(1, hotelId);
                deleteHotelStmt.executeUpdate();
            }
        }
    }

    private void deleteEntriesFromFlights(Connection connection, List<String> flightIds) throws SQLException {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try (PreparedStatement deleteFlightStmt = connection.prepareStatement(
                "DELETE FROM flight WHERE flight_id = ?")) {
            for (String flightId : flightIds) {
                deleteFlightStmt.setString(1, flightId);
                deleteFlightStmt.executeUpdate();
            }
        }
    }

    private void deleteEntriesFromActivities(Connection connection, List<String> activityIds) throws SQLException {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try (PreparedStatement deleteActivityStmt = connection.prepareStatement(
                "DELETE FROM activity WHERE activity_id = ?")) {
            for (String activityId : activityIds) {
                deleteActivityStmt.setString(1, activityId);
                deleteActivityStmt.executeUpdate();
            }
        }
    }
}
