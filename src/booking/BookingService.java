package booking;

import car.Car;
import car.CarService;
import user.User;
import user.UserService;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Scanner;
import java.util.UUID;

public class BookingService {
    CarService carService = new CarService();
    UserService userService = new UserService();


    public void createBooking(UUID userId, UUID carId, LocalDate startDate, LocalDate endDate) throws IOException {
        User user = userService.getUserById(userId);
        Car car = carService.getCarById(carId);

        if (!isCarAvailable(carId, startDate, endDate)) {
            System.out.println("Car is already booked.");
            return;
        }

        Booking booking = new Booking(
                UUID.randomUUID(),
                car,
                startDate,
                endDate,
                LocalDateTime.now(),
                BookingStatus.ACTIVE,
                user
        );

        Booking[] bookings = getAllBookings();
        Booking[] updated = Arrays.copyOf(bookings, bookings.length + 1);
        updated[bookings.length] = booking;
        saveAllBookings(updated);

        System.out.println("Booking created successfully.");
        System.out.println("Price: " + calculatePrice(car, startDate, endDate));
    }

    private BigDecimal calculatePrice(Car car, LocalDate startDate, LocalDate endDate) {
        long days = ChronoUnit.DAYS.between(startDate, endDate);
        return car.getPricePerDay().multiply(BigDecimal.valueOf(days));
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
        Booking[] bookings = getAllBookings();
        boolean found = false;

        for (int i = 0; i < bookings.length; i++) {
            if (bookings[i].getId().equals(id)
                    && bookings[i].getStatus() == BookingStatus.ACTIVE) {
                bookings[i].setStatus(BookingStatus.CANCELED);
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("There is no active booking with this ID");
            return;
        }

        saveAllBookings(bookings);
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


    private void saveAllBookings(Booking[] bookings) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("id,carId,startDate,endDate,bookedAt,status,userId\n");

        for (Booking booking : bookings) {
            sb.append(toCsvLine(booking)).append("\n");
        }

        Files.writeString(Path.of("src/Bookings.csv"), sb.toString());
    }

    private String toCsvLine(Booking booking) {
        return booking.getId() + ","
                + booking.getCar().getId() + ","
                + booking.getStartDate() + ","
                + booking.getEndDate() + ","
                + booking.getBookedAt() + ","
                + booking.getStatus() + ","
                + booking.getUser().getId();
    }

}
