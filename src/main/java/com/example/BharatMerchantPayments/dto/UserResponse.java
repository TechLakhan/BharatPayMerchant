package com.example.BharatMerchantPayments.dto;

import com.example.BharatMerchantPayments.enums.UserCreationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private String username;
    private UserCreationStatus userCreationStatus;
}
