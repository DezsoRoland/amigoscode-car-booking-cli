package car;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

public class Car {
    private UUID id;
    private String carBrand;
    private String model;
    private String registrationNumber;
    private BigDecimal pricePerDay;
    private boolean isElectric;

    public Car() {
    }

    public Car(UUID id, String carBrand, String model, String registrationNumber, BigDecimal pricePerDay, boolean isElectric) {
        this.id = id;
        this.carBrand = carBrand;
        this.model = model;
        this.registrationNumber = registrationNumber;
        this.pricePerDay = pricePerDay;
        this.isElectric = isElectric;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCarBrand() {
        return carBrand;
    }

    public void setCarBrand(String carBrand) {
        this.carBrand = carBrand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public BigDecimal getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(BigDecimal pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    public boolean isElectric() {
        return isElectric;
    }

    public void setElectric(boolean electric) {
        isElectric = electric;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return isElectric == car.isElectric && Objects.equals(id, car.id) && Objects.equals(carBrand, car.carBrand) && Objects.equals(model, car.model) && Objects.equals(registrationNumber, car.registrationNumber) && Objects.equals(pricePerDay, car.pricePerDay);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, carBrand, model, registrationNumber, pricePerDay, isElectric);
    }

    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", carBrand='" + carBrand + '\'' +
                ", model='" + model + '\'' +
                ", registrationNumber='" + registrationNumber + '\'' +
                ", pricePerDay=" + pricePerDay +
                ", isElectric=" + isElectric +
                '}';
    }
}
