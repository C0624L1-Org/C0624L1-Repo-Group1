package com.example.shoplaptop.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    private OrderSummary orderSummary;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false, columnDefinition = "decimal(10,2)")
    private BigDecimal unitPrice;

    @Column(nullable = false, columnDefinition = "decimal(12,2)")
    private BigDecimal totalPrice;

    public OrderItem() {}

    public OrderItem(Integer id, Product product, OrderSummary orderSummary, Integer quantity, BigDecimal unitPrice, BigDecimal totalPrice) {
        this.id = id;
        this.product = product;
        this.orderSummary = orderSummary;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public OrderSummary getOrder() {
        return orderSummary;
    }

    public void setOrder(OrderSummary orderSummary) {
        this.orderSummary = orderSummary;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "id=" + id +
                ", product=" + product +
                ", order=" + orderSummary +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                ", totalPrice=" + totalPrice +
                '}';
    }
}
