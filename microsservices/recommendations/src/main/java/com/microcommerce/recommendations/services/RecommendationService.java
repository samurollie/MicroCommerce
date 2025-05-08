package com.microcommerce.recommendations.services;

import com.microcommerce.recommendations.dto.ClientProductDTO;
import com.microcommerce.recommendations.external.CatalogueServiceClient;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecommendationService {
    private final CatalogueServiceClient catalogueServiceClient;

    public RecommendationService(CatalogueServiceClient catalogueServiceClient) {
        this.catalogueServiceClient = catalogueServiceClient;
    }

    public List<ClientProductDTO> getRecommendations() {
        List<ClientProductDTO> top30 = this.catalogueServiceClient.listCatalogue()
                .stream()
                .sorted(Comparator.comparingDouble(ClientProductDTO::getRating).reversed())
                .limit(30)
                .collect(Collectors.toList());

        Collections.shuffle(top30);

        return top30.stream()
                .limit(5)
                .collect(Collectors.toList());
    }

}
