package com.example.shoplaptop.controller;

import com.example.shoplaptop.model.Product;
import com.example.shoplaptop.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/dashboard/products")
public class ProductController {
    @Autowired
    private IProductService iProductService;

    @GetMapping("/list")
    public String showProductList(@RequestParam(name = "page", defaultValue = "0", required = false) int page, Model model) {
        Pageable pageable = PageRequest.of(page,3);
        Page<Product> products =  iProductService.findAll(pageable);
        model.addAttribute("products", products);
        return "dashboard/products/list";
    }

}
