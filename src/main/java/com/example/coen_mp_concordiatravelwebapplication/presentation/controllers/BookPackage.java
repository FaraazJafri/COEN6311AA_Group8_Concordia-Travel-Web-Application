package com.example.coen_mp_concordiatravelwebapplication.presentation.controllers;

import com.example.coen_mp_concordiatravelwebapplication.dataaccess.*;
import com.example.coen_mp_concordiatravelwebapplication.models.bookingModels.Booking;
import com.example.coen_mp_concordiatravelwebapplication.models.bookingModels.Customer;
import com.example.coen_mp_concordiatravelwebapplication.models.packageModels.TravelPackage;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "BookPackageServlet", value = "/BookPackageServlet")
public class BookPackage extends HttpServlet {
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

        // Get all the travel packages
        List<TravelPackage> travelPackages = packageDAO.getAllPackages();
        List<Customer> customers = customerDAO.getAllCustomers();
        List<Booking> bookings = new ArrayList<>();
        request.setAttribute("travelPackages", travelPackages);
        request.setAttribute("customers", customers);

        // Forward the request to the JSP page
        request.getRequestDispatcher("bookapackage.jsp").forward(request, response);

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession();
        int customerID = 0;
        if ("Customer".equals(session.getAttribute("role"))) {
            String customerUsername = (String) session.getAttribute("username");
            customerID = userDAO.getID(customerUsername);
        }

        System.out.println();
        String customerIdToBook;
        // Retrieve the package ID, customer ID, and departure date from the request
        String packageIdToBook = request.getParameter("packageId");
        if ("Customer".equals(session.getAttribute("role"))) {
            customerIdToBook = String.valueOf(customerID);
        } else {
            customerIdToBook = request.getParameter("customerId");
        }
        String departureDate = request.getParameter("departureDate");

        String bookingId = generateBookingId();

        bookingDAO.saveBooking(bookingId, packageIdToBook, customerIdToBook, departureDate);
        List<Booking> bookings = bookingDAO.getAllBookings();
        request.setAttribute("bookings", bookings);

        request.getRequestDispatcher("createpackagesuccess.jsp").forward(request, response);
    }

    private String generateBookingId() {
        int random = (int) (Math.random() * 9000) + 1000;
        long timestamp = System.currentTimeMillis();
        String bookingId = String.valueOf(random) + String.valueOf(timestamp);
        bookingId = bookingId.substring(bookingId.length() - 4);
        return bookingId;
    }
}
