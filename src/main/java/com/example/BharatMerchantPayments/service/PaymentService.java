package com.example.BharatMerchantPayments.service;

import com.example.BharatMerchantPayments.dto.PaymentRequest;
import com.example.BharatMerchantPayments.dto.PaymentResponse;
import com.example.BharatMerchantPayments.enums.PaymentStatus;
import com.example.BharatMerchantPayments.model.Payment;
import com.example.BharatMerchantPayments.model.User;
import com.example.BharatMerchantPayments.repository.UserRepository;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PaymentService {

    private final Set<String> is_allowed_payment_method = Set.of("UPI", "CASH", "NET_BANKING");
    private final static String SUCCESS = "SUCCESS";
    private final Map<UUID, Payment> payments = new ConcurrentHashMap<>();
    private final Map<String, PaymentRequest> processedRequests = new ConcurrentHashMap<>();
    private final Map<String, PaymentResponse> paymentResponseMap = new ConcurrentHashMap<>();

    private final Environment environment;

    private final UserRepository userRepository;

    private final JwtService jwtService;

    public PaymentService(Environment environment, UserRepository userRepository, JwtService jwtService) {
        this.environment = environment;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    public PaymentResponse initiatePayment(final PaymentRequest request, final String idempotencyKey, UUID userId) {
        Payment payment = new Payment();
        payment.setUserId(String.valueOf(userId));
        payment.setPaymentId(UUID.randomUUID());
        payment.setAmount(request.getAmount());
        payment.setCurrency(request.getCurrency());
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setStatus(PaymentStatus.SUCCESS);
        payments.put(payment.getPaymentId(), payment);
        PaymentResponse paymentResponse = new PaymentResponse(payment.getPaymentId(), payment.getStatus());
        paymentResponseMap.put(idempotencyKey, paymentResponse);
        return paymentResponse;
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

    public Payment getPaymentById(final UUID paymentId) {
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

    public PaymentResponse validateKey(final String idempotencyKey, PaymentRequest request, UUID userId) {
        if (processedRequests.containsKey(idempotencyKey)) {
//            return idempotencyKeys.get(idempotencyKey);
            return checkPaymentConfigurations(request, idempotencyKey, String.valueOf(userId));
        }
        processedRequests.put(idempotencyKey, request);
        return initiatePayment(request, idempotencyKey, userId);
    }

    public String getUserIdFromToken(final String authHeader) {
        String token = checkTokenValidity(authHeader);
        String userId = jwtService.extractUserId(token);
        User user = userRepository.findByUserId(UUID.fromString(userId));
        if (!SUCCESS.equals(user.getLogonStatus())) {
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

    private PaymentResponse checkPaymentConfigurations(PaymentRequest request, String idempotency, String username) {
        PaymentRequest paymentRequest = processedRequests.get(idempotency);
        if (!(Objects.equals(paymentRequest.getAmount(), request.getAmount())) || !(Objects.equals(paymentRequest.getPaymentMethod(), request.getPaymentMethod())) || !(Objects.equals(paymentRequest.getCurrency(), request.getCurrency()))) {
            return null;
        }
        return paymentResponseMap.get(idempotency);
    }
}
