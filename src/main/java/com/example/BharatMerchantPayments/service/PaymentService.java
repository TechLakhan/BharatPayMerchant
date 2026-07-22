package com.example.BharatMerchantPayments.service;

import com.example.BharatMerchantPayments.dto.PaymentRequest;
import com.example.BharatMerchantPayments.dto.PaymentResponse;
import com.example.BharatMerchantPayments.enums.PaymentStatus;
import com.example.BharatMerchantPayments.model.Payment;
import com.example.BharatMerchantPayments.model.User;
import com.example.BharatMerchantPayments.repository.PaymentRepository;
import com.example.BharatMerchantPayments.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Service
public class PaymentService {

    private final Set<String> is_allowed_payment_method = Set.of("UPI", "CASH", "NET_BANKING");
    private final static String SUCCESS = "SUCCESS";

    private final UserRepository userRepository;

    private final JwtService jwtService;

    private final PaymentRepository paymentRepository;

    public PaymentService(UserRepository userRepository, JwtService jwtService, PaymentRepository paymentRepository) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.paymentRepository = paymentRepository;
    }

    public PaymentResponse initiatePayment(final PaymentRequest request, final String idempotencyKey, User user) {
        Payment payment = new Payment();
        payment.setPaymentId(UUID.randomUUID());
        payment.setAmount(request.getAmount());
        payment.setCurrency(request.getCurrency());
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setStatus(PaymentStatus.PENDING);
        payment.setIdempotencyKey(idempotencyKey);
        payment.setUser(user);
        paymentRepository.save(payment);
        return new PaymentResponse(user.getUserId(), payment.getAmount(), payment.getCurrency(), payment.getPaymentMethod(), payment.getPaymentId(), payment.getStatus());
    }

    public void validateRequestBody(final PaymentRequest request) {
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

    public PaymentResponse getPaymentById(final UUID paymentId, UUID userIdFromToken) {
        if (paymentId == null || paymentId.toString().isBlank()) {
            throw new RuntimeException("paymentId is invalid or blank");
        }
        Payment payment = paymentRepository.getPaymentByPaymentId(paymentId);
        return new PaymentResponse(userIdFromToken, payment.getAmount(), payment.getCurrency(), payment.getPaymentMethod(), payment.getPaymentId(), payment.getStatus());

    }

    public List<PaymentResponse> getAllPaymentsByUser(final String authHeader) {
        String token = checkTokenValidity(authHeader);
        String userId = jwtService.extractUserId(token);
        List<Payment> payments = paymentRepository.getPaymentByUserUserId(UUID.fromString(userId));
        return payments.stream()
                .map(payment -> new PaymentResponse(
                        UUID.fromString(userId),
                        payment.getAmount(),
                        payment.getCurrency(),
                        payment.getPaymentMethod(),
                        payment.getPaymentId(),
                        payment.getStatus()
                )).toList();
    }

    public PaymentResponse validateKeyAgainstUserId(final String idempotencyKey, PaymentRequest request, UUID userId) throws NullPointerException {
        Payment existingPayments = paymentRepository.getPaymentByUserUserIdAndIdempotencyKey(userId, idempotencyKey);
        User user = userRepository.findByUserId(userId);
        if (existingPayments != null && existingPayments.get()) {
            return checkPaymentConfigurations(request, , user);
        }  else {
            return initiatePayment(request, idempotencyKey, user);
        }
    }

    public String getUserIdFromToken(final String authHeader) {
        String token = checkTokenValidity(authHeader);
        String userId = jwtService.extractUserId(token);
        User user = userRepository.findByUserId(UUID.fromString(userId));
        if (!SUCCESS.equals(user.getLogonStatus().name())) {
            throw new RuntimeException("User is not logged in");
        }
        return userId;
    }

    public String checkTokenValidity(final String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "")
                    .replace("bpm_*", "");
            jwtService.validateToken(token);
            return token;
        } catch (Exception e) {
            throw new RuntimeException("Invalid or expired token");
        }
    }

    private PaymentResponse checkPaymentConfigurations(PaymentRequest request, Payment payment, User user) {
         payment.stream()
                 .filter(payment1 -> new PaymentResponse(
                         user.getUserId(),

                 ))
    }
}
