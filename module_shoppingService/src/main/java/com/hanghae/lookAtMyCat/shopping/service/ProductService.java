package com.hanghae.lookAtMyCat.shopping.service;

import com.hanghae.lookAtMyCat.shopping.dto.ProductResponseDTO;
import com.hanghae.lookAtMyCat.shopping.dto.ProductListResponseDTO;
import com.hanghae.lookAtMyCat.shopping.handler.exception.ProductNotFoundException;
import com.hanghae.lookAtMyCat.shopping.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    // 상품 목록 조회 (최신등록순)
    @Transactional
    public List<ProductListResponseDTO> getProductListOrderByLatest() {
        return productRepository.getProductListOrderByLatest();
    }

    // 상품 목록 조회 (구매순)
    @Transactional
    public List<ProductListResponseDTO> getProductListOrderByPurchase() {
        return productRepository.getProductListOrderByPurchase();
    }

    // 상품 상세 조회
    @Transactional
    public ProductResponseDTO getProduct(Long prodKey) {
        return productRepository.getProductDetail(prodKey).orElseThrow(ProductNotFoundException::new);
    }
}
