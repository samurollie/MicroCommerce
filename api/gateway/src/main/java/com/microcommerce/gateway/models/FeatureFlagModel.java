package com.microcommerce.gateway.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "feature_flag")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeatureFlagModel {
    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private boolean enabled;

    @Column(columnDefinition = "jsonb")
    private String config;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
