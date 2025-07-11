package com.example.bankcards.service;
import com.example.bankcards.dto.CardDto;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.util.CardNumberEncryptor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardServiceTest {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CardNumberEncryptor encryptor;

    @InjectMocks
    private CardService cardService;

    @Test
    void createCard_ShouldEncryptCardNumber() {
        CardDto cardDto = new CardDto();
        cardDto.setCardNumber("1234567812345678");
        cardDto.setCardHolderName("TEST USER");
        cardDto.setExpirationDate("12/25");
        cardDto.setCvv("123");
        cardDto.setBalance(BigDecimal.valueOf(1000));

        when(encryptor.encrypt(anyString())).thenReturn("encrypted");
        when(userRepository.findById(any())).thenReturn(Optional.of(new User()));
        when(cardRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        CardDto result = cardService.createCard(cardDto);

        verify(encryptor, times(1)).encrypt("1234567812345678");
        assertNotNull(result);
    }

    @Test
    void getCardById_ShouldReturnMaskedNumber() {
        // Тест для метода получения карты
    }
}
