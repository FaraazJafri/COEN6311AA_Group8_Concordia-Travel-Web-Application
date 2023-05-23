package com.example.coen_mp_concordiatravelwebapplication;

import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.example.coen_mp_concordiatravelwebapplication.models.packageModels.Activity;
import com.example.coen_mp_concordiatravelwebapplication.models.packageModels.Flight;
import com.example.coen_mp_concordiatravelwebapplication.models.packageModels.Hotel;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.example.coen_mp_concordiatravelwebapplication.config.CONFIG;

@WebServlet(name = "CreatePackageServlet", value = "/CreatePackageServlet")
public class CreatePackageServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Retrieve package information
        String packageId = request.getParameter("packageId");
        String packageName = request.getParameter("packageName");
        String packageDescription = request.getParameter("packageDescription");
        double packagePrice = Double.parseDouble(request.getParameter("packagePrice"));

        // Retrieve flight information
        List<Flight> flights = new ArrayList<>();
        String[] flightIds = request.getParameterValues("flightId");
        String[] airlines = request.getParameterValues("airline");
        String[] departures = request.getParameterValues("departure");
        String[] arrivals = request.getParameterValues("arrival");
        String[] flightPrices = request.getParameterValues("flightPrice");

        for (int i = 0; i < flightIds.length; i++) {
            String flightId = flightIds[i];
            String airline = airlines[i];
            String departureString = departures[i];
            String arrivalString = arrivals[i];
            double flightPrice = Double.parseDouble(flightPrices[i]);


            // Convert departure and arrival strings to timestamps
            SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            Timestamp departure = null;
            try {
                departure = new Timestamp(inputDateFormat.parse(departureString).getTime());
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            Timestamp arrival = null;
            try {
                arrival = new Timestamp(inputDateFormat.parse(arrivalString).getTime());
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            Flight flight = new Flight(flightId, airline, departure, arrival, flightPrice);
            flights.add(flight);
        }

        // Retrieve hotel information
        List<Hotel> hotels = new ArrayList<>();
        String[] hotelIds = request.getParameterValues("hotelId");
        String[] hotelNames = request.getParameterValues("hotelName");
        String[] hotelLocations = request.getParameterValues("hotelLocation");
        String[] hotelPrices = request.getParameterValues("hotelPrice");

        for (int i = 0; i < hotelIds.length; i++) {
            String hotelId = hotelIds[i];
            String hotelName = hotelNames[i];
            String hotelLocation = hotelLocations[i];
            double hotelPrice = Double.parseDouble(hotelPrices[i]);

            Hotel hotel = new Hotel(hotelId, hotelName, hotelLocation, hotelPrice);
            hotels.add(hotel);
        }

        // Retrieve activity information
        List<Activity> activities = new ArrayList<>();
        String[] activityIds = request.getParameterValues("activityId");
        String[] activityNames = request.getParameterValues("activityName");
        String[] activityDescriptions = request.getParameterValues("activityDescription");
        String[] activityPrices = request.getParameterValues("activityPrice");

        for (int i = 0; i < activityIds.length; i++) {
            String activityId = activityIds[i];
            String activityName = activityNames[i];
            String activityDescription = activityDescriptions[i];
            double activityPrice = Double.parseDouble(activityPrices[i]);

            Activity activity = new Activity(activityId, activityName, activityDescription, activityPrice);
            activities.add(activity);
        }

        // Save the package information into the database
        savePackageToDatabase(packageId, packageName, packageDescription, packagePrice, flights, hotels, activities);

        // Perform further processing or store the retrieved information as needed

        // Redirect or forward to a success page
        response.sendRedirect("createpackagesuccess.jsp");
    }

    private void savePackageToDatabase(String packageId, String packageName, String packageDescription,
                                       double packagePrice, List<Flight> flights, List<Hotel> hotels, List<Activity> activities) {
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
}
