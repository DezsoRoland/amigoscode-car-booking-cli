package com.dezsoroland.booking;

import java.util.UUID;

public class BookingDao {
    private Booking[] bookings = new Booking[0];

    public Booking[] getAllBookings() {
        return bookings;
    }

    public Booking getBookingById(UUID id) {
        for (Booking booking : bookings) {
            if (booking.getId().equals(id)) {
                return booking;
            }
        }
        return null;
    }

    public void save(Booking booking) {
        Booking[] updated = new Booking[bookings.length + 1];
        for (int i = 0; i < bookings.length; i++) {
            updated[i] = bookings[i];
        }
        updated[bookings.length] = booking;
        bookings = updated;
    }
}
