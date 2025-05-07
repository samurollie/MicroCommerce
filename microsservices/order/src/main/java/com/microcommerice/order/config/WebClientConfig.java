package com.microcommerice.order.config;

import org.springframework.context.annotation.Configuration;

import org.springframework.beans.factory.annotation.Value;
//import org.springframework.cloud.client.loadbalancer.LoadBalanced; // Para usar com Service Discovery
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${microcommerce.services.catalogue.base-url}")
    private String catalogueServiceBaseUrl;

    @Value("${microcommerce.services.cart.base-url}")
    private String cartServiceBaseUrl;

    // Bean para WebClient.Builder geral, pode ser usado para criar WebClients específicos
    // Se estiver usando Service Discovery (Eureka, Consul), anote com @LoadBalanced
    @Bean
    // @LoadBalanced // usando service discovery e 'lb://' URIs
    public WebClient.Builder loadBalancedWebClientBuilder() {
        return WebClient.builder();
    }

    @Bean("catalogueWebClient")
    public WebClient catalogueWebClient(WebClient.Builder webClientBuilder) {
        // Se não usar @LoadBalanced no builder, construa com URL base direta
        // return WebClient.builder().baseUrl(catalogueServiceBaseUrl).build();
        return webClientBuilder.baseUrl(catalogueServiceBaseUrl).build(); // Ex: "lb://catalogue-service" ou "http://localhost:8083"
    }

    @Bean("cartWebClient")
    public WebClient cartWebClient(WebClient.Builder webClientBuilder) {
        // return WebClient.builder().baseUrl(cartServiceBaseUrl).build();
        return webClientBuilder.baseUrl(cartServiceBaseUrl).build(); // Ex: "lb://cart-service" ou "http://localhost:8082"
    }
}