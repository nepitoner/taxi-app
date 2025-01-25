package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.PagedPassengerResponse;
import org.example.dto.PassengerRequest;
import org.example.dto.PassengerResponse;
import org.example.entity.Passenger;
import org.example.validator.PassengerValidator;
import org.example.mapper.PassengerMapper;
import org.example.repository.PassengerRepository;
import org.example.service.PassengerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    @Override
    public PagedPassengerResponse getAllPassengers(int page, int limit) {
        Pageable pageable = PageRequest.of(page, limit);
        Page<Passenger> responsePage = passengerRepository.findByIsDeletedIsFalse(pageable);
        PagedPassengerResponse pagedPassengerResponse = passengerMapper.mapPageEntityToPagedDto(page, limit,
                responsePage.getTotalElements(), responsePage.get()
                        .map(passengerMapper::mapEntityToDto)
                        .toList());
        log.info("Getting all passengers.");
        return pagedPassengerResponse;
    }

    @Override
    @Transactional
    public UUID registerPassenger(PassengerRequest dto) {
        passengerValidator.checkUniqueness(dto);

        Passenger passengerToRegister = passengerMapper.mapDtoToEntity(dto);
        UUID passengerId = passengerRepository.save(passengerToRegister).getPassengerId();

        log.info("Passenger with email {} was successfully created. New id {}",
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
        log.info("Passenger with id {} was successfully updated", passengerId);
        return passengerMapper.mapEntityToDto(newPassenger);
    }

    @Override
    @Transactional
    public void deletePassenger(UUID passengerId) {
        passengerValidator.checkExistenceAndPresence(passengerId);

        Passenger passenger = passengerRepository.findByPassengerIdAndIsDeletedIsFalse(passengerId);
        passenger.setIsDeleted(true);
        passengerRepository.save(passenger);
        log.info("Passenger with id {} was successfully deleted", passengerId);
    }

    @Override
    @Transactional
    public UUID addPhoto(UUID passengerId, String fileRef) {
        passengerValidator.checkExistenceAndPresence(passengerId);

        Passenger passenger = passengerRepository.findByPassengerIdAndIsDeletedIsFalse(passengerId);
        passenger.setProfilePictureRef(fileRef);
        log.info("Photo for passenger with id {} was successfully added", passengerId);
        return passengerRepository.save(passenger).getPassengerId();
    }

}
