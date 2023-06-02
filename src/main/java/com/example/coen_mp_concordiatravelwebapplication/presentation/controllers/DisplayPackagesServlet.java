package com.example.coen_mp_concordiatravelwebapplication.presentation.controllers;

import com.example.coen_mp_concordiatravelwebapplication.dataaccess.PackageDAO;
import com.example.coen_mp_concordiatravelwebapplication.dataaccess.PackageDAOImpl;
import com.example.coen_mp_concordiatravelwebapplication.models.packageModels.TravelPackage;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "DisplayPackagesServlet", value = "/DisplayPackagesServlet")
public class DisplayPackagesServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private PackageDAO packageDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        // Initialize the PackageDAO implementation
        packageDAO = new PackageDAOImpl();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        // Create an empty list to store travel packages
        List<TravelPackage> travelPackages = packageDAO.getAllPackages();

        // Set the travel packages as a request attribute
        request.setAttribute("travelPackages", travelPackages);

        // Forward the request to the JSP page
        request.getRequestDispatcher("displaypackages.jsp").forward(request, response);
    }
}
