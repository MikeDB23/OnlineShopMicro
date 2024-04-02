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
import org.springframework.web.bind.annotation.RestController;

import com.shop.market.dto.orderItem.OrderItemDto;
import com.shop.market.dto.orderItem.OrderItemToSaveDto;
import com.shop.market.exceptions.NotAbleToDeleteException;
import com.shop.market.exceptions.NotFoundException;
import com.shop.market.service.orderItem.OrderItemService;

@RestController
@RequestMapping("/api/v1/order-items")
public class OrderItemController {
    private final OrderItemService orderItemService;

    public OrderItemController(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    @GetMapping()
    public ResponseEntity<List<OrderItemDto>> getOrdersItems(){
        try{
            List<OrderItemDto> ordersItemsDto = orderItemService.getAllOrderItems();
            return ResponseEntity.ok().body(ordersItemsDto);
        }catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderItemDto> getOrderItemById(@PathVariable("id") Long id){
        try{
            OrderItemDto orderItemDto = orderItemService.findOrderItemById(id);
            return ResponseEntity.ok().body(orderItemDto);
        }catch(NotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/order/{id}")
    public ResponseEntity<List<OrderItemDto>> getOrderItemsByOrder(@PathVariable("id") Long idOrder){
        List<OrderItemDto> orderItemsDto = orderItemService.findByOrderId(idOrder);
        return ResponseEntity.ok().body(orderItemsDto);
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<List<OrderItemDto>> getOrderItemsByProduct(@PathVariable("id") Long idProduct){
        List<OrderItemDto> orderItemsDto = orderItemService.findByProductId(idProduct);
        return ResponseEntity.ok().body(orderItemsDto);
    }
    
    @PostMapping()
    public ResponseEntity<OrderItemDto> createNewOrderItem(@RequestBody OrderItemToSaveDto orderItemToSaveDto){
        try{
            OrderItemDto orderItemDto = orderItemService.saveOrderItem(orderItemToSaveDto);
            return ResponseEntity.ok().body(orderItemDto);
        }catch(DataIntegrityViolationException e){
            return ResponseEntity.notFound().build();
        }catch (Exception e){
            return ResponseEntity.unprocessableEntity().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderItemDto> updateOrderItem(@PathVariable("id") Long id,
                                                 @RequestBody OrderItemToSaveDto orderItemToSaveDto){
        try{
            OrderItemDto orderItemDto = orderItemService.updateOrderItem(id, orderItemToSaveDto);
            return ResponseEntity.ok().body(orderItemDto);
        }catch(DataIntegrityViolationException e){
            return ResponseEntity.notFound().build();
        }catch (NotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable("id") Long id){
        try{
            orderItemService.deleteOrderItem(id);
            return ResponseEntity.ok().build();
        }catch (NotAbleToDeleteException e){
            return ResponseEntity.notFound().build();
        }
    }

}
