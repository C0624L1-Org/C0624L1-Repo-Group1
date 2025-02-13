package com.example.shoplaptop.repository;

import com.example.shoplaptop.model.OrderSummary;
import jdk.jfr.Registered;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IOrderRepository extends JpaRepository<OrderSummary, Integer> {
    OrderSummary save(OrderSummary orderSummary);
}
