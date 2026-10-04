package com.krishna.ecommerce.security;

import com.krishna.ecommerce.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.LocalDateTime;

public class CustomAuthenticationEntryPoint
        implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException)
            throws IOException {

        ErrorResponse errorResponse = new ErrorResponse(
                "Authentication required",
                HttpServletResponse.SC_UNAUTHORIZED,
                LocalDateTime.now()
        );

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        objectMapper.writeValue(
                response.getOutputStream(),
                errorResponse
        );
    }
}