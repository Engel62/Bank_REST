package com.example.bankcards.service;

import com.example.bankcards.dto.CardDto;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.entity.User;
import com.example.bankcards.mapper.CardMapper;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.security.services.UserDetailsImpl;
import com.example.bankcards.util.CardNumberEncryptor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final CardMapper cardMapper;
    private final CardNumberEncryptor cardNumberEncryptor;

    public List<CardDto> getAllUserCards() {
        User user = getCurrentUser();
        return cardRepository.findByUser(user).stream()
                .map(this::maskCardNumber)
                .map(cardMapper::toDto)
                .collect(Collectors.toList());
    }

    public Page<CardDto> getAllUserCards(Pageable pageable) {
        User user = getCurrentUser();
        return cardRepository.findByUser(user, pageable)
                .map(this::maskCardNumber)
                .map(cardMapper::toDto);
    }

    public CardDto getCardById(Long id) {
        User user = getCurrentUser();
        Card card = cardRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Card not found with id: " + id));
        return cardMapper.toDto(maskCardNumber(card));
    }

    @Transactional
    public CardDto createCard(CardDto cardDto) {
        User user = getCurrentUser();

        if (cardRepository.existsByCardNumber(cardDto.getCardNumber())) {
            throw new BadRequestException("Card with this number already exists");
        }

        Card card = cardMapper.toEntity(cardDto);
        card.setUser(user);
        card.setStatus(CardStatus.ACTIVE);

        // Encrypt card number before saving
        card.setCardNumber(cardNumberEncryptor.encrypt(card.getCardNumber()));

        Card savedCard = cardRepository.save(card);
        return cardMapper.toDto(maskCardNumber(savedCard));
    }

    @Transactional
    public CardDto updateCard(Long id, CardDto cardDto) {
        User user = getCurrentUser();
        Card existingCard = cardRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Card not found with id: " + id));

        existingCard.setCardHolderName(cardDto.getCardHolderName());
        existingCard.setExpirationDate(cardDto.getExpirationDate());
        existingCard.setCvv(cardDto.getCvv());

        Card updatedCard = cardRepository.save(existingCard);
        return cardMapper.toDto(maskCardNumber(updatedCard));
    }

    @Transactional
    public void deleteCard(Long id) {
        User user = getCurrentUser();
        Card card = cardRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Card not found with id: " + id));
        cardRepository.delete(card);
    }

    @Transactional
    public CardDto blockCard(Long id) {
        User user = getCurrentUser();
        Card card = cardRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Card not found with id: " + id));

        if (card.getStatus() == CardStatus.BLOCKED) {
            throw new BadRequestException("Card is already blocked");
        }

        card.setStatus(CardStatus.BLOCKED);
        Card updatedCard = cardRepository.save(card);
        return cardMapper.toDto(maskCardNumber(updatedCard));
    }

    @Transactional
    public CardDto activateCard(Long id) {
        User user = getCurrentUser();
        Card card = cardRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Card not found with id: " + id));

        if (card.getStatus() == CardStatus.ACTIVE) {
            throw new BadRequestException("Card is already active");
        }

        if (isCardExpired(card.getExpirationDate())) {
            throw new BadRequestException("Cannot activate expired card");
        }

        card.setStatus(CardStatus.ACTIVE);
        Card updatedCard = cardRepository.save(card);
        return cardMapper.toDto(maskCardNumber(updatedCard));
    }

    private User getCurrentUser() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        return userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private Card maskCardNumber(Card card) {
        String encryptedNumber = card.getCardNumber();
        String decryptedNumber = cardNumberEncryptor.decrypt(encryptedNumber);
        String maskedNumber = "**** **** **** " + decryptedNumber.substring(12);
        card.setCardNumber(maskedNumber);
        return card;
    }

    private boolean isCardExpired(String expirationDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");
        LocalDate expDate = LocalDate.parse("01/" + expirationDate, DateTimeFormatter.ofPattern("dd/MM/yy"));
        return LocalDate.now().isAfter(expDate);
    }
}
