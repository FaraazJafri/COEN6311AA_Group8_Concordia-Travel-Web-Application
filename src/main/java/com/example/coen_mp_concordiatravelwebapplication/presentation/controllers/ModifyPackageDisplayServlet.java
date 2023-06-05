package com.example.coen_mp_concordiatravelwebapplication.presentation.controllers;

import com.example.coen_mp_concordiatravelwebapplication.dataaccess.*;
import com.example.coen_mp_concordiatravelwebapplication.models.packageModels.TravelPackage;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "ModifyPackageDisplayServlet", value = "/ModifyPackageDisplayServlet")
public class ModifyPackageDisplayServlet extends HttpServlet {
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


    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {


        List<TravelPackage> travelPackages = packageDAO.getAllPackages();


        request.setAttribute("travelPackages", travelPackages);

        request.getRequestDispatcher("modifypackagedisplay.jsp").forward(request, response);

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {


        // Retrieve the package ID to modify from the request
        String packageIdToModify = request.getParameter("packageId");

        // Create a travel package object to store the retrieved package and related information
        TravelPackage travelPackage = packageDAO.getSelectedTravelPackage(packageIdToModify);
        // Set the travel package as a request attribute
        request.setAttribute("travelPackage", travelPackage);

        // Forward the request to the JSP page for displaying the editable form
        request.getRequestDispatcher("modifypackageform.jsp").forward(request, response);
    }
}