package com.example.coen_mp_concordiatravelwebapplication.dataaccess;

import com.example.coen_mp_concordiatravelwebapplication.models.bookingModels.Booking;

import java.util.List;

public interface BookingDAO {
    void saveBooking(String bookingId, String packageIdToBook, String customerIdToBook, String departureDate);

    List<Booking> getAllBookings();

    int cancelBooking(String customerId, String bookingId);
}
