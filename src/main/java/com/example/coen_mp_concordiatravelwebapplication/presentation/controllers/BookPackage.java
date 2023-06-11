package com.example.coen_mp_concordiatravelwebapplication.presentation.controllers;

import com.example.coen_mp_concordiatravelwebapplication.dataaccess.*;
import com.example.coen_mp_concordiatravelwebapplication.models.bookingModels.Booking;
import com.example.coen_mp_concordiatravelwebapplication.models.bookingModels.Customer;
import com.example.coen_mp_concordiatravelwebapplication.models.packageModels.TravelPackage;
import com.example.coen_mp_concordiatravelwebapplication.models.userModels.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "BookPackageServlet", value = "/BookPackageServlet")
public class BookPackage extends HttpServlet {
    private PackageDAO packageDAO;
    private CustomerDAO customerDAO;
    private UserDAO userDAO;
    private BookingDAO bookingDAO;
    private NotificationDAO notificationDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        // Initialize the implementation
        packageDAO = new PackageDAOImpl();
        customerDAO = new CustomerDAOImpl();
        userDAO = new UserDAOImpl();
        bookingDAO = new BookingDAOImpl();
        notificationDAO = new NotificationDAOImpl();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        HttpSession session = request.getSession();
        // Get all the travel packages
        List<TravelPackage> travelPackages = packageDAO.getAllPackages();

        List<User> customers = new ArrayList<>();
        if ("Admin".equals(session.getAttribute("role"))) {
            // Retrieve the list of customers from the database
            customers = userDAO.getOnlyCustomers();
        }
        if ("Agent".equals(session.getAttribute("role"))) {
            // Retrieve the list of customers from the database
            String customerUsername = (String) session.getAttribute("username");
            String userId = String.valueOf(userDAO.getID(customerUsername));
            customers = userDAO.getLinkedCustomers(userId);
        }
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

        // Convert departure and arrival strings to timestamps
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        Timestamp departure = null;
        try {
            departure = new Timestamp(inputDateFormat.parse(departureDate).getTime());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }


        String bookingId = generateBookingId();

        bookingDAO.saveBooking(bookingId, packageIdToBook, customerIdToBook, departure);
        List<Booking> bookings = bookingDAO.getAllBookings();
        request.setAttribute("bookings", bookings);

        //Notification code
        if ("Customer".equals(session.getAttribute("role"))) {
            String message = "The booking was successful with the booking ID: " + bookingId + ", check your bookings in (View Your Bookings) section now!";
            notificationDAO.sendNotificationToUser(String.valueOf(customerID), message);
        }else if("Agent".equals(session.getAttribute("role"))){
            String customerUsername = (String) session.getAttribute("username");
            customerID = userDAO.getID(customerUsername);

            User user = userDAO.getUserById(String.valueOf(customerID));

            String fullname = user.getFirstName() + " " + user.getLastName();

            String message = "Your agent: " + fullname +" has booked a package with booking Id: " + bookingId + ", check your bookings in (View Your Bookings) section now!";
            notificationDAO.sendNotificationToUser(String.valueOf(customerIdToBook), message);
        }else if("Admin".equals(session.getAttribute("role"))){

            String message = "The Admin has booked a package with booking Id: " + bookingId + ", check your bookings in (View Your Bookings) section now!";
            notificationDAO.sendNotificationToUser(String.valueOf(customerIdToBook), message);
        }

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
