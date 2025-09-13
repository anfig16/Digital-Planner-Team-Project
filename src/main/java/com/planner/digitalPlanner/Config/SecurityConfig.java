package com.planner.digitalPlanner.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // Marks this class as a source of bean definitions for the application context
public class SecurityConfig {

    @Bean // Declares that the method will produce a bean to be managed by Spring's container
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // Configuring HTTP security for the application
        http
            .csrf(csrf -> csrf.disable()) // Disables CSRF protection 
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll() // Allows all requests without authentication (public access for now)
            );

        return http.build(); // Builds and returns the security filter chain
    }

}
