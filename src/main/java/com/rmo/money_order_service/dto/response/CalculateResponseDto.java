package com.rmo.money_order_service.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
public class CalculateResponseDto {
    private List<MoneyOrderSplitDto> moneyOrders;
    private BigDecimal fee;
    private BigDecimal grandTotal;
    private boolean requiresCompliance;
}
