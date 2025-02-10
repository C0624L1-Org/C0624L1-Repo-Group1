package com.example.shoplaptop.service;

import com.example.shoplaptop.model.CartItem;
import com.example.shoplaptop.model.Product;
import com.example.shoplaptop.model.Users;

import java.util.List;

public interface ICartItemService {
    List<CartItem> getCartItemsByUser(Users user);
    Product selectProductInCartItemOfUser(Users user, Product product);
    void addProductToCartItemOfUser(Users user, Product product);
    Long totalPriceOfCartItemOfUser(Users user);
    int countProductInCartItemOfUser(Users user);
}
