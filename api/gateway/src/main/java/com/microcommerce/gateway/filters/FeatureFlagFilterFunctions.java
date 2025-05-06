package com.microcommerce.gateway.filters;

import org.springframework.web.servlet.function.HandlerFilterFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

public class FeatureFlagFilterFunctions {
    /**
     * Filtro “antes” que verifica se a feature está habilitada para o usuário.
     * @param feature nome da feature flag a checar
     * @return HandlerFilterFunction que bloqueia ou prossegue com a requisição
     */
    public static HandlerFilterFunction<ServerResponse, ServerResponse> featureEnabled(String feature) {
        return (request, next) -> {
            boolean isFeatureEnabled = checkFeatureFlag(feature, request);
            if (!isFeatureEnabled) {
                return ServerResponse.status(403).body("Feature desabilitada");
            }
            return next.handle(request);
        };
    }

    private static boolean checkFeatureFlag(String feature, ServerRequest request) {
        return true;
    }
}
