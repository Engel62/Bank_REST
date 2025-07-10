package com.example.bankcards.service;

import com.example.bankcards.dto.TransactionDto;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.entity.Transaction;
import com.example.bankcards.entity.User;
import com.example.bankcards.mapper.TransactionMapper;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.TransactionRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.security.services.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final TransactionMapper transactionMapper;

    @Transactional
    public TransactionDto createTransaction(TransactionDto transactionDto) {
        User user = getCurrentUser();

        Card fromCard = cardRepository.findByIdAndUser(transactionDto.getFromCardId(), user)
                .orElseThrow(() -> new ResourceNotFoundException("From card not found"));

        Card toCard = cardRepository.findById(transactionDto.getToCardId())
                .orElseThrow(() -> new ResourceNotFoundException("To card not found"));

        validateTransaction(fromCard, toCard, transactionDto.getAmount());

        // Perform the transfer
        fromCard.setBalance(fromCard.getBalance().subtract(transactionDto.getAmount()));
        toCard.setBalance(toCard.getBalance().add(transactionDto.getAmount()));

        cardRepository.save(fromCard);
        cardRepository.save(toCard);

        // Create transaction record
        Transaction transaction = transactionMapper.toEntity(transactionDto);
        transaction.setFromCard(fromCard);
        transaction.setToCard(toCard);

        Transaction savedTransaction = transactionRepository.save(transaction);
        return transactionMapper.toDto(savedTransaction);
    }

    public List<TransactionDto> getAllUserTransactions() {
        User user = getCurrentUser();
        List<Card> userCards = cardRepository.findByUser(user);
        return transactionRepository.findByFromCardOrToCard(userCards.get(0), userCards.get(0)).stream()
                .map(transactionMapper::toDto)
                .collect(Collectors.toList());
    }

    public Page<TransactionDto> getAllUserTransactions(Pageable pageable) {
        User user = getCurrentUser();
        List<Card> userCards = cardRepository.findByUser(user);
        return transactionRepository.findByFromCardOrToCard(userCards.get(0), userCards.get(0), pageable)
                .map(transactionMapper::toDto);
    }

    public TransactionDto getTransactionById(Long id) {
        User user = getCurrentUser();
        List<Card> userCards = cardRepository.findByUser(user);
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));

        if (!userCards.contains(transaction.getFromCard()) && !userCards.contains(transaction.getToCard())) {
            throw new ResourceNotFoundException("Transaction not found");
        }

        return transactionMapper.toDto(transaction);
    }

    private void validateTransaction(Card fromCard, Card toCard, BigDecimal amount) {
        if (fromCard.getStatus() != CardStatus.ACTIVE || toCard.getStatus() != CardStatus.ACTIVE) {
            throw new BadRequestException("One of the cards is not active");
        }

        if (fromCard.getBalance().compareTo(amount) < 0) {
            throw new BadRequestException("Insufficient funds");
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Amount must be positive");
        }
    }

    private User getCurrentUser() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        return userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
