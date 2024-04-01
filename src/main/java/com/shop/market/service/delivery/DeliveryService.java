package com.shop.market.service.delivery;

import java.util.List;

import com.shop.market.Utils.Status;
import com.shop.market.dto.delivery.DeliveryDto;
import com.shop.market.dto.delivery.DeliveryToSaveDto;

public interface DeliveryService {
    DeliveryDto saveDelivery(DeliveryToSaveDto deliveryToSaveDto);
    DeliveryDto updateDelivery(Long id, DeliveryToSaveDto deliveryToSaveDto);
    DeliveryDto findDeliveryById(Long id);
    void deleteDelivery(Long id);
    List<DeliveryDto> getAllDeliveries(); 

    DeliveryDto findByOrderId(Long orderId);
    List<DeliveryDto> findByCompany(String company);
    List<DeliveryDto> findByOrderStatus(Status status);
}
