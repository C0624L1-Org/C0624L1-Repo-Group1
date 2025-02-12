package com.example.shoplaptop.controller;

import com.example.shoplaptop.model.CartItem;
import com.example.shoplaptop.model.Product;
import com.example.shoplaptop.model.Users;
import com.example.shoplaptop.repository.IProductRepository;
import com.example.shoplaptop.repository.IUserRepository;
import com.example.shoplaptop.service.IProductService;
import com.example.shoplaptop.service.impl.CartItemService;
import com.example.shoplaptop.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartItemController {
    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IProductRepository productRepository;
    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private IProductService iProductService;

    @RequestMapping("")
    public String index(Model model, Principal principal) {
        String username = principal.getName();
        Users user = userRepository.findByUsername(username);
        model.addAttribute("user", user);
        List<CartItem> cartItemList = cartItemService.getCartItemsByUser(user);
        int countProduct = cartItemService.countProductInCartItemOfUser(user);
        long totalPrice = cartItemService.totalPriceInCartItemOfUser(user);

        model.addAttribute("countProduct", countProduct);
        model.addAttribute("cartItem",cartItemList);
        model.addAttribute("totalPrice", totalPrice);
        return "/listCart";
    }

    @RequestMapping("/add")
    public String addProductInCart(@RequestParam(name = "page", defaultValue = "0", required = false) int page,
                                   @RequestParam(name = "userId") Long userId, Model model,
                                   @RequestParam(name = "productId") int productId) {
        if(userId == null){
            return "redirect:/login";
        }
        Users user = userRepository.getById(userId);
        Product product = productRepository.getById(productId);
        cartItemService.addProductToCartItemOfUser(user, product);

        Pageable pageable = PageRequest.of(page,10);
        Page<Product> products =  iProductService.findAll(pageable);
        model.addAttribute("products", products);
        return "redirect:/allProduct";
    }

    @RequestMapping("/addQuantity")
    public String addQuantityProduct(@RequestParam(name="userId") long userId,
                                     @RequestParam(name ="productId") int productId,
                                     Model model) {
        Users user = userRepository.getById(userId);
        Product product = productRepository.getById(productId);
        cartItemService.addProductToCartItemOfUser(user, product);
        List<CartItem> cartItemList = cartItemService.getCartItemsByUser(user);
        model.addAttribute("cartItem",cartItemList);
        return "redirect:/cart";
    }

    @RequestMapping("/reduceQuantity")
    public String reduceQuantityProduct(@RequestParam(name="userId") long userId,
                                        @RequestParam(name = "productId") int productId,
                                        Model model) {
        Users user = userRepository.getById(userId);
        Product product = productRepository.getById(productId);
        cartItemService.removeProductFromCartItemOfUser(user, product);
        return "redirect:/cart";
    }
}
