package com.example.BharatMerchantPayments.repository;

import com.example.BharatMerchantPayments.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    Payment getPaymentById(UUID);
}
