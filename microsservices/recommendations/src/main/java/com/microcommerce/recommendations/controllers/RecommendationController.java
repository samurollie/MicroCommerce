package com.microcommerce.recommendations.controllers;

import com.microcommerce.recommendations.dto.ClientProductDTO;
import com.microcommerce.recommendations.services.RecommendationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
@CrossOrigin(origins = "*")
public class RecommendationController {
    private RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping
    public ResponseEntity<List<ClientProductDTO>> getRecommendations() {
        List<ClientProductDTO> products = this.recommendationService.getRecommendations();

        return ResponseEntity.ok(products);
    }
}
