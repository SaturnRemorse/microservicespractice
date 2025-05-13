package com.saturn.ecommerce.order_service.controller;


import com.saturn.ecommerce.order_service.dto.OrderRequestDto;
import com.saturn.ecommerce.order_service.service.OrdersService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/core")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrdersService ordersService;

    @GetMapping("/helloOrders")
    public String helloOrders(){
        return "hello from orders";
    }

    @GetMapping
    public ResponseEntity<List<OrderRequestDto>> getAllOrders(){
        log.info("Fetching all orders via controller");
        List<OrderRequestDto> orders = ordersService.getAllOrder();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderRequestDto> getOrderById(@PathVariable Long id){
        log.info("getting order by id, {}", id);
        OrderRequestDto order = ordersService.getOrderById(id);
        return ResponseEntity.ok(order);
    }
}
