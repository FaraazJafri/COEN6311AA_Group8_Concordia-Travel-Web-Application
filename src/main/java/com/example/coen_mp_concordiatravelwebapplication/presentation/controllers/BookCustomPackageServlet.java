package com.example.coen_mp_concordiatravelwebapplication.presentation.controllers;


import com.example.coen_mp_concordiatravelwebapplication.dataaccess.*;
import com.example.coen_mp_concordiatravelwebapplication.models.packageModels.TravelPackage;
import com.example.coen_mp_concordiatravelwebapplication.models.userModels.User;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "BookCustomPackageServlet", value = "/BookCustomPackageServlet")
public class BookCustomPackageServlet extends HttpServlet {


    private PackageDAO packageDAO;
    private CustomerDAO customerDAO;
    private UserDAO userDAO;
    private BookingDAO bookingDAO;
    private NotificationDAO notificationDAO;
    private CartDAO cartDAO;

    private CustomPackageDAO customPackageDAO;

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
        customPackageDAO = new CustomPackageDAOImpl();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        HttpSession session = request.getSession();

        String customerUsername = (String) session.getAttribute("username");
        String userID = String.valueOf(userDAO.getID(customerUsername));

        List<TravelPackage> userPackages = customPackageDAO.retrieveUserPackages(userID);

        request.setAttribute("userPackages", userPackages);

        List<User> customers = new ArrayList<>();
        if ("Admin".equals(session.getAttribute("role"))) {
            // Retrieve the list of customers from the database
            customers = userDAO.getOnlyCustomers();
        }
        if ("Agent".equals(session.getAttribute("role"))) {
            // Retrieve the list of customers from the database
            customerUsername = (String) session.getAttribute("username");
            String userId = String.valueOf(userDAO.getID(customerUsername));
            customers = userDAO.getLinkedCustomers(userId);
        }
        request.setAttribute("customers", customers);

        request.getRequestDispatcher("bookcustompackage.jsp").forward(request, response);
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
        int packageIdToBook = Integer.parseInt(request.getParameter("packageId")) + 499;
        boolean isDuplicate = false;

        if ("Customer".equals(session.getAttribute("role"))) {
            customerIdToBook = String.valueOf(customerID);
            isDuplicate = cartDAO.checkDuplicatePackage(customerIdToBook, String.valueOf(packageIdToBook), null);
        } else {
            customerIdToBook = request.getParameter("customerId");
            String agentUsername = (String) session.getAttribute("username");
            String agentID = String.valueOf(userDAO.getID(agentUsername));
            isDuplicate = cartDAO.checkDuplicatePackage(customerIdToBook, String.valueOf(packageIdToBook), agentID);
        }

        if (!isDuplicate) {
            if ("Customer".equals(session.getAttribute("role"))) {
                cartDAO.addToCart(customerIdToBook, null, null, String.valueOf(packageIdToBook), departure);
            } else {
                customerIdToBook = request.getParameter("customerId");
                String agentUsername = (String) session.getAttribute("username");
                String agentID = String.valueOf(userDAO.getID(agentUsername));
                cartDAO.addToCart(customerIdToBook, null, agentID, String.valueOf(packageIdToBook), departure);
            }
        }

        request.setAttribute("heading", "Booking:");
        request.setAttribute("message", "Your booking has been added to the cart!! Proceed to View Cart for checkout.");
        request.getRequestDispatcher("modify_success.jsp").forward(request, response);
    }
}
