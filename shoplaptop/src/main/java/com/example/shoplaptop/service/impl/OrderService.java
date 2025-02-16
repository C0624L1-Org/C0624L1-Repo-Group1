package com.example.shoplaptop.service.impl;

import com.example.shoplaptop.model.CartItem;
import com.example.shoplaptop.model.OrderItem;
import com.example.shoplaptop.model.OrderSummary;
import com.example.shoplaptop.model.Users;
import com.example.shoplaptop.repository.IOrderItemRepository;
import com.example.shoplaptop.repository.IOrderRepository;
import com.example.shoplaptop.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService implements IOrderService {
    @Autowired
    private IOrderRepository iOrderRepository;

    @Autowired
    IOrderItemRepository iOrderItemRepository;


    @Override
    public void saveOrderItems(OrderSummary order, Users user) {
        List<CartItem> cartItemList = user.getCartItemList();
        for (CartItem cartItem : cartItemList) {
            OrderItem orderItem = new OrderItem();
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setUnitPrice(orderItem.getProduct().getPrice());
            orderItem.setTotalPrice(orderItem.getUnitPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity().doubleValue())));
            orderItem.setOrder(order);
            iOrderItemRepository.save(orderItem);
        }
    }

    @Override
    public void saveAllOrderItemList(List<OrderItem> orderItemList) {
        try {
            iOrderItemRepository.saveAll(orderItemList);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public OrderSummary save(OrderSummary orderSummary) {
        try {
            return iOrderRepository.save(orderSummary);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Page<OrderSummary> findAll(Pageable pageable) {
        return iOrderRepository.findAll(pageable);
    }
}
