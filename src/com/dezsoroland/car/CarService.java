package com.dezsoroland.car;

import java.util.UUID;

public class CarService {
    private final CarDao carDao = new CarDao();

    public Car[] getAllCars() {
        return carDao.getAllCars();
    }


    public boolean isValidCar(UUID id) {
        Car[] cars = getAllCars();

        System.out.println("Searching for: " + id);

        for (Car car : cars) {
            if(id.equals(car.getId())) {
                return true;
            };
        }

        return false;
    };

    public Car getCarById(UUID id) {
        return carDao.getCarById(id);
    }
}
