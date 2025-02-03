package com.example.shoplaptop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {
    @GetMapping
    public String dashboard() {
        return "/dashboard/dashboard";
    }

    @GetMapping("/users")
    public String users() {
        return "redirect:/dashboard/users/list";
    }
}
