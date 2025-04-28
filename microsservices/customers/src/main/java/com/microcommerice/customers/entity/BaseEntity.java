package com.microcommerice.customers.entity;

import jakarta.persistence.MappedSuperclass;

import java.util.Date;

@MappedSuperclass
public class BaseEntity {

    private Date createdAt;
    private Date updatedAt;
}
