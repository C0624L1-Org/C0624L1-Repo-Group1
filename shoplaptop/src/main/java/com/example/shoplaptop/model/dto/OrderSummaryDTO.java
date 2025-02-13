package com.example.shoplaptop.model.dto;

import com.example.shoplaptop.model.OrderItem;
import com.example.shoplaptop.model.OrderStatus;
import com.example.shoplaptop.model.PaymentType;
import com.example.shoplaptop.model.Users;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderSummaryDTO implements Validator {
    private Integer id;
    private Users user; //role Customer
    private LocalDateTime orderDateTime;
    private BigDecimal orderAmount;
    private OrderStatus orderStatus;
    private PaymentType paymentType;
    private Boolean isPaid;
    private List<OrderItem> orderItemList;
    private String fullName;
    private String address;
    private String phoneNumber;

    public OrderSummaryDTO() {}

    public OrderSummaryDTO(Integer id, Users user, LocalDateTime orderDateTime, BigDecimal orderAmount, OrderStatus orderStatus, PaymentType paymentType, Boolean isPaid, List<OrderItem> orderItemList, String fullName, String address, String phoneNumber) {
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
        return "OrderSummaryDTO{" +
                "id=" + id +
                ", user=" + user +
                ", orderDateTime=" + orderDateTime +
                ", orderAmount=" + orderAmount +
                ", orderStatus=" + orderStatus +
                ", paymentType=" + paymentType +
                ", isPaid=" + isPaid +
                ", orderItemList=" + orderItemList +
                ", fullName='" + fullName + '\'' +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object target, Errors errors) {
        OrderSummaryDTO orderSummaryDTO = (OrderSummaryDTO) target;

        String fullName = orderSummaryDTO.getFullName();
        if(fullName.trim().isEmpty()) {
            errors.rejectValue("fullName", "input.null");
        } else if (fullName.length() > 50) {
            errors.rejectValue("fullName", "", "Tên người nhận hàng phải dưới 50 ký tự !");
        }

        String phoneNumber = orderSummaryDTO.getPhoneNumber();
        if (phoneNumber.trim().isEmpty()) {
            errors.rejectValue("phoneNumber", "input.null", "Vui lòng nhập số điện thoại!");
        } else if (!phoneNumber.matches("^\\d{2,15}$")) {
            errors.rejectValue("phoneNumber", "", "Số điện thoại phải là chữ số và chỉ chứa tối đa 15 số!");
        }

        String address = orderSummaryDTO.getAddress();
        if(address.trim().isEmpty()) {
            errors.rejectValue("address", "input.null");
        } else if (address.length() > 100) {
            errors.rejectValue("address", "", "Địa chỉ nhận hàng phải dưới 100 ký tự !");
        }

        PaymentType paymentType = orderSummaryDTO.getPaymentType();
        if(paymentType == null) {
            errors.rejectValue("paymentType", "", "Vui lòng chọn phương thức thanh toán !");
        }
    }
}
