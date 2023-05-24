package com.example.coen_mp_concordiatravelwebapplication;

import com.example.coen_mp_concordiatravelwebapplication.config.CONFIG;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet(name = "ModifyPackageServlet", value = "/ModifyPackageServlet")
public class ModifyPackageServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        // Get the updated package information from the request parameters
        String packageId = request.getParameter("packageId");
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        double price = Double.parseDouble(request.getParameter("price"));

        // Get the updated flights, hotels, and activities information from the request parameters
        String[] flightIds = request.getParameterValues("flights");
        String[] airlines = request.getParameterValues("airlines");
        String[] departures = request.getParameterValues("departures");
        String[] arrivals = request.getParameterValues("arrivals");
        String[] flightPrices = request.getParameterValues("prices");

        String[] hotelIds = request.getParameterValues("hotels");
        String[] hotelNames = request.getParameterValues("hotelNames");
        String[] locations = request.getParameterValues("locations");
        String[] hotelPrices = request.getParameterValues("hotelPrices");

        String[] activityIds = request.getParameterValues("activities");
        String[] activityNames = request.getParameterValues("activityNames");
        String[] activityDescriptions = request.getParameterValues("descriptions");
        String[] activityPrices = request.getParameterValues("activityPrices");

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        try (Connection connection = DriverManager.getConnection(CONFIG.SQLURL, CONFIG.SQLUSER, CONFIG.SQLPASS)) {
            // Update the travel package information
            String updatePackageQuery = "UPDATE travel_package SET name=?, description=?, price=? WHERE package_id=?";
            PreparedStatement updatePackageStatement = connection.prepareStatement(updatePackageQuery);
            updatePackageStatement.setString(1, name);
            updatePackageStatement.setString(2, description);
            updatePackageStatement.setDouble(3, price);
            updatePackageStatement.setString(4, packageId);
            updatePackageStatement.executeUpdate();

            // Update the flights information
            String updateFlightQuery = "UPDATE flight SET airline=?, departure=?, arrival=?, price=? WHERE flight_id=?";
            PreparedStatement updateFlightStatement = connection.prepareStatement(updateFlightQuery);
            for (int i = 0; i < flightIds.length; i++) {
                updateFlightStatement.setString(1, airlines[i]);
                updateFlightStatement.setString(2, departures[i]);
                updateFlightStatement.setString(3, arrivals[i]);
                updateFlightStatement.setDouble(4, Double.parseDouble(flightPrices[i]));
                updateFlightStatement.setString(5, flightIds[i]);
                updateFlightStatement.executeUpdate();
            }

            // Update the hotels information
            String updateHotelQuery = "UPDATE hotel SET name=?, location=?, price=? WHERE hotel_id=?";
            PreparedStatement updateHotelStatement = connection.prepareStatement(updateHotelQuery);
            for (int i = 0; i < hotelIds.length; i++) {
                updateHotelStatement.setString(1, hotelNames[i]);
                updateHotelStatement.setString(2, locations[i]);
                updateHotelStatement.setDouble(3, Double.parseDouble(hotelPrices[i]));
                updateHotelStatement.setString(4, hotelIds[i]);
                updateHotelStatement.executeUpdate();
            }

            // Update the activities information
            String updateActivityQuery = "UPDATE activity SET name=?, description=?, price=? WHERE activity_id=?";
            PreparedStatement updateActivityStatement = connection.prepareStatement(updateActivityQuery);
            for (int i = 0; i < activityIds.length; i++) {
                updateActivityStatement.setString(1, activityNames[i]);
                updateActivityStatement.setString(2, activityDescriptions[i]);
                updateActivityStatement.setDouble(3, Double.parseDouble(activityPrices[i]));
                updateActivityStatement.setString(4, activityIds[i]);
                updateActivityStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }

        // Redirect to the success page after the modification is complete
        response.sendRedirect("modify_success.jsp");
    }
}
