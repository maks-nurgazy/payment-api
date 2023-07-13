package com.maksnurgazy.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class PaymentResponse {
    private BigDecimal remainBalance;
    private String message;
}
