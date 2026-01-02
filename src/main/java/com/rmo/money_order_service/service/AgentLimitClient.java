package com.rmo.money_order_service.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.UUID;

@FeignClient(name = "agent-limit-service", url = "${agent-limit.url}")
public interface AgentLimitClient {

    @PostMapping("/agent-limit/validate")
    void validateLimit(@RequestParam UUID agentId,
                       @RequestParam BigDecimal amount);

    @PostMapping("/agent-limit/consume")
    void consumeLimit(@RequestParam UUID agentId,
                      @RequestParam BigDecimal amount);
}

