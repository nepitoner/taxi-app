package org.modsen.repository;

import org.modsen.entity.Rating;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RatingRepository extends MongoRepository<Rating, UUID> {

    boolean existsByFromIdAndRideId(UUID fromId, UUID rideId);

    boolean existsByRatingIdAndFromId(UUID ratingId, UUID fromId);

    Optional<Boolean> existsByRatingIdAndCommentNotNull(UUID ratingId);

    @Query(value = "{ 'toId': ?0 }", sort = "{ 'created_at': -1 }")
    List<Rating> findTopNByToId(UUID toId, int limit);

}
