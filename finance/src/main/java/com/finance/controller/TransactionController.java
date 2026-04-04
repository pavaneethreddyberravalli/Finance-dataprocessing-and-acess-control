package com.finance.controller;

import com.finance.dto.request.TransactionalRequest;
import com.finance.dto.response.ApiResponse;
import com.finance.dto.response.PaginationResponse;
import com.finance.entity.Transaction;
import com.finance.security.UserDetailsImpl;
import com.finance.service.TransactionalService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;


@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    @Autowired
    private TransactionalService transactionService;



    @GetMapping
    @PreAuthorize("hasAnyRole('VIEWER','ANALYST','ADMIN')")
    public ResponseEntity<ApiResponse<PaginationResponse<Transaction>>> getAll(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Transaction> transactions = transactionService.getAll(type, category, startDate, endDate,  pageable);

        PaginationResponse<Transaction> response = new PaginationResponse<>(
                transactions.getContent(),
                transactions.getNumber(),
                transactions.getSize(),
                transactions.getTotalElements(),
                transactions.getTotalPages(),
                transactions.isLast()
        );

        return ResponseEntity.ok(ApiResponse.ok(response));
    }
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('VIEWER','ANALYST','ADMIN')")
    public ResponseEntity<ApiResponse<Transaction>> getById(@PathVariable Long id) {
        Transaction transaction = transactionService.getById(id);
        return ResponseEntity.ok(ApiResponse.ok(transaction));
    }


    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Transaction>> create(
            @Valid @RequestBody TransactionalRequest request,
            @AuthenticationPrincipal UserDetailsImpl currentUser
    ) {
        Transaction created = transactionService.create(request, currentUser.getId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Transaction created.", created));
    }


    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Transaction>> update(
            @PathVariable Long id,
            @RequestBody TransactionalRequest request
    ) {
        Transaction updated = transactionService.update(id, request);
        return ResponseEntity.ok(ApiResponse.ok("Transaction updated.", updated));
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        transactionService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok("Transaction deleted.", null));
    }

}

