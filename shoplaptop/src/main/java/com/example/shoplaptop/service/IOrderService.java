package com.example.shoplaptop.service;

import com.example.shoplaptop.model.CartItem;
import com.example.shoplaptop.model.OrderItem;
import com.example.shoplaptop.model.OrderSummary;
import com.example.shoplaptop.model.Users;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.hibernate.query.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;


public interface IOrderService {
    void saveOrderItems(OrderSummary order, Users user);

    void saveAllOrderItemList(List<OrderItem> orderItemList);

    OrderSummary save(OrderSummary orderSummary);

    Page<OrderSummary> findAll(Pageable pageable);

    OrderSummary getById(Integer id);

    List<OrderItem> getOrderItemsByOrderId(Integer orderId);

    long countOrder();
}
