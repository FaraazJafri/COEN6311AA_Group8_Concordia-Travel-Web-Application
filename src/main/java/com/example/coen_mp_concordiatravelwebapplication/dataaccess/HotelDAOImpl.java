package com.example.coen_mp_concordiatravelwebapplication.dataaccess;

import com.example.coen_mp_concordiatravelwebapplication.config.CONFIG;
import com.example.coen_mp_concordiatravelwebapplication.models.packageModels.Hotel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HotelDAOImpl implements HotelDAO {
    @Override
    public List<Hotel> getAllHotels() {
        List<Hotel> hotels = new ArrayList<>();

        // JDBC variables
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            // Establish database connection
            connection = DriverManager.getConnection(CONFIG.SQLURL, CONFIG.SQLUSER, CONFIG.SQLPASS);

            // Create and execute the SQL query
            String query = "SELECT * FROM hotel";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

            // Process the result set
            while (resultSet.next()) {
                String hotelId = resultSet.getString("hotel_id");
                String name = resultSet.getString("name");
                String location = resultSet.getString("location");
                double price = resultSet.getDouble("price");

                // Create Hotel object and add it to the list
                Hotel hotel = new Hotel(hotelId, name, location, price);
                hotels.add(hotel);
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

        return hotels;
    }
}
