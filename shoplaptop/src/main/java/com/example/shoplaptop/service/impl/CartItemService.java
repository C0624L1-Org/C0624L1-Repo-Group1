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

    @Autowired
    private ProductService productService;

    @Override
    public List<CartItem> getCartItemsByUser(Users user) {
        return cartItemRepository.getCartItemsByUser(user);
    }

    @Override
    public boolean checkProductAndUserInCartItem(Users user, Product product) {
        return cartItemRepository.existsByUserAndProduct(user,product);
    }

    @Override
    public CartItem selectCartItemOfUser(Users user) {
        CartItem cartItem = null;
        return cartItem;
    }

    @Override
    public void addProductToCartItemOfUser(Users user, Product product) {
        if(!checkProductAndUserInCartItem(user,product)) {
            CartItem cartItem = new CartItem(user,product,1);
            cartItemRepository.save(cartItem);
            product.setStock(product.getStock()-1);
            productService.save(product);
        }
        else{
            List<CartItem> cartItemList = cartItemRepository.getCartItemsByUser(user);
            for(CartItem cartItem : cartItemList){
                if(cartItem.getProduct().getId() == product.getId()){
                    if(cartItem.getQuantity() < product.getStock()){
                        cartItem.setQuantity(cartItem.getQuantity()+1);
                        cartItemRepository.save(cartItem);
                        break;
                    }
                }
            }

            for (CartItem cartItem : cartItemList) {
                if(cartItem.getProduct().getId() == product.getId()){
                    product.setStock(product.getStock() - 1);
                    productService.save(product);
                    break;
                }
            }
        }
    }

    @Override
    public void removeProductFromCartItemOfUser(Users user, Product product) {
        List<CartItem> cartItemList = cartItemRepository.getCartItemsByUser(user);
        for(CartItem cartItem : cartItemList){
            if(cartItem.getProduct().getId() == product.getId()){
                if(cartItem.getQuantity() == 1){
                    cartItemList.remove(cartItem);
                    cartItemRepository.delete(cartItem);
                    product.setStock(product.getStock()+1);
                    productService.save(product);
                    break;
                }
                cartItem.setQuantity(cartItem.getQuantity() - 1);
                product.setStock(product.getStock()+1);
                productService.save(product);
                cartItemRepository.save(cartItem);
            }
        }
    }

    @Override
    public int countProductInCartItemOfUser(Users user) {
        List<CartItem> cartItemList = cartItemRepository.getCartItemsByUser(user);
        return cartItemList.size();
    }

    @Override
    public long totalPriceInCartItemOfUser(Users user) {
        long totalPrice = 0;
        List<CartItem> cartItemList = cartItemRepository.getCartItemsByUser(user);
        for(CartItem cartItem : cartItemList){
            totalPrice += cartItem.getProduct().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())).doubleValue();
        }
        return totalPrice;
    }

}
