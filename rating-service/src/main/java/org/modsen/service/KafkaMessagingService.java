package org.modsen.service;


import java.util.UUID;

public interface KafkaMessagingService {

    void sendPassengerMessage(UUID message);

}
