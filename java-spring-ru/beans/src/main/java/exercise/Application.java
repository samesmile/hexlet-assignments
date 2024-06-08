package exercise;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.chrono.ChronoLocalDateTime;

import exercise.daytime.Daytime;
import exercise.daytime.Day;
import exercise.daytime.Night;
import org.springframework.context.annotation.Bean;

// BEGIN

// END

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public Daytime getDaytime() {
        LocalTime currentTime = LocalTime.now();
        if (currentTime.isAfter(LocalTime.of(6, 0)) && currentTime.isBefore(LocalTime.of(22, 0))) {
            return new Day();
        } else {
            return new Night();
        }
    }}
