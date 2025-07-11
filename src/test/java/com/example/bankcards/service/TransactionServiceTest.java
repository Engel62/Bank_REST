package com.example.bankcards.service;
import com.example.bankcards.dto.TransactionDto;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.exception.BadRequestException;
import com.example.bankcards.entity.Card;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private CardRepository cardRepository;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    void createTransaction_ShouldFailWhenInsufficientFunds() {
        TransactionDto dto = new TransactionDto();
        dto.setAmount(BigDecimal.valueOf(1000));
        dto.setFromCardId(1L);
        dto.setToCardId(2L);

        Card fromCard = new Card();
        fromCard.setBalance(BigDecimal.valueOf(500));
        fromCard.setStatus(CardStatus.ACTIVE);

        Card toCard = new Card();
        toCard.setStatus(CardStatus.ACTIVE);

        when(cardRepository.findById(1L)).thenReturn(Optional.of(fromCard));
        when(cardRepository.findById(2L)).thenReturn(Optional.of(toCard));

        assertThrows(BadRequestException.class, () -> {
            transactionService.createTransaction(dto);
        });
    }

    @Test
    void createTransaction_ShouldTransferFunds() {
        // Тест для успешного перевода
    }
}