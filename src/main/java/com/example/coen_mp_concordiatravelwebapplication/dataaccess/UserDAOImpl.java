package com.example.coen_mp_concordiatravelwebapplication.dataaccess;

import com.example.coen_mp_concordiatravelwebapplication.config.CONFIG;
import com.example.coen_mp_concordiatravelwebapplication.models.userModels.User;
import jakarta.servlet.ServletException;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;

public class UserDAOImpl implements UserDAO {

    @Override
    public boolean isUsernameUnique(String username) {
        String query = "SELECT * FROM users WHERE username = ?";

        try (Connection connection = DriverManager.getConnection(CONFIG.SQLURL, CONFIG.SQLUSER, CONFIG.SQLPASS);
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            return !resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public void addUser(User user) {
        String insertQuery = "INSERT INTO users (first_name, last_name, date_of_birth, email, username, password, phone_number, age, gender, role) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(CONFIG.SQLURL, CONFIG.SQLUSER, CONFIG.SQLPASS);
             PreparedStatement statement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setString(3, String.valueOf(user.getDateOfBirth()));
            statement.setString(4, user.getEmail());
            statement.setString(5, user.getUsername());
            statement.setString(6, BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
            statement.setString(7, user.getPhoneNumber());
            statement.setInt(8, user.getAge());
            statement.setString(9, user.getGender());
            statement.setString(10, user.getRole());

            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int userId = generatedKeys.getInt(1);
                    String insertCustomerQuery = "INSERT INTO customer (customerId, firstName, lastName, phoneNumber, age, gender, emailId) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?)";

                    try (PreparedStatement customerStatement = connection.prepareStatement(insertCustomerQuery)) {
                        customerStatement.setString(1, String.valueOf(userId));
                        customerStatement.setString(2, user.getFirstName());
                        customerStatement.setString(3, user.getLastName());
                        customerStatement.setString(4, user.getPhoneNumber());
                        customerStatement.setInt(5, user.getAge());
                        customerStatement.setString(6, user.getGender());
                        customerStatement.setString(7, user.getEmail());

                        int rowsInserted1 = customerStatement.executeUpdate();

                        if (rowsInserted1 > 0) {
                            System.out.println("User saved as customer");
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public User authenticate(String username, String password) throws ServletException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new ServletException(e);
        }

        // Establish a connection to the database
        try (Connection connection = DriverManager.getConnection(CONFIG.SQLURL, CONFIG.SQLUSER, CONFIG.SQLPASS)) {
            // Construct the SQL query
            String query = "SELECT * FROM users WHERE username = ?";

            // Create a prepared statement
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);

            // Execute the query
            ResultSet resultSet = statement.executeQuery();

            // Check if any rows are returned
            if (resultSet.next()) {
                // Retrieve hashed password from the database
                String hashedPassword = resultSet.getString("password");

                // Verify the entered password against the hashed password
                if (BCrypt.checkpw(password, hashedPassword)) {
                    // Retrieve role information
                    String role = resultSet.getString("role");
                    return new User(username, role);
                }
            }
        } catch (SQLException e) {
            throw new ServletException("Unable to connect to the database.", e);
        }
        return null;
    }

    @Override
    public int getID(String customerUsername) {
        String url = CONFIG.SQLURL;
        String username = CONFIG.SQLUSER;
        String password = CONFIG.SQLPASS;

        int customerID = 0;
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            String query = "SELECT id FROM users WHERE username = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, customerUsername);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                customerID = resultSet.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customerID;
    }
}
