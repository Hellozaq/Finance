package com.myproject.finance.dto;

import jakarta.validation.constraints.NotNull;

public record DeleteRequestDTO(@NotNull Long id) {}