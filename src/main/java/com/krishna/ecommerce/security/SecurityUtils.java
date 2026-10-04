package com.krishna.ecommerce.security;

import com.krishna.ecommerce.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    public static User getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        if (authentication == null ||
            !authentication.isAuthenticated() ||
            !(authentication.getPrincipal() instanceof User)) {

            return null;
        }

        return (User) authentication.getPrincipal();
    }
}