package com.baro.noi_that_api.module.payments.repository;

import com.baro.noi_that_api.module.payments.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    Optional<Payment> findByOrderId(Integer orderId);

    Optional<Payment> findByTransactionId(String transactionId);
}