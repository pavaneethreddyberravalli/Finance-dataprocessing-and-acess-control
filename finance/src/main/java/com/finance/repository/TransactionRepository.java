package com.finance.repository;

import com.finance.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;  // ✅ This is required

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {
    List<Transaction> findByDeletedFalse();
    @Query("""
        SELECT t FROM Transaction t
        WHERE t.deleted = false
        AND (:type IS NULL OR t.type = :type)
        AND (:category IS NULL OR t.category = :category)
        AND (:startDate IS NULL OR t.date >= :startDate)
        AND (:endDate IS NULL OR t.date <= :endDate)
        """)
    Page<Transaction> findAllWithFilters(
            @Param("type") String type,
            @Param("category") String category,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable
    );

}
