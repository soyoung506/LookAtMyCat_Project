package com.hanghae.lookAtMyCat.shopping.service;

import com.hanghae.lookAtMyCat.shopping.dto.InventoryResponseDTO;
import com.hanghae.lookAtMyCat.shopping.dto.PurchaseDTO;
import com.hanghae.lookAtMyCat.shopping.dto.PurchaseProductDTO;
import com.hanghae.lookAtMyCat.shopping.handler.exception.ProductNotFoundException;
import com.hanghae.lookAtMyCat.shopping.handler.exception.ProductSoldOutException;
import com.hanghae.lookAtMyCat.shopping.handler.exception.PurchaseRefusedException;
import com.hanghae.lookAtMyCat.shopping.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final StringRedisTemplate redisTemplate;
    private final ProductRepository productRepository;

    // 상품 재고 조회
    @Transactional
    public InventoryResponseDTO getInventory(Long prodKey) {
        String redisProdKey = "product:stock:" + prodKey;
        String stringStock = redisTemplate.opsForValue().get(redisProdKey);
        if (stringStock == null) {
            throw new ProductNotFoundException();
        }
        Long stock = Long.valueOf(stringStock);
        if (stock <= 0) {
            throw new ProductSoldOutException(prodKey);
        }
        return InventoryResponseDTO.builder().prodKey(prodKey).stock(stock).build();
    }

    // 상품 재고 감소 (결제)
    @Transactional
    public List<InventoryResponseDTO> updateInventory(PurchaseDTO purchaseDTO) {
        List<InventoryResponseDTO> inventoryResponseDTOList = new ArrayList<>();
        for (PurchaseProductDTO purchaseProd : purchaseDTO.getPurchaseProduct()) {
            String redisProdKey = "product:stock:" + purchaseProd.getProdKey();
            redisTemplate.execute(new SessionCallback<Object>() {
                @Override
                public List<Object> execute(RedisOperations operations) throws DataAccessException {
                    redisTemplate.multi();
                    redisTemplate.opsForValue().decrement(redisProdKey, purchaseProd.getPurProdCount());
                    List<Object> result = redisTemplate.exec();
                    if (result.isEmpty()) {
                        throw new ProductNotFoundException();
                    }
                    Long stock = (Long) result.get(0);
                    if (stock < 0) {
                        redisTemplate.opsForValue().increment(redisProdKey, purchaseProd.getPurProdCount());
                        throw new PurchaseRefusedException(purchaseProd.getProdKey());
                    }
                    inventoryResponseDTOList.add(InventoryResponseDTO.builder()
                            .prodKey(purchaseProd.getProdKey()).stock(stock).build());
                    // mysql db에도 재고수량 반영
                    productRepository.subtractProdCount(purchaseProd.getProdKey(), purchaseProd.getPurProdCount());
                    return result;
                }
            });
        }
        return inventoryResponseDTOList;
    }

    // 상품 재고 증가 (결제 중 취소 및 실패)
    @Transactional
    public List<InventoryResponseDTO> cancelInventory(PurchaseDTO purchaseDTO) {
        List<InventoryResponseDTO> inventoryResponseDTOList = new ArrayList<>();
        for (PurchaseProductDTO purchaseProd : purchaseDTO.getPurchaseProduct()) {
            String redisProdKey = "product:stock:" + purchaseProd.getProdKey();
            redisTemplate.execute(new SessionCallback<Object>() {
                @Override
                public List<Object> execute(RedisOperations operations) throws DataAccessException {
                    redisTemplate.multi();
                    redisTemplate.opsForValue().increment(redisProdKey, purchaseProd.getPurProdCount());
                    List<Object> result = redisTemplate.exec();
                    if (result.isEmpty()) {
                        throw new ProductNotFoundException();
                    }
                    Long stock = (Long) result.get(0);
                    inventoryResponseDTOList.add(InventoryResponseDTO.builder()
                            .prodKey(purchaseProd.getProdKey()).stock(stock).build());
                    // mysql db에도 재고수량 반영
                    productRepository.addProdCount(purchaseProd.getProdKey(), purchaseProd.getPurProdCount());
                    return result;
                }
            });
        }
        return inventoryResponseDTOList;
    }
}
