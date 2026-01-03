package com.rmo.money_order_service.service;

import com.rmo.money_order_service.dto.client.*;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AgentLimitClient {

    private final RestClient restClient;
    private final HttpServletRequest request;

    @Value("${agent.limit.service.url}")
    private String baseUrl;

    // -----------------------------
    // VALIDATE LIMIT
    // -----------------------------
    public void validateLimit(UUID agentId, BigDecimal amount) {

        ValidateRequestDto dto = new ValidateRequestDto();
        dto.setTransactionAmount(amount);

        ValidateResponseDto response =
                restClient.post()
                        .uri(baseUrl + "/agents/{agentId}/validate", agentId)
                        .header(HttpHeaders.AUTHORIZATION, getJwt())
                        .body(dto)
                        .retrieve()
                        .body(ValidateResponseDto.class);

        if (response == null || !response.isAllowed()) {
            throw new RuntimeException(
                    response != null ? response.getReason() : "LIMIT_VALIDATION_FAILED"
            );
        }
    }

    // -----------------------------
    // CONSUME LIMIT
    // -----------------------------
    public void consumeLimit(UUID agentId, BigDecimal amount) {

        ConsumeRequestDto dto = new ConsumeRequestDto();
        dto.setAmount(amount);

        restClient.post()
                .uri(baseUrl + "/agents/{agentId}/consume", agentId)
                .header(HttpHeaders.AUTHORIZATION, getJwt())
                .body(dto)
                .retrieve()
                .toBodilessEntity();
    }

    // -----------------------------
    // JWT FORWARDING
    // -----------------------------
    private String getJwt() {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing Authorization header");
        }
        return authHeader;
    }
}
