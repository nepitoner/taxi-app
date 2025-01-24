package org.example.repository;

import org.example.entity.Passenger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PassengerRepository extends JpaRepository<Passenger, UUID> {

    boolean existsByPhoneNumberOrEmail(String phoneNumber, String email);

    boolean existsByPhoneNumberAndPassengerIdIsNot(String phoneNumber, UUID passengerID);

    boolean existsByEmailAndPassengerIdIsNot(String email, UUID passengerId);

    boolean existsByPassengerIdAndIsDeletedIsFalse(UUID passengerId);

    Page<Passenger> findByIsDeletedIsFalse(Pageable pageable);

    Passenger findByPassengerIdAndIsDeletedIsFalse(UUID passengerId);
}
