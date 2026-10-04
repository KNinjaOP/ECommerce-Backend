package com.krishna.ecommerce.controller;

import com.krishna.ecommerce.dto.ChangePasswordRequest;
import com.krishna.ecommerce.dto.UserRequest;
import com.krishna.ecommerce.dto.UserResponse;
import com.krishna.ecommerce.dto.UserUpdateRequest;
import com.krishna.ecommerce.security.SecurityUtils;
import com.krishna.ecommerce.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Users",
        description = "APIs for user registration, profile management, and administration"
)
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
            summary = "Register a new user",
            description = "Creates a new customer account."
    )
    @PostMapping
    public UserResponse createUser(
            @Valid @RequestBody UserRequest request) {
        return userService.createUser(request);
    }

    @Operation(
            summary = "Get my profile",
            description = "Returns the profile of the currently authenticated user."
    )
    @GetMapping("/me")
    public UserResponse getMyProfile() {

        return userService.getUserById(
                SecurityUtils.getCurrentUser().getId()
        );
    }

    @Operation(
            summary = "Get user by ID",
            description = "Returns a user's details by ID. This endpoint is restricted to administrators."
    )
    @GetMapping("/{id}")
    public UserResponse getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @Operation(
            summary = "Get all users",
            description = "Returns a list of all registered users. This endpoint is restricted to administrators."
    )
    @GetMapping
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    @Operation(
            summary = "Update my profile",
            description = "Updates the profile of the currently authenticated user."
    )
    @PutMapping("/me")
    public UserResponse updateMyProfile(
            @Valid @RequestBody UserUpdateRequest request) {

        return userService.updateUser(
                SecurityUtils.getCurrentUser().getId(),
                request
        );
    }

    @Operation(
            summary = "Update user",
            description = "Updates a user's details by ID. This endpoint is restricted to administrators."
    )
    @PutMapping("/{id}")
    public UserResponse updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateRequest request) {

        return userService.updateUser(id, request);
    }

    @Operation(
            summary = "Delete user",
            description = "Deletes a user by ID. This endpoint is restricted to administrators."
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @Operation(
            summary = "Change password",
            description = "Changes the password of the currently authenticated user after verifying the current password."
    )
    @PutMapping("/me/password")
    public void changePassword(
            @Valid @RequestBody ChangePasswordRequest request) {

        userService.changePassword(
                SecurityUtils.getCurrentUser().getId(),
                request
        );
    }
}