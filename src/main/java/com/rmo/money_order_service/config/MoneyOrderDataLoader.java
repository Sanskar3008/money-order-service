package com.rmo.money_order_service.config;

import com.rmo.money_order_service.entity.MoneyOrder;
import com.rmo.money_order_service.entity.MoneyOrderTransaction;
import com.rmo.money_order_service.enums.MoneyOrderStatus;
import com.rmo.money_order_service.enums.TransactionStatus;
import com.rmo.money_order_service.repository.MoneyOrderRepository;
import com.rmo.money_order_service.repository.MoneyOrderTransactionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Configuration
public class MoneyOrderDataLoader {

    @Bean
    CommandLineRunner loadData(
            MoneyOrderTransactionRepository transactionRepo,
            MoneyOrderRepository moneyOrderRepo
    ) {
        return args -> {

            UUID agentId =
                    UUID.fromString("11111111-1111-1111-1111-111111111111");

            UUID operatorId =
                    UUID.fromString("22222222-2222-2222-2222-222222222222");

            // -------------------------------
            // Create Transaction
            // -------------------------------
            MoneyOrderTransaction txn = new MoneyOrderTransaction();
            txn.setAgentId(agentId);
            txn.setOperatorId(operatorId);
            txn.setTotalAmount(new BigDecimal("45000"));
            txn.setTotalFees(new BigDecimal("500"));
            txn.setGrandTotal(new BigDecimal("45500"));
            txn.setRequiresCompliance(false);
            txn.setStatus(TransactionStatus.CREATED);
            txn.setCreatedAt(LocalDateTime.now());

            transactionRepo.save(txn);

            UUID txnId = txn.getId();

            // -------------------------------
            // Create Money Orders
            // -------------------------------
            MoneyOrder mo1 = new MoneyOrder();
            mo1.setTransactionId(txnId);
            mo1.setAmount(new BigDecimal("25000"));
            mo1.setStatus(MoneyOrderStatus.INIT);

            MoneyOrder mo2 = new MoneyOrder();
            mo2.setTransactionId(txnId);
            mo2.setAmount(new BigDecimal("20000"));
            mo2.setStatus(MoneyOrderStatus.INIT);

            moneyOrderRepo.saveAll(List.of(mo1, mo2));

            System.out.println("✅ Money Order sample data loaded");
        };
    }
}
