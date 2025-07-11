package com.example.bankcards.service;

import com.example.bankcards.dto.CardDto;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.util.CardNumberEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
class CardServiceIntegrationTest {

    @Autowired
    private CardService cardService;

    @MockBean
    private CardRepository cardRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private CardNumberEncryptor encryptor;

    @Test
    void getAllUserCards_ShouldReturnMaskedNumbers() {
        // 1. Подготовка тестовых данных
        Card card = new Card();
        card.setId(1L);
        card.setCardNumber("encrypted_1234");
        card.setCardHolderName("Test User");
        card.setExpirationDate("12/25");
        card.setCvv("123");
        card.setBalance(BigDecimal.valueOf(1000));

        User testUser = new User();
        testUser.setId(1L);


        when(userRepository.findById(any(Long.class))).thenReturn(java.util.Optional.of(testUser));
        when(cardRepository.findByUser(any(User.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(card), PageRequest.of(0, 10), 1));


        when(encryptor.decrypt(anyString())).thenReturn("1234567812345678");


        List<CardDto> cards = cardService.getAllUserCards();


        assertEquals(1, cards.size());
        assertEquals("**** **** **** 5678", cards.get(0).getCardNumber());
    }
}