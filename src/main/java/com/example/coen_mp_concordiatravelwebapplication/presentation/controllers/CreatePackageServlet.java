package com.example.coen_mp_concordiatravelwebapplication.presentation.controllers;

import com.example.coen_mp_concordiatravelwebapplication.dataaccess.PackageDAO;
import com.example.coen_mp_concordiatravelwebapplication.dataaccess.PackageDAOImpl;
import com.example.coen_mp_concordiatravelwebapplication.models.packageModels.Activity;
import com.example.coen_mp_concordiatravelwebapplication.models.packageModels.Flight;
import com.example.coen_mp_concordiatravelwebapplication.models.packageModels.Hotel;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "CreatePackageServlet", value = "/CreatePackageServlet")
public class CreatePackageServlet extends HttpServlet {
    private PackageDAO packageDAO;
    @Override
    public void init() throws ServletException {
        super.init();
        // Initialize the PackageDAO implementation
        packageDAO = new PackageDAOImpl();
    }
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
        packageDAO.savePackage(packageId, packageName, packageDescription, packagePrice, flights, hotels, activities);

        // Perform further processing or store the retrieved information as needed

        // Redirect or forward to a success page
        response.sendRedirect("createpackagesuccess.jsp");
    }
}
