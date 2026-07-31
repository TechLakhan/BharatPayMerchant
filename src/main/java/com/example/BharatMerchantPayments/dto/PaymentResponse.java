package com.example.BharatMerchantPayments.dto;

import com.example.BharatMerchantPayments.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class PaymentResponse {
    private UUID userId;
    private Double amount;
    private String currency;
    private String paymentMethod;
    private UUID paymentId;
    private PaymentStatus status;

    public PaymentResponse(UUID userId, Double amount, String currency, String paymentMethod, UUID paymentId, PaymentStatus status) {
        this.userId = userId;
        this.amount = amount;
        this.currency = currency;
        this.paymentMethod = paymentMethod;
        this.paymentId = paymentId;
        this.status = status;
    }
}
