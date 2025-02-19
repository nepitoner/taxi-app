package org.modsen.repository;

import java.util.Optional;
import org.modsen.entity.Driver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DriverRepository extends JpaRepository<Driver, UUID> {

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumberAndIdIsNot(String phoneNumber, UUID id);

    boolean existsByEmailAndIdIsNot(String email, UUID id);

    boolean existsByIdAndIsDeletedIsFalse(UUID id);

    Page<Driver> findByIsDeletedIsFalse(Pageable pageable);

    Driver findByIdAndIsDeletedIsFalse(UUID id);

    Optional<Driver> findFirstByIsAvailableTrue();

}
