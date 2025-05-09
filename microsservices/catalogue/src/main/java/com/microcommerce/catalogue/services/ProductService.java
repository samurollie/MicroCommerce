package com.microcommerce.catalogue.services;

import com.microcommerce.catalogue.dto.ProductDTO;
import com.microcommerce.catalogue.models.ProductModel;
import com.microcommerce.catalogue.repos.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.stream.StreamSupport;

@Service
@Transactional
public class ProductService {
    private final ProductRepository productRepository;

    @Value("${catalogue.images.base-url}")
    private String imagesBaseUrl;

    public ProductService( ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductModel save(ProductDTO product) {
        ProductModel productModel = new ProductModel();

        productModel.setName(product.getName());
        productModel.setDescription(product.getDescription());
        productModel.setPrice(product.getPrice());
        productModel.setQuantity(product.getQuantity());
        productModel.setType(product.getType());
        productModel.setRating(product.getRating());
        productModel.setSeller(product.getSeller());
        productModel.setImageName(product.getImageName());
        return productRepository.save(productModel);
    }

    public Iterable<ProductDTO> getProducts() {
        var products = productRepository.findAll();
        return StreamSupport.stream(products.spliterator(), false)
                .map(this::toDTO)
                .toList();
    }

    public ProductDTO getProductById(long id) {
        var product = productRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
        return toDTO(product);
    }

    private ProductDTO toDTO(ProductModel entity) {
        ProductDTO dto = new ProductDTO();
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setPrice(entity.getPrice());
        dto.setQuantity(entity.getQuantity());
        dto.setRating(entity.getRating());
        dto.setType(entity.getType());
        dto.setSeller(entity.getSeller());

        dto.setImageName(entity.getImageName());

        // e monta a URL completa para o cliente
        dto.setImageUrl(imagesBaseUrl + entity.getImageName());
        return dto;
    }

}
