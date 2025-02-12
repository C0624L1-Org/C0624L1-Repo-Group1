package com.example.shoplaptop.repository;

import com.example.shoplaptop.model.PasswordResetToken;
import com.example.shoplaptop.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    PasswordResetToken findByToken(String token);
    PasswordResetToken findByUsers_Id(Long id);
    void deleteByUsers(Users users);
}
