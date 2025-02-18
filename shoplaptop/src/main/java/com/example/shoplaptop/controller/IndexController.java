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
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;

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
    public String home(
            @RequestParam(value = "productName", required = false) String productName,
            @RequestParam(value = "categoryName", required = false) String categoryName,
            @RequestParam(value = "priceMin", required = false) String priceMinStr,
            @RequestParam(value = "priceMax", required = false) String priceMaxStr,
            @RequestParam(value = "sortOrder", required = false) String sortOrder,
            @RequestParam(value = "page", defaultValue = "0") int page,
            Model model) {

        // Chuyển đổi priceMin và priceMax từ String sang BigDecimal
        BigDecimal priceMin = (priceMinStr == null || priceMinStr.trim().isEmpty())
                ? null : new BigDecimal(priceMinStr);
        BigDecimal priceMax = (priceMaxStr == null || priceMaxStr.trim().isEmpty())
                ? null : new BigDecimal(priceMaxStr);

        if (priceMin != null && priceMin.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Giá tối thiểu không được nhỏ hơn 0");
        }
        if (priceMax != null && priceMax.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Giá tối đa không được nhỏ hơn 0");
        }


        //Sắp xếp
        Sort sort = Sort.unsorted();
        if ("asc".equalsIgnoreCase(sortOrder)) {
            sort = Sort.by("price").ascending();
        } else if ("desc".equalsIgnoreCase(sortOrder)) {
            sort = Sort.by("price").descending();
        }

        Pageable pageable = PageRequest.of(page, 6, sort);

        boolean isSearch = (productName != null && !productName.trim().isEmpty())
                || (categoryName != null && !categoryName.trim().isEmpty())
                || (priceMin != null)
                || (priceMax != null);

        if (isSearch) {
            Page<Product> productPage = iProductService.findByFilters(productName, categoryName, priceMin, priceMax, pageable);
            model.addAttribute("productPage", productPage);
        } else {
            Page<Product> productPage = iProductService.findAll(pageable);
            model.addAttribute("productPage", productPage);
        }

        model.addAttribute("categories", iCategoryService.findAll());
        Users user = globalControllerAdvice.currentUser();
        model.addAttribute("user", user);

        model.addAttribute("productName", productName);
        model.addAttribute("categoryName", categoryName);
        model.addAttribute("products", iProductService.findAll());
        model.addAttribute("brands", iCategoryService.findAll());

        model.addAttribute("priceMin", priceMin);
        model.addAttribute("priceMax", priceMax);
        model.addAttribute("sortOrder", sortOrder);
        model.addAttribute("currentPage", page);

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
