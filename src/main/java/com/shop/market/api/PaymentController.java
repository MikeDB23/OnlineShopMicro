package com.shop.market.api;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shop.market.Utils.PaymentMethod;
import com.shop.market.dto.payment.PaymentDto;
import com.shop.market.dto.payment.PaymentToSaveDto;
import com.shop.market.exceptions.NotAbleToDeleteException;
import com.shop.market.exceptions.NotFoundException;
import com.shop.market.service.payment.PaymentService;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping()
    public ResponseEntity getPayments(){
        try{
            List<PaymentDto> paymentsDto = paymentService.getAllPayments();
            return ResponseEntity.ok().body(paymentsDto);
        }catch (Exception e){
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentDto> getPaymentById(@PathVariable("id") Long id){
        try{
            PaymentDto paymentDto = paymentService.findPaymentById(id);
            return ResponseEntity.ok().body(paymentDto);
        }catch(NotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/order/{id}")
    public ResponseEntity<PaymentDto> getPaymentByOrderId(@PathVariable("id") Long idOrder,
                                                          @RequestParam PaymentMethod paymentMethod){
        try{
            PaymentDto paymentDto = paymentService.findByOrderIdAndPaymentMethod(idOrder, paymentMethod);
            return ResponseEntity.ok().body(paymentDto);
        }catch(NotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<PaymentDto>> getPaymentsByTimeOfPaymentBetween(@RequestParam LocalDateTime startDate,
                                                                            @RequestParam LocalDateTime endDate){
        try{
            List<PaymentDto> paymentsDto = paymentService.findByTimeOfPaymentBetween(startDate, endDate);
            return ResponseEntity.ok().body(paymentsDto);
        }catch (NotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping()
    public ResponseEntity createNewPayment(@RequestBody PaymentToSaveDto paymentToSaveDto){
        try{
            PaymentDto paymentDto = paymentService.savePayment(paymentToSaveDto);
            return ResponseEntity.ok().body(paymentDto);
        }catch (Exception e){
            return ResponseEntity
                    .status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity updatePayment(@PathVariable("id") Long id,
                                                 @RequestBody PaymentToSaveDto paymentToSaveDto){
        try{
            PaymentDto paymentDto = paymentService.updatePayment(id, paymentToSaveDto);
            return ResponseEntity.ok().body(paymentDto);
        }catch (NotFoundException e){
            return ResponseEntity.notFound().build();
        }catch (Exception e){
            return ResponseEntity
                    .status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable("id") Long id){
        try{
            paymentService.deletePayment(id);
            return ResponseEntity.ok().build();
        }catch (NotAbleToDeleteException e){
            return ResponseEntity.notFound().build();
        }
    }
}
