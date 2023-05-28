package com.example.coen_mp_concordiatravelwebapplication;

import com.example.coen_mp_concordiatravelwebapplication.config.CONFIG;
import com.example.coen_mp_concordiatravelwebapplication.models.bookingModels.Bookings;
import com.example.coen_mp_concordiatravelwebapplication.models.bookingModels.Customer;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/CustomerBookingsServlet")
public class CustomerBookingsServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve the list of customers from the database
        List<Customer> customers = new ArrayList<>();

        try {
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            Connection connection = DriverManager.getConnection(CONFIG.SQLURL, CONFIG.SQLUSER, CONFIG.SQLPASS);

            // Create a PreparedStatement to execute the query
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM customer");

            // Execute the query and obtain the ResultSet
            ResultSet resultSet = statement.executeQuery();

            // Process the result set and populate the list of customers
            while (resultSet.next()) {
                String customerId = resultSet.getString("customerId");
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");
                String phoneNumber = resultSet.getString("phoneNumber");
                int age = resultSet.getInt("age");
                String gender = resultSet.getString("gender");
                String emailId = resultSet.getString("emailId");

                Customer customer = new Customer(customerId, firstName, lastName, phoneNumber, age, gender, emailId);
                customers.add(customer);
            }

            // Close the ResultSet and statement
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle any database errors
            // You can redirect to an error page or display an error message
        }

        // Set the list of customers as a request attribute
        request.setAttribute("customers", customers);

        String condition = request.getParameter("condition");
        if (condition != null && condition.equals("Cancel")) {
            request.getRequestDispatcher("cancelbooking.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("bookings.jsp").forward(request, response);
        }

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String url = CONFIG.SQLURL;
        String username = CONFIG.SQLUSER;
        String password = CONFIG.SQLPASS;

        String customerId = request.getParameter("customerId");
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            Statement statement = connection.createStatement();

            // Retrieve customer bookings from the database for the selected customer
            String selectQuery = "SELECT b.bookingId, b.packageId, b.departureDate, c.firstName, c.lastName " +
                    "FROM bookings AS b " +
                    "JOIN customer AS c ON b.customerId = c.customerId " +
                    "WHERE b.customerId = '" + customerId + "'";
            ResultSet resultSet = statement.executeQuery(selectQuery);

            List<Bookings> customerBookings = new ArrayList<>();
            while (resultSet.next()) {

                // Retrieve customer information
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");

                String fullName = firstName + " " + lastName;
                // Set the selected customer attribute

                request.setAttribute("selectedCustomer", fullName);


                String bookingIdResult = resultSet.getString("bookingId");
                String packageIdResult = resultSet.getString("packageId");
                Timestamp departureDateResult = resultSet.getTimestamp("departureDate");

                Bookings booking = new Bookings(bookingIdResult, packageIdResult, customerId, departureDateResult);
                customerBookings.add(booking);
            }

            request.setAttribute("customerBookings", customerBookings);
            doGet(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}