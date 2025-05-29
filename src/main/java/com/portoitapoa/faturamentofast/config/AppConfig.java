package com.portoitapoa.faturamentofast.config;

import com.portoitapoa.faturamentofast.security.converter.prop.JwtConfiguration;
import feign.RequestInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * @author Fernando de Lima
 */
@EnableAsync
@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AppConfig {

    private final JwtConfiguration jwtConfiguration;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public RequestInterceptor requestTokenBearerInterceptor() {

        return requestTemplate -> {

            final var token = SecurityContextHolder.getContext().getAuthentication().getDetails();

            if(!requestTemplate.path().contains("email")) {
                requestTemplate.header(jwtConfiguration.getHeader().getName(),
                        jwtConfiguration.getHeader().getPrefix() + token);
            }
        };
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedMethods(Arrays.asList("*", "HEAD", "GET", "POST", "PUT", "DELETE", "PATCH"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(600L);
        configuration.setAllowedHeaders(
            Arrays.asList("*", "Origin", "X-Requested-With", "Content-Type", "Accept", "Authorization",
                "Access-Control-Allow-Headers", "Access-Control-Request-Method", "Access-Control-Request-Headers"));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}