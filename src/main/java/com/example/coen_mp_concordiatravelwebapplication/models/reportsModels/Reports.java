package com.example.coen_mp_concordiatravelwebapplication.models.reportsModels;

import com.example.coen_mp_concordiatravelwebapplication.dataaccess.ReportsDAO;import com.example.coen_mp_concordiatravelwebapplication.models.bookingModels.Booking;
import com.example.coen_mp_concordiatravelwebapplication.models.packageModels.TravelPackage;

import java.sql.SQLException;
import java.util.List;

public class Reports {

    public static List<Integer> getCustomerIdsForAgent(int agentId) throws SQLException {
        ReportsDAO reportsDAO = new ReportsDAO();
        return reportsDAO.getCustomerIdsForAgent(agentId);
    }

    public static List<Booking> getBookingsForCustomerIds(List<Integer> customerIds) throws SQLException {
        ReportsDAO reportsDAO = new ReportsDAO();
        return reportsDAO.getBookingsForCustomerIds(customerIds);
    }

    public static List<TravelPackage> getPackagesForBookings(List<Booking> bookings) throws SQLException {
        ReportsDAO reportsDAO = new ReportsDAO();
        return reportsDAO.getPackagesForBookings(bookings);
    }
}
