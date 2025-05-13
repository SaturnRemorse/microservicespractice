package com.saturn.ecommerce.inventory_service.service;

import com.saturn.ecommerce.inventory_service.dto.OrderRequestDto;
import com.saturn.ecommerce.inventory_service.dto.OrderRequestItemDto;
import com.saturn.ecommerce.inventory_service.dto.ProductDto;
import com.saturn.ecommerce.inventory_service.entity.Product;
import com.saturn.ecommerce.inventory_service.repository.ProductRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepo productRepo;
    private final ModelMapper modelMapper;


    public List<ProductDto> getAllInventory(){
        log.info("Fetching all inventory items");
        List<Product> inventories = productRepo.findAll();
        return inventories.stream()
                .map(product -> modelMapper.map(product, ProductDto.class)).toList();
    }

    public ProductDto getProductById(Long id){
        log.info("Fetching Product By Id");
        Optional<Product> inventory = productRepo.findById(id);
        return inventory.map(item -> modelMapper.map(item, ProductDto.class))
                .orElseThrow(()-> new RuntimeException("Inventory not found"));
    }

    @Transactional
    public Double reduceStocks(OrderRequestDto orderRequestDto) {
        log.info("Reducing the stocks");
        Double totalPrice = 0.0;
        for(OrderRequestItemDto orderRequestItemDto: orderRequestDto.getItems()){
            Long productId = orderRequestItemDto.getProduct_id();
            Integer quantity = orderRequestItemDto.getQuantity();

            Product product = productRepo.findById(productId).orElseThrow(
                    ()-> new RuntimeException("product not found with id: "+productId)
            );

            if(product.getStock()<quantity){
                throw new RuntimeException("product stock not sufficient");
            }

            product.setStock(product.getStock()-quantity);
            productRepo.save(product);
            totalPrice += quantity*product.getPrice();


        }
        return totalPrice;
    }
}
