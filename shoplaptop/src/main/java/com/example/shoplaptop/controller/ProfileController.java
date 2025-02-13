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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
public class ProfileController {
    @Autowired
    private IUserService iUserService;

    @GetMapping("/profile/{id}")
    public String profileForm(@PathVariable Long id,
                              RedirectAttributes redirectAttributes,
                              Principal principal,
                              Model model) {
        String loggedInUsername = principal.getName();
        Users loggedInUser = iUserService.findByUsername(loggedInUsername);
        Users user = iUserService.getById(id);

        if (user == null) {
            redirectAttributes.addFlashAttribute("messageType", "error");
            redirectAttributes.addFlashAttribute("message", "Bạn cần phải đăng nhập!");
            return "redirect:/login";
        }

        if (!loggedInUser.getUsername().equals(user.getUsername())) {
            redirectAttributes.addFlashAttribute("messageType", "error");
            redirectAttributes.addFlashAttribute("message", "Không hợp lệ!");
            return "redirect:/home";
        }

        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);

        model.addAttribute("user", userDTO);
        return "profile";
    }

    @PostMapping("/profile")
    public String profileUser(@Valid @ModelAttribute("user") UserDTO userDTO,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes,
                              Principal principal,
                              Model model) {
        String loggedInUsername = principal.getName();
        Users loggedInUser = iUserService.findByUsername(loggedInUsername);
        Users userToUpdate = iUserService.getById(userDTO.getId());

        if (userToUpdate == null) {
            redirectAttributes.addFlashAttribute("messageType", "error");
            redirectAttributes.addFlashAttribute("message", "Bạn cần phải đăng nhập!");
            return "redirect:/home";
        }

        if (!userToUpdate.getEmail().equals(userDTO.getEmail()) && iUserService.existsByEmail(userDTO.getEmail())) {
            model.addAttribute("user", userDTO);
            bindingResult.rejectValue("email", "", "Email này đã tồn tại!");
            return "profile";
        }

//        if (loggedInUser.getUsername().equals(loggedInUsername)) {
//            userDTO.setRole();
//        }
        userDTO.validate(userDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", userDTO);
            model.addAttribute("roles", Role.values());
            return "profile";
        }

        userToUpdate.setEmail(userDTO.getEmail());
        userToUpdate.setPhone(userDTO.getPhone());
        userToUpdate.setFullName(userDTO.getFullName());
        userToUpdate.setGender(userDTO.getGender());
        userToUpdate.setAddress(userDTO.getAddress());
        userToUpdate.setAvatar(userDTO.getAvatar());

        iUserService.save(userToUpdate);

        redirectAttributes.addFlashAttribute("messageType", "success");
        redirectAttributes.addFlashAttribute("message", "Thay đổi thông tin thành công!");
        return "redirect:/home";
    }
}
