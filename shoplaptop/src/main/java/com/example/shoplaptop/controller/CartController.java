package com.example.shoplaptop.controller;

import com.example.shoplaptop.model.Product;
import com.example.shoplaptop.model.Users;
import com.example.shoplaptop.service.impl.CartItemService;
import com.example.shoplaptop.service.impl.ProductService;
import com.example.shoplaptop.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CartItemService cartItemService;

    @RequestMapping("")
    public String index(Model model) {

        return "listCart";
    }

    @RequestMapping("/add")
    public String addProductInCart(@RequestParam(name = "userId") int userId,
                                   @RequestParam(name ="productId") int productId,
                                   Model model){
        Users user = userService.getById((long) userId);
        Product product = productService.getById(productId);

        cartItemService.addProductToCartItemOfUser(user, product);

        return "redirect:/allProduct";
    }
}
