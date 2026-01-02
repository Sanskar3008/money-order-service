package com.rmo.money_order_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class MoneyOrderSplitDto {
    private int index;
    private BigDecimal amount;
}
