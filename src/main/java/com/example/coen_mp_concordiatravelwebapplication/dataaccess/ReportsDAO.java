package com.example.coen_mp_concordiatravelwebapplication.dataaccess;

import com.example.coen_mp_concordiatravelwebapplication.config.CONFIG;
import com.example.coen_mp_concordiatravelwebapplication.models.bookingModels.Booking;
import com.example.coen_mp_concordiatravelwebapplication.models.packageModels.TravelPackage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReportsDAO {

    public List<Integer> getCustomerIdsForAgent(int agentId) throws SQLException {
        List<Integer> customerIds = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(CONFIG.SQLURL, CONFIG.SQLUSER, CONFIG.SQLPASS)) {
            String customerIdsQuery = "SELECT customer_id FROM agent_customer_link WHERE agent_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(customerIdsQuery)) {
                stmt.setInt(1, agentId);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        int customerId = rs.getInt("customer_id");
                        customerIds.add(customerId);
                    }
                }
            }
        }

        return customerIds;
    }

    public List<Booking> getBookingsForCustomerIds(List<Integer> customerIds) throws SQLException {
        List<Booking> bookings = new ArrayList<>();

        if (!customerIds.isEmpty()) {
            try (Connection conn = DriverManager.getConnection(CONFIG.SQLURL, CONFIG.SQLUSER, CONFIG.SQLPASS)) {
                String bookingsQuery = "SELECT bookingId, customerId, packageId FROM bookings WHERE customerId IN (";
                for (int i = 0; i < customerIds.size(); i++) {
                    bookingsQuery += (i == 0 ? "?" : ", ?");
                }
                bookingsQuery += ")";
                try (PreparedStatement stmt = conn.prepareStatement(bookingsQuery)) {
                    for (int i = 0; i < customerIds.size(); i++) {
                        stmt.setString(i + 1, String.valueOf(customerIds.get(i)));
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
        }

        return bookings;
    }

    public List<TravelPackage> getPackagesForBookings(List<Booking> bookings) throws SQLException {
        List<TravelPackage> packages = new ArrayList<>();

        if (!bookings.isEmpty()) {
            try (Connection conn = DriverManager.getConnection(CONFIG.SQLURL, CONFIG.SQLUSER, CONFIG.SQLPASS)) {
                String packagesQuery = "SELECT package_id, name AS package_name, description AS package_description, price AS package_price "
                        + "FROM travel_package WHERE package_id IN (";
                for (int i = 0; i < bookings.size(); i++) {
                    packagesQuery += (i == 0 ? "?" : ", ?");
                }
                packagesQuery += ")";
                try (PreparedStatement stmt = conn.prepareStatement(packagesQuery)) {
                    for (int i = 0; i < bookings.size(); i++) {
                        stmt.setString(i + 1, bookings.get(i).getPackageId());
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
        }

        return packages;
    }
}
