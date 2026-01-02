package com.rmo.money_order_service.util;


import java.util.UUID;

public record AuthUser(UUID agentId, UUID operatorId) {
}