package com.microcommerce.catalogue.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

@Entity
@Table(name = "products")
@Data
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
    @Enumerated(EnumType.STRING)
    private ProductType type;

    @ManyToOne
    private SellerModel seller;

    private Date createdAt;
    private Date updatedAt;
}
