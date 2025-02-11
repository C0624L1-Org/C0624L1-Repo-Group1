package com.example.shoplaptop.controller;

import com.example.shoplaptop.model.Role;
import com.example.shoplaptop.model.Users;
import com.example.shoplaptop.model.dto.UserDTO;
import com.example.shoplaptop.service.IUserService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/register")
public class RegisterController {
    @Autowired
    private IUserService iUserService;

    @GetMapping("")
    public String registerForm(Model model) {
        model.addAttribute("users", new Users());
        return "register";
    }

    @PostMapping("")
    public String registerUser(@Valid @ModelAttribute("users") UserDTO userDTO,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes,
                               Model model) {

        if (iUserService.existsByUsername(userDTO.getUsername())) {
            bindingResult.rejectValue("username", "","Tài khoản đã tồn tại!");
        }

        if (iUserService.existsByEmail(userDTO.getEmail())) {
            bindingResult.rejectValue("email", "", "Email đã tồn tại!");
        }

        new UserDTO().validate(userDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            return "register";
        }

        Users newUser = new Users();
        BeanUtils.copyProperties(userDTO, newUser);
        iUserService.save(newUser);

        redirectAttributes.addFlashAttribute("messageType", "success");
        redirectAttributes.addFlashAttribute("message", "Đăng ký thành công! Hãy đăng nhập.");

        return "redirect:/login";
    }

}
