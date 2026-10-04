package com.krishna.ecommerce.security;

import tools.jackson.databind.ObjectMapper;
import com.krishna.ecommerce.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.time.LocalDateTime;

public class CustomAccessDeniedHandler
        implements AccessDeniedHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException)
            throws IOException {

        ErrorResponse errorResponse = new ErrorResponse(
                "Access denied",
                HttpServletResponse.SC_FORBIDDEN,
                LocalDateTime.now()
        );

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");

        objectMapper.writeValue(
                response.getOutputStream(),
                errorResponse
        );
    }
}