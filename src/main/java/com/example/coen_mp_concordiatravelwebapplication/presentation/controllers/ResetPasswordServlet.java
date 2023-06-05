package com.example.coen_mp_concordiatravelwebapplication.presentation.controllers;

import com.example.coen_mp_concordiatravelwebapplication.dataaccess.UserDAO;
import com.example.coen_mp_concordiatravelwebapplication.dataaccess.UserDAOImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "ResetPasswordServlet", value = "/resetpassword")
public class ResetPasswordServlet extends HttpServlet {
    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        // Initialize the UserDAO implementation
        userDAO = new UserDAOImpl();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String newPassword = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        Integer userID = Integer.parseInt(request.getParameter("userID"));

        if (userID != null && newPassword.equals(confirmPassword)) {
            // Update the user's password in the database
            if (userDAO.updatePassword(userID, newPassword)) {
                request.setAttribute("successMessage", "Password reset successful. Please login with your new password.");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            } else {
                request.setAttribute("errorMessage", "Failed to reset password. Please try again.");
                request.getRequestDispatcher("resetpassword.jsp").forward(request, response);
            }
        } else {
            request.setAttribute("errorMessage", "Passwords do not match.");
            request.getRequestDispatcher("resetpassword.jsp").forward(request, response);
        }
    }
}
