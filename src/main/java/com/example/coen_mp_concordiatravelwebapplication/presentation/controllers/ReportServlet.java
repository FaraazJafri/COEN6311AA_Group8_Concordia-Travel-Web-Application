package com.example.coen_mp_concordiatravelwebapplication.presentation.controllers;

import com.example.coen_mp_concordiatravelwebapplication.dataaccess.*;
import com.example.coen_mp_concordiatravelwebapplication.models.bookingModels.Booking;
import com.example.coen_mp_concordiatravelwebapplication.models.packageModels.TravelPackage;
import com.example.coen_mp_concordiatravelwebapplication.models.reportsModels.Reports;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/report")
public class ReportServlet extends HttpServlet {

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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {

            HttpSession session = request.getSession();
            String username = (String) session.getAttribute("username");

            String agentId = String.valueOf(userDAO.getID(username));

            List<Integer> customerIds = Reports.getCustomerIdsForAgent(Integer.parseInt(agentId));


            List<Booking> bookings = Reports.getBookingsForCustomerIds(customerIds);


            List<TravelPackage> packages = Reports.getPackagesForBookings(bookings);


            request.setAttribute("customerIds", customerIds);
            request.setAttribute("bookings", bookings);
            request.setAttribute("packages", packages);
            request.getRequestDispatcher("report.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();

        }
    }
}
