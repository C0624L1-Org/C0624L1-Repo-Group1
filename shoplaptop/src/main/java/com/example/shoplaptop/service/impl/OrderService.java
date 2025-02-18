package com.example.shoplaptop.service.impl;

import com.example.shoplaptop.model.*;
import com.example.shoplaptop.repository.IOrderItemRepository;
import com.example.shoplaptop.repository.IOrderRepository;
import com.example.shoplaptop.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
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

    @Override
    public Page<OrderSummary> findAllByUserId(Long userId, Pageable pageable) {
        return iOrderRepository.findAllByUserId(userId, pageable);
    }

    @Override
    public OrderSummary getById(Integer id) {
        return iOrderRepository.getById(id);
    }

    @Override
    public List<OrderItem> getOrderItemsByOrderId(Integer orderId) {
        return iOrderItemRepository.findAllByOrderSummaryId(orderId);
    }

    @Override
    public long countOrder() {
        return iOrderRepository.count();
    }


    @Override
    public Page<OrderSummary> findAllByOrderStatus(OrderStatus status, Pageable pageable) {
        return iOrderRepository.findAllByOrderStatus(status, pageable);
    }

    @Override
    public Page<OrderSummary> findAllByOrderStatus(Long userId, OrderStatus status, Pageable pageable) {
        return iOrderRepository.findAllByOrderStatus(userId, status, pageable);
    }
}
