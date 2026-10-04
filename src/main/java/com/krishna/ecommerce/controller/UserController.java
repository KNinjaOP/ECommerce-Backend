package com.krishna.ecommerce.controller;

import com.krishna.ecommerce.dto.ChangePasswordRequest;
import com.krishna.ecommerce.dto.UserRequest;
import com.krishna.ecommerce.dto.UserResponse;
import com.krishna.ecommerce.dto.UserUpdateRequest;
import com.krishna.ecommerce.security.SecurityUtils;
import com.krishna.ecommerce.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public UserResponse createUser(@Valid @RequestBody UserRequest request) {
        return userService.createUser(request);
    }

    @GetMapping("/me")
    public UserResponse getMyProfile() {

        return userService.getUserById(
                SecurityUtils.getCurrentUser().getId()
        );
    }

    @GetMapping("/{id}")
    public UserResponse getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @GetMapping
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    @PutMapping("/me")
    public UserResponse updateMyProfile(
            @Valid @RequestBody UserUpdateRequest request) {

        return userService.updateUser(
                SecurityUtils.getCurrentUser().getId(),
                request
        );
    }

    @PutMapping("/{id}")
    public UserResponse updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateRequest request) {

        return userService.updateUser(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @PutMapping("/me/password")
    public void changePassword(
            @Valid @RequestBody ChangePasswordRequest request) {

        userService.changePassword(
                SecurityUtils.getCurrentUser().getId(),
                request
        );
    }

}
