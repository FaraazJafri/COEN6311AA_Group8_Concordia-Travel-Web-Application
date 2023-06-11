package com.example.coen_mp_concordiatravelwebapplication.presentation.controllers;

import com.example.coen_mp_concordiatravelwebapplication.dataaccess.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "DeleteNotificationServlet", value = "/DeleteNotificationServlet")
public class DeleteNotificationServlet extends HttpServlet {
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, IOException {
        // Get the notificationId parameter from the URL
        String notificationId = request.getParameter("notificationId");

        boolean ans = notificationDAO.deleteNotification(notificationId);

        if(ans){
            request.setAttribute("notificationDeleted", "Notification deleted successfully!");
            request.getRequestDispatcher("homepage.jsp").forward(request,response);
        }else{
            String errorMessage = "The notification deletion failed!";
            String errorCode = "500";
            request.getRequestDispatcher("error.jsp").forward(request,response);
        }
    }

}
