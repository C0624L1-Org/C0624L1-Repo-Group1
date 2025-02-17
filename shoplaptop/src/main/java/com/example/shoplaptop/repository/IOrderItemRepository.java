package com.example.shoplaptop.repository;

import com.example.shoplaptop.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IOrderItemRepository extends JpaRepository<OrderItem, Integer> {
    OrderItem save(OrderItem orderItem);

    @Query("select oi from OrderItem oi left join OrderSummary os on oi.orderSummary.id = os.id where os.id= :orderId ")
    List<OrderItem> findAllByOrderSummaryId(@Param("orderId") Integer orderId);
}
