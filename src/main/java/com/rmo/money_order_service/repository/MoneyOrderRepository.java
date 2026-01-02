package com.rmo.money_order_service.repository;

import com.rmo.money_order_service.entity.MoneyOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MoneyOrderRepository
        extends JpaRepository<MoneyOrder, UUID> {

    List<MoneyOrder> findByTransactionId(UUID transactionId);
}
