package com.example.shoplaptop.controller;

import com.example.shoplaptop.common.EncryptPasswordUtils;
import com.example.shoplaptop.model.Users;
import com.example.shoplaptop.model.dto.ChangePasswordDTO;
import com.example.shoplaptop.service.IUserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
public class ChangePasswordController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private JavaMailSender mailSender;

    @GetMapping("/change-password")
    public String showChangePasswordForm(Model model) {
        model.addAttribute("changePasswordDTO", new ChangePasswordDTO());
        return "change-password";
    }

    @PostMapping("/change-password")
    public String processChangePasswordForm(@Valid @ModelAttribute("changePasswordDTO") ChangePasswordDTO dto,
                                            BindingResult bindingResult,
                                            RedirectAttributes redirectAttributes,
                                            HttpSession session,
                                            Principal principal) {

        if (bindingResult.hasErrors()) {
            return "change-password";
        }

        String username = principal.getName();
        Users user = iUserService.findByUsername(username);

        if (!EncryptPasswordUtils.ParseEncrypt(user.getPassword(), dto.getOldPassword())) {
            bindingResult.rejectValue("oldPassword", "", "Mật khẩu cũ không chính xác!");
            return "change-password";
        }

        if (!dto.getNewPassword().equals(dto.getConfirmNewPassword())) {
            bindingResult.rejectValue("confirmNewPassword", "", "Xác nhận mật khẩu không khớp!");
            return "change-password";
        }

        String otp = String.valueOf((int) (Math.random() * 900000) + 100000);

        session.setAttribute("changePasswordNewPassword", dto.getNewPassword());
        session.setAttribute("changePasswordOTP", otp);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Mã xác nhận thay đổi mật khẩu");
        message.setText("Mã xác nhận của bạn là: " + otp);
        mailSender.send(message);

        redirectAttributes.addFlashAttribute("message", "Mã xác nhận đã được gửi đến email của bạn. Vui lòng kiểm tra email.");
        return "redirect:/change-password/verify";
    }

    @GetMapping("/change-password/verify")
    public String showVerifyCodeForm() {
        return "change-password-verify";
    }

    @PostMapping("/change-password/verify")
    public String processVerifyCode(@RequestParam("otp") String otpInput,
                                    HttpSession session,
                                    RedirectAttributes redirectAttributes,
                                    Principal principal) {
        String sessionOtp = (String) session.getAttribute("changePasswordOTP");
        String newPassword = (String) session.getAttribute("changePasswordNewPassword");

        if (sessionOtp == null || newPassword == null) {
            redirectAttributes.addFlashAttribute("messageType", "error");
            redirectAttributes.addFlashAttribute("message", "Phiên làm việc của bạn đã hết hạn, vui lòng thử lại!");
            return "redirect:/change-password";
        }

        if (!sessionOtp.equals(otpInput)) {
            redirectAttributes.addFlashAttribute("messageType", "error");
            redirectAttributes.addFlashAttribute("message", "Mã xác nhận không chính xác!");
            return "redirect:/change-password/verify";
        }

        String username = principal.getName();
        Users user = iUserService.findByUsername(username);
        String encodedPassword = EncryptPasswordUtils.encryptPasswordUtils(newPassword);
        user.setPassword(encodedPassword);
        iUserService.save(user);

        // Xóa dữ liệu tạm lưu trong session
        session.removeAttribute("changePasswordOTP");
        session.removeAttribute("changePasswordNewPassword");

        redirectAttributes.addFlashAttribute("messageType", "success");
        redirectAttributes.addFlashAttribute("message", "Mật khẩu đã được thay đổi thành công!");
        return "redirect:/profile/" + user.getId();
    }
}
