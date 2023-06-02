package com.example.coen_mp_concordiatravelwebapplication.presentation.controllers;

import com.example.coen_mp_concordiatravelwebapplication.dataaccess.CustomerDAO;
import com.example.coen_mp_concordiatravelwebapplication.dataaccess.CustomerDAOImpl;
import com.example.coen_mp_concordiatravelwebapplication.models.bookingModels.Customer;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "AddCustomerServlet", value = "/AddCustomerServlet")
public class AddCustomerServlet extends HttpServlet {
    private final CustomerDAO customerDAO;

    public AddCustomerServlet() {
        this.customerDAO = new CustomerDAOImpl();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get the form data
        String customerId = request.getParameter("customerId");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String phoneNumber = request.getParameter("phoneNumber");
        int age = Integer.parseInt(request.getParameter("age"));
        String gender = request.getParameter("gender");
        String emailId = request.getParameter("emailId");

        // Create a new Customer object
        Customer customer = new Customer(customerId, firstName, lastName, phoneNumber, age, gender, emailId);

        // Call the service layer to add the customer
        customerDAO.addCustomer(customer);

        // Redirect to a confirmation page
        response.sendRedirect("homepage.jsp");
    }
}
