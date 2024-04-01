package com.shop.market.dto.payment;

import org.mapstruct.Mapper;

import com.shop.market.entities.Payment;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    Payment dtoToEntity(PaymentDto paymentDto);
    Payment saveDtoToEntity(PaymentToSaveDto paymentToSaveDto);
    PaymentDto entityToDto(Payment payment);
}
