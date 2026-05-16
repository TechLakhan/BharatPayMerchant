package com.example.BharatMerchantPayments.dto;

import com.example.BharatMerchantPayments.enums.UserLoginStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {

    private String username;
    private String sessionToken;
    private UserLoginStatus IsLogonSuccessful;
}
