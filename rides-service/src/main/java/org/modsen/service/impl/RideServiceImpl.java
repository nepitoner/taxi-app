package org.modsen.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modsen.dto.request.RideRequest;
import org.modsen.dto.request.RideRequestParams;
import org.modsen.dto.request.RideStatusRequest;
import org.modsen.dto.response.PagedRideResponse;
import org.modsen.dto.response.RideResponse;
import org.modsen.entity.Ride;
import org.modsen.entity.RideStatus;
import org.modsen.mapper.RideMapper;
import org.modsen.repository.RideRepository;
import org.modsen.service.RideService;
import org.modsen.utils.calculator.RidePriceCalculator;
import org.modsen.validator.RideValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RideServiceImpl implements RideService {

    private final Clock clock;

    private final RideMapper rideMapper;

    private final RideRepository rideRepository;

    private final RideValidator rideValidator;

    private final RidePriceCalculator ridePriceCalculator;

    @Override
    @Transactional(readOnly = true)
    public PagedRideResponse getAllRides(RideRequestParams requestParams) {
        int limit = Math.min(requestParams.limit(), 50);
        Sort sort = Sort.by(Sort.Direction.fromString(requestParams.sortDirection()), requestParams.sortBy());
        Pageable pageable = PageRequest.of(requestParams.page(), limit, sort);
        Page<Ride> responsePage = rideRepository.findAll(pageable);

        PagedRideResponse pagedRideResponse = rideMapper
                .mapPageEntityToPagedDto(requestParams.page(), requestParams.limit(), responsePage);
        log.info("Ride Service. Get all request. Pages amount {}", responsePage.getTotalPages());
        return pagedRideResponse;
    }

    @Override
    @Transactional(readOnly = true)
    public PagedRideResponse getAllRidesByDriverId(RideRequestParams requestParams, UUID driverId) {
        int limit = Math.min(requestParams.limit(), 50);
        Sort sort = Sort.by(Sort.Direction.fromString(requestParams.sortDirection()), requestParams.sortBy());
        Pageable pageable = PageRequest.of(requestParams.page(), limit, sort);
        Page<Ride> responsePage = rideRepository.findByDriverId(driverId, pageable);

        PagedRideResponse pagedRideResponse = rideMapper
                .mapPageEntityToPagedDto(requestParams.page(), requestParams.limit(), responsePage);
        log.info("Ride Service. Get all by driver id request. Pages amount {}", responsePage.getTotalPages());
        return pagedRideResponse;
    }

    @Override
    @Transactional(readOnly = true)
    public PagedRideResponse getAllRidesByPassengerId(RideRequestParams requestParams, UUID passengerId) {
        int limit = Math.min(requestParams.limit(), 50);
        Sort sort = Sort.by(Sort.Direction.fromString(requestParams.sortDirection()), requestParams.sortBy());
        Pageable pageable = PageRequest.of(requestParams.page(), limit, sort);
        Page<Ride> responsePage = rideRepository.findByPassengerId(passengerId, pageable);

        PagedRideResponse pagedRideResponse = rideMapper
                .mapPageEntityToPagedDto(requestParams.page(), requestParams.limit(), responsePage);
        log.info("Ride Service. Get all by passenger id request. Pages amount {}", responsePage.getTotalPages());
        return pagedRideResponse;
    }

    @Override
    @Transactional
    public UUID createRide(RideRequest request) {
        BigDecimal price = ridePriceCalculator
                .calculateRidePrice(request.startingCoordinates(), request.endingCoordinates());
        Ride rideToCreate = rideMapper.mapRequestToEntity(request, LocalDateTime.now(clock), price);
        UUID rideId = rideRepository.save(rideToCreate).getRideId();

        log.info("Ride Service. Create new ride. New ride id {}", rideId);
        return rideId;
    }

    @Override
    @Transactional
    public RideResponse updateRide(UUID rideId, RideRequest request) {
        rideValidator.checkExistence(rideId);

        Ride ride = rideRepository.findById(rideId).get();
        Ride rideSave = rideMapper.mapDtoToEntity(request, ride);
        Ride updatedRide = rideRepository.save(rideSave);

        log.info("Ride Service. Update ride with id {}", rideId);
        return rideMapper.mapEntityToResponse(updatedRide);
    }

    @Override
    @Transactional
    public RideResponse changeRideStatus(UUID rideId, RideStatusRequest request) {
        rideValidator.checkExistence(rideId);
        if (RideStatus.valueOf(request.rideStatus()) != RideStatus.CANCELED) {
            rideValidator.checkStatusProcessing(request, rideId);
        } 

        Ride ride = rideRepository.findById(rideId).get();
        Ride rideSave = rideMapper.mapDtoToEntityStatusUpdate(request, ride);
        Ride updatedRide = rideRepository.save(rideSave);

        log.info("Ride Service. Change ride status to {}. Ride id {}", updatedRide.getRideStatus(), rideId);
        return rideMapper.mapEntityToResponse(updatedRide);
    }

}
