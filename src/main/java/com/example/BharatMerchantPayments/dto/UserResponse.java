package com.example.BharatMerchantPayments.dto;

import com.example.BharatMerchantPayments.enums.UserCreationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    private UUID userId;
    private String username;
    private UserCreationStatus status;

    public UserResponse(String username, UserCreationStatus status) {
        this.username = username;
        this.status = status;
    }
}
