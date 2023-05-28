package com.example.coen_mp_concordiatravelwebapplication;

import com.example.coen_mp_concordiatravelwebapplication.config.CONFIG;
import com.example.coen_mp_concordiatravelwebapplication.models.bookingModels.Bookings;
import com.example.coen_mp_concordiatravelwebapplication.models.bookingModels.Customer;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "CancelBookingServlet", value = "/CancelBookingServlet")
public class CancelBookingServlet extends HttpServlet {
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

        // Forward the request to the JSP page
        request.getRequestDispatcher("cancelbooking.jsp").forward(request, response);
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String url = CONFIG.SQLURL;
        String username = CONFIG.SQLUSER;
        String password = CONFIG.SQLPASS;

        String customerId = request.getParameter("customerId");
        String bookingId = request.getParameter("bookingId");

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            // Create a PreparedStatement to delete the booking from the database
            String deleteQuery = "DELETE FROM bookings WHERE customerId = ? AND bookingId = ?";
            PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
            deleteStatement.setString(1, customerId);
            deleteStatement.setString(2, bookingId);

            // Execute the delete statement
            int rowsDeleted = deleteStatement.executeUpdate();

            // Close the statement
            deleteStatement.close();

            if (rowsDeleted > 0) {
                // Booking deletion successful
                response.sendRedirect("CustomerBookingsServlet?customerId=" + customerId);
            } else {
                // Booking deletion failed
                response.sendRedirect("CustomerBookingsServlet?customerId=" + customerId + "&error=1");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle any database errors
            // You can redirect to an error page or display an error message
        }
    }


}
