package com.example.shoplaptop.controller;

import com.example.shoplaptop.model.Product;
import com.example.shoplaptop.model.Users;
import com.example.shoplaptop.service.IProductService;
import com.example.shoplaptop.service.IUserService;
import com.example.shoplaptop.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
public class IndexController {
    @Autowired
    private IProductService iProductService;

    @Autowired
    private IUserService iUserService;
    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String index() {
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/allProduct")
    public String showProductList(@RequestParam(name = "page", defaultValue = "0", required = false) int page,
                                  Principal principal,
                                  Model model) {
        Pageable pageable = PageRequest.of(page,10);
        Page<Product> products =  iProductService.findAll(pageable);
        model.addAttribute("products", products);

        String username = principal.getName();
        Users user = userService.findByUsername(username);
        model.addAttribute("user", user);
        return "allProduct";
    }
}
