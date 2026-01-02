package com.rmo.money_order_service.repository;

import com.rmo.money_order_service.entity.MoneyOrderTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MoneyOrderTransactionRepository
        extends JpaRepository<MoneyOrderTransaction, UUID> {
}
