package com.example.coen_mp_concordiatravelwebapplication.presentation.controllers;

import com.example.coen_mp_concordiatravelwebapplication.dataaccess.CustomerDAO;
import com.example.coen_mp_concordiatravelwebapplication.dataaccess.CustomerDAOImpl;
import com.example.coen_mp_concordiatravelwebapplication.dataaccess.UserDAO;
import com.example.coen_mp_concordiatravelwebapplication.dataaccess.UserDAOImpl;
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

    @Override
    public void init() throws ServletException {
        super.init();
        // Initialize the implementation
        customerDAO = new CustomerDAOImpl();
        userDAO = new UserDAOImpl();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        List<User> customers = userDAO.getNotLinkedCustomers();

        request.setAttribute("customers", customers);

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

        // Link the customer to the agent
        boolean success = customerDAO.linkCustomerToAgent(Integer.parseInt(customerId), agentId);

        if (success) {
            // Agent linked to the customer successfully
            request.setAttribute("message", "Customer linked to the agent successfully");
        } else {
            // Failed to link the customer to the agent
            request.setAttribute("message", "Failed to link customer to the agent");
        }

        // Forward the request to the JSP page
        doGet(request,response);
    }
}
