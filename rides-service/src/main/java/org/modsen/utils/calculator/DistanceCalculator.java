package org.modsen.utils.calculator;

import static org.modsen.utils.constant.ExceptionConstant.SERVICE_IS_NOT_AVAILABLE_MESSAGE;
import static org.modsen.utils.constant.RideConstant.COORDINATES_FORMAT;

import lombok.RequiredArgsConstructor;
import org.modsen.config.properties.RouterApiProperties;
import org.modsen.dto.response.DistanceResponse;
import org.modsen.exception.ServiceIsNotAvailable;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.Objects;


@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(RouterApiProperties.class)
public class DistanceCalculator {

    private final RouterApiProperties properties;

    private final RestTemplate restTemplate;

    public double getRoadDistance(Point from, Point to) {
        String url = properties.link();

        String requestBody = String.format(
                COORDINATES_FORMAT,
                from.getLongitude(), from.getLatitude(),
                to.getLongitude(), to.getLatitude()
        );

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", properties.apiKey());
        headers.set("Content-Type", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<DistanceResponse> response;

        try {
             response = restTemplate.postForEntity(url, entity, DistanceResponse.class);
        } catch (Exception e) {
            throw new ServiceIsNotAvailable(SERVICE_IS_NOT_AVAILABLE_MESSAGE);
        }

        return Objects.requireNonNull(response.getBody()).getTotalDistance();
    }

}


