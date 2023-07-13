package com.maksnurgazy.controllers;

import com.maksnurgazy.dto.PaymentResponse;
import com.maksnurgazy.services.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/v1")
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/payment")
    public PaymentResponse payment(Authentication authentication) {
        return paymentService.performPayment(authentication);
    }
}
