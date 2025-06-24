package com.myproject.finance.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record TransactionDTO(
    @NotNull Long id,
    @NotNull @Min(1) Double amount,
    @NotBlank String description,
    @NotBlank String category,
    @NotNull LocalDate date
) {}