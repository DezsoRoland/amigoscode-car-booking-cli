package com.dezsoroland.car;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

public class CarService {

    public Car[] getAllCars() throws IOException {
        String content = Files.readString(Path.of("src/Cars.csv"));

        String[] lines = content.split("\\R");

        Car[] cars = new Car[lines.length - 1];

        for (int i = 1; i < lines.length; i++) {
            String[] data = lines[i].split(",");
            UUID id = UUID.fromString(data[0]);
            String brand = data[1];
            String model = data[2];
            String registrationNumber = data[3];
            BigDecimal pricePerDay = new BigDecimal(data[4]);
            boolean isElectric = Boolean.parseBoolean(data[5]);



            cars[i - 1] = new Car(id, brand, model, registrationNumber, pricePerDay, isElectric);
        }

        return cars;
    }


    public boolean isValidCar(UUID id) throws IOException {
        Car[] cars = getAllCars();

        System.out.println("Searching for: " + id);

        for (Car car : cars) {
            if(id.equals(car.getId())) {
                return true;
            };
        }

        return false;
    };

    public Car getCarById(UUID id) throws IOException {
        for (Car car : getAllCars()) {
            if (car.getId().equals(id)) {
                return car;
            }
        }
        return null;
    }
}
