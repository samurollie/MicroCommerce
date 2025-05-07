package com.microcommerice.cart.client;


import lombok.Getter;
import lombok.Setter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "catalogue-service", url = "${services.catalogue.url:http://localhost:8084}")
public interface CatalogueClient {

    @GetMapping("/api/catalogue/products/{id}")
    ProductDto getProductById(@PathVariable("id") Long id);

    // DTO inner class
    @Getter
    @Setter
    class ProductDto {
        private Long id;
        private String name;
        private String description;
        private java.math.BigDecimal price;
        private String imageUrl;
        private Integer stock;
    }
}