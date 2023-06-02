package com.example.coen_mp_concordiatravelwebapplication.dataaccess;

import com.example.coen_mp_concordiatravelwebapplication.config.CONFIG;
import com.example.coen_mp_concordiatravelwebapplication.models.bookingModels.Booking;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingDAOImpl implements BookingDAO {
    String url = CONFIG.SQLURL;
    String username = CONFIG.SQLUSER;
    String password = CONFIG.SQLPASS;

    @Override
    public void saveBooking(String bookingId, String packageIdToBook, String customerIdToBook, String departureDate) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            Statement statement = connection.createStatement();

            // Insert the booking into the database
            String insertQuery = "INSERT INTO bookings (bookingId, packageId, customerId, departureDate) VALUES " +
                    "('" + bookingId + "', '" + packageIdToBook + "', '" + customerIdToBook + "', '" + departureDate + "')";
            statement.executeUpdate(insertQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Booking> getAllBookings() {
        List<Booking> bookings = new ArrayList<>();
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            Statement statement = connection.createStatement();

            // Retrieve all bookings from the database
            String selectQuery = "SELECT * FROM bookings";
            ResultSet resultSet = statement.executeQuery(selectQuery);


            while (resultSet.next()) {
                String bookingIdResult = resultSet.getString("bookingId");
                String packageIdResult = resultSet.getString("packageId");
                String customerIdResult = resultSet.getString("customerId");
                Timestamp departureDateResult = resultSet.getTimestamp("departureDate");

                Booking booking = new Booking(bookingIdResult, packageIdResult, customerIdResult, departureDateResult);
                bookings.add(booking);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    @Override
    public int cancelBooking(String customerId, String bookingId) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String deleteQuery = "DELETE FROM bookings WHERE customerId = ? AND bookingId = ?";
            PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
            deleteStatement.setString(1, customerId);
            deleteStatement.setString(2, bookingId);

            // Execute the delete statement
            int rowsDeleted = deleteStatement.executeUpdate();

            // Close the statement
            deleteStatement.close();
            return rowsDeleted;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
