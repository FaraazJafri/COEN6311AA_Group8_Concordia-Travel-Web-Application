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

import java.io.IOException;
import java.util.List;

@WebServlet(name = "RemoveAgentServlet", value = "/RemoveAgentServlet")
public class RemoveAgentServlet extends HttpServlet {
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

        List<User> customers = userDAO.getAllAgents();
        request.setAttribute("customers", customers);

        // Forward the request to the JSP page
        request.getRequestDispatcher("removeagent.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String customerId = request.getParameter("customerId");
        //change his role to customer
        if (userDAO.changeRole(customerId, "Customer")) {
            request.setAttribute("message", "Agent removed successfully");
        } else {
            request.setAttribute("message", "Agent couldnt be removed");
        }

        doGet(request, response);
    }
}
