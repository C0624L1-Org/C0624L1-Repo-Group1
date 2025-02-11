package com.example.shoplaptop.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class SessionController {

    @RequestMapping(value = "/clear-session", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<Void> clearSession(@RequestParam String key, HttpSession session) {
        session.removeAttribute(key);
        return ResponseEntity.ok().build();
    }
}
