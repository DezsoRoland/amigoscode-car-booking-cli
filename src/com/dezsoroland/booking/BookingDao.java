package com.dezsoroland.booking;

import java.util.UUID;

public interface BookingDao {

    Booking[] getAllBookings();

    Booking getBookingById(UUID id);

    void save(Booking booking);
}
