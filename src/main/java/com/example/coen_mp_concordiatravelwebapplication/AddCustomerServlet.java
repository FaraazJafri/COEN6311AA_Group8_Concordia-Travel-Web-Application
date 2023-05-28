package com.example.coen_mp_concordiatravelwebapplication;

import com.example.coen_mp_concordiatravelwebapplication.config.CONFIG;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet(name = "AddCustomerServlet", value = "/AddCustomerServlet")
public class AddCustomerServlet extends HttpServlet {
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

            // Database connection details
            String dbUrl = CONFIG.SQLURL;
            String username = CONFIG.SQLUSER;
            String password = CONFIG.SQLPASS;

            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            // SQL insert query
            String insertQuery = "INSERT INTO customer (customerId, firstName, lastName, phoneNumber, age, gender, emailId) VALUES (?, ?, ?, ?, ?, ?, ?)";

            try {
                // Establish the database connection
                Connection connection = DriverManager.getConnection(dbUrl, username, password);

                // Create the prepared statement
                PreparedStatement statement = connection.prepareStatement(insertQuery);

                // Set the parameter values
                statement.setString(1, customerId);
                statement.setString(2, firstName);
                statement.setString(3, lastName);
                statement.setString(4, phoneNumber);
                statement.setInt(5, age);
                statement.setString(6, gender);
                statement.setString(7, emailId);

                // Execute the insert query
                int rowsInserted = statement.executeUpdate();

                if (rowsInserted > 0) {
                    // Redirect to a confirmation page
                    response.sendRedirect("homepage.jsp");
                } else {
                    // Handle the case when the insertion fails
                    // Display an error message or redirect to an error page
                }

                // Close the statement and connection
                statement.close();
                connection.close();

            } catch (SQLException e) {
                // Handle any database-related errors
                e.printStackTrace();
            }
        }
}