package com.example.shoplaptop.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class OrderSummary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Users user; //role Customer

    private LocalDateTime orderDateTime;

    @Column(nullable = false, columnDefinition = "decimal(12,2)")
    private BigDecimal orderAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar(20)")
    private OrderStatus orderStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar(20)")
    private PaymentType paymentType;

    private Boolean isPaid;

    @OneToMany(mappedBy = "orderSummary")
    private List<OrderItem> orderItemList;

    @Column(nullable = false, columnDefinition = "varchar(50)")
    private String fullName;

    @Column(nullable = false, columnDefinition = "varchar(100)")
    private String address;

    @Column(nullable = false, columnDefinition = "varchar(15)")
    private String phoneNumber;

    public OrderSummary() {}

    public OrderSummary(Integer id, Users user, LocalDateTime orderDateTime, BigDecimal orderAmount, OrderStatus orderStatus, PaymentType paymentType, Boolean isPaid, List<OrderItem> orderItemList, String fullName, String address, String phoneNumber) {
        this.id = id;
        this.user = user;
        this.orderDateTime = orderDateTime;
        this.orderAmount = orderAmount;
        this.orderStatus = orderStatus;
        this.paymentType = paymentType;
        this.isPaid = isPaid;
        this.orderItemList = orderItemList;
        this.fullName = fullName;
        this.address = address;
        this.phoneNumber = phoneNumber;
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

    public Boolean isPaid() {
        return isPaid;
    }

    public void setPaid(Boolean paid) {
        isPaid = paid;
    }

    public List<OrderItem> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<OrderItem> orderItems) {
        this.orderItemList = orderItems;
    }

    public Boolean getPaid() {
        return isPaid;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    @Override
    public String toString() {
        return "OrderSummary{" +
                "id=" + id +
                ", user=" + user +
                ", orderDateTime=" + orderDateTime +
                ", orderAmount=" + orderAmount +
                ", orderStatus=" + orderStatus +
                ", paymentType=" + paymentType +
                ", isPaid=" + isPaid +
                ", fullName='" + fullName + '\'' +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
