package com.example.BharatMerchantPayments.dto;

import com.example.BharatMerchantPayments.enums.UserLoginStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class LoginResponse {

    public LoginResponse() {

    }

    public LoginResponse(String username, String sessionToken, UserLoginStatus isLogonSuccessful) {
    }
}
