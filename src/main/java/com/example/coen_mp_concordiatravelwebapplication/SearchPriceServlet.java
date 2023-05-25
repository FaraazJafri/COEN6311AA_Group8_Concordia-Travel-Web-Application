package com.example.coen_mp_concordiatravelwebapplication;

import com.example.coen_mp_concordiatravelwebapplication.config.CONFIG;
import com.example.coen_mp_concordiatravelwebapplication.models.packageModels.Activity;
import com.example.coen_mp_concordiatravelwebapplication.models.packageModels.Flight;
import com.example.coen_mp_concordiatravelwebapplication.models.packageModels.Hotel;
import com.example.coen_mp_concordiatravelwebapplication.models.packageModels.TravelPackage;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.annotation.WebServlet;

@WebServlet(name = "SearchPriceServlet", value = "/SearchPriceServlet")
public class SearchPriceServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Retrieve the price range from the form
        int minPrice = Integer.parseInt(request.getParameter("minPrice"));
        int maxPrice = Integer.parseInt(request.getParameter("maxPrice"));

        // Create an empty list to store the search results
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

        // Set the search results as a request attribute
        request.setAttribute("searchResults", searchResults);

        // Forward the request to the displaysearchresults.jsp page
        request.getRequestDispatcher("/displaysearchresults.jsp").forward(request, response);
    }
}
