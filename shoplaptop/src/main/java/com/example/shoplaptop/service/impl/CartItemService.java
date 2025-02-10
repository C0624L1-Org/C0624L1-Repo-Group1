package com.example.shoplaptop.service.impl;

import com.example.shoplaptop.model.CartItem;
import com.example.shoplaptop.model.Product;
import com.example.shoplaptop.model.Users;
import com.example.shoplaptop.repository.ICartItemRepository;
import com.example.shoplaptop.service.ICartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CartItemService implements ICartItemService {
    @Autowired
    private ICartItemRepository cartItemRepository;

    @Override
    public List<CartItem> getCartItemsByUser(Users user) {
        return cartItemRepository.getCartItemsByUser(user);
    }

    @Override
    public CartItem selectProductInCartItemOfUser(Users user, Product product) {
        CartItem findcartItem = null;
        if(cartItemRepository.existsByProductAndUser(user, product)){

            for(CartItem cartItem : cartItemRepository.getCartItemsByUser(user)){

                if(cartItem.getProduct().equals(product)){
                    findcartItem = cartItem;
                    return findcartItem;
                }
            }
        }
        return findcartItem;
    }

    @Override
    public void addProductToCartItemOfUser(Users user, Product product) {
        if(!cartItemRepository.existsByProductAndUser(user, product)){

           CartItem cartItem = new CartItem(user,product,1);
           cartItemRepository.save(cartItem);
        }
        else{
            CartItem cartItem = selectProductInCartItemOfUser(user, product);
            cartItem.setQuantity(cartItem.getQuantity()+1);
            cartItemRepository.save(cartItem);
        }

    }



    @Override
    public int countProductInCartItemOfUser(Users user) {
        return 0;
    }
}



