package com.hanghae.lookAtMyCat.shopping.repository.customRepository;


import com.hanghae.lookAtMyCat.shopping.dto.*;

import java.util.List;
import java.util.Optional;

public interface CustomRepository {

    // 상품 목록 조회 (최신순, 구매순)
    List<ProductListResponseDTO> getProductListOrderByLatest();
    List<ProductListResponseDTO> getProductListOrderByPurchase();

    // 상품 상세 조회
    Optional<ProductResponseDTO> getProductDetail(Long prodKey);

    // 구매 목록 조회
    List<PurchaseListResponseDTO> getPurchaseList(Long userKey);

    // 구매내역 상세 조회
    Optional<PurchaseResponseDTO> getPurchaseDetail(Long purKey);

    // db 재고 업데이트
    void subtractProdCount(Long prodKey, int purProdCount);
    void addProdCount(Long prodKey, int purProdCount);

    // 장바구니 리스트 조회
    List<CartListResponseDTO> getCartList(Long userKey);
}
