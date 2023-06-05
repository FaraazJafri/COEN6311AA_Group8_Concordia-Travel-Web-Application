package com.example.coen_mp_concordiatravelwebapplication.presentation.controllers;

import com.example.coen_mp_concordiatravelwebapplication.dataaccess.UserDAO;
import com.example.coen_mp_concordiatravelwebapplication.dataaccess.UserDAOImpl;
import com.example.coen_mp_concordiatravelwebapplication.models.userModels.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "ModifyAgentDetailsServlet", value = "/ModifyAgentDetailsServlet")
public class ModifyAgentDetailsServlet extends HttpServlet {
    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        // Initialize the implementation
        userDAO = new UserDAOImpl();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        List<User> agents = userDAO.getAllAgents();
        request.setAttribute("agents", agents);

        // Forward the request to the JSP page
        request.getRequestDispatcher("modifyagentdetails.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String customerId = request.getParameter("customerId");

        // Retrieve the User object based on the customerId
        UserDAO userDAO = new UserDAOImpl();
        User agent = userDAO.getUserById(customerId);

        // Set the agent as an attribute in the request
        request.setAttribute("agent", agent);

        // Forward the request to the modifyagentdetailsform.jsp
        request.getRequestDispatcher("modifyagentdetailsform.jsp").forward(request, response);
    }
}
