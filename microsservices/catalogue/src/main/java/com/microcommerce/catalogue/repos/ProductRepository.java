package com.microcommerce.catalogue.repos;

import com.microcommerce.catalogue.models.ProductModel;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<ProductModel, Long> {
}
