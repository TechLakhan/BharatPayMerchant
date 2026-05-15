package com.example.BharatMerchantPayments.model;

import com.example.BharatMerchantPayments.dto.PaymentRequest;
import com.example.BharatMerchantPayments.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Payment {

    private String userId;
    private UUID paymentId;
    private Double amount;
    private String currency;
    private String paymentMethod;
    private PaymentStatus status;

}
