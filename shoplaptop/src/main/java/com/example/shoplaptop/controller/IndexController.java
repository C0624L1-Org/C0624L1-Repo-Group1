package com.example.shoplaptop.controller;

import com.example.shoplaptop.model.Product;
import com.example.shoplaptop.model.Users;
import com.example.shoplaptop.service.ICategoryService;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
public class IndexController {
    @Autowired
    private IProductService iProductService;

    @Autowired
    private ICategoryService iCategoryService;

    @Autowired
    private IUserService iUserService;

    @Autowired
    private UserService userService;

    @Autowired
    private GlobalControllerAdvice globalControllerAdvice;

    @GetMapping("/")
    public String index() {
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("products", iProductService.findAll());
        model.addAttribute("categories", iCategoryService.findAll());
        Users user = globalControllerAdvice.currentUser();
        model.addAttribute("user", user);
        return "home";
    }

    @GetMapping("/allProduct")
    public String showProductList(@RequestParam(name = "page", defaultValue = "0", required = false) int page,
                                  Model model) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<Product> products = iProductService.findAll(pageable);
        model.addAttribute("products", products);
        Users user = globalControllerAdvice.currentUser();
        model.addAttribute("user", user);
        return "allProduct";
    }

    @GetMapping("/allProduct/inform")
    public String displayRequestLogin(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("messageType", "error");
        redirectAttributes.addFlashAttribute("message", "Bạn cần phải đăng nhập!");
        return "redirect:/login";
    }

    @GetMapping("/detail-product/{id}")
    public String detailProduct(@PathVariable int id, Model model) {
        model.addAttribute("product", iProductService.getById(id));
        return "detail-product";
    }
}
