package com.example.coen_mp_concordiatravelwebapplication.presentation.controllers;

import com.example.coen_mp_concordiatravelwebapplication.config.CONFIG;
import com.example.coen_mp_concordiatravelwebapplication.models.bookingModels.Booking;
import com.example.coen_mp_concordiatravelwebapplication.models.packageModels.TravelPackage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) throws SQLException {
        try {
            // Step 1: Establish database connection
            Connection conn = DriverManager.getConnection(CONFIG.SQLURL, CONFIG.SQLUSER, CONFIG.SQLPASS);

            // Step 2: Query 1 - Get customer IDs linked to the agent
            List<Integer> customerIds = new ArrayList<>();
            String customerIdsQuery = "SELECT customer_id FROM agent_customer_link WHERE agent_Id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(customerIdsQuery)) {
                stmt.setInt(1, 1); // Replace 1 with the actual agent ID
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        int customerId = rs.getInt("customer_id");
                        customerIds.add(customerId);
                    }
                }
            }

            for (Integer customerId : customerIds) {
                System.out.println("customerID" + customerId);
            }
            // Step 3: Query 2 - Get bookings based on customer IDs
            List<Booking> bookings = new ArrayList<>();
            if (!customerIds.isEmpty()) {
                String bookingsQuery = "SELECT bookingId, customerId, packageId FROM bookings WHERE customerId IN (";
                for (int i = 0; i < customerIds.size(); i++) {
                    bookingsQuery += (i == 0 ? "?" : ", ?");
                }
                bookingsQuery += ")";
                try (PreparedStatement stmt = conn.prepareStatement(bookingsQuery)) {
                    for (int i = 0; i < customerIds.size(); i++) {
                        stmt.setString(i + 1, String.valueOf(customerIds.get(i))); // Set the customerId as a string parameter
                    }
                    try (ResultSet rs = stmt.executeQuery()) {
                        while (rs.next()) {
                            String bookingId = rs.getString("bookingId");
                            String customerId = rs.getString("customerId");
                            String packageId = rs.getString("packageId");
                            Booking booking = new Booking(bookingId, customerId, packageId);
                            bookings.add(booking);
                        }
                    }
                }
            }

            for (Booking booking : bookings) {
                System.out.println("bookingsIDs" + booking.getBookingId());
            }
            // Step 4: Query 3 - Get package information based on package IDs
            List<TravelPackage> packages = new ArrayList<>();
            if (!bookings.isEmpty()) {
                String packagesQuery = "SELECT package_id, name AS package_name, description AS package_description, price AS package_price "
                        + "FROM travel_package WHERE package_id IN (";
                for (int i = 0; i < bookings.size(); i++) {
                    packagesQuery += (i == 0 ? "?" : ", ?");
                }
                packagesQuery += ")";
                try (PreparedStatement stmt = conn.prepareStatement(packagesQuery)) {
                    for (int i = 0; i < bookings.size(); i++) {
                        stmt.setString(i + 1, bookings.get(i).getPackageId()); // Set the packageId as a string parameter
                    }
                    try (ResultSet rs = stmt.executeQuery()) {
                        while (rs.next()) {
                            String packageId = rs.getString("package_id");
                            String packageName = rs.getString("package_name");
                            String packageDescription = rs.getString("package_description");
                            double packagePrice = rs.getDouble("package_price");
                            TravelPackage travelPackage = new TravelPackage(packageId, packageName, packageDescription, packagePrice);
                            packages.add(travelPackage);
                        }
                    }
                }
            }

            for (TravelPackage travelPackage : packages) {
                System.out.println("travelpackages" + travelPackage.getName());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
