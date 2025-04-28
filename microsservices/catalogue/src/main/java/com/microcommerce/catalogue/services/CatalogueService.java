package com.microcommerce.catalogue.services;

import com.microcommerce.catalogue.models.ProductModel;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Transactional
@Service
public class CatalogueService {

    private final ProductService productService;

    public CatalogueService(ProductService productService) {
        this.productService = productService;
    }

    public List<ProductModel> getFullCatalogue() {
        var list = productService.getProducts();

        return StreamSupport.stream(list.spliterator(), false)
                            .collect(Collectors.toList());
    }
}
