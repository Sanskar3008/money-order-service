package com.rmo.money_order_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class PrintMoneyOrderResponseDto {
    private UUID transactionId;
    private List<MoneyOrderSplitDto> printedOrders;
}
