package com.krishna.ecommerce.security;

import com.krishna.ecommerce.model.User;
import com.krishna.ecommerce.repository.UserRepository;
import com.krishna.ecommerce.service.JwtService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            UserRepository userRepository) {

        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            jakarta.servlet.http.HttpServletResponse response,
            jakarta.servlet.FilterChain filterChain)
            throws jakarta.servlet.ServletException, java.io.IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = authHeader.substring(7);

            String email = jwtService.extractEmail(token);

            User user = userRepository.findByEmail(email)
                    .orElse(null);

            if (user != null && jwtService.isTokenValid(token, user)) {

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                user,
                                null,
                                Collections.singletonList(
                                        new SimpleGrantedAuthority(
                                                "ROLE_" + user.getRole().name()
                                        )
                                )
                        );

                SecurityContextHolder.getContext()
                        .setAuthentication(authentication);
            }

        } catch (Exception e) {
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}