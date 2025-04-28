package com.microcommerce.catalogue.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

@Entity
@Table(name = "products")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @NotBlank
    @Length(max = 30)
    private String name;

    @NotNull
    @NotBlank
    @Length(max = 255)
    private String description;

    @NotNull
    private float price;

    @NotNull
    private int quantity;

    @NotNull
    @ColumnDefault("0")
    @Max(value = 5)
    @Min(value = 0)
    private float rating;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ProductType type;

    @ManyToOne
    private SellerModel seller;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;
}
