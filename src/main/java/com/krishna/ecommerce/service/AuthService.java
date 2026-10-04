package com.krishna.ecommerce.service;

import com.krishna.ecommerce.dto.LoginRequest;
import com.krishna.ecommerce.dto.LoginResponse;
import com.krishna.ecommerce.exception.InvalidCredentialsException;
import com.krishna.ecommerce.exception.ResourceNotFoundException;
import com.krishna.ecommerce.model.User;
import com.krishna.ecommerce.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with email: " + request.getEmail()
                        ));

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword())) {

            throw new InvalidCredentialsException(
                    "Invalid email or password"
            );
        }

        String token = jwtService.generateToken(user);

        return new LoginResponse(token);
    }
}
