package com.microcommerice.cart.config;

import lombok.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${microcommerce.services.catalogue.base-url}")
    private String catalogueServiceBaseUrl;

    @Bean
    // @LoadBalanced // Remova ou comente se NÃO estiver usando service discovery e 'lb://' URIs
    public WebClient.Builder loadBalancedWebClientBuilder() {
        return WebClient.builder();
    }

    @Bean("catalogueWebClient")
    public WebClient catalogueWebClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder.baseUrl(catalogueServiceBaseUrl).build();
    }
}