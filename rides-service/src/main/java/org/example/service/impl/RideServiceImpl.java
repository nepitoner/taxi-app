package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.request.RideRequest;
import org.example.dto.request.RideStatusRequest;
import org.example.dto.response.PagedRideResponse;
import org.example.dto.response.RideResponse;
import org.example.entity.Ride;
import org.example.entity.RideStatus;
import org.example.mapper.RideMapper;
import org.example.repository.RideRepository;
import org.example.service.RideService;
import org.example.utils.calculator.RidePriceCalculator;
import org.example.validator.RideValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public PagedRideResponse getAllRides(int page, int limit) {
        Pageable pageable = PageRequest.of(page, limit);
        Page<Ride> responsePage = rideRepository.findAll(pageable);

        PagedRideResponse pagedRideResponse = rideMapper.mapPageEntityToPagedDto(page, limit, responsePage);
        log.info("Ride Service. Get all request. Pages amount {}", responsePage.getTotalPages());
        return pagedRideResponse;
    }

    @Override
    public PagedRideResponse getAllRidesByDriverId(int page, int limit, UUID driverId) {
        Pageable pageable = PageRequest.of(page, limit);
        Page<Ride> responsePage = rideRepository.findByDriverId(driverId, pageable);

        PagedRideResponse pagedRideResponse = rideMapper.mapPageEntityToPagedDto(page, limit, responsePage);
        log.info("Ride Service. Get all by driver id request. Pages amount {}", responsePage.getTotalPages());
        return pagedRideResponse;
    }

    @Override
    public PagedRideResponse getAllRidesByPassengerId(int page, int limit, UUID passengerId) {
        Pageable pageable = PageRequest.of(page, limit);
        Page<Ride> responsePage = rideRepository.findByPassengerId(passengerId, pageable);

        PagedRideResponse pagedRideResponse = rideMapper.mapPageEntityToPagedDto(page, limit, responsePage);
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
