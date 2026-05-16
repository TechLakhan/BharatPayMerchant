package com.example.BharatMerchantPayments.model;

import com.example.BharatMerchantPayments.enums.UserCreationStatus;
import com.example.BharatMerchantPayments.enums.UserLoginStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private UUID userId;
    private String username;
    private String email;
    private String phoneNo;
    private String password;
    private UserCreationStatus status;
    private UserLoginStatus isLogonSuccessfully;
}
