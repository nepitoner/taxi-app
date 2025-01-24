package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.PassengerDtoRequest;
import org.example.dto.PassengerDtoResponse;
import org.example.entity.Passenger;
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
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PassengerServiceImpl implements PassengerService {

    private final PassengerMapper passengerMapper;

    private final PassengerRepository passengerRepository;

    private final Clock clock;

    @Override
    public List<PassengerDtoResponse> getAllPassengers(int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<Passenger> responsePage = passengerRepository.findByIsDeletedIsFalse(pageable);
        List<Passenger> passengers = responsePage.getContent();
        int totalPageAmount = responsePage.getTotalPages();

        log.info("Getting all passengers. Total page amount {}", totalPageAmount);
        return passengers.stream()
                .map(passenger -> passengerMapper.mapEntityToDto(passenger, totalPageAmount))
                .toList();
    }

    @Override
    @Transactional
    public UUID registerPassenger(PassengerDtoRequest dto) {
        Passenger passengerToRegister = passengerMapper.mapDtoToEntity(dto);
        UUID passengerId = passengerRepository.save(passengerToRegister).getPassengerId();

        log.info("Passenger with email {} was successfully created. New id {}",
                passengerToRegister.getEmail(), passengerId);
        return passengerId;
    }

    @Override
    @Transactional
    public PassengerDtoResponse updatePassenger(UUID passengerId, PassengerDtoRequest passengerDtoRequest) {
        Passenger passenger = passengerRepository.findByPassengerIdAndIsDeletedIsFalse(passengerId);
        Passenger updatePassenger = passengerMapper.mapDtoToEntity(passengerDtoRequest, passengerId,
                passenger.getCreatedAt(), LocalDateTime.now(clock));

        Passenger newPassenger = passengerRepository.save(updatePassenger);
        log.info("Passenger with id {} was successfully updated", passengerId);
        return passengerMapper.mapEntityToDto(newPassenger);
    }

    @Override
    @Transactional
    public void deletePassenger(UUID passengerId) {
        Passenger passenger = passengerRepository.findById(passengerId).orElseThrow();
        passenger.setIsDeleted(true);
        passengerRepository.save(passenger);
        log.info("Passenger with id {} was successfully deleted", passengerId);
    }

    @Override
    @Transactional
    public UUID addPhoto(UUID passengerId, String fileRef) {
        Passenger passenger = passengerRepository.findById(passengerId).orElseThrow();
        passenger.setProfilePictureRef(fileRef);
        log.info("Photo for passenger with id {} was successfully added", passengerId);
        return passengerRepository.save(passenger).getPassengerId();
    }

}
