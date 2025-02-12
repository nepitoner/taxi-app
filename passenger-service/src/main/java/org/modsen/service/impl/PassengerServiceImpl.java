package org.modsen.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modsen.client.RatingClient;
import org.modsen.dto.request.RequestParams;
import org.modsen.dto.response.PagedPassengerResponse;
import org.modsen.dto.request.PassengerRequest;
import org.modsen.dto.response.PassengerResponse;
import org.modsen.entity.Passenger;
import org.modsen.mapper.PassengerMapper;
import org.modsen.repository.PassengerRepository;
import org.modsen.service.PassengerService;
import org.modsen.util.validator.PassengerValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PassengerServiceImpl implements PassengerService {

    private final PassengerValidator passengerValidator;

    private final PassengerMapper passengerMapper;

    private final PassengerRepository passengerRepository;

    private final Clock clock;

    private final RatingClient ratingClient;

    @Override
    @Transactional(readOnly = true)
    public PagedPassengerResponse getAllPassengers(RequestParams requestParams) {
        int limit = Math.min(requestParams.limit(), 50);
        Sort sort = Sort.by(Sort.Direction.fromString(requestParams.sortDirection()), requestParams.sortBy());
        Pageable pageable = PageRequest.of(requestParams.page(), limit, sort);
        Page<Passenger> responsePage = passengerRepository.findByIsDeletedIsFalse(pageable);

        PagedPassengerResponse pagedPassengerResponse = passengerMapper
                .mapPageEntityToPagedDto(requestParams.page(), limit, responsePage);
        log.info("Passenger Service. Get all request. Pages amount {}", responsePage.getTotalPages());
        return pagedPassengerResponse;
    }

    @Override
    @Transactional
    public UUID registerPassenger(PassengerRequest dto) {
        passengerValidator.checkUniqueness(dto);

        Passenger passengerToRegister = passengerMapper.mapDtoToEntity(dto);
        UUID passengerId = passengerRepository.save(passengerToRegister).getPassengerId();

        log.info("Passenger Service. Register new passenger with email {}. New passenger id {}",
                passengerToRegister.getEmail(), passengerId);
        return passengerId;
    }

    @Override
    @Transactional
    public PassengerResponse updatePassenger(UUID passengerId, PassengerRequest passengerRequest) {
        passengerValidator.checkExistenceAndPresence(passengerId);
        passengerValidator.checkUniqueness(passengerId, passengerRequest);

        Passenger passenger = passengerRepository.findByPassengerIdAndIsDeletedIsFalse(passengerId);
        Passenger updatePassenger = passengerMapper.mapDtoToEntity(passengerRequest, passengerId,
                passenger.getCreatedAt(), LocalDateTime.now(clock));

        Passenger newPassenger = passengerRepository.save(updatePassenger);
        log.info("Passenger Service. Update passenger with id {}", passengerId);
        return passengerMapper.mapEntityToDto(newPassenger);
    }

    @Override
    @Transactional
    public void deletePassenger(UUID passengerId) {
        passengerValidator.checkExistenceAndPresence(passengerId);

        Passenger passenger = passengerRepository.findByPassengerIdAndIsDeletedIsFalse(passengerId);
        passenger.setIsDeleted(true);
        passengerRepository.save(passenger);
        log.info("Passenger Service. Delete passenger with id {}", passengerId);
    }

    @Override
    @Transactional
    public UUID addPhoto(UUID passengerId, String fileRef) {
        passengerValidator.checkExistenceAndPresence(passengerId);

        Passenger passenger = passengerRepository.findByPassengerIdAndIsDeletedIsFalse(passengerId);
        passenger.setProfilePictureRef(fileRef);
        log.info("Passenger Service. Add photo. Passenger id {}", passengerId);
        return passengerRepository.save(passenger).getPassengerId();
    }

    @Override
    public void updatePassengerRating(UUID passengerId) {
        passengerValidator.checkExistenceAndPresence(passengerId);

        Passenger passenger = passengerRepository.findByPassengerIdAndIsDeletedIsFalse(passengerId);
        float rating = ratingClient.getRating(passengerId).rating();
        passenger.setRating(rating);
        passengerRepository.save(passenger);

        log.info("Passenger Service. Update rating. Passenger id {}", passengerId);
    }

}
