package org.modsen.repository;

import org.modsen.entity.Ride;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface RideRepository extends MongoRepository<Ride, UUID> {

    Page<Ride> findByDriverId(UUID driverId, Pageable pageable);

    Page<Ride> findByPassengerId(UUID passengerId, Pageable pageable);

}
