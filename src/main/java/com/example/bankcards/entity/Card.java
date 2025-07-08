package com.example.bankcards.entity;

import jakarta.persistence.*;
import jakarta.transaction.Transaction;
import lombok.*;
import org.apache.catalina.User;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "cards")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "encrypt number", nullable = false)
    private String encryptNumber;

    @Column(name = "masked_number", nullable = false, length = 19)
    private String maskedNumber;

    @Column(name = "expire_date", nullable = false)
    private LocalDate expireDate;

    @Column(nullable = false)
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CardStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "fromCard", cascade = CascadeType.ALL)
    private List<Transaction> outGoingTransactions;

    @OneToMany(mappedBy = "toCard", cascade = CascadeType.ALL)
    private List<Transaction> incomingTransactions;
}
