package com.microcommerice.customers.repository;

import com.microcommerice.customers.entity.Address;
import com.microcommerice.customers.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findByCustomer(Customer customer);

    Optional<Address> findByCustomerAndMain(Customer customer, boolean main);
}

