package com.example.coen_mp_concordiatravelwebapplication.dataaccess;

import com.example.coen_mp_concordiatravelwebapplication.config.CONFIG;
import com.example.coen_mp_concordiatravelwebapplication.models.packageModels.Activity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ActivityDAOImpl implements ActivityDAO {
    @Override
    public List<Activity> getAllActivities() {
        List<Activity> activities = new ArrayList<>();

        // JDBC variables
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            // Establish database connection
            connection = DriverManager.getConnection(CONFIG.SQLURL, CONFIG.SQLUSER, CONFIG.SQLPASS);

            // Create and execute the SQL query
            String query = "SELECT * FROM activity";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

            // Process the result set
            while (resultSet.next()) {
                String activityId = resultSet.getString("activity_id");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                double price = resultSet.getDouble("price");

                // Create Activity object and add it to the list
                Activity activity = new Activity(activityId, name, description, price);
                activities.add(activity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close JDBC objects
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return activities;
    }
}
