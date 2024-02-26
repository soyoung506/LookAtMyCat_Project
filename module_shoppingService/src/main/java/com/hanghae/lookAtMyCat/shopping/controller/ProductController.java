package com.hanghae.lookAtMyCat.shopping.controller;

import com.hanghae.lookAtMyCat.shopping.dto.*;
import com.hanghae.lookAtMyCat.shopping.handler.exception.ListTypeNotFoundException;
import com.hanghae.lookAtMyCat.shopping.service.InventoryService;
import com.hanghae.lookAtMyCat.shopping.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;
    private final InventoryService inventoryService;

    // 상품 목록 조회
    @GetMapping("/list")
    public ResponseEntity<List<ProductListResponseDTO>> getProductList(@RequestBody ListTypeDTO listTypeDTO) {
        String listType = listTypeDTO.getListType().isEmpty() ? "최신순" : listTypeDTO.getListType();
        List<ProductListResponseDTO> productListResponseDTO;
        if (listType.equals("최신순")) {
            productListResponseDTO = productService.getProductListOrderByLatest();
        } else if (listType.equals("구매순")) {
            productListResponseDTO = productService.getProductListOrderByPurchase();
        } else {
            throw new ListTypeNotFoundException();
        }
        return ResponseEntity.ok(productListResponseDTO);
    }

    // 상품 상세 페이지 조회
    @GetMapping
    public ResponseEntity<ProductResponseDTO> getProduct(@RequestBody ProductKeyDTO productKeyDTO) {
        ProductResponseDTO productResponseDTO = productService.getProduct(productKeyDTO.getProdKey());
        return ResponseEntity.ok(productResponseDTO);
    }

    // 상품 남은 수량 조회(redis)
    @GetMapping("/inventory")
    public ResponseEntity<InventoryResponseDTO> getInventory(@RequestBody ProductKeyDTO productKeyDTO) {
        InventoryResponseDTO inventoryResponseDTO = inventoryService.getInventory(productKeyDTO.getProdKey());
        return ResponseEntity.ok(inventoryResponseDTO);
    }

}
