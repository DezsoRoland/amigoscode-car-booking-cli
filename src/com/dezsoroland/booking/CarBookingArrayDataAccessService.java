package com.dezsoroland.booking;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CarBookingArrayDataAccessService implements BookingDao {
    private final List<Booking> bookings = new ArrayList<>();

    @Override
    public List<Booking> getAllBookings() {
        return new ArrayList<>(bookings);
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
        bookings.add(booking);
    }

    @Override
    public void update(Booking booking) {}
}
