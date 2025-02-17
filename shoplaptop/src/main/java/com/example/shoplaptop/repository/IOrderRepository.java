package com.example.shoplaptop.repository;

import com.example.shoplaptop.model.OrderStatus;
import com.example.shoplaptop.model.OrderSummary;
import jdk.jfr.Registered;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IOrderRepository extends JpaRepository<OrderSummary, Integer> {
    OrderSummary save(OrderSummary orderSummary);

    Page<OrderSummary> findAll(Pageable pageable);

    @Query("select order from OrderSummary order where order.user.id = :userId")
    Page<OrderSummary> findAllByUserId(@Param("userId")Long userId, Pageable pageable);

    OrderSummary getById(Integer id);

    long count();


    @Query("select order from OrderSummary order where order.orderStatus = :status")
    Page<OrderSummary> findAllByOrderStatus(@Param("status") OrderStatus status, Pageable pageable);


    @Query("select order from OrderSummary order where order.user.id = :userId and order.orderStatus = :status")
    Page<OrderSummary> findAllByOrderStatus(@Param("userId")Long userId, @Param("status") OrderStatus status, Pageable pageable);
}
