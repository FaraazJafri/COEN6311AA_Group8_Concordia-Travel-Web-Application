package com.example.coen_mp_concordiatravelwebapplication.presentation.controllers;

import com.example.coen_mp_concordiatravelwebapplication.dataaccess.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "ProcessPaymentServlet", value = "/ProcessPaymentServlet")
public class ProcessPaymentServlet extends HttpServlet {


    private PackageDAO packageDAO;
    private CustomerDAO customerDAO;
    private UserDAO userDAO;
    private BookingDAO bookingDAO;

    private NotificationDAO notificationDAO;

    private PaymentDAO paymentDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        // Initialize the implementation
        packageDAO = new PackageDAOImpl();
        customerDAO = new CustomerDAOImpl();
        userDAO = new UserDAOImpl();
        bookingDAO = new BookingDAOImpl();
        notificationDAO = new NotificationDAOImpl();
        paymentDAO = new PaymentDAOImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {


        request.setAttribute("paymentAmount", "100");
        request.getRequestDispatcher("checkout.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        String paymentAmount = request.getParameter("paymentAmount");

        String cardNumber = request.getParameter("cardNumber");

        String status = paymentDAO.processPayement(paymentAmount, cardNumber);

        if (status.equals("succeeded")) {

            request.setAttribute("heading", "Payment Confirmation:");
            request.setAttribute("message", "Your Payment is successfully processed!!");
            request.getRequestDispatcher("modify_success.jsp").forward(request, response);
        } else{
            request.setAttribute("errorMessage", "Payment Failed:");
            request.setAttribute("errorCode", "Your Payment has failed!!");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }


    }
}
