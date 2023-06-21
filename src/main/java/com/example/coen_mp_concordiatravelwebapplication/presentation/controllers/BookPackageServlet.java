package com.example.coen_mp_concordiatravelwebapplication.presentation.controllers;

import com.example.coen_mp_concordiatravelwebapplication.dataaccess.*;
import com.example.coen_mp_concordiatravelwebapplication.models.bookingModels.Booking;
import com.example.coen_mp_concordiatravelwebapplication.models.packageModels.TravelPackage;
import com.example.coen_mp_concordiatravelwebapplication.models.userModels.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "BookPackageServlet", value = "/BookPackageServlet")
public class BookPackageServlet extends HttpServlet {
    private PackageDAO packageDAO;
    private CustomerDAO customerDAO;
    private UserDAO userDAO;
    private BookingDAO bookingDAO;
    private NotificationDAO notificationDAO;
    private CartDAO cartDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        // Initialize the implementation
        packageDAO = new PackageDAOImpl();
        customerDAO = new CustomerDAOImpl();
        userDAO = new UserDAOImpl();
        bookingDAO = new BookingDAOImpl();
        notificationDAO = new NotificationDAOImpl();
        cartDAO = new CartDAOImpl();
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

        String departureDate = request.getParameter("departureDate");

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime departureCheck = LocalDateTime.parse(departureDate, DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        if (departureCheck.isBefore(now)) {
            request.setAttribute("errorMessage", "Departure date must be after the current date and time.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }

        SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        Timestamp departure = null;
        try {
            departure = new Timestamp(inputDateFormat.parse(departureDate).getTime());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }


        String customerIdToBook;
        // Retrieve the package ID, customer ID, and departure date from the request
        String packageIdToBook = request.getParameter("packageId");
        boolean isDuplicate = false; // Flag to check for duplicates

        if ("Customer".equals(session.getAttribute("role"))) {
            customerIdToBook = String.valueOf(customerID);
            isDuplicate = cartDAO.checkDuplicatePackage(customerIdToBook, packageIdToBook, null);
        } else {
            customerIdToBook = request.getParameter("customerId");
            String agentUsername = (String) session.getAttribute("username");
            String agentID = String.valueOf(userDAO.getID(agentUsername));
            isDuplicate = cartDAO.checkDuplicatePackage(customerIdToBook, packageIdToBook, agentID);
        }

        if (!isDuplicate) {
            if ("Customer".equals(session.getAttribute("role"))) {
                cartDAO.addToCart(customerIdToBook, packageIdToBook, null, null, departure);
            } else {
                customerIdToBook = request.getParameter("customerId");
                String agentUsername = (String) session.getAttribute("username");
                String agentID = String.valueOf(userDAO.getID(agentUsername));
                cartDAO.addToCart(customerIdToBook, packageIdToBook, agentID, null, departure);
            }
        }

        request.setAttribute("heading", "Booking:");
        request.setAttribute("message", "Your booking has been added to the cart!! Proceed to View Cart for checkout.");
        request.getRequestDispatcher("modify_success.jsp").forward(request, response);
    }


}
