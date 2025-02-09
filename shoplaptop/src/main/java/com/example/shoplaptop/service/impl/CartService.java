package com.example.shoplaptop.service.impl;

import com.example.shoplaptop.model.CartItem;
import com.example.shoplaptop.model.Product;
import com.example.shoplaptop.model.Users;
import com.example.shoplaptop.repository.ICartItemRepository;
import com.example.shoplaptop.service.ICartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService implements ICartItemService {
    @Autowired
    private ICartItemRepository cartItemRepository;

    //kiem tra xem co ton tai trong gio hang khong
    @Override
    public boolean checkProductInCartItemOfUser(Users user, Product product) {
        return cartItemRepository.existsByUserAndProduct(user, product);
    }

    @Override
    public CartItem selectProductInCartItemOfUser(Users user, Product product) {
        CartItem findCartItem = null;
        if(cartItemRepository.existsByUserAndProduct(user, product)) {
            List<CartItem> cartItems = cartItemRepository.getCartItemsByUser(user);
            for(CartItem cartItem : cartItems) {
                if(cartItem.getProduct().equals(product)) {
                    findCartItem = cartItem;
                    return findCartItem;
                }
            }
        }
        return findCartItem;
    }

    @Override
    public void addProductToCartOfUser(Users user, Product product) {

        if(!checkProductInCartItemOfUser(user, product)) {
            CartItem cartItem = new CartItem(user,product,1);
            cartItemRepository.save(cartItem);
        }
        else{
            CartItem cartItem = selectProductInCartItemOfUser(user,product);
            cartItem.setQuantity(cartItem.getQuantity()+1);
            cartItemRepository.save(cartItem);
        }
    }

    @Override
    public int countProductInCartOfUser(Users user, Product product) {
        if(checkProductInCartItemOfUser(user, product)) {
            List<CartItem> cartItems = cartItemRepository.getCartItemsByUser(user);
            return cartItems.size();
        }
        return 0;
    }

    @Override
    public double totalPriceOfProductAllInCartOfUser(Users user, Product product) {
        double totalPrice = 0;
        if(checkProductInCartItemOfUser(user, product)) {
            List<CartItem> cartItems = cartItemRepository.getCartItemsByUser(user);
            for(CartItem cartItem : cartItems) {
                totalPrice+= cartItem.getProduct().getPrice()*cartItem.getQuantity();
            }
        }
        return totalPrice;
    }
}
