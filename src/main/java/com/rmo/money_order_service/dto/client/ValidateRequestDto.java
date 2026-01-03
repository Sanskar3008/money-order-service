package com.rmo.money_order_service.dto.client;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Setter
@Getter
public class ValidateRequestDto {
    private BigDecimal transactionAmount;
}
