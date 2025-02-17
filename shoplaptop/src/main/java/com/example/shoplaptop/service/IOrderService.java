package com.example.shoplaptop.service;

import com.example.shoplaptop.model.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.hibernate.query.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;


public interface IOrderService {
    void saveOrderItems(OrderSummary order, Users user);

    void saveAllOrderItemList(List<OrderItem> orderItemList);

    OrderSummary save(OrderSummary orderSummary);

    Page<OrderSummary> findAll(Pageable pageable);

    Page<OrderSummary> findAllByUserId(@Param("userId")Long userId, Pageable pageable);


    OrderSummary getById(Integer id);

    List<OrderItem> getOrderItemsByOrderId(Integer orderId);

    long countOrder();

    Page<OrderSummary> findAllByOrderStatus(@Param("status") OrderStatus status, Pageable pageable);

    Page<OrderSummary> findAllByOrderStatus(@Param("userId") Long userId, @Param("status") OrderStatus status, Pageable pageable);
}
