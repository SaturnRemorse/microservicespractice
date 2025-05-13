package com.saturn.ecommerce.order_service.service;

import com.saturn.ecommerce.order_service.clients.InventoryClient;
import com.saturn.ecommerce.order_service.dto.OrderRequestDto;
import com.saturn.ecommerce.order_service.entity.OrderItem;
import com.saturn.ecommerce.order_service.entity.Orders;
import com.saturn.ecommerce.order_service.entity.OrdersStatus;
import com.saturn.ecommerce.order_service.repository.OrdersRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.query.Order;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrdersService {

    private final OrdersRepo ordersRepo;
    private final ModelMapper modelMapper;
    private final InventoryClient inventoryClient;

    public List<OrderRequestDto> getAllOrder(){
        log.info("Fetching all orders");
        List<Orders> orders = ordersRepo.findAll();
        return orders.stream()
                .map(order -> modelMapper.map(order, OrderRequestDto.class)).toList();


    }

    public OrderRequestDto getOrderById(Long id){
        log.info("Fetching order with Id: {}", id);
        Orders order = ordersRepo.findById(id).orElseThrow(()-> new RuntimeException("Order Not Found"));
        return modelMapper.map(order, OrderRequestDto.class);
    }

    public OrderRequestDto createOrder(OrderRequestDto orderRequestDto) {
        Double totalPrice = inventoryClient.reduceStocks(orderRequestDto);

        Orders orders = modelMapper.map(orderRequestDto, Orders.class);
        for(OrderItem orderItem: orders.getItems()){
            orderItem.setOrder(orders);
        }
        orders.setTotal_price(totalPrice);
        orders.setOrdersStatus(OrdersStatus.CONFIRMED);
        Orders savedOrder = ordersRepo.save(orders);
        return modelMapper.map(savedOrder, OrderRequestDto.class);
    }
}
