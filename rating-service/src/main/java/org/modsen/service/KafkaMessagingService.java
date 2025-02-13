package org.modsen.service;

import org.modsen.dto.response.RateResponse;


public interface KafkaMessagingService {

    void sendMessage(RateResponse rateResponse);

}
