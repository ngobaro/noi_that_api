package com.baro.noi_that_api.module.customers.repository;

import com.baro.noi_that_api.module.customers.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    Optional<Customer> findByEmail(String email);

    Optional<Customer> findByIdGoogle(String idGoogle);

    boolean existsByEmail(String email);
}