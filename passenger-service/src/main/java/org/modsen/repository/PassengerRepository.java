package org.modsen.repository;

import java.util.UUID;
import org.modsen.entity.Passenger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassengerRepository extends JpaRepository<Passenger, UUID> {

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumberAndPassengerIdIsNot(String phoneNumber, UUID passengerID);

    boolean existsByEmailAndPassengerIdIsNot(String email, UUID passengerId);

    boolean existsByPassengerIdAndIsDeletedIsFalse(UUID passengerId);

    Page<Passenger> findByIsDeletedIsFalse(Pageable pageable);

    Passenger findByPassengerIdAndIsDeletedIsFalse(UUID passengerId);

}
