package org.modsen.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.modsen.dto.request.PassengerRequest;
import org.modsen.dto.request.RequestParams;
import org.modsen.dto.response.PagedPassengerResponse;
import org.modsen.dto.response.PassengerResponse;
import org.modsen.dto.response.PassengerWithRatingResponse;
import org.modsen.dto.response.RateResponse;
import org.modsen.entity.Passenger;
import org.modsen.entity.SexType;

public final class TestUtil {

    private static final ObjectMapper MAPPER = new ObjectMapper()
        .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        .registerModule(new JavaTimeModule());

    private TestUtil() {
    }

    public static String asJson(Object object) throws JsonProcessingException {
        return MAPPER.writeValueAsString(object);
    }

    public static RequestParams requestParams() {
        return RequestParams.builder()
            .page(0)
            .limit(10)
            .sortBy("createdAt")
            .sortDirection("asc")
            .build();
    }

    public static PagedPassengerResponse pagedPassengerResponse(RequestParams requestParams,
                                                          List<PassengerResponse> passengerResponses) {
        return PagedPassengerResponse.builder()
            .page(requestParams.page())
            .limit(requestParams.limit())
            .passengers(passengerResponses)
            .build();
    }

    public static PassengerWithRatingResponse passengerWithRatingResponse(UUID passengerId, float rating) {
        return PassengerWithRatingResponse.builder()
            .passengerId(passengerId)
            .rating(rating)
            .build();
    }

    public static PassengerResponse passengerDtoResponse(UUID passengerId, String email) {
        return PassengerResponse.builder()
            .passengerId(passengerId)
            .firstName("Buil")
            .lastName("Horevov")
            .dateOfBirth(LocalDate.parse("2010-01-01"))
            .email(email)
            .sex(SexType.MALE)
            .phoneNumber("+375291234567")
            .isDeleted(false)
            .createdAt(LocalDateTime.parse("2025-01-23T19:12:07.645986"))
            .updatedAt(LocalDateTime.parse("2025-01-23T19:12:07.645987"))
            .build();
    }

    public static PassengerResponse passengerDtoResponse(UUID passengerId, String email, Clock clock) {
        return PassengerResponse.builder()
            .passengerId(passengerId)
            .firstName("Buil")
            .lastName("Horevov")
            .dateOfBirth(LocalDate.parse("2010-01-01"))
            .email(email)
            .sex(SexType.MALE)
            .phoneNumber("+375291234567")
            .isDeleted(false)
            .createdAt(LocalDateTime.now(clock))
            .updatedAt(LocalDateTime.now(clock))
            .build();
    }


    public static PassengerRequest passengerDtoRequest() {
        return PassengerRequest.builder()
            .firstName("Buil")
            .lastName("Horevov")
            .dateOfBirth(LocalDate.parse("2010-01-01"))
            .email("buil@example.com")
            .phoneNumber("+375291234567")
            .sex("MALE")
            .build();
    }

    public static Passenger passenger(UUID passengerId, Clock clock) {
        return Passenger.builder()
            .passengerId(passengerId)
            .firstName("Buil")
            .lastName("Horevov")
            .dateOfBirth(LocalDate.parse("2010-01-01"))
            .email("buil@example.com")
            .phoneNumber("+375291234567")
            .isDeleted(false)
            .sex(SexType.MALE)
            .createdAt(LocalDateTime.now(clock))
            .updatedAt(LocalDateTime.now(clock))
            .build();
    }

    public static RateResponse rateResponse(UUID passengerId){
        return RateResponse.builder()
            .toId(passengerId.toString())
            .eventId(Integer.toString(1))
            .rating(5f)
            .build();
    }

}
