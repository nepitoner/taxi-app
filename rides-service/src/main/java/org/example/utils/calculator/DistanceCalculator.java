package org.example.utils.calculator;

import lombok.RequiredArgsConstructor;
import org.example.config.RouterApiProperties;
import org.example.dto.response.DistanceResponse;
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
        String url = "https://api.openrouteservice.org/v2/directions/driving-car/json";

        String requestBody = String.format(
                "{\"coordinates\":[[%f,%f],[%f,%f]]}",
                from.getLongitude(), from.getLatitude(),
                to.getLongitude(), to.getLatitude()
        );


        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", properties.apiKey());
        headers.set("Content-Type", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<DistanceResponse> response = restTemplate.postForEntity(url, entity, DistanceResponse.class);

        return Objects.requireNonNull(response.getBody()).getTotalDistance();
    }
}


