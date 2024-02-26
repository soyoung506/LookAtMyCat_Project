package com.hanghae.lookAtMyCat.shopping.service;

import com.hanghae.lookAtMyCat.shopping.dto.PurchaseDetailDTO;
import com.hanghae.lookAtMyCat.shopping.dto.PurchaseListResponseDTO;
import com.hanghae.lookAtMyCat.shopping.dto.PurchaseProductDTO;
import com.hanghae.lookAtMyCat.shopping.dto.PurchaseResponseDTO;
import com.hanghae.lookAtMyCat.shopping.entity.Product;
import com.hanghae.lookAtMyCat.shopping.entity.Purchase;
import com.hanghae.lookAtMyCat.shopping.entity.PurchaseProd;
import com.hanghae.lookAtMyCat.shopping.handler.exception.ProductNotFoundException;
import com.hanghae.lookAtMyCat.shopping.handler.exception.PurchaseNotFoundException;
import com.hanghae.lookAtMyCat.shopping.repository.ProductRepository;
import com.hanghae.lookAtMyCat.shopping.repository.PurchaseProdRepository;
import com.hanghae.lookAtMyCat.shopping.repository.PurchaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final PurchaseProdRepository purchaseProdRepository;
    private final ProductRepository productRepository;

    // 상품 결제
    @Transactional
    public void postPurchase(PurchaseDetailDTO purchaseDetailDTO, Long userKey) {
        int purPrice = 0;
        List<Map<String, Object>> productDTO = new ArrayList<>();
        for (PurchaseProductDTO purchaseProductDTO : purchaseDetailDTO.getPurchaseProduct()) {
            // 상품 확인
            Product product = productRepository.findById(purchaseProductDTO.getProdKey())
                    .orElseThrow(ProductNotFoundException::new);
            // 결제 총금액 산출
            int purProdPrice = product.getProdPrice() * purchaseProductDTO.getPurProdCount();
            purPrice += purProdPrice;
            // Product와 purProdCount 객체 저장
            Map<String, Object> data = new HashMap<>();
            data.put("product", product);
            data.put("purProdPrice", purProdPrice);
            data.put("purProdCount", purchaseProductDTO.getPurProdCount());
            productDTO.add(data);
        }
        // 구매 정보 저장
        Purchase purchase = Purchase.builder()
                .userKey(userKey)
                .purPrice(purPrice)
                .purCount(purchaseDetailDTO.getPurchaseProduct().size())
                .purName(purchaseDetailDTO.getPurName())
                .purAddress(purchaseDetailDTO.getPurAddress())
                .purTel(purchaseDetailDTO.getPurTel())
                .purState("결제완료")
                .build();
        Purchase savePurchase = purchaseRepository.save(purchase);
        for (Map<String, Object> data : productDTO) {
            // 구매 상품 정보 저장
            PurchaseProd purchaseProd = PurchaseProd.builder()
                    .purchase(savePurchase)
                    .product((Product) data.get("product"))
                    .purProdPrice((Integer) data.get("purProdPrice"))
                    .purProdCount((Integer) data.get("purProdCount"))
                    .build();
            purchaseProdRepository.save(purchaseProd);
        }

    }

    // 구매 내역 리스트
    @Transactional
    public List<PurchaseListResponseDTO> getPurchaseList(Long userKey) {
        return purchaseRepository.getPurchaseList(userKey);
    }

    // 구매 상세 내역
    @Transactional
    public PurchaseResponseDTO getPurchase(Long purKey) {
        return purchaseRepository.getPurchaseDetail(purKey).orElseThrow(PurchaseNotFoundException::new);
    }
}
