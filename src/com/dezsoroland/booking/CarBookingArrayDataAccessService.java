package com.dezsoroland.booking;

import java.util.UUID;

public class CarBookingArrayDataAccessService implements BookingDao {
    private Booking[] bookings = new Booking[0];

    @Override
    public Booking[] getAllBookings() {
        return bookings;
    }

    @Override
    public Booking getBookingById(UUID id) {
        for (Booking booking : bookings) {
            if (booking.getId().equals(id)) {
                return booking;
            }
        }
        return null;
    }

    @Override
    public void save(Booking booking) {
        Booking[] updated = new Booking[bookings.length + 1];
        for (int i = 0; i < bookings.length; i++) {
            updated[i] = bookings[i];
        }
        updated[bookings.length] = booking;
        bookings = updated;
    }
}
