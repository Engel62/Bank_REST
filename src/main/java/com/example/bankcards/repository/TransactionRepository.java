package com.example.bankcards.repository;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByFromCardOrToCard(Card fromCard, Card toCard);
    Page<Transaction> findByFromCardOrToCard(Card fromCard, Card toCard, Pageable pageable);
}
