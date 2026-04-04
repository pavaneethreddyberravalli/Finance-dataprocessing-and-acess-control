package com.finance.service;

import com.finance.entity.Transaction;
import com.finance.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class DashBoardService {
    @Autowired
    private TransactionRepository transactionRepository;

public BigDecimal getTotalIncome() {
    return transactionRepository.findByDeletedFalse().stream()
            .filter(t -> "CREDIT".equalsIgnoreCase(t.getType()))
            .map(Transaction::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
}

    public BigDecimal getTotalExpense() {
        List<Transaction> transactions = transactionRepository.findByDeletedFalse();
        return transactions.stream()
                .filter(t -> "DEBIT".equalsIgnoreCase(t.getType()))
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getBalance() {
        return getTotalIncome().subtract(getTotalExpense());
    }
}

