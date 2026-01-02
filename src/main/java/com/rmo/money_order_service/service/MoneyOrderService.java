package com.rmo.money_order_service.service;

import com.rmo.money_order_service.dto.response.CalculateResponseDto;
import com.rmo.money_order_service.dto.response.MoneyOrderSplitDto;
import com.rmo.money_order_service.dto.response.PrintMoneyOrderResponseDto;
import com.rmo.money_order_service.entity.MoneyOrder;
import com.rmo.money_order_service.entity.MoneyOrderTransaction;
import com.rmo.money_order_service.enums.MoneyOrderStatus;
import com.rmo.money_order_service.enums.TransactionStatus;
import com.rmo.money_order_service.exception.BusinessException;
import com.rmo.money_order_service.repository.MoneyOrderRepository;
import com.rmo.money_order_service.repository.MoneyOrderTransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class MoneyOrderService {

    private final MoneyOrderTransactionRepository txRepo;
    private final MoneyOrderRepository moRepo;
    private final AgentLimitClient agentLimitClient;

    @Value("${rmo.per-check-limit}")
    private BigDecimal perCheckLimit;

    @Value("${rmo.max-orders}")
    private int maxOrders;

    @Value("${rmo.fee-amount}")
    private BigDecimal feeAmount;

    @Value("${rmo.compliance-threshold}")
    private BigDecimal complianceThreshold;

    public CalculateResponseDto calculate(BigDecimal amount, boolean feeEnabled) {
        List<MoneyOrderSplitDto> splits = splitAmount(amount);

        BigDecimal fee = feeEnabled ? feeAmount : BigDecimal.ZERO;
        BigDecimal grandTotal = amount.add(fee);

        return CalculateResponseDto.builder()
                .moneyOrders(splits)
                .fee(fee)
                .grandTotal(grandTotal)
                .requiresCompliance(amount.compareTo(complianceThreshold) > 0)
                .build();
    }

    public UUID create(BigDecimal amount, boolean feeEnabled,
                       UUID agentId, UUID operatorId) {

        agentLimitClient.validateLimit(agentId, amount);

        List<MoneyOrderSplitDto> splits = splitAmount(amount);

        MoneyOrderTransaction tx = new MoneyOrderTransaction();
        tx.setAgentId(agentId);
        tx.setOperatorId(operatorId);
        tx.setTotalAmount(amount);
        tx.setTotalFees(feeEnabled ? feeAmount : BigDecimal.ZERO);
        tx.setGrandTotal(amount.add(tx.getTotalFees()));
        tx.setRequiresCompliance(amount.compareTo(complianceThreshold) > 0);
        tx.setStatus(TransactionStatus.CREATED);
        tx.setCreatedAt(LocalDateTime.now());

        txRepo.save(tx);

        splits.forEach(s ->
                moRepo.save(createMoneyOrder(tx.getId(), s.getAmount()))
        );

        return tx.getId();
    }

    public PrintMoneyOrderResponseDto print(UUID txId, UUID agentId) {

        MoneyOrderTransaction tx = txRepo.findById(txId)
                .orElseThrow(() -> new BusinessException("Transaction not found"));

        List<MoneyOrder> orders = moRepo.findByTransactionId(txId);

        int serial[] = {100000};
        orders.forEach(o -> {
            o.setSerialNumber("WU" + serial[0]++);
            o.setStatus(MoneyOrderStatus.PRINTED);
        });

        tx.setStatus(TransactionStatus.PRINTED);
        agentLimitClient.consumeLimit(agentId, tx.getTotalAmount());

        return new PrintMoneyOrderResponseDto(
                txId,
                orders.stream()
                        .map(o -> new MoneyOrderSplitDto(0, o.getAmount()))
                        .toList()
        );
    }

    private List<MoneyOrderSplitDto> splitAmount(BigDecimal amount) {
        List<MoneyOrderSplitDto> list = new ArrayList<>();
        BigDecimal remaining = amount;
        int index = 1;

        while (remaining.compareTo(BigDecimal.ZERO) > 0) {
            if (index > maxOrders)
                throw new BusinessException("Max money order limit exceeded");

            BigDecimal split = remaining.min(perCheckLimit);
            list.add(new MoneyOrderSplitDto(index++, split));
            remaining = remaining.subtract(split);
        }
        return list;
    }

    private MoneyOrder createMoneyOrder(UUID txId, BigDecimal amount) {
        MoneyOrder mo = new MoneyOrder();
        mo.setTransactionId(txId);
        mo.setAmount(amount);
        mo.setStatus(MoneyOrderStatus.INIT);
        return mo;
    }
}
