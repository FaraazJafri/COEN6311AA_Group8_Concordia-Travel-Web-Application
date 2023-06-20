package com.example.coen_mp_concordiatravelwebapplication.presentation.controllers;

import com.example.coen_mp_concordiatravelwebapplication.dataaccess.*;
import com.example.coen_mp_concordiatravelwebapplication.models.packageModels.Activity;
import com.example.coen_mp_concordiatravelwebapplication.models.packageModels.Flight;
import com.example.coen_mp_concordiatravelwebapplication.models.packageModels.Hotel;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "CreateCustomPackage", value = "/CreateCustomPackage")
public class CreateCustomPackage extends HttpServlet {
    private ActivityDAO activityDAO;
    private FlightDAO flightDAO;
    private HotelDAO hotelDAO;
    private UserDAO userDAO;

    private PackageDAO customPackageDAO;
    @Override
    public void init() throws ServletException {
        super.init();
        // Initialize the DAO implementations
        activityDAO = new ActivityDAOImpl();
        flightDAO = new FlightDAOImpl();
        hotelDAO = new HotelDAOImpl();
        customPackageDAO = new PackageDAOImpl();
        userDAO = new UserDAOImpl();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Retrieve activities, flights, and hotels from the database
        List<Activity> activities = activityDAO.getAllActivities();
        List<Flight> flights = flightDAO.getAllFlights();
        List<Hotel> hotels = hotelDAO.getAllHotels();

        // Set the attributes in the request object
        request.setAttribute("activities", activities);
        request.setAttribute("flights", flights);
        request.setAttribute("hotels", hotels);

        // Forward the request to the JSP for rendering
        request.getRequestDispatcher("createcustompackage.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve the submitted form data
        String activityIds = request.getParameter("activityIds");
        String flightIds = request.getParameter("flightIds");
        String hotelIds = request.getParameter("hotelIds");
        HttpSession session = request.getSession();
        String customerUsername = (String) session.getAttribute("username");
        String userID = String.valueOf(userDAO.getID(customerUsername));
        // Insert the data into the user_packages table
        if (customPackageDAO.addCustomPackage(userID, activityIds, flightIds, hotelIds)) {
            // Redirect the user to the login page
            request.setAttribute("heading", "Custom Package Created:");
            request.setAttribute("message", "Your package is created successfully!!");
            request.getRequestDispatcher("modify_success.jsp").forward(request, response);
        } else {
            request.setAttribute("errorMessage", "Custom Package Creation Failed:");
            request.setAttribute("errorCode", "Your package creation has failed!!");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

}
