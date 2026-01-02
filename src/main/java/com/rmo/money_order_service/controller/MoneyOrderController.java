package com.rmo.money_order_service.controller;

import com.rmo.money_order_service.dto.request.CalculateRequestDto;
import com.rmo.money_order_service.dto.request.CreateMoneyOrderRequestDto;
import com.rmo.money_order_service.dto.response.CalculateResponseDto;
import com.rmo.money_order_service.dto.response.CreateMoneyOrderResponseDto;
import com.rmo.money_order_service.dto.response.PrintMoneyOrderResponseDto;
import com.rmo.money_order_service.util.AuthUser;
import com.rmo.money_order_service.service.MoneyOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/money-orders")
@RequiredArgsConstructor
public class MoneyOrderController {

    private final MoneyOrderService service;

    // ------------------------------------------------
    // 1. Calculate split (NO DB WRITE)
    // ------------------------------------------------
    @PostMapping("/calculate")
    public CalculateResponseDto calculate(
            @RequestBody CalculateRequestDto req,
            Authentication authentication) {

        // JWT is already validated by filter
        return service.calculate(
                req.getEnteredAmount(),
                req.isFeeEnabled()
        );
    }

    // ------------------------------------------------
    // 2. Create Money Order Transaction
    // ------------------------------------------------
    @PostMapping
    public CreateMoneyOrderResponseDto create(
            @RequestBody CreateMoneyOrderRequestDto req,
            Authentication authentication) {

        AuthUser user = (AuthUser) authentication.getPrincipal();

        UUID transactionId = service.create(
                req.getEnteredAmount(),
                req.isFeeEnabled(),
                user.agentId(),
                user.operatorId()
        );

        return new CreateMoneyOrderResponseDto(
                transactionId,
                "CREATED"
        );
    }

    // ------------------------------------------------
    // 3. Print Money Orders
    // ------------------------------------------------
    @PostMapping("/{transactionId}/print")
    public PrintMoneyOrderResponseDto print(
            @PathVariable UUID transactionId,
            Authentication authentication) {

        AuthUser user = (AuthUser) authentication.getPrincipal();

        return service.print(
                transactionId,
                user.agentId()
        );
    }
}
