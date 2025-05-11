package com.microcommerce.gateway.services;

import com.microcommerce.gateway.models.FeatureFlagModel;
import com.microcommerce.gateway.repos.FeatureFlagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class FeatureFlagService {
    private final FeatureFlagRepository featureFlagRepository;

    public FeatureFlagService(FeatureFlagRepository featureFlagRepository) {
        this.featureFlagRepository = featureFlagRepository;
    }

    public List<FeatureFlagModel> listAll() {
        return featureFlagRepository.findAll();
    }

    public FeatureFlagModel getById(Long id) {
        return featureFlagRepository.findById(id)
                                    .orElseThrow(() -> new RuntimeException("Flag não encontrada"));
    }

    public FeatureFlagModel create(FeatureFlagModel flag) {
        return featureFlagRepository.save(flag);
    }

    public FeatureFlagModel update(Long id, FeatureFlagModel updated) {
        FeatureFlagModel existing = this.getById(id);
        existing.setName(updated.getName());
        existing.setEnabled(updated.isEnabled());
        existing.setConfig(updated.getConfig());
        return featureFlagRepository.save(existing);
    }

    public void delete(Long id) {
        featureFlagRepository.deleteById(id);
    }

    public static boolean isFeatureEnabled(String featureName) {
        /*return featureFlagRepository.findByName(featureName)
                         .map(FeatureFlagModel::isEnabled)
                         .orElse(false);*/
        return true;
    }
}