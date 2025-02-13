package com.example.shoplaptop.repository;

import com.example.shoplaptop.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IOrderItemRepository extends JpaRepository<OrderItem, Integer> {
    OrderItem save(OrderItem orderItem);
}
