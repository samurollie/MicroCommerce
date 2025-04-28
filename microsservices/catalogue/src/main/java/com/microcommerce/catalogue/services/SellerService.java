package com.microcommerce.catalogue.services;

import com.microcommerce.catalogue.models.SellerModel;
import com.microcommerce.catalogue.repos.SellerRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class SellerService {
    private SellerRepository sellerRepository;

    public Optional<SellerModel> getOptionalSellerById(Long id) {
        return sellerRepository.findById(id);
    }

    public SellerModel getSellerById(Long id) {
        return sellerRepository.findById(id).orElseThrow(() -> new RuntimeException("Seller not found"));
    }
}
