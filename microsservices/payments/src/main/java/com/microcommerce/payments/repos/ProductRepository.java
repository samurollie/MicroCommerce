package com.microcommerce.payments.repos;

import com.microcommerce.payments.models.ProductModel;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<ProductModel, Long> {
}
