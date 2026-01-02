package com.rmo.money_order_service.entity;

import com.rmo.money_order_service.enums.MoneyOrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "money_order")
@Getter
@Setter
public class MoneyOrder {

    @Id
    @GeneratedValue
    private UUID id;

    private UUID transactionId;

    @Column(unique = true)
    private String serialNumber;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private MoneyOrderStatus status;
}
