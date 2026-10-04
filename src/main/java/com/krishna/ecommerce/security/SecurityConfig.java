package com.krishna.ecommerce.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {
         http
                 .csrf(csrf -> csrf.disable())
                 .sessionManagement(session ->
                         session.sessionCreationPolicy(
                                 SessionCreationPolicy.STATELESS
                         )
                 )
                 .authorizeHttpRequests(auth -> auth

                         // Public
                         .requestMatchers("/auth/login").permitAll()
                         .requestMatchers(HttpMethod.POST, "/users").permitAll()

                         // Authenticated user's own profile
                         .requestMatchers("/users/me").authenticated()

                         // Admin-only: Users
                         .requestMatchers(HttpMethod.GET, "/users/{id}").hasRole("ADMIN")
                         .requestMatchers(HttpMethod.GET, "/users").hasRole("ADMIN")
                         .requestMatchers(HttpMethod.PUT, "/users/{id}").hasRole("ADMIN")
                         .requestMatchers(HttpMethod.DELETE, "/users/{id}").hasRole("ADMIN")

                         // Admin-only: Products
                         .requestMatchers(HttpMethod.POST, "/products").hasRole("ADMIN")
                         .requestMatchers(HttpMethod.PUT, "/products/**").hasRole("ADMIN")
                         .requestMatchers(HttpMethod.DELETE, "/products/**").hasRole("ADMIN")

                         // Admin-only: Categories
                         .requestMatchers(HttpMethod.POST, "/categories").hasRole("ADMIN")
                         .requestMatchers(HttpMethod.PUT, "/categories/**").hasRole("ADMIN")
                         .requestMatchers(HttpMethod.DELETE, "/categories/**").hasRole("ADMIN")

                         // Admin-only: Order/Payment processing
                         .requestMatchers(
                                 HttpMethod.PATCH,
                                 "/orders/*/status"
                         ).hasRole("ADMIN")

                         .requestMatchers(
                                 HttpMethod.PATCH,
                                 "/payments/*/process"
                         ).hasRole("ADMIN")

                         // Everything else requires login
                         .anyRequest().authenticated()
                 )
                 .exceptionHandling(exception -> exception
                         .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                         .accessDeniedHandler(new CustomAccessDeniedHandler())
                 )
                 .addFilterBefore(
                         jwtAuthenticationFilter,
                         UsernamePasswordAuthenticationFilter.class
                 );

         return http.build();
    }
}
