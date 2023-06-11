package com.example.coen_mp_concordiatravelwebapplication.dataaccess;

import com.example.coen_mp_concordiatravelwebapplication.models.bookingModels.Booking;

import java.sql.Timestamp;
import java.util.List;

public interface BookingDAO {
    void saveBooking(String bookingId, String packageIdToBook, String customerIdToBook, Timestamp departureDate);

    List<Booking> getAllBookings();

    int cancelBooking(String customerId, String bookingId);

    Boolean modifyBooking(String bookingId, String packageId, Timestamp departureDate);
    List<Booking> getBookingsByAgentId(int agentId);

}
