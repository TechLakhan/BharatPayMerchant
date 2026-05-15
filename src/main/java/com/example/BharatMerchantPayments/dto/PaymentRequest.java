package com.example.BharatMerchantPayments.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {
/*
    {
        "userId": "U123",
            "amount": 5000,
            "currency": "INR",
            "paymentMethod": "UPI"
    }

 */
    private String userId;
    private double amount;
    private String currency;
    private String paymentMethod;
}
