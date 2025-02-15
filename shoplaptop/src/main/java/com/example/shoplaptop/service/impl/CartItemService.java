package com.example.shoplaptop.service.impl;

import com.example.shoplaptop.model.CartItem;
import com.example.shoplaptop.model.Product;
import com.example.shoplaptop.model.Users;
import com.example.shoplaptop.repository.ICartItemRepository;
import com.example.shoplaptop.service.ICartItemService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
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

    @PersistenceContext
    private EntityManager entityManager;

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
                    if (product.getStock() >0){
                        cartItem.setQuantity(cartItem.getQuantity()+1);
                        cartItemRepository.save(cartItem);
                        product.setStock(product.getStock() - 1);
                        productService.save(product);
                        break;
                    }
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
                break;
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


    @Transactional
    @Override
    public void deleteByUser(Users user) {
        try {
            List<CartItem> cartItemList = cartItemRepository.getCartItemsByUser(user);
            cartItemRepository.deleteByUserId(user.getId());

            entityManager.flush();
            entityManager.clear();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
