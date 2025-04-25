package com.microcommerce.catalogue.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.Date;

@Data
@Table(name = "user")
@Entity
public class UserModel {
    @Id
    private Long id;

    private String name;
    private String password;
    private String email;
    private Date createdAt;
}
