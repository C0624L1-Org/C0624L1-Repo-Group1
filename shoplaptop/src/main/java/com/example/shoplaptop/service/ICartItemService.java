package com.example.shoplaptop.service;

import com.example.shoplaptop.model.CartItem;
import com.example.shoplaptop.model.Product;
import com.example.shoplaptop.model.Users;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ICartItemService {
    List<CartItem> getCartItemsByUser(Users user);
    boolean checkProductAndUserInCartItem(Users user, Product product);
    CartItem selectCartItemOfUser(Users user);
    void addProductToCartItemOfUser(Users user, Product product);
    void removeProductFromCartItemOfUser(Users user, Product product);
    int countProductInCartItemOfUser(Users user);
    long totalPriceInCartItemOfUser(Users user);

    void deleteByUser(Users user);
}
