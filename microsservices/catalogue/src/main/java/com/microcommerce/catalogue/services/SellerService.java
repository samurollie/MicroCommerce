package com.microcommerce.catalogue.services;

import com.microcommerce.catalogue.models.SellerModel;
import com.microcommerce.catalogue.repos.SellerRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@Transactional
public class SellerService {
    private final SellerRepository sellerRepository;

    public SellerService(SellerRepository sellerRepository) {
        this.sellerRepository = sellerRepository;
    }

    public Optional<SellerModel> getOptionalSellerById(Long id) {
        return sellerRepository.findById(id);
    }

    public SellerModel getSellerById(Long id) {
        return sellerRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Seller not found"));
    }
}
