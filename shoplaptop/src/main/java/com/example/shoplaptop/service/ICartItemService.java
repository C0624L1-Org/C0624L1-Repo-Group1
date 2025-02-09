package com.example.shoplaptop.service;

import com.example.shoplaptop.model.CartItem;
import com.example.shoplaptop.model.Product;
import com.example.shoplaptop.model.Users;

public interface ICartItemService {
    boolean checkProductInCartItemOfUser(Users user, Product product);
    CartItem selectProductInCartItemOfUser(Users user, Product product);
    void addProductToCartOfUser(Users user, Product product);
    int countProductInCartOfUser(Users user, Product product);
    double totalPriceOfProductAllInCartOfUser(Users user, Product product);
}
