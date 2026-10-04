package com.krishna.ecommerce.controller;

import com.krishna.ecommerce.dto.LoginRequest;
import com.krishna.ecommerce.dto.LoginResponse;
import com.krishna.ecommerce.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }
}