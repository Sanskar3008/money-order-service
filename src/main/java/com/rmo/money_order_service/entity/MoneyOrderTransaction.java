package com.rmo.money_order_service.entity;

import com.rmo.money_order_service.enums.TransactionStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "money_order_transaction")
@Getter
@Setter
public class MoneyOrderTransaction {

    @Id
    @GeneratedValue
    private UUID id;

    private UUID agentId;
    private UUID operatorId;

    private BigDecimal totalAmount;
    private BigDecimal totalFees;
    private BigDecimal grandTotal;

    private Boolean requiresCompliance;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    private LocalDateTime createdAt;
}
