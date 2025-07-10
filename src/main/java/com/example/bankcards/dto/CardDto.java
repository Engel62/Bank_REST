package com.example.bankcards.dto;

import com.example.bankcards.entity.CardStatus;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class CardDto {
    private Long id;

    @NotBlank
    @Size(min = 16, max = 16)
    private String cardNumber;

    @NotBlank
    @Size(max = 100)
    private String cardHolderName;

    @NotBlank
    @Pattern(regexp = "^(0[1-9]|1[0-2])/[0-9]{2}$")
    private String expirationDate;

    @NotBlank
    @Size(min = 3, max = 3)
    private String cvv;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal balance;

    private CardStatus status;
    private LocalDateTime createdAt;
}
