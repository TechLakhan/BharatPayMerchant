package com.example.BharatMerchantPayments.service;

import com.example.BharatMerchantPayments.dto.LoginRequest;
import com.example.BharatMerchantPayments.dto.LoginResponse;
import com.example.BharatMerchantPayments.dto.UserRequest;
import com.example.BharatMerchantPayments.dto.UserResponse;
import com.example.BharatMerchantPayments.enums.UserCreationStatus;
import com.example.BharatMerchantPayments.model.User;
import com.example.BharatMerchantPayments.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.example.BharatMerchantPayments.enums.UserLoginStatus.LOGGED_OUT;
import static com.example.BharatMerchantPayments.enums.UserLoginStatus.SUCCESS;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    Map<String, User> users = new HashMap<>();
    Map<UUID, UserResponse> userResponse = new HashMap<>();

    public UserService(PasswordEncoder passwordEncoder, JwtService jwtService, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    public UserResponse createUser(UserRequest request) {
        boolean userExists = userRepository.existsByUsername(request.getUsername());
        if (!userExists) {
            User newUser = new User();
            newUser.setUserId(UUID.randomUUID());
            newUser.setUsername(request.getUsername());
            newUser.setEmail(request.getEmail());
            newUser.setPhoneNo(request.getPhoneNo());
            String rawPassword = request.getPassword();
            newUser.setPassword(encodePassword(rawPassword));
            newUser.setStatus(UserCreationStatus.SUCCESS);
            userRepository.save(newUser);
            return new UserResponse(newUser.getUsername(), newUser.getStatus());
        }
        else {
            throw new RuntimeException("User not found");
        }
    }

    public String encodePassword(final String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    public LoginResponse loginUserAccount(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername());
        boolean isValidPassword = passwordEncoder.matches
                (request.getPassword(), user.getPassword());
        if (!isValidPassword) {
            throw new RuntimeException("invalid credentials.");
        }
        user.setLogonStatus(SUCCESS);
        String token = jwtService.jwtGenerator(user.getUserId());
        String sessionToken = "bpm_*" + token;
        user.setActiveSession(token);
        userRepository.save(user);
        return new LoginResponse(user.getUsername(), sessionToken, user.getLogonStatus());
    }

    public String logoutUser(String authHeader) {
        String token = authHeader.replace("Bearer ", "")
                .replace("bpm_*", "");

        String userId = jwtService.extractUserId(token);
        User user = userRepository.findByUserId(UUID.fromString(userId));

        user.setLogonStatus(LOGGED_OUT);
        user.setActiveSession(null);
        userRepository.save(user);
        return "User Logged out successfully.";
    }
}
