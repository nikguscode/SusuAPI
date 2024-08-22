package com.nikguscode.SusuAPI;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;

@SpringBootApplication
@Log4j2
public class SusuAPI {
    public static void main(String[] args) {
        SpringApplication.run(SusuAPI.class, args);
        log.info("SusuAPI started at {}", LocalDateTime.now());
    }
}
