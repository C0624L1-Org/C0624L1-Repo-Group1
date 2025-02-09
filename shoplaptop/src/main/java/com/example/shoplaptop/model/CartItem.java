package com.example.shoplaptop.model;

import jakarta.persistence.*;
import jakarta.persistence.criteria.Fetch;
import jakarta.validation.constraints.Min;

import java.util.List;

@Entity
@Table(name = "cart_item")
public class CartItem {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="user_id")
    private Users user ;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Min(1)
    private int quantity;



    public CartItem() {
    }

    public CartItem(Long id, Users user, Product product, int quantity) {
        this.id = id;
        this.user = user;
        this.product = product;
        this.quantity = quantity;
    }

    public CartItem(Users user, Product product, int quantity) {
        this.user = user;
        this.product = product;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Users getUser() {
        return user;
    }
    public void setUser(Users user) {
        this.user = user;
    }
    public Product getProduct() {
        return product;
    }
    public void setProduct(Product product) {
        this.product = product;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


    public double totalPrice() {
        return this.quantity*this.product.getPrice();
    }

    // check san pham co trong gio hang hay chua
    public boolean checkProductInCartItemOfUser(Users user, Product product) {

        List<CartItem> cartItems = user.getCartItems();

        for (CartItem cartItem : cartItems) {
            if (cartItem.getProduct().equals(product)) {
                return true;
            }
        }
        return false;
    }

    //chon san pham trong gio hang
    public CartItem selectProductInCartItemOfUser(Users user, Product product) {
        CartItem selectCartItem = null;
        if (checkProductInCartItemOfUser(user, product)) {
            List<CartItem> cartItems = user.getCartItems();
            for (CartItem cartItem : cartItems) {
                if(cartItem.getProduct().equals(product)) {
                    selectCartItem = cartItem;
                }
            }
        }
        return selectCartItem;
    }

    // them san pham vao gio hang khi chua co va da co trong gio hang
    public CartItem addProductInCartItemOfUser(Users user, Product product) {
        if (!checkProductInCartItemOfUser(user, product)) {
            CartItem cartItem = new CartItem(user, product, 1);
            return cartItem;
        }
        else{
            CartItem cartItem = selectProductInCartItemOfUser(user, product);
            cartItem.setQuantity(cartItem.getQuantity()+1);
            return cartItem;
        }
    }

    //dem mot nguoi mua bao nhieu san pham trong gio hang
    public int countProductInCartItemOfUser(Users user, Product product) {
        List<CartItem> cartItems = user.getCartItems();
        return cartItems.size();
    }

    // dem tong so tien
    public double totalPriceOfProductInCartItemOfUser(Users user, Product product) {
        List<CartItem> cartItems = user.getCartItems();

        double totalPrice = 0;
        for (CartItem cartItem : cartItems) {
            totalPrice+=cartItem.getQuantity()*cartItem.getProduct().getPrice();
        }
        return totalPrice;
    }
}
