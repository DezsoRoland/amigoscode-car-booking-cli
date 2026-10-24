package com.dezsoroland.car;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarServiceTest {

    @Mock
    private CarDao carDao;

    @InjectMocks
    private CarService carService;



    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("Returns all car from DAO")
    void getAllCars() {
        Car corolla = new Car(UUID.randomUUID(), "Toyota", "Corolla", "ABCD-123",
                new BigDecimal("50.00"), false);
        Car model3 = new Car(UUID.randomUUID(), "Tesla", "Model 3", "ABCD-123",
                new BigDecimal("50.00"), true);
        when(carDao.getAllCars()).thenReturn(List.of(corolla, model3));

        List<Car> result = carService.getAllCars();

        assertThat(result).contains(corolla, model3);
    }

    @Test
    @DisplayName("Returns True when car exist")
    void isValidCar() {
        Car corolla = new Car(UUID.randomUUID(), "Toyota", "Corolla", "ABCD-123",
                new BigDecimal("50.00"), false);

        when(carDao.getCarById(corolla.getId())).thenReturn(corolla);

        assertThat(carService.isValidCar(corolla.getId())).isTrue();
    }

    @Test
    @DisplayName("Returns Car when found")
    void getCarById() {
        Car corolla = new Car(UUID.randomUUID(), "Toyota", "Corolla", "ABCD-123",
                new BigDecimal("50.00"), false);
        when(carDao.getCarById(corolla.getId())).thenReturn(corolla);

        assertThat(carService.getCarById(corolla.getId())).isEqualTo(corolla);
    }
}