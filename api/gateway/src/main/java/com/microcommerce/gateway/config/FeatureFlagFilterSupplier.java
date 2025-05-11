package com.microcommerce.gateway.config;

import com.microcommerce.gateway.filters.FeatureFlagFilterFunctions;
import org.springframework.cloud.gateway.server.mvc.filter.SimpleFilterSupplier;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeatureFlagFilterSupplier extends SimpleFilterSupplier {
    public FeatureFlagFilterSupplier() {
        super(FeatureFlagFilterFunctions.class);
    }
}