package com.rmo.money_order_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class CreateMoneyOrderResponseDto {
    private UUID transactionId;
    private String status;
}
