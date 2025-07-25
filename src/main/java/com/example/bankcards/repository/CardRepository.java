package com.example.bankcards.repository;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    Page<Card> findByUser(User user, Pageable pageable);
    Optional<Card> findByIdAndUser(Long id, User user);
    List<Card> findByUser(User user);
    Optional<Card> findByCardNumber(String cardNumber);
    boolean existsByCardNumber(String cardNumber);
}
