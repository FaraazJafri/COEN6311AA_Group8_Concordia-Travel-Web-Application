package com.example.coen_mp_concordiatravelwebapplication.dataaccess;

import com.example.coen_mp_concordiatravelwebapplication.config.CONFIG;
import com.example.coen_mp_concordiatravelwebapplication.models.bookingModels.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAOImpl implements CustomerDAO {

    @Override
    public void addCustomer(Customer customer) {
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
            statement.setString(1, customer.getCustomerId());
            statement.setString(2, customer.getFirstName());
            statement.setString(3, customer.getLastName());
            statement.setString(4, customer.getPhoneNumber());
            statement.setInt(5, customer.getAge());
            statement.setString(6, customer.getGender());
            statement.setString(7, customer.getEmailId());

            // Execute the insert query
            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {
                // Handle successful insertion
            } else {
                // Handle the case when the insertion fails
                // Display an error message or throw an exception
            }

            // Close the statement and connection
            statement.close();
            connection.close();

        } catch (SQLException e) {
            // Handle any database-related errors
            e.printStackTrace();
        }
    }

    @Override
    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        //Establish a connection to the database
        try (Connection connection = DriverManager.getConnection(CONFIG.SQLURL, CONFIG.SQLUSER, CONFIG.SQLPASS)) {
            // Create a statement
            Statement statement = connection.createStatement();

            String queryCustomer = "SELECT * FROM customer";
            ResultSet resultSetCustomer = statement.executeQuery(queryCustomer);

            // Process the result set
            while (resultSetCustomer.next()) {
                String customerId = resultSetCustomer.getString("customerId");
                String firstName = resultSetCustomer.getString("firstName");
                String lastName = resultSetCustomer.getString("lastName");
                String phoneNumber = resultSetCustomer.getString("phoneNumber");
                String emailId = resultSetCustomer.getString("emailId");
                int age = resultSetCustomer.getInt("age");
                String gender = resultSetCustomer.getString("gender");


                Customer customer = new Customer(customerId, firstName, lastName, phoneNumber, age, gender, emailId);

                // Add the travel package to the list
                customers.add(customer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }
}
