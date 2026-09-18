package com.dezsoroland.booking;

import com.dezsoroland.car.Car;
import com.dezsoroland.car.CarService;
import com.dezsoroland.user.User;
import com.dezsoroland.user.UserService;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.UUID;

public class BookingService {
    CarService carService = new CarService();
    UserService userService = new UserService();
    BookingDao bookingDao = new BookingDao();


    public void createBooking(UUID userId, UUID carId, LocalDate startDate, LocalDate endDate) {
        User user = userService.getUserById(userId);
        Car car = carService.getCarById(carId);

        if (!isCarAvailable(carId, startDate, endDate)) {
            System.out.println("Car is already booked.");
            return;
        }

        BigDecimal price = calculatePrice(car, startDate, endDate);

        Booking booking = new Booking(
                UUID.randomUUID(),
                car,
                startDate,
                endDate,
                LocalDateTime.now(),
                BookingStatus.ACTIVE,
                price,
                user
        );

        bookingDao.save(booking);

        System.out.println("Booking created successfully.");
        System.out.println("Price: " + price);
    }

    private BigDecimal calculatePrice(Car car, LocalDate startDate, LocalDate endDate) {
        long days = ChronoUnit.DAYS.between(startDate, endDate);
        return car.getPricePerDay().multiply(BigDecimal.valueOf(days));
    }

    public boolean isCarAvailable(UUID carId, LocalDate startDate, LocalDate endDate) {
        for (Booking booking : getAllBookings()) {
            if (!booking.getCar().getId().equals(carId)) {
                continue;
            }
            if (booking.getStatus() != BookingStatus.ACTIVE) {
                continue;
            }
            boolean overlaps = startDate.isBefore(booking.getEndDate())
                    && endDate.isAfter(booking.getStartDate());
            if (overlaps) {
                return false;
            }
        }
        return true;
    }

    public Booking[] getAllBookings() {
            return bookingDao.getAllBookings();
    }

    public Booking[] getBookingsByUserId(UUID userId) {
        Booking[] bookings = bookingDao.getAllBookings();
        Booking[] result = new Booking[bookings.length];
        int count = 0;

        for (Booking booking : bookings) {
            if (booking.getUser().getId().equals(userId)) {
                result[count] = booking;
                count++;
            }
        }

        return Arrays.copyOf(result, count);
    }


    public Car[] getAvailableCars() {
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);
        Car[] cars = carService.getAllCars();

        int availableCount = 0;
        for (Car car : cars) {
            if (isCarAvailable(car.getId(), today, tomorrow)) {
                availableCount++;
            }
        }

        Car[] availableCars = new Car[availableCount];
        int index = 0;
        for (Car car : cars) {
            if (isCarAvailable(car.getId(), today, tomorrow)) {
                availableCars[index] = car;
                index++;
            }
        }

        return availableCars;
    }

    public Car[] getAvailableElectricCars() {
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);
        Car[] cars = carService.getAllCars();

        int availableCount = 0;
        for (Car car : cars) {
            if (car.isElectric() && isCarAvailable(car.getId(), today, tomorrow)) {
                availableCount++;
            }
        }

        Car[] availableCars = new Car[availableCount];
        int index = 0;
        for (Car car : cars) {
            if (car.isElectric() && isCarAvailable(car.getId(), today, tomorrow)) {
                availableCars[index] = car;
                index++;
            }
        }

        return availableCars;
    }

    public void deleteBooking(UUID id) {
        Booking booking = bookingDao.getBookingById(id);

        if (booking == null || booking.getStatus() != BookingStatus.ACTIVE) {
            System.out.println("There is no active booking with this ID");
            return;
        }

        booking.setStatus(BookingStatus.CANCELED);
        System.out.println("Booking canceled");
    }

    public boolean isValidBooking(UUID id) throws IOException {
        System.out.println("Searching for: " + id);
        for (Booking booking : getAllBookings()) {
            if (booking.getId().equals(id)) {
                return true;
            }
        }

        return false;
    };
}
