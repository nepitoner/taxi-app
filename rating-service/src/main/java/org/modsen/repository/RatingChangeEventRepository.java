package org.modsen.repository;

import java.time.LocalDateTime;
import org.modsen.entity.RatingChangeEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface RatingChangeEventRepository extends MongoRepository<RatingChangeEvent, Integer> {

    @Query("{ 'changed_at': { $lte: ?0 } }")
    Page<RatingChangeEvent> findTop100ByChangedAtLessThanEqual(LocalDateTime currentDate, Pageable pageable);

}
