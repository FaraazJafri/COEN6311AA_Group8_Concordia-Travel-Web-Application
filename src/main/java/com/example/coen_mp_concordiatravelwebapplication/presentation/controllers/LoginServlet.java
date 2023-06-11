package com.example.coen_mp_concordiatravelwebapplication.presentation.controllers;

import com.example.coen_mp_concordiatravelwebapplication.dataaccess.NotificationDAO;
import com.example.coen_mp_concordiatravelwebapplication.dataaccess.NotificationDAOImpl;
import com.example.coen_mp_concordiatravelwebapplication.dataaccess.UserDAO;
import com.example.coen_mp_concordiatravelwebapplication.dataaccess.UserDAOImpl;
import com.example.coen_mp_concordiatravelwebapplication.models.notficationModels.Notification;
import com.example.coen_mp_concordiatravelwebapplication.models.userModels.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "LoginServlet", value = "/login")
public class LoginServlet extends HttpServlet {

    private UserDAO userDAO;

    private NotificationDAO notificationDAO;
    @Override
    public void init() throws ServletException {
        super.init();
        // Initialize the UserDAO implementation
        userDAO = new UserDAOImpl();
        notificationDAO = new NotificationDAOImpl();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Retrieve user credentials from the request parameters
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Perform user authentication
        User user = userDAO.authenticate(username, password);

        if (user != null) {
            String firstName = user.getFirstName();
            String lastName = user.getLastName();

            String fullName = firstName + " " + lastName;

            // Authentication successful, store user information in session
            HttpSession session = request.getSession();
            session.setAttribute("username", username);
            session.setAttribute("role", user.getRole());
            session.setAttribute("fullName", fullName);

            String userId = String.valueOf(userDAO.getID(username));

            List<Notification> notifications = notificationDAO.getNotificationsByUserId(userId);
            request.setAttribute("notifications", notifications);
            // Redirect to the home page
            request.getRequestDispatcher("homepage.jsp").forward(request, response);
        } else {
            // Authentication failed, display an error message
            request.setAttribute("errorMessage", "Invalid username or password.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}
