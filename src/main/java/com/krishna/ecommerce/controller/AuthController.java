package com.krishna.ecommerce.controller;

import com.krishna.ecommerce.dto.LoginRequest;
import com.krishna.ecommerce.dto.LoginResponse;
import com.krishna.ecommerce.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Authentication",
        description = "APIs for user authentication"
)
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(
            summary = "User login",
            description = "Authenticates a user using email and password and returns a JWT token."
    )
    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }
}