package com.dezsoroland;

import com.dezsoroland.booking.*;
import com.dezsoroland.car.CarArrayDataAccessService;
import com.dezsoroland.car.CarDao;
import com.dezsoroland.car.CarService;
import com.dezsoroland.user.UserArrayDataAccessService;
import com.dezsoroland.user.UserDao;
import com.dezsoroland.user.UserService;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.UUID;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    static UserDao userDao = new UserArrayDataAccessService();
    static CarDao carDao = new CarArrayDataAccessService();

    static BookingDao bookingDao = new CarBookingFileDataAccessService();
    //static BookingDao bookingDao = new CarBookingArrayDataAccessService();

    static UserService userService = new UserService(userDao);
    static CarService carService = new CarService(carDao);
    static BookingService bookingService = new BookingService(carService, userService, bookingDao);

    public static UUID readUserId(Scanner scanner) throws IOException {
        System.out.println("Please give a user Id:");
        while (true) {
            String input = scanner.nextLine().trim();
            try {
                UUID id = UUID.fromString(input);
                if (userService.isValidUser(id)) {
                    return id;
                }
                System.out.println("No user found with this Id, please try again:");
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid Id format, please try again:");
            }
        }
    }


    public static UUID readCarId(Scanner scanner) throws IOException {
        System.out.println("Please give a car Id:");
        while (true) {
            String input = scanner.nextLine().trim();
            try {
                UUID id = UUID.fromString(input);
                if (carService.isValidCar(id)) {
                    return id;
                }
                System.out.println("No car found with this Id, please try again:");
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid Id format, please try again:");
            }
        }
    }

    public static UUID readBookingId(Scanner scanner) throws IOException {
        System.out.println("Please give a booking Id:");
        while (true) {
            String input = scanner.nextLine().trim();
            try {
                UUID id = UUID.fromString(input);
                if (bookingService.isValidBooking(id)) {
                    return id;
                }
                System.out.println("No booking found with this Id, please try again:");
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid Id format, please try again:");
            }
        }
    }

    public static LocalDate readStartDate(Scanner scanner) {
        System.out.println("Please give a start date (YYYY-MM-dd):");
        while (true) {
            String input = scanner.nextLine().trim();
            try {
                LocalDate startDate = LocalDate.parse(input);
                if (startDate.isBefore(LocalDate.now())) {
                    System.out.println("Start date can't be in the past, please try again:");
                    continue;
                }
                return startDate;
            } catch (DateTimeParseException e) {
                System.out.println("Please give a valid date format (YYYY-MM-dd):");
            }
        }
    }

    public static LocalDate readEndDate(Scanner scanner, LocalDate startDate) {
        System.out.println("Please give an end date (YYYY-MM-dd):");
        while (true) {
            String input = scanner.nextLine().trim();
            try {
                LocalDate endDate = LocalDate.parse(input);
                if (!endDate.isAfter(startDate)) {
                    System.out.println("End date must be after " + startDate + ", please try again:");
                    continue;
                }
                return endDate;
            } catch (DateTimeParseException e) {
                System.out.println("Please give a valid date format (YYYY-MM-dd):");
            }
        }
    }

    public static void createBooking(Scanner scanner) throws IOException {

       UUID userId = readUserId(scanner);

       UUID carId = readCarId(scanner);

       LocalDate startDate = readStartDate(scanner);

       LocalDate endDate = readEndDate(scanner, startDate);

       bookingService.createBooking(userId, carId, startDate, endDate);
    }

    public static void getAllBookings() throws IOException {
        Booking[] bookings = bookingService.getAllBookings();
        if (bookings.length == 0) {
            System.out.println("There are no bookings");
        } else {
            for (Booking booking : bookings) {
                System.out.println(booking);
            }
        }

    }

    public static void getBookingByUserId(Scanner scanner) throws IOException {
       UUID userId = readUserId(scanner);

       Booking[] bookingsByUser = bookingService.getBookingsByUserId(userId);
       System.out.println(Arrays.toString(bookingsByUser));
    }

    public static void getAvailableCars() throws IOException {
        System.out.println(Arrays.toString(bookingService.getAvailableCars()));
    }

    public static void getAvailableElectricCars() throws IOException {
        System.out.println(Arrays.toString(bookingService.getAvailableElectricCars()));
    }

    public static void deleteBookingById(Scanner scanner) throws IOException {
        UUID bookingId = readBookingId(scanner);

        bookingService.deleteBooking(bookingId);
    }


    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            printMenu();
            int menuOption = readMenuOption(scanner);

            switch (menuOption) {
                case 1 -> createBooking(scanner);
                case 2 -> deleteBookingById(scanner);
                case 3 -> getBookingByUserId(scanner);
                case 4 -> getAllBookings();
                case 5 -> getAvailableCars();
                case 6 -> getAvailableElectricCars();
                case 7 -> userService.listAllUserNames();
                case 8 -> running = false;
            }
        }

        scanner.close();
        System.out.println("Goodbye!");
    }

    private static int readMenuOption(Scanner scanner) {
        while (true) {
            try {
                int menuOption = Integer.parseInt(scanner.nextLine().trim());
                if (menuOption >= 1 && menuOption <= 8) {
                    return menuOption;
                }
                System.out.println("Please enter a number between 1 and 8.");
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number!");
            }
        }
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("1 - Book Car");
        System.out.println("2 - Delete Booking");
        System.out.println("3 - View All User Booked Cars");
        System.out.println("4 - View All Bookings");
        System.out.println("5 - View Available Cars");
        System.out.println("6 - View Available Electric Cars");
        System.out.println("7 - View All Users");
        System.out.println("8 - Exit");
    }
}