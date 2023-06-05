package com.example.coen_mp_concordiatravelwebapplication.presentation.controllers;

import com.example.coen_mp_concordiatravelwebapplication.dataaccess.*;
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
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@WebServlet(name = "ModifyPackageServlet", value = "/ModifyPackageServlet")
public class ModifyPackageServlet extends HttpServlet {

    private PackageDAO packageDAO;
    private CustomerDAO customerDAO;
    private UserDAO userDAO;
    private BookingDAO bookingDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        // Initialize the implementation
        packageDAO = new PackageDAOImpl();
        customerDAO = new CustomerDAOImpl();
        userDAO = new UserDAOImpl();
        bookingDAO = new BookingDAOImpl();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
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

        TravelPackage travelPackage = new TravelPackage();
        travelPackage.setName(name);
        travelPackage.setDescription(description);
        travelPackage.setPrice(price);


        for (int i = 0; i < flightIds.length; i++) {
            String flightId = flightIds[i];
            String airline = airlines[i];

            // Convert departure and arrival strings to timestamps
            SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            Timestamp departure = null;

            try {
                departure = new Timestamp(inputDateFormat.parse(departures[i]).getTime());
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            Timestamp arrival = null;
            try {
                arrival = new Timestamp(inputDateFormat.parse(arrivals[i]).getTime());
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            double flightPrice = Double.parseDouble(flightPrices[i]);

            // Create a new Flight object with the extracted values
            Flight flight = new Flight(flightId, airline, departure, arrival, flightPrice);

            // Add the Flight object to the list
            travelPackage.addFlight(flight);
        }


        for (int i = 0; i < hotelIds.length; i++) {
            String hotelId = hotelIds[i];
            String hotelName = hotelNames[i];
            String location = locations[i];
            double hotelPrice = Double.parseDouble(hotelPrices[i]);

            // Create a new Hotel object with the extracted values
            Hotel hotel = new Hotel(hotelId, hotelName, location, hotelPrice);

            // Add the Hotel object to the list
            travelPackage.addHotel(hotel);
        }


        for (int i = 0; i < activityIds.length; i++) {
            String activityId = activityIds[i];
            String activityName = activityNames[i];
            String activityDescription = activityDescriptions[i];
            double acitivityPrice = Double.parseDouble(activityPrices[i]);

            // Create a new Activity object with the extracted values
            Activity activity = new Activity(activityId, activityName, activityDescription, acitivityPrice);

            // Add the Activity object to the list
            travelPackage.addActivity(activity);
        }


        Boolean modifyCompletion = packageDAO.modifyPackageDetails(packageId, travelPackage);

        if (modifyCompletion) {
            // Redirect to the success page after the modification is complete
            response.sendRedirect("modify_success.jsp");
        } else {
            request.setAttribute("errorMessage", "An error occurred while processing the request for modifying the package.");
            request.setAttribute("errorCode", 500);
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
}
