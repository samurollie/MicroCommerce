package com.microcommerce.catalogue.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/catalogue")
public class CatalogueController {
    @GetMapping
    public ResponseEntity<String> getCatalogue() {
        return ResponseEntity.ok("Catalogue");
    }
}
