package com.microcommerce.catalogue.controllers;

import com.microcommerce.catalogue.dto.ProductDTO;
import com.microcommerce.catalogue.models.ProductModel;
import com.microcommerce.catalogue.services.CatalogueService;
import com.microcommerce.catalogue.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/catalogue")
public class CatalogueController {

    private final CatalogueService catalogueService;
    private final ProductService productService;

    public CatalogueController(CatalogueService catalogueService, ProductService productService) {
        this.catalogueService = catalogueService;
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<ProductModel>> getFullCatalogue() {
        try {
            var list = catalogueService.getFullCatalogue();
            return ResponseEntity.ok(list);
        }  catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductModel> getProductById(@PathVariable long id) {
        try {
            var product = productService.getProductById(id);
            return ResponseEntity.ok(product);
        }  catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<ProductModel> addProduct(@RequestBody @Valid ProductDTO product) {
        try {
            var savedProduct = productService.save(product);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
