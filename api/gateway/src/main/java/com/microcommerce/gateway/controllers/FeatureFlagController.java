package com.microcommerce.gateway.controllers;

import com.microcommerce.gateway.models.FeatureFlagModel;
import com.microcommerce.gateway.services.FeatureFlagService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/flags")
public class FeatureFlagController {

    private final FeatureFlagService featureFlagService;

    public FeatureFlagController(FeatureFlagService featureFlagService) {
        this.featureFlagService = featureFlagService;
    }

    // Listar todas as flags
    @GetMapping
    public ResponseEntity<List<FeatureFlagModel>> list() {
        return ResponseEntity.ok(featureFlagService.listAll());
    }

    // Buscar flag por ID
    @GetMapping("/{id}")
    public ResponseEntity<FeatureFlagModel> get(@PathVariable Long id) {
        return ResponseEntity.ok(featureFlagService.getById(id));
    }

    // Criar nova flag
    @PostMapping
    public ResponseEntity<FeatureFlagModel> create(@RequestBody FeatureFlagModel flag) {
        FeatureFlagModel created = featureFlagService.create(flag);
        return ResponseEntity
                .created(URI.create("/api/v1/flags/" + created.getId()))
                .body(created);
    }

    // Atualizar flag existente
    @PutMapping("/{id}")
    public ResponseEntity<FeatureFlagModel> update(
            @PathVariable Long id,
            @RequestBody FeatureFlagModel flag) {
        return ResponseEntity.ok(featureFlagService.update(id, flag));
    }

    // Deletar flag
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        featureFlagService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

