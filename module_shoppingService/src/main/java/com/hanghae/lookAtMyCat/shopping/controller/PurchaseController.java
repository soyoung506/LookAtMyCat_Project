package com.hanghae.lookAtMyCat.shopping.controller;

import com.hanghae.lookAtMyCat.shopping.dto.*;
import com.hanghae.lookAtMyCat.shopping.service.InventoryService;
import com.hanghae.lookAtMyCat.shopping.service.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/purchase")
public class PurchaseController {

    private final PurchaseService purchaseService;
    private final InventoryService inventoryService;

    // 주문창 진입 및 재고 수량 감소
    // 프론트단에서 오픈 상품 구매 시 구매수량 1개로 제한
    @PatchMapping
    public ResponseEntity<List<InventoryResponseDTO>> updateInventory(@RequestBody PurchaseDTO purchaseDTO) {
        List<InventoryResponseDTO> inventoryResponseDTO = inventoryService.updateInventory(purchaseDTO);
        return ResponseEntity.ok(inventoryResponseDTO);
    }

    // 결제 중 취소 및 실패
    @DeleteMapping
    public ResponseEntity<List<InventoryResponseDTO>> cancelInventory(@RequestBody PurchaseDTO purchaseDTO) {
        List<InventoryResponseDTO> inventoryResponseDTO = inventoryService.cancelInventory(purchaseDTO);
        return ResponseEntity.ok(inventoryResponseDTO);
    }

    // 상품 결제 완료
    @PostMapping
    public ResponseEntity<String> postPurchase(@RequestBody PurchaseDetailDTO purchaseDetailDTO, Authentication authentication) {
        purchaseService.postPurchase(purchaseDetailDTO, Long.parseLong(authentication.getName()));
        return ResponseEntity.ok("결제 완료");
    }

    // 구매 내역 조회
    @GetMapping("/list")
    public ResponseEntity<List<PurchaseListResponseDTO>> getPurchaseList(Authentication authentication) {
        List<PurchaseListResponseDTO> purchaseListResponseDTO = purchaseService.getPurchaseList(Long.parseLong(authentication.getName()));
        return ResponseEntity.ok(purchaseListResponseDTO);
    }

    // 구매 상세 내역 조회
    @GetMapping("/detail")
    public ResponseEntity<PurchaseResponseDTO> getPurchase(@RequestBody PurchaseKeyDTO purchaseKeyDTO) {
        PurchaseResponseDTO purchaseResponseDTO = purchaseService.getPurchase(purchaseKeyDTO.getPurKey());
        return ResponseEntity.ok(purchaseResponseDTO);
    }


}
