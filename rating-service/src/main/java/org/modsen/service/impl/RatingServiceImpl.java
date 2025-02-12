package org.modsen.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modsen.config.properties.RatingServiceProperties;
import org.modsen.dto.request.RatingRequest;
import org.modsen.dto.request.RequestParams;
import org.modsen.dto.request.RideCommentRequest;
import org.modsen.dto.response.PagedRatingResponse;
import org.modsen.dto.response.RateResponse;
import org.modsen.dto.response.RatingResponse;
import org.modsen.dto.response.RideResponse;
import org.modsen.entity.Outbox;
import org.modsen.entity.Rating;
import org.modsen.mapper.RatingMapper;
import org.modsen.repository.OutboxRepository;
import org.modsen.repository.RatingRepository;
import org.modsen.service.RatingService;
import org.modsen.utils.validator.RatingValidator;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(RatingServiceProperties.class)
public class RatingServiceImpl implements RatingService {

    private final RatingValidator ratingValidator;

    private final RatingMapper ratingMapper;

    private final RatingRepository ratingRepository;

    private final RatingServiceProperties properties;

    private final OutboxRepository outboxRepository;

    @Override
    @Transactional(readOnly = true)
    public PagedRatingResponse getAllRatings(RequestParams requestParams) {
        int limit = Math.min(requestParams.limit(), 50);
        Sort sort = Sort.by(Sort.Direction.fromString(requestParams.sortDirection()), requestParams.sortBy());
        Pageable pageable = PageRequest.of(requestParams.page(), limit, sort);
        Page<Rating> responsePage = ratingRepository.findAll(pageable);

        PagedRatingResponse pagedRatingResponse = ratingMapper
                .mapPageEntityToPagedDto(requestParams.page(), limit, responsePage);
        log.info("Rating Service. Get all request. Pages amount {}", responsePage.getTotalPages());
        return pagedRatingResponse;
    }

    @Override
    @Transactional(readOnly = true)
    public RateResponse getRateById(UUID toId) {
        List<Rating> ratings = ratingRepository.findTopNByToId(toId, properties.limit());

        float rating = (float) ratings.stream()
                .mapToDouble(Rating::getRating)
                .average()
                .orElse(0.0);

        log.info("Rating Service. Get rating by id. Rating {}", rating);
        return ratingMapper.mapToRateResponse(toId, rating);
    }

    @Override
    @Transactional
    public UUID createRating(RatingRequest request, UUID participantId) {
        RideResponse rideResponse = ratingValidator.checkRideExistenceAndPresence(request.rideId(), participantId);
        ratingValidator.checkIfAlreadyRated(participantId, request.rideId());

        Rating ratingToCreate;
        UUID toId;
        if (participantId == rideResponse.driverId()) {
            toId = rideResponse.passengerId();
            ratingToCreate = ratingMapper.mapRequestToEntity(request, rideResponse.driverId(),
                    rideResponse.passengerId());
        } else {
            toId = rideResponse.driverId();
            ratingToCreate = ratingMapper.mapRequestToEntity(request, rideResponse.passengerId(),
                    rideResponse.driverId());
        }

        outboxRepository.save(Outbox.builder()
                .participantId(toId)
                .build());
        log.info("Rating for {} was successfully stored to outbox", toId);

        UUID ratingId = ratingRepository.save(ratingToCreate).getRatingId();

        log.info("Rating Service. Create new rating. New rating id {}", ratingId);
        return ratingId;
    }

    @Override
    @Transactional
    public RatingResponse addRideComment(UUID ratingId, RideCommentRequest request) {
        ratingValidator.checkRatingExistence(ratingId, request.fromId());
        ratingValidator.checkIfAlreadyCommented(ratingId);

        Rating rating = ratingRepository.findById(ratingId).get();
        Rating ratingSave = ratingMapper.mapDtoToEntityCommentUpdate(request, rating);
        Rating updatedRating = ratingRepository.save(ratingSave);

        log.info("Rating Service. Add rating comment. Rating id {}", ratingId);
        return ratingMapper.mapEntityToResponse(updatedRating);
    }

}
