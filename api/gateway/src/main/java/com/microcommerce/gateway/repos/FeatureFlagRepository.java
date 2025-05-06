package com.microcommerce.gateway.repos;

import com.microcommerce.gateway.models.FeatureFlagModel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface FeatureFlagRepository extends JpaRepository<FeatureFlagModel, Long> {
    Optional<FeatureFlagModel> findByName(String name);
}
