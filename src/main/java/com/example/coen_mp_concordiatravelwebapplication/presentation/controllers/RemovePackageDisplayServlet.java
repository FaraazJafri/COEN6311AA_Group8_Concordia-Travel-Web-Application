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

@WebServlet(name = "RemovePackageDisplayServlet", value = "/RemovePackageDisplayServlet")
public class RemovePackageDisplayServlet extends HttpServlet {
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


        // Set the travel packages as a request attribute
        request.setAttribute("travelPackages", travelPackages);

        // Forward the request to the JSP page
        request.getRequestDispatcher("removepackagedisplay.jsp").forward(request, response);


    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        // Retrieve the package ID to delete from the request
        String packageIdToDelete = request.getParameter("packageId");

        Boolean removeAnswer = packageDAO.removePackage(packageIdToDelete);

        if(removeAnswer) {
            // Redirect to the successful deletion page
            response.sendRedirect("deletesuccessful.jsp");
        }else{
            request.setAttribute("errorMessage", "An error occurred while processing the request for deleting the package.");
            request.setAttribute("errorCode", 500);
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }


    }


}