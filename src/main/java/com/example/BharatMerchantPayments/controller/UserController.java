package com.example.BharatMerchantPayments.controller;

import com.example.BharatMerchantPayments.dto.LoginRequest;
import com.example.BharatMerchantPayments.dto.LoginResponse;
import com.example.BharatMerchantPayments.dto.UserRequest;
import com.example.BharatMerchantPayments.dto.UserResponse;
import com.example.BharatMerchantPayments.enums.UserCreationStatus;
import com.example.BharatMerchantPayments.exception.DataAccessException;
import com.example.BharatMerchantPayments.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/auth/reg")
    public UserResponse registerUser(@RequestBody final UserRequest request) {
        try {
            return userService.createUser(request);
        }
        catch (RuntimeException e) {
            throw new RuntimeException(HttpStatus.BAD_REQUEST.getReasonPhrase());
        }
    }


    @PostMapping(value = "/auth/login")
    public ResponseEntity<LoginResponse> userLogin(@RequestBody final LoginRequest request) {
        try {
            return new ResponseEntity<>(userService.loginUserAccount(request), HttpStatus.OK);
        }
        catch (Exception e) {
            throw new RuntimeException("Error in user login.");
        }
    }

    @PostMapping(value = "/auth/logoff")
    public String userLogoff(@RequestHeader ("authorisation") final String authHeader) {
        try {
            return userService.logoutUser(authHeader);
        } catch (Exception e) {
            throw new RuntimeException("Failure in user logoff API");
        }
    }

    @GetMapping
    public ResponseEntity<Map<String, UserCreationStatus>> allUsers() {
        return ResponseEntity.ok(userService.getAllUser());
    }
}
