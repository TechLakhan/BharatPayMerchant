package com.example.BharatMerchantPayments.service;

import com.example.BharatMerchantPayments.dto.PaymentRequest;
import com.example.BharatMerchantPayments.dto.PaymentResponse;
import com.example.BharatMerchantPayments.enums.PaymentStatus;
import com.example.BharatMerchantPayments.model.Payment;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class PaymentService {

    private final Set<String> is_allowed_payment_method = Set.of("UPI", "CASH", "NET_BANKING");
    private final Map<UUID, Payment> payments = new ConcurrentHashMap<>();


    private final Environment environment;

    public PaymentResponse initiatePayment(final PaymentRequest request) {
        Payment payment = new Payment();
        payment.setUserId(request.getUserId());
        payment.setPaymentId(UUID.randomUUID());
        payment.setAmount(request.getAmount());
        payment.setCurrency(request.getCurrency());
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setStatus(PaymentStatus.SUCCESS);
        payments.put(payment.getPaymentId(), payment);
        return new PaymentResponse(payment.getPaymentId(), payment.getStatus());
    }

    public PaymentService(Environment environment) {
        this.environment = environment;
    }

    public void validateRequest(final PaymentRequest request) {
        if (request.getAmount() <= 0) {
            throw new RuntimeException("Amount must be greater than zero to proceed the transaction");
        }
        if (!is_allowed_payment_method.contains(request.getPaymentMethod())) {
            throw new RuntimeException("Desired payment method is not available");
        }
        if (request.getCurrency() == null || request.getCurrency().isBlank()) {
            throw new RuntimeException("Currency is required for transaction");
        }
    }

    public Payment getPaymentById(UUID paymentId) {
        if (paymentId == null || paymentId.toString().isBlank()) {
            throw new RuntimeException("paymentId is invalid or blank");
        }

        if (!payments.containsKey(paymentId)) {
            throw new RuntimeException("PaymentId is not found");
        }
        Payment payment = payments.get(paymentId);
        return payment;
    }

    public ConcurrentHashMap<UUID, Payment> getAllPayments() {
        return (ConcurrentHashMap<UUID, Payment>) payments;
    }
}
