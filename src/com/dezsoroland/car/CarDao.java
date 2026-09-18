package com.dezsoroland.car;

import java.math.BigDecimal;
import java.util.UUID;

public class CarDao {
    private static final Car[] CARS = {
            new Car(UUID.fromString("3ea1a2cf-0135-4b08-8a4c-5c39988a51ff"),
                    "Chevrolet", "Malibu", "JSHA-560", new BigDecimal("77.00"), false),
            new Car(UUID.fromString("f04c2cf2-c618-484c-a273-7e8c6847ac87"),
                    "Mercedes-Benz", "EQE", "ZNPA-884", new BigDecimal("113.00"), true),
            new Car(UUID.fromString("1848fa7f-d5eb-4205-a6a0-2cc519ffb785"),
                    "Hyundai", "Ioniq 5", "DPAD-147", new BigDecimal("81.00"), true),
            new Car(UUID.fromString("8a246f40-465c-4c9e-bdfb-4cc411cb1396"),
                    "Toyota", "Corolla", "EVMC-336", new BigDecimal("74.00"), false),
            new Car(UUID.fromString("ab3ae714-27ee-4fd7-bfff-24227ed05858"),
                    "Toyota", "RAV4", "THZY-163", new BigDecimal("78.00"), false)
    };

    public Car[] getAllCars() {
        return CARS;
    }

    public Car getCarById(UUID id) {
        for (Car car : CARS) {
            if (car.getId().equals(id)) {
                return car;
            }
        }
        return null;
    }
}
