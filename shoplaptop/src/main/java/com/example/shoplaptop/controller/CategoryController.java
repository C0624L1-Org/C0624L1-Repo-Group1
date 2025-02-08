package com.example.shoplaptop.controller;


import com.example.shoplaptop.model.Category;
import com.example.shoplaptop.repository.ICategoryRepository;
import com.example.shoplaptop.service.ICategoryService;
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
@RequestMapping("/dashboard/productTypes")
public class CategoryController {
    @Autowired
    private ICategoryService iCategoryService;

    @GetMapping("")
    public String category() {
        return "dashboard/category/list";
    }

    @GetMapping("/list")
    public String showCategoryPage(@RequestParam(name = "page", defaultValue = "0", required = false) int page, Model model) {
        Pageable pageable = PageRequest.of(page,3);
        Page<Category> categories = iCategoryService.findAll(pageable);
        model.addAttribute("categories", categories);
        return "dashboard/category/list";
    }
}
