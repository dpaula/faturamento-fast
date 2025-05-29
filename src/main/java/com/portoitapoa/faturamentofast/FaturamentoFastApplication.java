package com.portoitapoa.faturamentofast;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@Slf4j
@EnableFeignClients
@SpringBootApplication
public class FaturamentoFastApplication {

    @Value("${config.message}")
    private String message;

    public static void main(final String[] args) {
        SpringApplication.run(FaturamentoFastApplication.class, args);
    }

    @Bean
    CommandLineRunner init() {
        return args -> log.info(message);
    }
}
