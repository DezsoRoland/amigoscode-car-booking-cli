package com.dezsoroland.booking;

import java.io.*;
import java.util.UUID;

public class CarBookingFileDataAccessService implements BookingDao {

    private static final String FILE_NAME = "bookings.dat";

    private Booking[] bookings;

    public CarBookingFileDataAccessService() {
        bookings = readFromFile();
    }

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

        writeToFile();
    }

    @Override
    public void update(Booking booking) {
        writeToFile();
    }

    private Booking[] readFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return new Booking[0];
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            return (Booking[]) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Could not read bookings: " + e.getMessage());
            return new Booking[0];
        }
    }

    private void writeToFile() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            out.writeObject(bookings);
        } catch (IOException e) {
            System.out.println("Could not save bookings: " + e.getMessage());
        }
    }
}
