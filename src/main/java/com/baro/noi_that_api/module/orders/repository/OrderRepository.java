package com.baro.noi_that_api.module.orders.repository;

import com.baro.noi_that_api.module.orders.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    List<Order> findByCustomerIdOrderByCreatedAtDesc(Integer customerId);

    List<Order> findByStatus(Order.OrderStatus status);
}