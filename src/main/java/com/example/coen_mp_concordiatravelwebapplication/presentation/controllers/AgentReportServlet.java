package com.example.coen_mp_concordiatravelwebapplication.presentation.controllers;

import com.example.coen_mp_concordiatravelwebapplication.dataaccess.*;
import com.example.coen_mp_concordiatravelwebapplication.models.bookingModels.Booking;
import com.example.coen_mp_concordiatravelwebapplication.models.bookingModels.Customer;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "AgentReportServlet", value = "/AgentReportServlet")
public class AgentReportServlet extends HttpServlet {
    private BookingDAO bookingDAO;
    private CustomerDAO customerDAO;
    private UserDAO userDAO;

    public void init() {
        // Initialize DAO instances
        bookingDAO = new BookingDAOImpl();
        customerDAO = new CustomerDAOImpl();
        userDAO = new UserDAOImpl();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get the agent ID from the request parameters
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        String agentId = String.valueOf(userDAO.getID(username));
        System.out.println("faraaz" + agentId);

        if (agentId != null && !agentId.isEmpty()) {
            // Retrieve bookings by agent ID
            List<Booking> bookings = bookingDAO.getBookingsByAgentId(Integer.parseInt(agentId));
            for (Booking book : bookings) {
                System.out.println(book.getBookingId());
            }
            // Retrieve customers by agent ID
            List<Customer> customers = customerDAO.getCustomersByAgentId(Integer.parseInt(agentId));
            for (Customer cust : customers) {
                System.out.println(cust.getCustomerId());
            }
            // Set data as attributes in the request scope
            request.setAttribute("agentId", agentId);
            request.setAttribute("bookings", bookings);
            request.setAttribute("customers", customers);
        }

        // Forward the request to the report.jsp file
        RequestDispatcher dispatcher = request.getRequestDispatcher("report.jsp");
        dispatcher.forward(request, response);
    }

}
