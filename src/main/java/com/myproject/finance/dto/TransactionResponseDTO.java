package com.myproject.finance.dto;

import java.time.LocalDate;

public record TransactionResponseDTO(
        Long id,
        Double amount,
        String description,
        String category,
        LocalDate date,
        String username
) {}