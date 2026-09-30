package com.dezsoroland.booking;

import java.util.List;
import java.util.UUID;

public interface BookingDao {

    List<Booking> getAllBookings();

    Booking getBookingById(UUID id);

    void save(Booking booking);

    void update(Booking booking);
}
