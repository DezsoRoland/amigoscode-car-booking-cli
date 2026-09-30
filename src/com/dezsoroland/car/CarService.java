package com.dezsoroland.car;

import java.util.List;
import java.util.UUID;

public class CarService {
    private final CarDao carDao;

    public CarService(CarDao carDao) {
        this.carDao = carDao;
    }

    public List<Car> getAllCars() {
        return carDao.getAllCars();
    }


    public boolean isValidCar(UUID id) {
        return getCarById(id) != null;
    };

    public Car getCarById(UUID id) {
        return carDao.getCarById(id);
    }
}
