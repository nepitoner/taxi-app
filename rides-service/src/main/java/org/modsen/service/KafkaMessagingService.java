package org.modsen.service;

import org.modsen.dto.request.RideAvailableEvent;

public interface KafkaMessagingService {

    void sendMessage(RideAvailableEvent rideAvailableEvent);

}
