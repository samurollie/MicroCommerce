package com.microcommerce.catalogue.repos;

import com.microcommerce.catalogue.models.SellerModel;
import org.springframework.data.repository.CrudRepository;

public interface SellerRepository extends CrudRepository<SellerModel, Long> {
}
