package com.example.shoplaptop.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Users user; //role Customer

    private LocalDateTime orderDateTime;

    private BigDecimal orderAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar(20)")
    private OrderStatus orderStatus;

    private boolean isPaid;

    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItemList;

    public Order() {}

    public Order(Integer id, Users user, LocalDateTime orderDateTime, BigDecimal orderAmount, OrderStatus orderStatus, boolean isPaid, List<OrderItem> orderItems) {
        this.id = id;
        this.user = user;
        this.orderDateTime = orderDateTime;
        this.orderAmount = orderAmount;
        this.orderStatus = orderStatus;
        this.isPaid = isPaid;
        this.orderItemList = orderItems;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public LocalDateTime getOrderDateTime() {
        return orderDateTime;
    }

    public void setOrderDateTime(LocalDateTime orderDateTime) {
        this.orderDateTime = orderDateTime;
    }

    public BigDecimal getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    public List<OrderItem> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<OrderItem> orderItems) {
        this.orderItemList = orderItems;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", user=" + user +
                ", orderDateTime=" + orderDateTime +
                ", orderAmount=" + orderAmount +
                ", orderStatus=" + orderStatus +
                ", isPaid=" + isPaid +
                ", orderItems=" + orderItemList +
                '}';
    }
}
