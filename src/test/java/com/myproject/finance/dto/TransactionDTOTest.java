package com.myproject.finance.dto;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

public class TransactionDTOTest {

    @Test
    void testTransactionDTOFields() {
        TransactionDTO dto = new TransactionDTO(
                1L,
                100.0,
                "Test Description",
                "Shopping", // ✅ this is 'category'
                LocalDate.of(2025, 1, 1)
        );

        assertEquals(1L, dto.id());
        assertEquals(100.0, dto.amount());
        assertEquals("Test Description", dto.description());
        assertEquals("Shopping", dto.category());
        assertEquals(LocalDate.of(2025, 1, 1), dto.date());
    }
}
