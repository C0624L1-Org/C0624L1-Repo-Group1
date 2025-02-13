package com.example.shoplaptop.service.impl;

import com.example.shoplaptop.model.PasswordResetToken;
import com.example.shoplaptop.model.Users;
import com.example.shoplaptop.repository.IPasswordResetTokenRepository;
import com.example.shoplaptop.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
public class PasswordResetService {
    private static final int EXPIRATION_TIME = 30;

    @Autowired
    private IPasswordResetTokenRepository iPasswordResetTokenRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private IUserService iUserService;

    public void createPasswordResetTokenForUser(Users user, String token) {
        PasswordResetToken existingToken = iPasswordResetTokenRepository.findByUsers_Id(user.getId());
        if (existingToken != null) {
            // Xoá token cũ
            iPasswordResetTokenRepository.deleteByUsers(existingToken.getUsers());
            iPasswordResetTokenRepository.flush();
        }

        // Tạo token mới
        PasswordResetToken myToken = new PasswordResetToken();
        myToken.setToken(token);
        myToken.setUsers(user);
        myToken.setExpiryDate(LocalDateTime.now().plusMinutes(EXPIRATION_TIME));
        iPasswordResetTokenRepository.save(myToken);
    }

    public String generateToken() {
        return UUID.randomUUID().toString();
    }

    public void sendPasswordResetEmail(String email, String token) {
        String resetUrl = "http://localhost:8080/reset-password?token=" + token;
        System.out.println("Gửi Email đến URL: " + resetUrl);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Password Reset");
        message.setText("Link đổi mật khẩu của bạn: " + email + "\nLink: " + resetUrl);

        javaMailSender.send(message);
    }

    public PasswordResetToken getPasswordResetToken(String token) {
        return iPasswordResetTokenRepository.findByToken(token);
    }

    public void deleteToken(Users user) {
        iPasswordResetTokenRepository.deleteByUsers(user);
    }
}