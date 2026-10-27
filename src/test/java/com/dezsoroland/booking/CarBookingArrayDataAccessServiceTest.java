package com.dezsoroland.booking;

import com.dezsoroland.booking.Booking;
import com.dezsoroland.booking.BookingStatus;
import com.dezsoroland.booking.CarBookingArrayDataAccessService;
import com.dezsoroland.car.Car;
import com.dezsoroland.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CarBookingArrayDataAccessServiceTest {

    private CarBookingArrayDataAccessService bookingDao;

    private User user;
    private Car corolla;

    @BeforeEach
    void setUp() {
        bookingDao = new CarBookingArrayDataAccessService();

        user = new User(UUID.randomUUID(), "Alice Smith");
        corolla = new Car(UUID.randomUUID(), "Toyota", "Corolla", "ABCD-123",
                new BigDecimal("50.00"), false);
    }

    private Booking booking() {
        return new Booking(UUID.randomUUID(), corolla,
                LocalDate.now().plusDays(1), LocalDate.now().plusDays(4),
                LocalDateTime.now(), BookingStatus.ACTIVE,
                new BigDecimal("150.00"), user);
    }

    @Test
    @DisplayName("Return all bookings")
    void getAllBookings() {
        bookingDao.save(booking());

        List<Booking> bookings = bookingDao.getAllBookings();
        bookings.add(booking());

        assertThat(bookingDao.getAllBookings()).hasSize(1);
    }

    @Test
    @DisplayName("Return booking found by ID")
    void getBookingById() {
        Booking booking = booking();
        bookingDao.save(booking);

        Booking found = bookingDao.getBookingById(booking.getId());

        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(booking.getId());
    }

    @Test
    @DisplayName("Save booking")
    void save() {
        Booking booking = booking();

        bookingDao.save(booking);

        assertThat(bookingDao.getAllBookings())
                .extracting(Booking::getId)
                .contains(booking.getId());
    }

    @Test
    @DisplayName("Update status of the booking")
    void update() {
        Booking booking = booking();
        bookingDao.save(booking);

        Booking stored = bookingDao.getBookingById(booking.getId());
        stored.setStatus(BookingStatus.CANCELED);
        bookingDao.update(stored);

        assertThat(bookingDao.getBookingById(booking.getId()).getStatus())
                .isEqualTo(BookingStatus.CANCELED);
    }
}