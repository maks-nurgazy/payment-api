package com.maksnurgazy.entities;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    public Transaction(Long userId, BigDecimal amount, LocalDateTime timestamp) {
        this.userId = userId;
        this.amount = amount;
        this.timestamp = timestamp;
    }
}