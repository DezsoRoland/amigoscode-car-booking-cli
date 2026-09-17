package com.dezsoroland;

import booking.Booking;
import booking.BookingService;
import car.CarService;
import user.UserService;
import utility.Utility;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Scanner;
import java.util.UUID;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    static UserService userService = new UserService();
    static CarService carService = new CarService();
    static BookingService bookingService = new BookingService();
    static Utility utility = new Utility();

    public static void exitMenu(Scanner scanner) {
        scanner.close();
    }

    public static void createBooking(Scanner scanner) throws IOException {

       UUID userId = userService.readUserId(scanner);

       UUID carId = carService.readCarId(scanner);

       LocalDate startDate = utility.readStartDate(scanner);

       LocalDate endDate = utility.readEndDate(scanner, startDate);

       bookingService.createBooking(userId, carId, startDate, endDate);

       scanner.close();
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
       UUID userId = userService.readUserId(scanner);

       Booking[] bookingsByUser = bookingService.getBookingsByUserId(userId);
       System.out.println(Arrays.toString(bookingsByUser));
    }

    public static void getAvailableCars() throws IOException {
        System.out.println(Arrays.toString(bookingService.getAvailableCars()));
    }

    public static void deleteBookingById(Scanner scanner) throws IOException {
        UUID bookingId = bookingService.readBookingId(scanner);

        bookingService.deleteBooking(bookingId);
        System.out.println("Booking deleted");
    }


    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);



        System.out.println("1 - Book Car");
        System.out.println("2 - Delete Booking");
        System.out.println("3 - View All User Booked Cars");
        System.out.println("4 - View All Bookings");
        System.out.println("5 - View Available Cars");
        System.out.println("6 - View Available Electric Cars");
        System.out.println("7 - View All Users");
        System.out.println("8 - Exit");

        int menuOption;

        while (true) {
            try {
                menuOption = Integer.parseInt(scanner.nextLine());
                if (menuOption >= 1 && menuOption <= 8) {
                    break;
                }

                System.out.println("Please enter a number between 1 and 8.");
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number!");
            }
        }

        switch (menuOption) {
            case 1 -> createBooking(scanner);
            case 2 -> deleteBookingById(scanner);
            case 3 -> getBookingByUserId(scanner);
            case 4 -> getAllBookings();
            case 5 -> getAvailableCars();
            case 6 -> carService.getElectricCars();
            case 7 -> userService.listAllUserNames();
            case 8 -> exitMenu(scanner);
        };
    }
}