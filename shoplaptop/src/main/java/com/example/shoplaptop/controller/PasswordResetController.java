package com.example.shoplaptop.controller;

import com.example.shoplaptop.model.PasswordResetToken;
import com.example.shoplaptop.model.Role;
import com.example.shoplaptop.model.Users;
import com.example.shoplaptop.model.dto.ForgotPasswordDTO;
import com.example.shoplaptop.model.dto.ResetPasswordDTO;
import com.example.shoplaptop.model.dto.UserDTO;
import com.example.shoplaptop.service.IUserService;
import com.example.shoplaptop.service.impl.PasswordResetService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PasswordResetController {
    @Autowired
    private IUserService iUserService;

    @Autowired
    private PasswordResetService passwordResetService;

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm(Model model) {
        model.addAttribute("forgotPasswordDTO", new ForgotPasswordDTO());
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String processForgotPasswordForm(@Valid @ModelAttribute("forgotPasswordDTO") ForgotPasswordDTO forgotPasswordDTO,
                                            BindingResult bindingResult,
                                            @RequestParam("email") String email,
                                            RedirectAttributes redirectAttributes,
                                            Model model) {

        if (bindingResult.hasErrors()) {
            return "forgot-password";
        }

        Users user = iUserService.findByEmail(email);
        if (user == null) {
            redirectAttributes.addFlashAttribute("messageType", "error");
            redirectAttributes.addFlashAttribute("message", "Không tìm thấy email này!");
            return "redirect:/forgot-password";
        }

        String token = passwordResetService.generateToken();
        passwordResetService.createPasswordResetTokenForUser(user, token);
        passwordResetService.sendPasswordResetEmail(email, token);

        redirectAttributes.addFlashAttribute("messageType", "success");
        redirectAttributes.addFlashAttribute("message", "Đã gửi yêu cầu thay đổi mật khẩu qua email của bạn!");
        return "redirect:/login";
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam("token") String token,
                                        Model model,
                                        RedirectAttributes redirectAttributes) {
        PasswordResetToken passToken = passwordResetService.getPasswordResetToken(token);
        if (passToken == null || passToken.isExpired()) {
            redirectAttributes.addFlashAttribute("messageType", "error");
            redirectAttributes.addFlashAttribute("message", "Liên kết đã hết hạn!");
            return "redirect:/forgot-password";
        }

        model.addAttribute("token", token);
        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String processResetPassword(@RequestParam("token") String token,
                                       @RequestParam("password") String newPassword,
                                       RedirectAttributes redirectAttributes,
                                       Model model) {

        PasswordResetToken passToken = passwordResetService.getPasswordResetToken(token);
        if (passToken == null || passToken.isExpired()) {
            redirectAttributes.addFlashAttribute("messageType", "error");
            redirectAttributes.addFlashAttribute("message", "Liên kết đặt lại mật khẩu không hợp lệ hoặc đã hết hạn.");
            return "redirect:/forgot-password";
        }

        Users user = passToken.getUsers();
        if (user == null) {
            redirectAttributes.addFlashAttribute("messageType", "error");
            redirectAttributes.addFlashAttribute("message", "Không tìm thấy người dùng cho token này.");
            return "redirect:/forgot-password";
        }

        user.setPassword(newPassword);
        iUserService.save(user);

        // Xóa token sau khi reset mật khẩu thành công
        passwordResetService.deleteToken(user);

        redirectAttributes.addFlashAttribute("messageType", "success");
        redirectAttributes.addFlashAttribute("message", "Mật khẩu đã được thay đổi thành công! Vui lòng tiếp tục đăng nhập!");
        return "redirect:/login";
    }
}
