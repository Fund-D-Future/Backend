package com.funddfuture.fund_d_future.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*"); // Allows all origins
        config.addAllowedHeader("*");        // Allows all headers
        config.addAllowedMethod("*");        // Allows all HTTP methods (GET, POST, etc.)

        source.registerCorsConfiguration("/**", config); // Applies to all endpoints
        return new CorsFilter(source);
    }
}
