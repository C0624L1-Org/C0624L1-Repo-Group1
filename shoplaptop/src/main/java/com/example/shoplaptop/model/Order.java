package com.example.shoplaptop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import org.apache.catalina.User;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id")
    private Users user;

    @Column(name = "order_date")
    private LocalDateTime orderDate;

    @OneToMany(mappedBy = "order",cascade= CascadeType.ALL,fetch= FetchType.LAZY)
    private List<OrderItem> orderItems;

    @Min(1)
    private long totalPayment;

    public Order(long id, Users user, LocalDateTime orderDate, List<OrderItem> orderItems) {
        this.id = id;
        this.user = user;
        this.orderDate = orderDate;
        this.orderItems = orderItems;
    }

    public Order() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
}
