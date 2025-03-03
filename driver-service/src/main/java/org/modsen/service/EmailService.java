package org.modsen.service;

import org.modsen.dto.request.RideAvailableEvent;
import org.modsen.entity.Driver;
import org.modsen.exception.RequestTimeoutException;

public interface EmailService {

    void sendEmail(Driver driver, RideAvailableEvent rideAvailableEvent) throws RequestTimeoutException;

}
