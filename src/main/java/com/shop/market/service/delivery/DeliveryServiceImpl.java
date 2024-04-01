package com.shop.market.service.delivery;

import java.util.List;

import org.springframework.stereotype.Service;

import com.shop.market.Utils.Status;
import com.shop.market.dto.delivery.DeliveryDto;
import com.shop.market.dto.delivery.DeliveryMapper;
import com.shop.market.dto.delivery.DeliveryToSaveDto;
import com.shop.market.entities.Delivery;
import com.shop.market.exceptions.NotAbleToDeleteException;
import com.shop.market.exceptions.NotFoundException;
import com.shop.market.repository.DeliveryRepository;

@Service
public class DeliveryServiceImpl implements DeliveryService{
    private final DeliveryMapper deliveryMapper;
    private final DeliveryRepository deliveryRepository;
    
    public DeliveryServiceImpl(DeliveryMapper deliveryMapper, DeliveryRepository deliveryRepository) {
        this.deliveryMapper = deliveryMapper;
        this.deliveryRepository = deliveryRepository;
    }

    @Override
    public void deleteDelivery(Long id) {
        Delivery delivery = deliveryRepository.findById(id)
                            .orElseThrow(()-> new NotAbleToDeleteException("Delivery data not found"));
        deliveryRepository.delete(delivery);    
    }
   
    @Override
    public DeliveryDto findDeliveryById(Long id) {
        Delivery delivery = deliveryRepository.findById(id)
                            .orElseThrow(() -> new NotFoundException("Delivery data not found"));
        return deliveryMapper.entityToDto(delivery);
    }

    @Override
    public List<DeliveryDto> getAllDeliveries() {
        List<Delivery> deliveries = deliveryRepository.findAll();
        return deliveries.stream()
                .map(delivery -> deliveryMapper.entityToDto(delivery))
                .toList();
    }

    @Override
    public DeliveryDto saveDelivery(DeliveryToSaveDto deliveryToSaveDto) {
        Delivery delivery = deliveryMapper.saveDtoToEntity(deliveryToSaveDto);
        Delivery savedDelivery = deliveryRepository.save(delivery);
        return deliveryMapper.entityToDto(savedDelivery);
    }

    @Override
    public DeliveryDto updateDelivery(Long id, DeliveryToSaveDto deliveryToSaveDto) {
        return deliveryRepository.findById(id)
                .map(deliveryDB -> {
                    deliveryDB.setAddress(deliveryToSaveDto.address());
                    deliveryDB.setCompany(deliveryToSaveDto.company());
                    deliveryDB.setOrder(deliveryToSaveDto.order());
                    deliveryDB.setWaybillNumber(deliveryToSaveDto.waybillNumber());
                    
                    Delivery savedDelivery = deliveryRepository.save(deliveryDB);
                    return deliveryMapper.entityToDto(savedDelivery);
                }).orElseThrow(() -> new NotFoundException("delivery data not found"));
    }

    @Override
    public List<DeliveryDto> findByCompany(String company) {
        List<Delivery> deliveries = deliveryRepository.findByCompany(company);
        return deliveries.stream()
                .map(delivery -> deliveryMapper.entityToDto(delivery))
                .toList();
    }

    @Override
    public DeliveryDto findByOrderId(Long orderId) {
        Delivery delivery = deliveryRepository.findByOrderId(orderId)
                                .orElseThrow(() -> new NotFoundException("delivery data not found"));
        return deliveryMapper.entityToDto(delivery);
    }
    
    @Override
    public List<DeliveryDto> findByOrderStatus(Status status) {
        List<Delivery> deliveries = deliveryRepository.findByOrderStatus(status);
        return deliveries.stream()
                .map(delivery -> deliveryMapper.entityToDto(delivery))
                .toList();
    }
}
