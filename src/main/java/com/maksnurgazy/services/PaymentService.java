package com.maksnurgazy.services;

import com.maksnurgazy.dto.PaymentResponse;
import org.springframework.security.core.Authentication;

public interface PaymentService {
    PaymentResponse processPayment(Authentication authentication);
}
