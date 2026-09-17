package booking;

import car.Car;
import car.CarService;
import user.User;
import user.UserService;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;
import java.util.UUID;

public class BookingService {
    CarService carService = new CarService();
    UserService userService = new UserService();


    public void createBooking(UUID userId, UUID carId, LocalDate startDate, LocalDate endDate) throws IOException {

        if (!userService.isValidUser(userId)) {
            System.out.println("Invalid user.");
            return;
        }

        if (!carService.isValidCar(carId)) {
            System.out.println("Invalid car.");
            return;
        }

        if (startDate.isAfter(endDate)) {
            System.out.println("Start date cannot be after end date.");
            return;
        }

        if (startDate.isBefore(LocalDate.now())) {
            System.out.println("Start date cannot be in the past.");
            return;
        }

        if (!isCarAvailable(carId, startDate, endDate)) {
            System.out.println("Car is already booked.");
            return;
        }

        User[] users = userService.getAllUsers();
        Car[] cars = carService.getAllCars();

        User selectedUser = null;
        Car selectedCar = null;

        for (User user : users) {
            if (user.getId().equals(userId)) {
                selectedUser = user;
                break;
            }
        }

        for (Car car : cars) {
            if (car.getId().equals(carId)) {
                selectedCar = car;
                break;
            }
        }

        Booking booking = new Booking(
                UUID.randomUUID(),
                selectedCar,
                startDate,
                endDate,
                LocalDateTime.now(),
                BookingStatus.ACTIVE,
                selectedUser
        );

        String bookingLine =
                booking.getId() + "," +
                        booking.getCar().getId() + "," +
                        booking.getStartDate() + "," +
                        booking.getEndDate() + "," +
                        booking.getBookedAt() + "," +
                        booking.getStatus() + "," +
                        booking.getUser().getId() +
                        System.lineSeparator();

        Path path = Path.of("src/Bookings.csv");

        if (!Files.exists(path)) {
            Files.writeString(
                    path,
                    "id,carId,startDate,endDate,bookedAt,status,userId"
                            + System.lineSeparator(),
                    StandardOpenOption.CREATE
            );
        }

        Files.writeString(
                path,
                bookingLine,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND
        );

        long days = ChronoUnit.DAYS.between(startDate, endDate);

        if (days == 0) {
            days = 1;
        }

        BigDecimal price = selectedCar.getPricePerDay().multiply(BigDecimal.valueOf(days));

        System.out.println("Booking created successfully.");
        System.out.println("Price: " + price);

    }

    public boolean isCarAvailable(UUID carId, LocalDate startDate, LocalDate endDate) throws IOException {
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

    public boolean isValidDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try {
            LocalDate.parse(date, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public Booking[] getAllBookings() throws IOException {
            String content = Files.readString(Path.of("src/Bookings.csv"));

            String[] lines = content.split("\\R");

            Booking[] bookings = new Booking[lines.length - 1];

            for (int i = 1; i < lines.length; i++) {
                String[] data = lines[i].split(",");

                UUID id = UUID.fromString(data[0]);
                UUID carId = UUID.fromString(data[1]);
                LocalDate startDate = LocalDate.parse(data[2]);
                LocalDate endDate = LocalDate.parse(data[3]);
                LocalDateTime bookedAt = LocalDateTime.parse(data[4]);
                BookingStatus status = BookingStatus.valueOf(data[5]);
                UUID userId = UUID.fromString(data[6]);

                Car car = carService.getCarById(carId);
                User user = userService.getUserById(userId);

                bookings[i - 1] = new Booking(id, car, startDate, endDate, bookedAt, status, user);
            }

            return bookings;
    }

    public Booking[] getBookingsByUserId(UUID userId) throws IOException {
        Booking[] bookings = getAllBookings();

        int count = 0;
        for (Booking booking : bookings) {
            if (booking.getUser().getId().equals(userId)) {
                count++;
            }
        }

        Booking[] bookingsByUser = new Booking[count];
        int index = 0;
        for (Booking booking : bookings) {
            if (booking.getUser().getId().equals(userId)) {
                bookingsByUser[index] = booking;
                index++;
            }
        }

        return bookingsByUser;
    }


    public Car[] getAvailableCars() throws IOException {
        Booking[] bookings = getAllBookings();
        Car[] cars = carService.getAllCars();

        int availableCount = 0;

        for(Car car : cars){
            boolean isBooked = false;
            for(Booking booking : bookings){
                if(booking.getStatus() == BookingStatus.ACTIVE && booking.getCar().getId().equals(car.getId())) {
                    isBooked = true;
                    break;
                }
            }
            if(!isBooked){
                availableCount ++;
            }

        }

        Car[] availableCars = new Car[availableCount];

        int index = 0;

        for (Car car : cars) {

            boolean isBooked = false;

            for (Booking booking : bookings) {

                if (booking.getStatus() == BookingStatus.ACTIVE
                        && booking.getCar().getId().equals(car.getId())) {

                    isBooked = true;
                    break;
                }
            }

            if (!isBooked) {
                availableCars[index] = car;
                index++;
            }
        }

        return availableCars;

    }

    public void deleteBooking(UUID id) throws IOException {

        for(Booking booking : getAllBookings()) {
            if(booking.getStatus() == BookingStatus.ACTIVE) {
                booking.setStatus(BookingStatus.CANCELED);
            } else {
                System.out.println("There no active booking with this ID");
            }
        }
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

    public UUID readBookingId(Scanner scanner) throws IOException {
        System.out.println("Please give a booking Id:");
        while (true) {
            String input = scanner.nextLine().trim();
            try {
                UUID id = UUID.fromString(input);
                if (isValidBooking(id)) {
                    return id;
                }
                System.out.println("No booking found with this Id, please try again:");
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid Id format, please try again:");
            }
        }
    }

}
