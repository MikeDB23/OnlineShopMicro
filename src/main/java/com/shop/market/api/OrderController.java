package com.shop.market.api;

import java.time.LocalDateTime;
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

import com.shop.market.dto.order.OrderDto;
import com.shop.market.dto.order.OrderToSaveDto;
import com.shop.market.dto.orderItem.OrderItemDto;
import com.shop.market.exceptions.NotAbleToDeleteException;
import com.shop.market.exceptions.NotFoundException;
import com.shop.market.service.order.OrderService;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping()
    public ResponseEntity<List<OrderDto>> getOrders(){
        try{
            List<OrderDto> ordersDto = orderService.getAllOrders();
            return ResponseEntity.ok().body(ordersDto);
        }catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable("id") Long id){
        try{
            OrderDto orderDto = orderService.findOrderById(id);
            return ResponseEntity.ok().body(orderDto);
        }catch (NotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<OrderItemDto>> getOrderItemsByClientId(@PathVariable("customerId") Long customerId){
        try{
            List<OrderItemDto> ordersDto = orderService.findByItemsPerOrder(customerId);
            return ResponseEntity.ok().body(ordersDto);
        }catch (NotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<OrderDto>> getOrdersByTimeOfOrderBetween(@RequestParam LocalDateTime startDate,
                                                                            @RequestParam LocalDateTime endDate){
        try{
            List<OrderDto> ordersDto = orderService.findByTimeOfOrderBetween(startDate, endDate);
            return ResponseEntity.ok().body(ordersDto);
        }catch (NotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping()
    public ResponseEntity<OrderDto> createNewOrder(@RequestBody OrderToSaveDto orderToSaveDto){
        try{
            OrderDto orderDto = orderService.saveOrder(orderToSaveDto);
            return ResponseEntity.ok().body(orderDto);
        }catch(DataIntegrityViolationException e){
            return ResponseEntity.notFound().build();
        }catch (Exception e){
            System.out.println(e);
            return ResponseEntity.unprocessableEntity().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderDto> updateOrder(@PathVariable("id") Long id,
                                                 @RequestBody OrderToSaveDto orderToSaveDto){
        try{
            OrderDto orderDto = orderService.updateOrder(id, orderToSaveDto);
            return ResponseEntity.ok().body(orderDto);
        }catch(DataIntegrityViolationException e){
            return ResponseEntity.notFound().build();
        }catch (NotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable("id") Long id){
        try{
            orderService.deleteOrder(id);
            return ResponseEntity.ok().build();
        }catch (NotAbleToDeleteException e){
            return ResponseEntity.notFound().build();
        }
    }
}
