package booking;

import car.Car;
import user.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Booking {
    private UUID id;
    private Car car;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDateTime bookedAt;
    private BookingStatus status;
    private User user;

    public Booking() {
    }

    public Booking(UUID id, Car car, LocalDate startDate, LocalDate endDate, LocalDateTime bookedAt, BookingStatus status, User user) {
        this.id = id;
        this.car = car;
        this.startDate = startDate;
        this.endDate = endDate;
        this.bookedAt = bookedAt;
        this.status = status;
        this.user = user;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalDateTime getBookedAt() {
        return bookedAt;
    }

    public void setBookedAt(LocalDateTime bookedAt) {
        this.bookedAt = bookedAt;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return Objects.equals(id, booking.id) && Objects.equals(car, booking.car) && Objects.equals(startDate, booking.startDate) && Objects.equals(endDate, booking.endDate) && Objects.equals(bookedAt, booking.bookedAt) && status == booking.status && Objects.equals(user, booking.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, car, startDate, endDate, bookedAt, status, user);
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", car=" + car +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", bookedAt=" + bookedAt +
                ", status=" + status +
                ", user=" + user +
                '}';
    }
}
