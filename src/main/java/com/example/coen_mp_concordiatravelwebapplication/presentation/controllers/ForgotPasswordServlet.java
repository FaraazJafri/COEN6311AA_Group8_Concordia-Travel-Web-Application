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

@WebServlet(name = "ForgotPasswordServlet", value = "/forgotpassword")
public class ForgotPasswordServlet extends HttpServlet {
    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        // Initialize the UserDAO implementation
        userDAO = new UserDAOImpl();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");

        // Check if the entered details match a user in the database
        User user = userDAO.checkUserDetails(username, email, phone);

        if (user != null) {
            // Store the user in session or request attribute to be used on the next page
            request.setAttribute("userID", user.getUserId());
            request.getRequestDispatcher("resetpassword.jsp").forward(request, response);
        } else {
            request.setAttribute("errorMessage", "Invalid username, email, or phone number.");
            request.getRequestDispatcher("forgotpassword.jsp").forward(request, response);
        }
    }
}
