package com.microcommerce.catalogue.services;

import com.microcommerce.catalogue.dto.ProductDTO;
import com.microcommerce.catalogue.models.ProductModel;
import com.microcommerce.catalogue.repos.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ProductService {

    private final SellerService sellerService;
    private final ProductRepository productRepository;

    public ProductService(SellerService sellerService, ProductRepository productRepository) {
        this.sellerService = sellerService;
        this.productRepository = productRepository;
    }

    public ProductModel save(ProductDTO product) {
        ProductModel productModel = new ProductModel();

        productModel.setName(product.getName());
        productModel.setDescription(product.getDescription());
        productModel.setPrice(product.getPrice());
        productModel.setQuantity(product.getQuantity());

        var seller = sellerService.getSellerById(product.getSellerId());

        productModel.setSeller(seller);
        return productRepository.save(productModel);
    }

    public Iterable<ProductModel> getProducts() {
        return productRepository.findAll();
    }
}
