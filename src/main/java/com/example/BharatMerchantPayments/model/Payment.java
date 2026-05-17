package com.example.BharatMerchantPayments.model;

import com.example.BharatMerchantPayments.dto.PaymentRequest;
import com.example.BharatMerchantPayments.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Payments")
public class Payment {

    private String userId;
    private UUID paymentId;
    private Double amount;
    private String currency;
    private String paymentMethod;
    private PaymentStatus status;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
