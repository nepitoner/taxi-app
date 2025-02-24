package org.modsen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class RidesApplication {

    public static void main(String[] args) {
        SpringApplication.run(RidesApplication.class, args);
    }
}