package com.shop.market.service.payment;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

import com.shop.market.Utils.PaymentMethod;
import com.shop.market.dto.payment.PaymentDto;
import com.shop.market.dto.payment.PaymentToSaveDto;

@Service
public interface PaymentService {
    PaymentDto savePayment(PaymentToSaveDto paymentToSaveDto);
    PaymentDto updatePayment(Long id, PaymentToSaveDto paymentToSaveDto);
    PaymentDto findPaymentById(Long id);
    void deletePayment(Long id);
    List<PaymentDto> getAllPayments(); 

    List<PaymentDto> findByTimeOfPaymentBetween(LocalDateTime firstDate, LocalDateTime endDate);
    PaymentDto findByOrderIdAndPaymentMethod(Long order, PaymentMethod paymentMethod);
}
