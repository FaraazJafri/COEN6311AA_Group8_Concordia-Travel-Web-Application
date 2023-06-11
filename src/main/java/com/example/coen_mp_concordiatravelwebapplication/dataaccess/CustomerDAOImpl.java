package com.example.coen_mp_concordiatravelwebapplication.dataaccess;

import com.example.coen_mp_concordiatravelwebapplication.config.CONFIG;
import com.example.coen_mp_concordiatravelwebapplication.models.bookingModels.Booking;
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
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
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
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
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

    @Override
    public Customer getSelectedCustomer(String customerId) {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        try (Connection connection = DriverManager.getConnection(CONFIG.SQLURL, CONFIG.SQLUSER, CONFIG.SQLPASS)) {
            Statement statement = connection.createStatement();

            // Retrieve customer bookings from the database for the selected customer
            String selectQuery = "SELECT * " +
                    "FROM bookings AS b " +
                    "JOIN customer AS c ON b.customerId = c.customerId " +
                    "WHERE b.customerId = '" + customerId + "'";
            ResultSet resultSet = statement.executeQuery(selectQuery);

            Customer customer = null;
            if (resultSet.next()) {
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");
                String phoneNumber = resultSet.getString("phoneNumber");
                int age = resultSet.getInt("age");
                String gender = resultSet.getString("gender");
                String emailId = resultSet.getString("emailId");

                customer = new Customer(customerId, firstName, lastName, phoneNumber, age, gender, emailId);

            }

            return customer;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<Booking> getSeletedCustomerBookings(String customerId) {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        List<Booking> customerBookings = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(CONFIG.SQLURL, CONFIG.SQLUSER, CONFIG.SQLPASS)) {
            Statement statement = connection.createStatement();

            // Retrieve customer bookings from the database for the selected customer
            String selectQuery = "SELECT * " +
                    "FROM bookings AS b " +
                    "JOIN customer AS c ON b.customerId = c.customerId " +
                    "WHERE b.customerId = '" + customerId + "'";
            ResultSet resultSet = statement.executeQuery(selectQuery);

            while (resultSet.next()) {
                String bookingId = resultSet.getString("bookingId");
                String packageId = resultSet.getString("packageId");
                Timestamp departureDate = resultSet.getTimestamp("departureDate");

                // Create a new Booking object
                Booking booking = new Booking(bookingId, packageId, customerId, departureDate);
                customerBookings.add(booking);
            }
            return customerBookings;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean linkCustomerToAgent(int customerId, int agentId) {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        // Example implementation:
        try (Connection connection = DriverManager.getConnection(CONFIG.SQLURL, CONFIG.SQLUSER, CONFIG.SQLPASS)) {

            // Prepare the SQL statement to link the customer to the agent
            String sql = "INSERT INTO agent_customer_link (agent_id, customer_id) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, agentId);
            statement.setInt(2, customerId);

            // Execute the SQL statement
            int rowsAffected = statement.executeUpdate();

            // Return true if at least one row was affected (successful linking), otherwise false
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle any exceptions or log the
        }
        return false;
    }

    @Override
    public List<Customer> getCustomersByAgentId(int agentId) {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        List<Customer> agentCustomers = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(CONFIG.SQLURL, CONFIG.SQLUSER, CONFIG.SQLPASS)) {
            Statement statement = connection.createStatement();

            // First, retrieve the customer IDs for the selected agent
            String customerIdsQuery = "SELECT customer_id FROM agent_customer_link WHERE agent_id = " + agentId;
            ResultSet customerIdsResultSet = statement.executeQuery(customerIdsQuery);

            List<String> customerIds = new ArrayList<>();
            while (customerIdsResultSet.next()) {
                String customerId = customerIdsResultSet.getString("customer_id");
                customerIds.add(customerId);
            }

            // Second, retrieve the customer details using the obtained customer IDs
            for (String customerId : customerIds) {
                String customerDetailsQuery = "SELECT * FROM customer WHERE customerId = '" + customerId + "'";
                ResultSet resultSet = statement.executeQuery(customerDetailsQuery);

                while (resultSet.next()) {
                    String firstName = resultSet.getString("firstName");
                    String lastName = resultSet.getString("lastName");
                    String phoneNumber = resultSet.getString("phoneNumber");
                    int age = resultSet.getInt("age");
                    String gender = resultSet.getString("gender");
                    String emailId = resultSet.getString("emailId");

                    Customer customer = new Customer(customerId, firstName, lastName, phoneNumber, age, gender, emailId);
                    agentCustomers.add(customer);
                }
            }

            return agentCustomers;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
