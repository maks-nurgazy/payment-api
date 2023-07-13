package com.maksnurgazy.services.impl;

import com.maksnurgazy.dto.PaymentResponse;
import com.maksnurgazy.entities.Transaction;
import com.maksnurgazy.entities.User;
import com.maksnurgazy.exception.BadRequestException;
import com.maksnurgazy.exception.NotFoundException;
import com.maksnurgazy.repositories.TransactionRepository;
import com.maksnurgazy.repositories.UserRepository;
import com.maksnurgazy.services.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private static final String PAYMENT_AMOUNT = "1.1";


    @Override
    public PaymentResponse processPayment(Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new NotFoundException("User not found."));

        // Check if the user has sufficient balance for the payment
        BigDecimal paymentAmount = new BigDecimal(PAYMENT_AMOUNT);
        if (user.getBalance().compareTo(paymentAmount) < 0) {
            throw new BadRequestException("Insufficient balance");
        }

        // Perform the payment operation
        BigDecimal newBalance = user.getBalance().subtract(paymentAmount);
        user.setBalance(newBalance);
        userRepository.save(user);

        // Record the transaction in the database
        Transaction transaction = new Transaction(user.getId(), paymentAmount, LocalDateTime.now());
        transactionRepository.save(transaction);

        return PaymentResponse.builder()
                .remainBalance(newBalance)
                .message("Payment successful")
                .build();
    }
}
