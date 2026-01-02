package com.rmo.money_order_service.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CreateMoneyOrderRequestDto {
    private BigDecimal enteredAmount;
    private boolean feeEnabled;
}

