package org.modsen.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RideEventListener {

    private final KafkaTemplate<String, RideAvailableEvent> kafkaTemplate;

    @KafkaListener(topics = "ride-passenger-topic", groupId = "ride")
    public void listen(RideRequestEvent rideRequestEvent) {
        kafkaTemplate.se
    }

    public void sendMessage(ideAvailableEvent ride) {
        kafkaTemplate.send(
                properties.topic()
                        .getPassengerDriverTopic(),
                properties.groupId(), rateResponse);
        log.info("Message {} was successfully sent", rateResponse);
    }


}
