package com.example.shoplaptop.service.impl;

import com.example.shoplaptop.common.UserDisabledException;
import com.example.shoplaptop.model.Users;
import com.example.shoplaptop.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private IUserRepository iUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = iUserRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Sai tài khoản hoặc mật khẩu");
        }

        if (user.getStatus() == null || !user.isStatus()) {
            throw new UserDisabledException("Tài khoản của bạn đã bị vô hiệu hóa!");
        }
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().name());

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
    }
}
