package com.example.BharatMerchantPayments.repository;

import com.example.BharatMerchantPayments.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    Payment getPaymentByPaymentId(UUID paymentId);
    List<Payment> getPaymentByUserUserId(UUID userId);
    Payment getPaymentByUserUserIdAndIdempotencyKey(UUID userId, String idempotencyKey);
}
