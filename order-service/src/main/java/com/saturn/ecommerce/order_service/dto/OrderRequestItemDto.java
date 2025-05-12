package com.saturn.ecommerce.order_service.dto;

import lombok.Data;

@Data
public class OrderRequestItemDto {

    private Long id;
    private Long product_id;
    private Integer quantity;
}
