package com.dezsoroland.car;

import com.dezsoroland.car.Car;
import com.dezsoroland.car.CarArrayDataAccessService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CarArrayDataAccessServiceTest {

    private CarArrayDataAccessService carDao;

    @BeforeEach
    void setUp() {
        carDao = new CarArrayDataAccessService();
    }

    @Test
    @DisplayName("Return all cars with complete data")
    void getAllCars() {
        List<Car> cars = carDao.getAllCars();

        assertThat(cars).isNotEmpty();
        assertThat(cars).allSatisfy(car -> {
            assertThat(car.getId()).isNotNull();
            assertThat(car.getCarBrand()).isNotBlank();
            assertThat(car.getModel()).isNotBlank();
            assertThat(car.getRegistrationNumber()).matches("[A-Z]{4}-\\d{3}");
            assertThat(car.getPricePerDay()).isGreaterThan(BigDecimal.ZERO);
        });
        assertThat(cars).extracting(Car::getId).doesNotHaveDuplicates();
    }

    @Test
    @DisplayName("Finding car by ID")
    void getCarById() {
        Car car = carDao.getAllCars().get(0);

        Car found = carDao.getCarById(car.getId());

        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(car.getId());
        assertThat(found.getModel()).isEqualTo(car.getModel());
    }
}