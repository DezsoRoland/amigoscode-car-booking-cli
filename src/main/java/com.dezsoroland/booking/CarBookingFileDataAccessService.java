package com.dezsoroland.booking;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CarBookingFileDataAccessService implements BookingDao {

    private static final String DEFAULT_FILE_NAME = "bookings.dat";

    private final String fileName;

    private final List<Booking> bookings;

    public CarBookingFileDataAccessService() {
        this(DEFAULT_FILE_NAME);
    }

    public CarBookingFileDataAccessService(String fileName) {
        this.fileName = fileName;
        this.bookings = readFromFile();
    }

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
        writeToFile();
    }

    @Override
    public void update(Booking booking) {
        writeToFile();
    }

    private List<Booking> readFromFile() {

        File file = new File(fileName);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            return (List<Booking>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalStateException("Could not read bookings from " + fileName, e);
        }
    }

    private void writeToFile() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
            out.writeObject(bookings);
        } catch (IOException e) {
            throw new IllegalStateException("Could not save bookings to " + fileName, e);
        }
    }
}
