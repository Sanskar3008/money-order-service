package com.rmo.money_order_service.dto.client;


import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ValidateResponseDto {

    private boolean allowed;
    private BigDecimal remainingAfter;
    private String reason;
}
