package com.example.BharatMerchantPayments.controller;

import com.example.BharatMerchantPayments.dto.PaymentRequest;
import com.example.BharatMerchantPayments.dto.PaymentResponse;
import com.example.BharatMerchantPayments.model.Payment;
import com.example.BharatMerchantPayments.service.PaymentService;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
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
    public PaymentResponse intiatePayments(@RequestBody final PaymentRequest request) {
        paymentService.validateRequest(request);
        return paymentService.initiatePayment(request);
    }

    @GetMapping(value = "/payments/{paymentId}")
    public ResponseEntity<Payment> aquirePayment(@PathVariable final UUID paymentId) {
        try {
            Payment payment = paymentService.getPaymentById(paymentId);
            return new ResponseEntity<>(payment, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/payments")
    public Map<UUID, Payment> getAllPayments() {
        Map<UUID, Payment> allPayments = paymentService.getAllPayments();
        return allPayments;
    }
}
