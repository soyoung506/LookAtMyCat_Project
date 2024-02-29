package com.hanghae.lookAtMyCat.shopping.controller;

import com.hanghae.lookAtMyCat.shopping.dto.CartDTO;
import com.hanghae.lookAtMyCat.shopping.dto.CartListResponseDTO;
import com.hanghae.lookAtMyCat.shopping.dto.PurchaseProductDTO;
import com.hanghae.lookAtMyCat.shopping.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    // 장바구니 조회
    @GetMapping
    public ResponseEntity<List<CartListResponseDTO>> getCartList(Authentication authentication) {
        List<CartListResponseDTO> cartListResponseDTO = cartService.getCartList(Long.parseLong(authentication.getName()));
        return ResponseEntity.ok(cartListResponseDTO);
    }

    // 장바구니 추가
    @PostMapping
    public ResponseEntity<String> addCart(@RequestBody PurchaseProductDTO purchaseProductDTO, Authentication authentication) {
        cartService.addCart(purchaseProductDTO, Long.parseLong(authentication.getName()));
        return ResponseEntity.ok("장바구니 상품 추가");
    }

    // 장바구니 수정
    @PatchMapping
    public ResponseEntity<String> updateCart(@RequestBody CartDTO cartDTO, Authentication authentication) {
        cartService.updateCart(cartDTO, Long.parseLong(authentication.getName()));
        return ResponseEntity.ok("장바구니 상품 수량 수정");
    }

    // 장바구니 삭제
    @DeleteMapping
    public ResponseEntity<String> deleteCart(@RequestBody CartDTO cartDTO, Authentication authentication) {
        cartService.deleteCart(cartDTO, Long.parseLong(authentication.getName()));
        return ResponseEntity.ok("장바구니 상품 삭제");
    }



}
