package com.example.shoplaptop.service;

import com.example.shoplaptop.model.CartItem;
import com.example.shoplaptop.model.OrderItem;
import com.example.shoplaptop.model.OrderSummary;
import com.example.shoplaptop.model.Users;

import java.util.List;


public interface IOrderService {
    void saveOrderItems(OrderSummary order, Users user);
    void saveAllOrderItemList(List<OrderItem> orderItemList);
    OrderSummary save(OrderSummary orderSummary);
}
