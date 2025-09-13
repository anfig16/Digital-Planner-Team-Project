package com.planner.digitalPlanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication // This annotation combines @Configuration, @EnableAutoConfiguration, and @ComponentScan
public class DigitalPlannerApplication {

    // Main method to run the Spring Boot application
    public static void main(String[] args) {
        SpringApplication.run(DigitalPlannerApplication.class, args); // Starts the Spring Boot application
    }
}
