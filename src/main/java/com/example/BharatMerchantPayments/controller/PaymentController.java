package com.example.BharatMerchantPayments.controller;

import com.example.BharatMerchantPayments.dto.PaymentRequest;
import com.example.BharatMerchantPayments.dto.PaymentResponse;
import com.example.BharatMerchantPayments.model.Payment;
import com.example.BharatMerchantPayments.service.JwtService;
import com.example.BharatMerchantPayments.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping(value = "/payments")
    public PaymentResponse intiatePayments(@RequestBody final PaymentRequest request,
                                           @RequestHeader final String idempotencyKey,
                                           @RequestHeader ("authorisation") final String authHeader) {
        paymentService.validateRequestBody(request);
        String userId = paymentService.getUserIdFromToken(authHeader);
        return paymentService.validateKey(idempotencyKey, request, UUID.fromString(userId));
    }



    @GetMapping(value = "/payments/{paymentId}")
    public ResponseEntity<Payment> aquirePayment(@PathVariable final UUID paymentId, @RequestHeader("authorisation") final String authHeader) {
        try {
            paymentService.checkTokenValidity(authHeader);
            Payment payment = paymentService.getPaymentById(paymentId);
            return new ResponseEntity<>(payment, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/payments")
    public ConcurrentHashMap<UUID, Payment> getAllPayments() {
        ConcurrentHashMap<UUID, Payment> allPayments = paymentService.getAllPayments();
        return allPayments;
    }
}
