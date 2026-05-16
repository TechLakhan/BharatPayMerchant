package com.example.BharatMerchantPayments.service;

import com.example.BharatMerchantPayments.dto.LoginRequest;
import com.example.BharatMerchantPayments.dto.LoginResponse;
import com.example.BharatMerchantPayments.dto.UserRequest;
import com.example.BharatMerchantPayments.dto.UserResponse;
import com.example.BharatMerchantPayments.enums.UserCreationStatus;
import com.example.BharatMerchantPayments.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.example.BharatMerchantPayments.enums.UserLoginStatus.SUCCESS;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    Map<String, User> users = new HashMap<>();
    Map<UUID, UserResponse> userResponse = new HashMap<>();

    public UserService(PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public UserResponse createUser(UserRequest request) {
        validateUserCreationRequest(request);
        User newUser = new User();
        newUser.setUserId(UUID.randomUUID());
        newUser.setUsername(request.getUsername());
        newUser.setEmail(request.getEmail());
        String rawPassword = request.getPassword();
        newUser.setPassword(encodePassword(rawPassword));
        newUser.setStatus(UserCreationStatus.SUCCESS);
        users.put(newUser.getUsername(), newUser);
        return new UserResponse(newUser.getUsername(), newUser.getStatus());
    }

    public String encodePassword(final String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }



    public void validateUserCreationRequest(UserRequest request) {
        if (users.containsKey(request.getUsername())) {
            throw new RuntimeException("User already registered");
        }
    }

    public LoginResponse loginUserAccount(LoginRequest request) {
        validateLogonRequest(request);
        User existingUser = users.get(request.getUsername());
        boolean isValidPassword = passwordEncoder.matches
                (request.getPassword(), existingUser.getPassword());
        if (!isValidPassword) {
            throw new RuntimeException("invalid credentials.");
        }
        existingUser.setIsLogonSuccessfully(SUCCESS);
        String token = jwtService.jwtGenerator(existingUser.getUserId());
        String sessionToken = "bpm_*" + token;
        return new LoginResponse(existingUser.getUsername(), sessionToken, existingUser.getIsLogonSuccessfully());
    }

    private ResponseEntity.BodyBuilder validateLogonRequest(LoginRequest request) {
        if (!users.containsKey(request.getUsername())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND);
        }
        return null;
    }
}
