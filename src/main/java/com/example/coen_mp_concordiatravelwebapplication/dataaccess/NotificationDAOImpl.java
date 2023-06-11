package com.example.coen_mp_concordiatravelwebapplication.dataaccess;

import com.example.coen_mp_concordiatravelwebapplication.config.CONFIG;
import com.example.coen_mp_concordiatravelwebapplication.models.notficationModels.Notification;
import com.example.coen_mp_concordiatravelwebapplication.models.userModels.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NotificationDAOImpl implements NotificationDAO {


    private UserDAO userDAO;

    public NotificationDAOImpl() {
        userDAO = new UserDAOImpl();
    }

    @Override
    public void sendNotificationToUser(String userId, String message) {

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        try (Connection connection = DriverManager.getConnection(CONFIG.SQLURL, CONFIG.SQLUSER, CONFIG.SQLPASS)) {
            String query = "INSERT INTO notifications (user_id, message, time_of_notification) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, userId);
            statement.setString(2, message);



            statement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Boolean sendNotificationToAllCustomers(String message) {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try (Connection connection = DriverManager.getConnection(CONFIG.SQLURL, CONFIG.SQLUSER, CONFIG.SQLPASS)) {
            List<User> allCustomers = userDAO.getOnlyCustomers();

            String query = "INSERT INTO notifications (user_id, message, time_of_notification) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);

            for (User customer : allCustomers) {
                statement.setString(1, customer.getUserId());
                statement.setString(2, message);
                statement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
                statement.addBatch();
            }

            statement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle any potential exceptions or log the error
        }
        return true;
    }

    @Override
    public Boolean sendNotificationToAllAgents(String message) {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try (Connection connection = DriverManager.getConnection(CONFIG.SQLURL, CONFIG.SQLUSER, CONFIG.SQLPASS)) {
            List<User> allCustomers = userDAO.getAllAgents();

            String query = "INSERT INTO notifications (user_id, message, time_of_notification) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);

            for (User customer : allCustomers) {
                statement.setString(1, customer.getUserId());
                statement.setString(2, message);
                statement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
                statement.addBatch();
            }

            statement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle any potential exceptions or log the error
        }
        return true;
    }

    @Override
    public List<Notification> getNotificationsByUserId(String userId) {
        List<Notification> notifications = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(CONFIG.SQLURL, CONFIG.SQLUSER, CONFIG.SQLPASS)) {
            String query = "SELECT * FROM notifications WHERE user_id = ? ORDER BY time_of_notification DESC";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, userId);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String notificationId = resultSet.getString("notification_id");
                String message = resultSet.getString("message");
                Timestamp timeOfNotification = resultSet.getTimestamp("time_of_notification");

                Notification notification = new Notification(userId, message, timeOfNotification);
                notification.setNotificationId(notificationId);

                notifications.add(notification);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle any potential exceptions or log the error
        }

//        Collections.reverse(notifications); // Reverse the list to get descending order

        return notifications;
    }

    @Override
    public Boolean deleteNotification(String notificationId) {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        try (Connection connection = DriverManager.getConnection(CONFIG.SQLURL, CONFIG.SQLUSER, CONFIG.SQLPASS)) {
            String query = "DELETE FROM notifications WHERE notification_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, notificationId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }


}
