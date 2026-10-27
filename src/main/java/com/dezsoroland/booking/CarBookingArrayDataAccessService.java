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
        return bookings.stream()
                .filter(booking -> booking.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void save(Booking booking) {
        bookings.add(booking);
    }

    @Override
    public void update(Booking booking) {
    }
}
