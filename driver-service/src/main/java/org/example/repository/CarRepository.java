package org.example.repository;

import org.example.entity.Car;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CarRepository extends JpaRepository<Car, UUID> {

    boolean existsByNumber(String number);

    boolean existsByNumberAndIdIsNot(String number, UUID id);

    boolean existsByIdAndIsDeletedIsFalse(UUID id);

    Page<Car> findByIsDeletedIsFalse(Pageable pageable);

    Car findByIdAndIsDeletedIsFalse(UUID id);
}
