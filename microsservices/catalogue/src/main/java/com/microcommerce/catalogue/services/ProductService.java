package com.microcommerce.catalogue.services;

import com.microcommerce.catalogue.dto.ProductDTO;
import com.microcommerce.catalogue.models.ProductModel;
import com.microcommerce.catalogue.repos.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class ProductService {
    private final ProductRepository productRepository;

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
        return productRepository.save(productModel);
    }

    public Iterable<ProductModel> getProducts() {
        return productRepository.findAll();
    }

    public ProductModel getProductById(long id) {
        return productRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
    }
}
