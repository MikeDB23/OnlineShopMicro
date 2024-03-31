package com.shop.market.service.payment;

import java.time.LocalDateTime;
import java.util.List;

import com.shop.market.Utils.PaymentMethod;
import com.shop.market.dto.payment.PaymentDto;
import com.shop.market.dto.payment.PaymentMapper;
import com.shop.market.dto.payment.PaymentToSaveDto;
import com.shop.market.entities.Payment;
import com.shop.market.exceptions.NotAbleToDeleteException;
import com.shop.market.exceptions.NotFoundException;
import com.shop.market.repository.PaymentRepository;

public class PaymentServiceDto implements PaymentService{
    PaymentRepository paymentRepository;
    PaymentMapper paymentMapper;
    
    public PaymentServiceDto(PaymentRepository paymentRepository, PaymentMapper paymentMapper) {
        this.paymentRepository = paymentRepository;
        this.paymentMapper = paymentMapper;
    }

    @Override
    public void deletePayment(Long id) {
        Payment payment = paymentRepository.findById(id)
                            .orElseThrow(() -> new NotAbleToDeleteException("Payment not found"));
        paymentRepository.delete(payment);
    }


    @Override
    public PaymentDto findPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id)
                            .orElseThrow(() -> new NotFoundException("Payment not found"));
        return paymentMapper.entityToDto(payment);
    }

    @Override
    public List<PaymentDto> getAllPayments() {
        List<Payment> payments = paymentRepository.findAll();
        return payments.stream()
                .map(payment -> paymentMapper.entityToDto(payment))
                .toList();
    }

    @Override
    public PaymentDto savePayment(PaymentToSaveDto paymentToSaveDto) {
        Payment payment = paymentMapper.saveDtoToEntity(paymentToSaveDto);
        Payment savedPayment = paymentRepository.save(payment);
        return paymentMapper.entityToDto(savedPayment);
    }

    @Override
    public PaymentDto updatePayment(Long id, PaymentToSaveDto paymentToSaveDto) {
        return paymentRepository.findById(id)
                .map(paymentDB -> {
                    paymentDB.setOrder(paymentToSaveDto.order());
                    paymentDB.setPaymentMethod(paymentToSaveDto.paymentMethod());
                    paymentDB.setTimeOfPayment(paymentToSaveDto.timeOfPayment());
                    paymentDB.setTotalPayment(paymentToSaveDto.totalPayment());
                    
                    Payment savedPayment = paymentRepository.save(paymentDB);
                    return paymentMapper.entityToDto(paymentDB);
                }).orElseThrow(() -> new NotFoundException("Payment not found"));
    }

    @Override
    public PaymentDto findByOrderIdAndPaymentMethod(Long orderId, PaymentMethod paymentMethod) {
        Payment payment = paymentRepository.findByOrderIdAndPaymentMethod(orderId, paymentMethod)
                            .orElseThrow(() -> new NotFoundException("Order's payment not found"));
        return paymentMapper.entityToDto(payment);
    }

    @Override
    public List<PaymentDto> findByTimeOfPaymentBetween(LocalDateTime firstDate, LocalDateTime endDate) {
        List<Payment> payments = paymentRepository.findByTimeOfPaymentBetween(firstDate, endDate);
        return payments.stream()
                .map(payment -> paymentMapper.entityToDto(payment))
                .toList();
    }
}