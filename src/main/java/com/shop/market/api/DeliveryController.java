package com.shop.market.api;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
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

import com.shop.market.dto.delivery.DeliveryDto;
import com.shop.market.dto.delivery.DeliveryToSaveDto;
import com.shop.market.exceptions.NotAbleToDeleteException;
import com.shop.market.exceptions.NotFoundException;
import com.shop.market.service.delivery.DeliveryService;

@RestController
@RequestMapping("/api/v1/shipping")
public class DeliveryController {
    private final DeliveryService deliveryService;

    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @GetMapping()
    public ResponseEntity<List<DeliveryDto>> getDeliveries(){
        try{
            List<DeliveryDto> deliveriesDto = deliveryService.getAllDeliveries();
            return ResponseEntity.ok().body(deliveriesDto);
        }catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeliveryDto> getDeliveryById(@PathVariable("id") Long id){
        try{
            DeliveryDto deliveryDto = deliveryService.findDeliveryById(id);
            return ResponseEntity.ok().body(deliveryDto);
        }catch(NotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/order/{id}")
    public ResponseEntity<DeliveryDto> getDeliveryByOrderId(@PathVariable("id") Long idOrder){
        try{
            DeliveryDto deliveryDto = deliveryService.findByOrderId(idOrder);
            return ResponseEntity.ok().body(deliveryDto);
        }catch(NotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/carrier")
    public ResponseEntity< List<DeliveryDto>> getDeliveryByCompany(@RequestParam String name){
        try{
            List<DeliveryDto> deliveriesDto = deliveryService.findByCompany(name);
            return ResponseEntity.ok().body(deliveriesDto);
        }catch(NotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping()
    public ResponseEntity<DeliveryDto> createNewDelivery(@RequestBody DeliveryToSaveDto deliveryToSaveDto){
        try{
            DeliveryDto deliveryDto = deliveryService.saveDelivery(deliveryToSaveDto);
            return ResponseEntity.ok().body(deliveryDto);
        }catch(DataIntegrityViolationException e){
            return ResponseEntity.unprocessableEntity().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<DeliveryDto> updateDelivery(@PathVariable("id") Long id,
                                                    @RequestBody DeliveryToSaveDto deliveryToSaveDto){
        try{
            DeliveryDto deliveryDto = deliveryService.updateDelivery(id, deliveryToSaveDto);
            return ResponseEntity.ok().body(deliveryDto);
        }catch(DataIntegrityViolationException e){
            return ResponseEntity.unprocessableEntity().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDelivery(@PathVariable("id") Long id){
        try{
            deliveryService.deleteDelivery(id);
            return ResponseEntity.ok().build();
        }catch (NotAbleToDeleteException e){
            return ResponseEntity.notFound().build();
        }
    }
}
