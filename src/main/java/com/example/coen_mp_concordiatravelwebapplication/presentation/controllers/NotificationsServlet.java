package com.example.coen_mp_concordiatravelwebapplication.presentation.controllers;

import java.io.IOException;
import java.util.List;

import com.example.coen_mp_concordiatravelwebapplication.dataaccess.*;
import com.example.coen_mp_concordiatravelwebapplication.models.notficationModels.Notification;
import com.fasterxml.jackson.databind.DatabindContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "NotificationsServlet", value = "/NotificationsServlet")
public class NotificationsServlet extends HttpServlet {
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
        HttpSession session = request.getSession();
        String customerUsername = (String) session.getAttribute("username");
        String userId = String.valueOf(userDAO.getID(customerUsername));
        List<Notification> notifications = notificationDAO.getNotificationsByUserId(userId);
        request.setAttribute("notifications", notifications);
//        request.getRequestDispatcher("homepage.jsp").forward(request, response);
    }

}
