package com.example.coen_mp_concordiatravelwebapplication;

import com.example.coen_mp_concordiatravelwebapplication.config.CONFIG;
import com.example.coen_mp_concordiatravelwebapplication.models.packageModels.Activity;
import com.example.coen_mp_concordiatravelwebapplication.models.packageModels.Flight;
import com.example.coen_mp_concordiatravelwebapplication.models.packageModels.Hotel;
import com.example.coen_mp_concordiatravelwebapplication.models.packageModels.TravelPackage;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "RemovePackageDisplayServlet", value = "/RemovePackageDisplayServlet")
public class RemovePackageDisplayServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        // JDBC connection details


        // Create an empty list to store travel packages
        List<TravelPackage> travelPackages = new ArrayList<>();

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        // Establish a connection to the database
        try (Connection connection = DriverManager.getConnection(CONFIG.SQLURL, CONFIG.SQLUSER, CONFIG.SQLPASS)) {
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

            // Set the travel packages as a request attribute
            request.setAttribute("travelPackages", travelPackages);

            // Forward the request to the JSP page
            request.getRequestDispatcher("removepackagedisplay.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        // JDBC connection details
        String url = CONFIG.SQLURL;
        String username = CONFIG.SQLUSER;
        String password = CONFIG.SQLPASS;

        // Retrieve the package ID to delete from the request
        String packageIdToDelete = request.getParameter("packageId");

        // Establish a connection to the database
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            // Disable auto-commit to create a transaction
            connection.setAutoCommit(false);

            try (PreparedStatement retrieveFlightIdsStmt = connection.prepareStatement(
                    "SELECT flight_id FROM package_flight WHERE package_id = ?")) {
                retrieveFlightIdsStmt.setString(1, packageIdToDelete);
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
                        deletePackageFlightStmt.setString(1, packageIdToDelete);
                        deletePackageFlightStmt.executeUpdate();
                    }

                    try (PreparedStatement retrieveHotelIdsStmt = connection.prepareStatement(
                            "SELECT hotel_id FROM package_hotel WHERE package_id = ?")) {
                        retrieveHotelIdsStmt.setString(1, packageIdToDelete);
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
                                deletePackageHotelStmt.setString(1, packageIdToDelete);
                                deletePackageHotelStmt.executeUpdate();
                            }

                            try (PreparedStatement retrieveActivityIdsStmt = connection.prepareStatement(
                                    "SELECT activity_id FROM package_activity WHERE package_id = ?")) {
                                retrieveActivityIdsStmt.setString(1, packageIdToDelete);
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
                                        deletePackageActivityStmt.setString(1, packageIdToDelete);
                                        deletePackageActivityStmt.executeUpdate();
                                    }

                                    // Delete the package entry
                                    try (PreparedStatement deletePackageStmt = connection.prepareStatement(
                                            "DELETE FROM travel_package WHERE package_id = ?")) {
                                        deletePackageStmt.setString(1, packageIdToDelete);
                                        deletePackageStmt.executeUpdate();
                                    }

                                    // Commit the transaction
                                    connection.commit();

                                    // Delete the entries from hotels, flights, and activities
                                    deleteEntriesFromHotels(connection, hotelIds);
                                    deleteEntriesFromFlights(connection, flightIds);
                                    deleteEntriesFromActivities(connection, activityIds);

                                    connection.commit();
                                    // Redirect to the successful deletion page
                                    response.sendRedirect("deletesuccessful.jsp");
                                }
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteEntriesFromHotels(Connection connection, List<String> hotelIds) throws SQLException {
        try (PreparedStatement deleteHotelStmt = connection.prepareStatement(
                "DELETE FROM hotel WHERE hotel_id = ?")) {
            for (String hotelId : hotelIds) {
                deleteHotelStmt.setString(1, hotelId);
                deleteHotelStmt.executeUpdate();
            }
        }
    }

    private void deleteEntriesFromFlights(Connection connection, List<String> flightIds) throws SQLException {
        try (PreparedStatement deleteFlightStmt = connection.prepareStatement(
                "DELETE FROM flight WHERE flight_id = ?")) {
            for (String flightId : flightIds) {
                deleteFlightStmt.setString(1, flightId);
                deleteFlightStmt.executeUpdate();
            }
        }
    }

    private void deleteEntriesFromActivities(Connection connection, List<String> activityIds) throws SQLException {
        try (PreparedStatement deleteActivityStmt = connection.prepareStatement(
                "DELETE FROM activity WHERE activity_id = ?")) {
            for (String activityId : activityIds) {
                deleteActivityStmt.setString(1, activityId);
                deleteActivityStmt.executeUpdate();
            }
        }
    }
}
