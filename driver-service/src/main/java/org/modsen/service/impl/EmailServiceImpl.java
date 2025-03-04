package org.modsen.service.impl;

import static org.modsen.util.constant.DriverServiceConstant.EMAIL_BUTTONS;
import static org.modsen.util.constant.DriverServiceConstant.EMAIL_SUBJECT;
import static org.modsen.util.constant.ExceptionConstant.MESSAGE_CAN_T_BE_SENT_RIGHT_NOW;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modsen.config.properties.ResendConfigProperties;
import org.modsen.dto.request.RideAvailableEvent;
import org.modsen.entity.Driver;
import org.modsen.exception.ServiceIsNotAvailable;
import org.modsen.service.EmailService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(ResendConfigProperties.class)
public class EmailServiceImpl implements EmailService {

    private final ResendConfigProperties resendConfigProperties;

    @Override
    public void sendEmail(Driver driver, RideAvailableEvent rideAvailableEvent) {
        Resend resend = new Resend(resendConfigProperties.apiKey());

        CreateEmailOptions params = CreateEmailOptions.builder()
            .from(resendConfigProperties.from())
            .to(driver.getEmail())
            .subject(EMAIL_SUBJECT)
            .html(EMAIL_BUTTONS.formatted(rideAvailableEvent.rating(), rideAvailableEvent.rideId(), driver.getId(),
                rideAvailableEvent.rideId(), driver.getId()))
            .build();

        try {
            resend.emails().send(params);
            log.info("EmailService. Send available right message to driver {}", driver.getId());
        } catch (ResendException e) {
            throw new ServiceIsNotAvailable(MESSAGE_CAN_T_BE_SENT_RIGHT_NOW);
        }
    }
}