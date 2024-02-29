package com.hanghae.lookAtMyCat.shopping.service;

import com.hanghae.lookAtMyCat.shopping.dto.CartDTO;
import com.hanghae.lookAtMyCat.shopping.dto.CartListResponseDTO;
import com.hanghae.lookAtMyCat.shopping.dto.PurchaseProductDTO;
import com.hanghae.lookAtMyCat.shopping.entity.Cart;
import com.hanghae.lookAtMyCat.shopping.entity.Product;
import com.hanghae.lookAtMyCat.shopping.handler.exception.CartNotFoundException;
import com.hanghae.lookAtMyCat.shopping.handler.exception.ProductNotFoundException;
import com.hanghae.lookAtMyCat.shopping.handler.exception.UserMismatchException;
import com.hanghae.lookAtMyCat.shopping.repository.CartRepository;
import com.hanghae.lookAtMyCat.shopping.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    // 장바구니 조회
    @Transactional
    public List<CartListResponseDTO> getCartList(Long userKey) {
        return cartRepository.getCartList(userKey);
    }

    // 장바구니 추가
    @Transactional
    public void addCart(PurchaseProductDTO purchaseProductDTO, Long userKey) {
        Product product = productRepository.findById(purchaseProductDTO.getProdKey())
                .orElseThrow(ProductNotFoundException::new);
        Cart cart = Cart.builder()
                .userKey(userKey)
                .product(product)
                .cartCount(purchaseProductDTO.getPurProdCount())
                .build();
        cartRepository.save(cart);
    }

    // 장바구니 수량 수정
    @Transactional
    public void updateCart(CartDTO cartDTO, Long userKey) {
        Cart cart = cartRepository.findById(cartDTO.getCartKey())
                .orElseThrow(CartNotFoundException::new);
        if (!userKey.equals(cart.getUserKey())) {
            throw new UserMismatchException();
        }
        cart.update(cartDTO.getCartCount());
        cartRepository.save(cart);
    }

    // 장바구니 삭제
    @Transactional
    public void deleteCart(CartDTO cartDTO, Long userKey) {
        Cart cart = cartRepository.findById(cartDTO.getCartKey())
                .orElseThrow(CartNotFoundException::new);
        if (!userKey.equals(cart.getUserKey())) {
            throw new UserMismatchException();
        }
        cartRepository.deleteById(cartDTO.getCartKey());
    }
}
