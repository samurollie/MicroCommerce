package com.microcommerice.cart.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${microcommerce.services.catalogue.base-url}")
    private String catalogueServiceBaseUrl;

    @Bean
    // @LoadBalanced
    public WebClient.Builder loadBalancedWebClientBuilder() {
        return WebClient.builder();
    }

    @Bean("catalogueWebClient")
    public WebClient catalogueWebClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder.baseUrl(catalogueServiceBaseUrl).build();
    }
}