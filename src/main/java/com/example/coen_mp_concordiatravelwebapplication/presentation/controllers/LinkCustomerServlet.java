package com.example.coen_mp_concordiatravelwebapplication.presentation.controllers;

import com.example.coen_mp_concordiatravelwebapplication.dataaccess.*;
import com.example.coen_mp_concordiatravelwebapplication.models.bookingModels.Customer;
import com.example.coen_mp_concordiatravelwebapplication.models.userModels.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "LinkCustomerServlet", value = "/LinkCustomerServlet")
public class LinkCustomerServlet extends HttpServlet {
    private CustomerDAO customerDAO;
    private UserDAO userDAO;
    private NotificationDAO notificationDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        // Initialize the implementation
        customerDAO = new CustomerDAOImpl();
        userDAO = new UserDAOImpl();
        notificationDAO = new NotificationDAOImpl();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        List<User> customers = userDAO.getNotLinkedCustomers();
        List<User> agents = userDAO.getAllAgents();

        request.setAttribute("customers", customers);
        request.setAttribute("agents", agents);

        // Forward the request to the JSP page
        request.getRequestDispatcher("linkcustomer.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();

        String agentUsername = (String) session.getAttribute("username");
        String customerId = request.getParameter("customerId");

        int agentId = userDAO.getID(agentUsername);
        System.out.println("Agent ID: " + agentId);
        System.out.println("Customer ID: " + customerId);

        if("Admin".equals(session.getAttribute("role"))){
            agentId = Integer.parseInt(request.getParameter("agentId"));
        }

        // Link the customer to the agent
        boolean success = customerDAO.linkCustomerToAgent(Integer.parseInt(customerId), agentId);

        if (success) {
            //Notification code
            if ("Agent".equals(session.getAttribute("role"))) {
                String customerUsername = (String) session.getAttribute("username");
                String customerIdOfAgent = String.valueOf(userDAO.getID(customerUsername));

                User user = userDAO.getUserById(customerIdOfAgent);

                String fullname = user.getFirstName() + " " + user.getLastName();

                String messageForNotif = fullname + " is now your Agent and can make modifications to your bookings.";
                notificationDAO.sendNotificationToUser(String.valueOf(customerId), messageForNotif);
            } else if ("Admin".equals(session.getAttribute("role"))) {

                User user = userDAO.getUserById(String.valueOf(agentId));

                String fullname = user.getFirstName() + " " + user.getLastName();
                String messageForNotif = "The Admin has added " + fullname +" as your Agent.";
                notificationDAO.sendNotificationToUser(customerId, messageForNotif);

                user = userDAO.getUserById(customerId);

                fullname = user.getFirstName() + " " + user.getLastName();

                messageForNotif = "The Admin has added " + fullname+ " as your customer.";
                notificationDAO.sendNotificationToUser(String.valueOf(agentId), messageForNotif);
            }
            // Agent linked to the customer successfully
            request.setAttribute("message", "Customer linked to the agent successfully");
        } else {
            // Failed to link the customer to the agent
            request.setAttribute("message", "Failed to link customer to the agent");
        }

        // Forward the request to the JSP page
        doGet(request, response);
    }
}
