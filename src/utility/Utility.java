package utility;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Utility {
    public LocalDate readStartDate(Scanner scanner) {
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

    public LocalDate readEndDate(Scanner scanner, LocalDate startDate) {
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
}
