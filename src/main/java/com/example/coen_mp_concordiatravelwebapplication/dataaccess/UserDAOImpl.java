package com.example.coen_mp_concordiatravelwebapplication.dataaccess;

import com.example.coen_mp_concordiatravelwebapplication.config.CONFIG;
import com.example.coen_mp_concordiatravelwebapplication.models.userModels.User;
import jakarta.servlet.ServletException;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl implements UserDAO {

    @Override
    public boolean isUsernameUnique(String username) throws ServletException {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            throw new ServletException(e);
        }
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
    public void addUser(User user) throws ServletException {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            throw new ServletException(e);
        }
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
            statement.setString(10, "Customer");

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
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
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
                    String firstName = resultSet.getString("first_name");
                    String lastName = resultSet.getString("last_name");

                    return new User(username, role, firstName, lastName);
                }
            }
        } catch (SQLException e) {
            throw new ServletException("Unable to connect to the database.", e);
        }
        return null;
    }

    @Override
    public int getID(String customerUsername) throws ServletException {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            throw new ServletException(e);
        }
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

    @Override
    public User checkUserDetails(String username, String email, String phone) throws ServletException {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            throw new ServletException(e);
        }
        User user = null;
        try (Connection connection = DriverManager.getConnection(CONFIG.SQLURL, CONFIG.SQLUSER, CONFIG.SQLPASS)) {
            String query = "SELECT * FROM users WHERE username = ? AND email = ? AND phone_number = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, email);
            statement.setString(3, phone);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                // Retrieve user details from the result set
                int userId = resultSet.getInt("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");

                // Create a User object
                user = new User(userId, username, email, phone, firstName, lastName);
            }
        } catch (SQLException e) {
            throw new ServletException("Unable to connect to the database.", e);
        }
        return user;
    }

    @Override
    public boolean updatePassword(int userID, String newPassword) throws ServletException {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            throw new ServletException(e);
        }
        try (Connection connection = DriverManager.getConnection(CONFIG.SQLURL, CONFIG.SQLUSER, CONFIG.SQLPASS)) {
            String query = "UPDATE users SET password = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, BCrypt.hashpw(newPassword, BCrypt.gensalt()));
            statement.setInt(2, userID);

            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            throw new ServletException("Unable to connect to the database.", e);
        }
    }

    @Override
    public boolean changeRole(String customerId, String role) {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try (Connection connection = DriverManager.getConnection(CONFIG.SQLURL, CONFIG.SQLUSER, CONFIG.SQLPASS)) {
            String query = "UPDATE users SET role = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, role);
            statement.setString(2, customerId);

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<User> getAllAgents() {
        List<User> agents = new ArrayList<>();

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try (Connection connection = DriverManager.getConnection(CONFIG.SQLURL, CONFIG.SQLUSER, CONFIG.SQLPASS)) {
            String query = "SELECT * FROM users WHERE role = 'Agent'";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String userId = resultSet.getString("id");
                String username = resultSet.getString("username");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String phoneNumber = resultSet.getString("phone_number");
                String emailId = resultSet.getString("email");
                int age = resultSet.getInt("age");
                String gender = resultSet.getString("gender");
                String dob = resultSet.getString("date_of_birth");
                String role = resultSet.getString("role");

                User agent = new User(userId, firstName, lastName, dob, emailId, username, role, phoneNumber, gender, age);
                agents.add(agent);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return agents;
    }

    @Override
    public List<User> getOnlyCustomers() {
        List<User> customers = new ArrayList<>();

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try (Connection connection = DriverManager.getConnection(CONFIG.SQLURL, CONFIG.SQLUSER, CONFIG.SQLPASS)) {
            String query = "SELECT * FROM users WHERE role = 'Customer'";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String userId = resultSet.getString("id");
                String username = resultSet.getString("username");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String phoneNumber = resultSet.getString("phone_number");
                String emailId = resultSet.getString("email");
                int age = resultSet.getInt("age");
                String gender = resultSet.getString("gender");
                String dob = resultSet.getString("date_of_birth");
                String role = resultSet.getString("role");

                User agent = new User(userId, firstName, lastName, dob, emailId, username, role, phoneNumber, gender, age);
                customers.add(agent);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }

    @Override
    public User getUserById(String userId) {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try (Connection connection = DriverManager.getConnection(CONFIG.SQLURL, CONFIG.SQLUSER, CONFIG.SQLPASS)) {
            String query = "SELECT * FROM users WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, userId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String username = resultSet.getString("username");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String phoneNumber = resultSet.getString("phone_number");
                String emailId = resultSet.getString("email");
                int age = resultSet.getInt("age");
                String gender = resultSet.getString("gender");
                String dob = resultSet.getString("date_of_birth");
                String role = resultSet.getString("role");

                User agent = new User(id, firstName, lastName, dob, emailId, username, role, phoneNumber, gender, age);
                return agent;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }



    @Override
    public boolean updateUser(User user) {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }

        try (Connection connection = DriverManager.getConnection(CONFIG.SQLURL, CONFIG.SQLUSER, CONFIG.SQLPASS)) {
            String query = "UPDATE users SET first_name = ?, last_name = ?, date_of_birth = ?, email = ?, phone_number = ?, gender = ?, age = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setString(3, user.getDateOfBirth());
            statement.setString(4, user.getEmail());
            statement.setString(5, user.getPhoneNumber());
            statement.setString(6, user.getGender());
            statement.setInt(7, user.getAge());
            statement.setString(8, user.getUserId());

            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<User> getNotLinkedCustomers() {
        List<User> customers = new ArrayList<>();

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try (Connection connection = DriverManager.getConnection(CONFIG.SQLURL, CONFIG.SQLUSER, CONFIG.SQLPASS)) {
            String query = "SELECT * FROM users WHERE role = 'Customer' AND id NOT IN (SELECT customer_id FROM agent_customer_link)";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String userId = resultSet.getString("id");
                String username = resultSet.getString("username");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String phoneNumber = resultSet.getString("phone_number");
                String emailId = resultSet.getString("email");
                int age = resultSet.getInt("age");
                String gender = resultSet.getString("gender");
                String dob = resultSet.getString("date_of_birth");
                String role = resultSet.getString("role");

                User agent = new User(userId, firstName, lastName, dob, emailId, username, role, phoneNumber, gender, age);
                customers.add(agent);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }

    @Override
    public List<User> getLinkedCustomers(String agentID) {
        List<User> customers = new ArrayList<>();

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try (Connection connection = DriverManager.getConnection(CONFIG.SQLURL, CONFIG.SQLUSER, CONFIG.SQLPASS)) {
            String query = "SELECT * FROM users WHERE role = 'Customer' AND id IN (SELECT customer_id FROM agent_customer_link WHERE agent_id = '" + agentID + "')";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String userId = resultSet.getString("id");
                String username = resultSet.getString("username");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String phoneNumber = resultSet.getString("phone_number");
                String emailId = resultSet.getString("email");
                int age = resultSet.getInt("age");
                String gender = resultSet.getString("gender");
                String dob = resultSet.getString("date_of_birth");
                String role = resultSet.getString("role");

                User agent = new User(userId, firstName, lastName, dob, emailId, username, role, phoneNumber, gender, age);
                customers.add(agent);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }

}
