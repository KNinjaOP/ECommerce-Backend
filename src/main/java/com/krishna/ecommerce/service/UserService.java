package com.krishna.ecommerce.service;

import com.krishna.ecommerce.dto.ChangePasswordRequest;
import com.krishna.ecommerce.dto.UserRequest;
import com.krishna.ecommerce.dto.UserResponse;
import com.krishna.ecommerce.dto.UserUpdateRequest;
import com.krishna.ecommerce.exception.DuplicateResourceException;
import com.krishna.ecommerce.exception.ResourceNotFoundException;
import com.krishna.ecommerce.model.Role;
import com.krishna.ecommerce.model.User;
import com.krishna.ecommerce.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse createUser(UserRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException(
                    "User already exists with email: " + request.getEmail()
            );
        }

        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );
        user.setRole(Role.CUSTOMER);

        userRepository.save(user);

        UserResponse response = new UserResponse();

        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());

        return response;
    }

    public UserResponse getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with id: " + id)
                );

        UserResponse response = new UserResponse();

        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());

        return response;
    }

    public List<UserResponse> getAllUsers() {

        List<User> users = userRepository.findAll();

        return users.stream()
                .map(user -> {
                    UserResponse response = new UserResponse();

                    response.setId(user.getId());
                    response.setName(user.getName());
                    response.setEmail(user.getEmail());
                    response.setRole(user.getRole());

                    return response;
                })
                .toList();
    }

    public UserResponse updateUser(Long id, UserUpdateRequest request) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: " + id
                        )
                );

        if (userRepository.existsByEmailAndIdNot(request.getEmail(), id)) {
            throw new DuplicateResourceException(
                    "User already exists with email: " + request.getEmail()
            );
        }

        user.setName(request.getName());
        user.setEmail(request.getEmail());

        userRepository.save(user);

        UserResponse response = new UserResponse();

        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());

        return response;

    }

    public void deleteUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: " + id
                        )
                );

        userRepository.delete(user);
    }

    public void changePassword(
            Long userId,
            ChangePasswordRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: " + userId
                        ));

        if (!passwordEncoder.matches(
                request.getCurrentPassword(),
                user.getPassword())) {

            throw new IllegalArgumentException(
                    "Current password is incorrect"
            );
        }

        user.setPassword(
                passwordEncoder.encode(request.getNewPassword())
        );

        userRepository.save(user);
    }

}
