package org.modsen.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.modsen.dto.car.CarRequest;
import org.modsen.dto.car.CarResponse;
import org.modsen.dto.driver.DriverRequest;
import org.modsen.dto.driver.DriverResponse;
import org.modsen.entity.Car;
import org.modsen.entity.Driver;

public final class TestUtil {

    private static final ObjectMapper MAPPER = new ObjectMapper()
        .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        .registerModule(new JavaTimeModule());

    private TestUtil() {
    }

    public static String asJson(Object object) throws JsonProcessingException {
        return MAPPER.writeValueAsString(object);
    }

    public static Driver driver(UUID driverId, String email, Clock clock) {
        return Driver.builder()
            .id(driverId)
            .firstName("Ivan")
            .lastName("Ivanov")
            .dateOfBirth(LocalDate.of(2000, 1, 1))
            .email(email)
            .phoneNumber("+375(29)1234567")
            .isDeleted(false)
            .isAvailable(true)
            .rating(4.5f)
            .sex(SexType.MALE)
            .createdAt(LocalDateTime.now(clock))
            .updatedAt(LocalDateTime.now(clock))
            .cars(new HashSet<>())
            .build();
    }

    public static DriverRequest driverRequest() {
        return new DriverRequest("Ivan", "Ivanov", LocalDate.of(2000, 1, 1),
            "driver@example.com", "+375(29)1234567", "MALE");
    }

    public static DriverResponse driverResponse(UUID driverId) {
        return DriverResponse.builder()
            .id(driverId)
            .firstName("Ivan")
            .lastName("Ivanov")
            .dateOfBirth(LocalDate.of(2000, 1, 1))
            .email("driver@example.com")
            .phoneNumber("+375(29)1234567")
            .isDeleted(false)
            .isAvailable(true)
            .sex("MALE")
            .rating(4.5f)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .carsIds(Set.of())
            .build();
    }

    public static Car car(UUID carId) {
        return Car.builder()
            .id(carId)
            .color("Red")
            .brand("Benz")
            .number("8471KI-5")
            .isDeleted(false)
            .drivers(new HashSet<>())
            .build();
    }

    public static CarRequest carRequest() {
        return new CarRequest("Red", "Benz", "8471KI-5");
    }

    public static CarResponse carResponse(UUID carId) {
        return CarResponse.builder()
            .id(carId)
            .color("Red")
            .brand("Benz")
            .number("8471KI-5")
            .isDeleted(false)
            .driversIds(Set.of())
            .build();
    }


}
