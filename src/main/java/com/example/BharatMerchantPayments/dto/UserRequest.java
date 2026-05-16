package com.example.BharatMerchantPayments.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class UserRequest {
    private String username;
    private String email;
    private String phoneNo;
    private String password;
}
