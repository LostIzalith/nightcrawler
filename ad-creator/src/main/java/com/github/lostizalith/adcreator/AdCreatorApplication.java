package com.github.lostizalith.adcreator;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;

import java.util.Arrays;

@SpringBootApplication
public class AdCreatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdCreatorApplication.class, args);
    }

    @Bean
    public CommandLineRunner run(final ApplicationContext context) {
        return args -> {
            // TODO some business
        };
    }
}
