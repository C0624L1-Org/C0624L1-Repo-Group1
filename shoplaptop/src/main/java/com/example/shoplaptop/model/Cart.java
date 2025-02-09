//package com.example.shoplaptop.model;
//
//import jakarta.persistence.*;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Entity
//@Table(name = "cart")
//public class Cart {
//    @Id
//    @GeneratedValue(strategy= GenerationType.IDENTITY)
//    private Long id;
//
//    @OneToOne
//    @JoinColumn(name="user_id_cart")
//    private Users user;
//
//    @OneToMany(mappedBy="cart",cascade=CascadeType.ALL)
//    private List<CartItem> cartItems;
//
//    public Cart() {
//    }
//
//    public Cart(Long id, Users user, List<CartItem> cartItems) {
//        this.id = id;
//        this.user = user;
//        this.cartItems = cartItems;
//    }
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public Users getUser() {
//        return user;
//    }
//
//    public void setUser(Users user) {
//        this.user = user;
//    }
//
//    public List<CartItem> getCartItems() {
//        return cartItems;
//    }
//
//    public void setCartItems(List<CartItem> cartItems) {
//        this.cartItems = cartItems;
//    }
//}
