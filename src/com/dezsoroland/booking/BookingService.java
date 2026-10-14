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
import java.util.List;
import java.util.UUID;

public class BookingService {
    private final CarService carService;
    private final UserService userService;
    private final BookingDao bookingDao;

    public BookingService(CarService carService, UserService userService, BookingDao bookingDao) {
        this.carService = carService;
        this.userService = userService;
        this.bookingDao = bookingDao;
    }

    public void createBooking(UUID userId, UUID carId, LocalDate startDate, LocalDate endDate) throws IOException {
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
        return isCarAvailable(bookingDao.getAllBookings(), carId, startDate, endDate);
    }

    private boolean isCarAvailable(List<Booking> bookings, UUID carId,
                                   LocalDate startDate, LocalDate endDate) {
        return bookings.stream()
                .filter(booking -> booking.getCar().getId().equals(carId))
                .filter(booking -> booking.getStatus() == BookingStatus.ACTIVE)
                .noneMatch(booking -> startDate.isBefore(booking.getEndDate())
                        && endDate.isAfter(booking.getStartDate()));
    }

    public List<Booking> getAllBookings() {
        return bookingDao.getAllBookings();
    }

    public List<Booking> getBookingsByUserId(UUID userId) {
        return bookingDao.getAllBookings().stream()
                .filter(booking -> booking.getUser().getId().equals(userId))
                .toList();
    }

    public List<Car> getAvailableCars() {
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);
        List<Booking> bookings = bookingDao.getAllBookings();

        return carService.getAllCars().stream()
                .filter(car -> isCarAvailable(bookings, car.getId(), today, tomorrow))
                .toList();
    }

    public List<Car> getAvailableElectricCars() {
        return getAvailableCars().stream()
                .filter(Car::isElectric)
                .toList();
    }

    public void deleteBooking(UUID id) {
        Booking booking = bookingDao.getBookingById(id);

        if (booking == null || booking.getStatus() != BookingStatus.ACTIVE) {
            System.out.println("There is no active booking with this ID");
            return;
        }

        booking.setStatus(BookingStatus.CANCELED);
        bookingDao.update(booking);
        System.out.println("Booking canceled");
    }

    public boolean isValidBooking(UUID id) {
        return getAllBookings().stream()
                .anyMatch(booking -> booking.getId().equals(id));
    }
}
