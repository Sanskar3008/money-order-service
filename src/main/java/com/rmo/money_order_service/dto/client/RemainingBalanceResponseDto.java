package com.rmo.money_order_service.dto.client;



import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class RemainingBalanceResponseDto {
    private UUID agentId;
    private BigDecimal dailyLimitAmount;
    private BigDecimal consumedAmount;
    private BigDecimal remainingBalance;
}
