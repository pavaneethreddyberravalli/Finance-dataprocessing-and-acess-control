package com.finance.service;

import com.finance.dto.request.TransactionalRequest;
import com.finance.entity.Transaction;
import com.finance.entity.User;
import com.finance.excption.TransactionNotFoundException;
import com.finance.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;



import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class TransactionalService {

    private final TransactionRepository transactionRepository;
    private User user;


    public Page<Transaction> getAll(String type,
                                    String category,
                                    LocalDate startDate,
                                    LocalDate endDate,
                                    Pageable pageable) {
        // Implement filtering logic here
        return transactionRepository.findAllWithFilters(type,
                category, startDate, endDate, pageable);
    }

    public Transaction create(TransactionalRequest request, Long userId) {
        Transaction transaction = new Transaction();
        transaction.setType(request.getType());
        transaction.setCategory(request.getCategory());
        transaction.setAmount(request.getAmount());
        transaction.setDescription(request.getDescription());
        transaction.setDate(request.getDate());
        transaction.setCreatedBy(userId);
        transaction.setUser(user); // assign entity, not just ID

        return transactionRepository.save(transaction);
    }

    public Transaction update(Long id, TransactionalRequest request) {
        Transaction transaction = transactionRepository.findById(id)
                .filter(t -> !t.isDeleted())  // ignore soft-deleted
                .orElseThrow(() -> new TransactionNotFoundException(
                        "Transaction not found with id: " + id
                ));

        if (request.getType() != null) transaction.setType(request.getType());
        if (request.getCategory() != null) transaction.setCategory(request.getCategory());
        if (request.getAmount() != null) transaction.setAmount(request.getAmount());
        if (request.getDescription() != null) transaction.setDescription(request.getDescription());
        if (request.getDate() != null) transaction.setDate(request.getDate());

        return transactionRepository.save(transaction);
    }

    public void delete(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found"));
        transaction.setDeleted(true); // soft delete flag
        transactionRepository.save(transaction);
    }
    public Transaction getById(Long id) {
        return transactionRepository.findById(id)
                .filter(t -> !t.isDeleted()) // ignore soft-deleted
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found"));
    }
}