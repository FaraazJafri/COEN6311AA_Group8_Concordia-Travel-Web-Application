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
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

import static java.lang.Integer.parseInt;

@WebServlet(name = "ModifyCustomPackageForm", value = "/ModifyCustomPackageForm")
public class ModifyCustomPackageForm extends HttpServlet {
    private ActivityDAO activityDAO;
    private FlightDAO flightDAO;
    private HotelDAO hotelDAO;
    private UserDAO userDAO;

    private CustomPackageDAO customPackageDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        // Initialize the DAO implementations
        activityDAO = new ActivityDAOImpl();
        flightDAO = new FlightDAOImpl();
        hotelDAO = new HotelDAOImpl();
        customPackageDAO = new CustomPackageDAOImpl();
        userDAO = new UserDAOImpl();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Activity> activities = activityDAO.getAllActivities();
        List<Flight> flights = flightDAO.getAllFlights();
        List<Hotel> hotels = hotelDAO.getAllHotels();

        String packageId = request.getParameter("packageId");
        request.setAttribute("packageId", packageId);
        request.setAttribute("activities", activities);
        request.setAttribute("flights", flights);
        request.setAttribute("hotels", hotels);
        request.getRequestDispatcher("modifycustompackageform.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String packageId = request.getParameter("packageId");
        packageId = String.valueOf(Integer.parseInt(packageId)+499);
        String activityIds = request.getParameter("activityIds");
        String flightIds = request.getParameter("flightIds");
        String hotelIds = request.getParameter("hotelIds");
        HttpSession session = request.getSession();
        String customerUsername = (String) session.getAttribute("username");
        String userID = String.valueOf(userDAO.getID(customerUsername));
        if (customPackageDAO.modifyCustomPackage(userID, packageId, activityIds, flightIds, hotelIds)) {
            request.setAttribute("heading", "Custom Package Modified:");
            request.setAttribute("message", "Your package is modified successfully!!");
            request.getRequestDispatcher("modify_success.jsp").forward(request, response);
        } else {
            request.setAttribute("errorMessage", "Custom Package Modification Failed:");
            request.setAttribute("errorCode", "Your package modification has failed!!");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
}
