package com.example.coen_mp_concordiatravelwebapplication.dataaccess;

import com.example.coen_mp_concordiatravelwebapplication.config.CONFIG;
import com.example.coen_mp_concordiatravelwebapplication.models.packageModels.Flight;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FlightDAOImpl implements FlightDAO {
    @Override
    public List<Flight> getAllFlights() {
        List<Flight> flights = new ArrayList<>();

        // JDBC variables
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            // Establish database connection
            connection = DriverManager.getConnection(CONFIG.SQLURL, CONFIG.SQLUSER, CONFIG.SQLPASS);

            // Create and execute the SQL query
            String query = "SELECT * FROM flight";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

            // Process the result set
            while (resultSet.next()) {
                String flightId = resultSet.getString("flight_id");
                String airline = resultSet.getString("airline");
                Timestamp departure = resultSet.getTimestamp("departure");
                Timestamp arrival = resultSet.getTimestamp("arrival");
                double price = resultSet.getDouble("price");

                // Create Flight object and add it to the list
                Flight flight = new Flight(flightId, airline, departure, arrival, price);
                flights.add(flight);
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

        return flights;
    }
}
