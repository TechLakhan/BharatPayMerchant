package com.example.BharatMerchantPayments.controller;

import com.example.BharatMerchantPayments.dto.PaymentRequest;
import com.example.BharatMerchantPayments.dto.PaymentResponse;
import com.example.BharatMerchantPayments.model.Payment;
import com.example.BharatMerchantPayments.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping(value = "/payments")
    public PaymentResponse initiatePayments(@RequestBody final PaymentRequest request,
                                           @RequestHeader final String idempotencyKey,
                                           @RequestHeader ("authorisation") final String authHeader) {
        paymentService.validateRequestBody(request);
        String userId = paymentService.getUserIdFromToken(authHeader);
        return paymentService.validateKeyAgainstUserId(idempotencyKey, request, UUID.fromString(userId));
    }



    @GetMapping(value = "/payments/{paymentId}")
    public ResponseEntity<PaymentResponse> getPayment(@PathVariable final UUID paymentId, @RequestHeader("authorisation") final String authHeader) {
        try {
            String token = paymentService.checkTokenValidity(authHeader);
            String userIdFromToken = paymentService.getUserIdFromToken(token);
            PaymentResponse payment = paymentService.getPaymentById(paymentId, UUID.fromString(userIdFromToken));
            return new ResponseEntity<>(payment, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/payments")
    public List<PaymentResponse> getAllPayments(@RequestHeader("authorisation") final String authHeader ) {
        try {
            return paymentService.getAllPaymentsByUser(authHeader);
        }
        catch (Exception e) {
            throw new RuntimeException("Error fetching payments for the user.");
        }
    }
}
