package com.finance.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionalRequest {
    @NotNull
    private String type;

    @NotNull
    private String category;

    @NotNull
    @Positive
    private BigDecimal amount;

    private String description;

    @NotNull
    private LocalDate date;
}
