package com.microcommerce.gateway.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microcommerce.gateway.models.FeatureFlagModel;
import com.microcommerce.gateway.repos.FeatureFlagRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.HashMap;

//@Configuration
public class DataLoader {

    /*private final ObjectMapper objectMapper = new ObjectMapper();

    @Bean
    CommandLineRunner initDatabase(FeatureFlagRepository repository) {
        return args -> {
            Arrays.asList(
                    "payment",
                    "shipping",
                    "orders",
                    "catalogue",
                    "cart",
                    "recommendation"
            ).forEach(featureName -> {
                if (repository.findByName(featureName).isEmpty()) {
                    FeatureFlagModel flag = new FeatureFlagModel();
                    flag.setName(featureName);
                    flag.setEnabled(true);

                    *//*try {
                        flag.setConfig(objectMapper.writeValueAsString(new HashMap<>()));
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }*//*
                    flag.setConfig(null);
                    repository.save(flag);
                }
            });
        };
    }*/
}