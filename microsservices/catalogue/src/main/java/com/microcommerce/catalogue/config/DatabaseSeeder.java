package com.microcommerce.catalogue.config;

import com.microcommerce.catalogue.models.ProductModel;
import com.microcommerce.catalogue.models.ProductType;
import com.microcommerce.catalogue.repos.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Configuration
public class DatabaseSeeder {

    @Bean
    CommandLineRunner initDatabase(ProductRepository productRepository) {
        return args -> {
            if (productRepository.count() == 0) {
                List<ProductModel> products = new ArrayList<>();
                Random random = new Random();

                for (ProductType type : ProductType.values()) {
                    String sellerName = getSeller(type);

                    for (int i = 1; i <= 10; i++) {
                        ProductModel product = new ProductModel();
                        product.setName(getProductName(type, i));
                        product.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer feugiat dui non scelerisque finibus. Etiam nec lorem felis. Fusce erat orci, tincidunt sed nulla ac, porttitor fermentum nunc. In at erat ut mi pharetra congue. Integer sollicitudin odio at condimentum elementum. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Nunc vehicula tincidunt purus semper euismod. Donec fermentum lobortis libero, ut auctor dolor aliquet nec. Maecenas at mollis massa, ac blandit magna. In vel feugiat nisi. Sed augue diam, efficitur eget nunc vel, sagittis sagittis orci. Morbi elementum tellus rhoncus, tincidunt sapien at, euismod lacus. Etiam volutpat sem metus, a sagittis est tincidunt id. Praesent auctor sodales mi, nec iaculis metus tincidunt vel. Pellentesque in mi eget mauris scelerisque molestie. Vivamus eu ornare libero. In pretium iaculis nulla id tempor. Mauris sagittis, risus tristique vestibulum lacinia, tortor diam convallis est, ac bibendum leo ex id leo. Nulla facilisi. ");
                        product.setPrice((float) (Math.round(random.nextFloat(500) * 1000 + 1) / 100.0));
                        product.setRating(Math.round(random.nextFloat() * 50) / 10.0f);
                        product.setSeller(sellerName);
                        product.setType(type);
                        product.setQuantity(10);

                        products.add(product);
                    }
                }

                productRepository.saveAll(products);
            }
        };
    }

    private String getSeller(ProductType type) {
        return switch (type) {
            case FOOD -> "Mercado Central";
            case BOOK -> "Livraria Popular";
            case ELECTRONIC -> "Tech Store";
            case CLOTHING -> "Fashion Shop";
            case FURNITURE -> "Móveis & Cia";
            case TOY -> "Toy World";
            case COSMETIC -> "Beauty Store";
            case STATIONERY -> "Papelaria Central";
            case SPORT -> "Sports Center";
            case OTHER -> "Loja Geral";
        };
    }

    private String getProductName(ProductType type, int counter) {
        return switch (type) {
            case FOOD -> "Alimento " + counter;
            case BOOK -> "Livro " + counter;
            case ELECTRONIC -> "Eletrônico " + counter;
            case CLOTHING -> "Roupa " + counter;
            case FURNITURE -> "Móvel " + counter;
            case TOY -> "Brinquedo " + counter;
            case COSMETIC -> "Cosmético " + counter;
            case STATIONERY -> "Material " + counter;
            case SPORT -> "Item Esportivo " + counter;
            case OTHER -> "Item " + counter;
        };
    }

    private String getDescription(ProductType type, int counter) {
        return switch (type) {
            case FOOD -> "Descrição do alimento " + counter;
            case BOOK -> "Descrição do livro " + counter;
            case ELECTRONIC -> "Descrição do eletrônico " + counter;
            case CLOTHING -> "Descrição da roupa " + counter;
            case FURNITURE -> "Descrição do móvel " + counter;
            case TOY -> "Descrição do brinquedo " + counter;
            case COSMETIC -> "Descrição do cosmético " + counter;
            case STATIONERY -> "Descrição do material " + counter;
            case SPORT -> "Descrição do item esportivo " + counter;
            case OTHER -> "Descrição do item " + counter;
        };
    }
}