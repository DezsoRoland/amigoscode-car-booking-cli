package com.dezsoroland.booking;

import com.dezsoroland.booking.Booking;
import com.dezsoroland.booking.BookingStatus;
import com.dezsoroland.booking.CarBookingFileDataAccessService;
import com.dezsoroland.car.Car;
import com.dezsoroland.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CarBookingFileDataAccessServiceTest {

    @TempDir
    Path tempDir;

    private CarBookingFileDataAccessService bookingDao;
    private String filePath;

    private User user;
    private Car corolla;

    @BeforeEach
    void setUp() {
        filePath = tempDir.resolve("bookings.dat").toString();
        bookingDao = new CarBookingFileDataAccessService(filePath);

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

        List<Booking> firstCall = bookingDao.getAllBookings();
        firstCall.add(booking());

        assertThat(bookingDao.getAllBookings()).hasSize(1);
    }

    @Test
    @DisplayName("Return booking found by ID")
    void getBookingById() {
        Booking booking = booking();
        bookingDao.save(booking);

        assertThat(bookingDao.getBookingById(booking.getId())).isNotNull();
    }

    @Test
    @DisplayName("Save booking")
    void save() {
        Booking booking = booking();

        bookingDao.save(booking);
        List<Booking> bookings = bookingDao.getAllBookings();

        assertThat(bookingDao.getAllBookings())
                .extracting(Booking::getId)
                .contains(booking.getId());
        assertThat(bookings).hasSize(1);
    }

    @Test
    @DisplayName("Update status of the booking")
    void update() {
        Booking saved = booking();
        bookingDao.save(saved);

        Booking stored = bookingDao.getBookingById(saved.getId());
        stored.setStatus(BookingStatus.CANCELED);
        bookingDao.update(stored);

        Booking fromFile = new CarBookingFileDataAccessService(filePath)
                .getBookingById(saved.getId());

        assertThat(fromFile.getStatus()).isEqualTo(BookingStatus.CANCELED);
    }
}