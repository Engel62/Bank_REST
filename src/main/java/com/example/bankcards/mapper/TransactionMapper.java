package com.example.bankcards.mapper;

import com.example.bankcards.dto.TransactionDto;
import com.example.bankcards.entity.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TransactionMapper {
    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);

    @Mapping(target = "fromCard", ignore = true)
    @Mapping(target = "toCard", ignore = true)
    Transaction toEntity(TransactionDto transactionDto);

    TransactionDto toDto(Transaction transaction);
}
