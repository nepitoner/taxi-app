package org.modsen.kafka;

import static org.modsen.util.constant.ExceptionConstant.NO_AVAILABLE_DRIVERS_MESSAGE;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modsen.dto.request.RideAvailableEvent;
import org.modsen.entity.Driver;
import org.modsen.exception.NoAvailableDriversException;
import org.modsen.exception.RequestTimeoutException;
import org.modsen.repository.DriverRepository;
import org.modsen.service.EmailService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RideListener {

    private final EmailService emailService;

    private final DriverRepository driverRepository;

    @KafkaListener(
        topics = "${spring.kafka.ride-consumer.ride-topic}",
        groupId = "${spring.kafka.ride-consumer.ride-group-id}",
        containerFactory = "kafkaRideListenerContainerFactory")
    public void onMessage(RideAvailableEvent rideAvailableEvent) throws RequestTimeoutException {

        Driver driver = driverRepository.findFirstByIsAvailableTrue().orElseThrow(
            () -> new NoAvailableDriversException(NO_AVAILABLE_DRIVERS_MESSAGE));

        emailService.sendEmail(driver, rideAvailableEvent);
    }

}
