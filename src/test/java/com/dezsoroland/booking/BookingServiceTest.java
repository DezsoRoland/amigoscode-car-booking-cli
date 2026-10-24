package com.dezsoroland.booking;

import com.dezsoroland.car.Car;
import com.dezsoroland.car.CarService;
import com.dezsoroland.user.User;
import com.dezsoroland.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private CarService carService;

    @Mock
    private BookingDao bookingDao;

    private BookingService bookingService;

    private User user;
    private Car corolla;
    private Car model3;

    @BeforeEach
    void setUp() {
        bookingService = new BookingService(carService, userService, bookingDao);

        user = new User(UUID.randomUUID(), "Alice Smith");
        corolla = new Car(UUID.randomUUID(), "Toyota", "Corolla", "ABCD-123",
                new BigDecimal("50.00"), false);
        model3 = new Car(UUID.randomUUID(), "Tesla", "Model 3", "EFGH-456",
                new BigDecimal("90.00"), true);
    }


    private Booking booking(Car car, LocalDate start, LocalDate end, BookingStatus status) {
        return new Booking(UUID.randomUUID(), car, start, end, LocalDateTime.now(),
                status, new BigDecimal("100.00"), user);
    }

    @Test
    @DisplayName("Saves booking with data")
    void createBooking() throws IOException {
        LocalDate start = LocalDate.now().plusDays(1);
        LocalDate end = start.plusDays(3);
        when(userService.getUserById(user.getId())).thenReturn(user);
        when(carService.getCarById(corolla.getId())).thenReturn(corolla);
        when(bookingDao.getAllBookings()).thenReturn(List.of());

        bookingService.createBooking(user.getId(), corolla.getId(), start, end);

        ArgumentCaptor<Booking> captor = ArgumentCaptor.forClass(Booking.class);
        verify(bookingDao).save(captor.capture());

        Booking saved = captor.getValue();
        assertThat(saved.getCar()).isEqualTo(corolla);
        assertThat(saved.getUser()).isEqualTo(user);
        assertThat(saved.getStartDate()).isEqualTo(start);
        assertThat(saved.getEndDate()).isEqualTo(end);
        assertThat(saved.getStatus()).isEqualTo(BookingStatus.ACTIVE);
        assertThat(saved.getPrice()).isEqualByComparingTo("150.00");
    }

    @Test
    @DisplayName("Returns true if car is free")
    void isCarAvailable() {
        when(bookingDao.getAllBookings()).thenReturn(List.of());

        assertThat(bookingService.isCarAvailable(
                corolla.getId(), LocalDate.of(2026, 11, 1), LocalDate.of(2026, 11, 5)))
                .isTrue();
    }

    @Test
    @DisplayName("Returns all bookings")
    void getAllBookings() {
        Booking first = booking(corolla, LocalDate.now(), LocalDate.now().plusDays(2),
                BookingStatus.ACTIVE);
        when(bookingDao.getAllBookings()).thenReturn(List.of(first));

        assertThat(bookingService.getAllBookings()).contains(first);
    }

    @Test
    @DisplayName("Returns bookings of a user")
    void getBookingsByUserId() {
        User other = new User(UUID.randomUUID(), "Bob Jones");
        Booking mine = booking(corolla, LocalDate.now(), LocalDate.now().plusDays(2),
                BookingStatus.ACTIVE);
        Booking theirs = new Booking(UUID.randomUUID(), model3, LocalDate.now(),
                LocalDate.now().plusDays(2), LocalDateTime.now(),
                BookingStatus.ACTIVE, new BigDecimal("180.00"), other);
        when(bookingDao.getAllBookings()).thenReturn(List.of(mine, theirs));

        assertThat(bookingService.getBookingsByUserId(user.getId()))
                .contains(mine);
    }

    @Test
    @DisplayName("Return non booked cars")
    void getAvailableCars() {
        when(carService.getAllCars()).thenReturn(List.of(corolla, model3));
        when(bookingDao.getAllBookings()).thenReturn(List.of(
                booking(corolla, LocalDate.now().minusDays(1), LocalDate.now().plusDays(2),
                        BookingStatus.ACTIVE)));

        assertThat(bookingService.getAvailableCars()).containsExactly(model3);
    }

    @Test
    @DisplayName("Return non booked electric cars")
    void getAvailableElectricCars() {
        when(carService.getAllCars()).thenReturn(List.of(corolla, model3));
        when(bookingDao.getAllBookings()).thenReturn(List.of());

        assertThat(bookingService.getAvailableElectricCars()).containsExactly(model3);
    }

    @Test
    @DisplayName("Sets status to canceled and updates Dao")
    void deleteBooking() {
        Booking booking = booking(corolla, LocalDate.now(), LocalDate.now().plusDays(2), BookingStatus.ACTIVE);

        when(bookingDao.getBookingById(booking.getId())).thenReturn(booking);

        bookingService.deleteBooking(booking.getId());

        assertThat(booking.getStatus()).isEqualTo(BookingStatus.CANCELED);
        verify(bookingDao).update(booking);
    }

    @Test
    @DisplayName("Returns true if booking ID exist")
    void isValidBooking() {
        Booking existing = booking(corolla, LocalDate.now(), LocalDate.now().plusDays(2),
                BookingStatus.ACTIVE);
        when(bookingDao.getAllBookings()).thenReturn(List.of(existing));

        assertThat(bookingService.isValidBooking(existing.getId())).isTrue();
    }
}