package com.hanghae.lookAtMyCat.shopping.repository.customRepository;

import com.hanghae.lookAtMyCat.shopping.dto.*;
import com.hanghae.lookAtMyCat.shopping.entity.*;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public class CustomRepositoryImpl implements CustomRepository {

    private final JPAQueryFactory query;

    public CustomRepositoryImpl(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    // 상품 목록 조회 (최신등록순)
    // 정렬: 오픈판매상품 > 최신등록순
    @Override
    public List<ProductListResponseDTO> getProductListOrderByLatest() {
        QProduct product = QProduct.product;
        return query
                .select(Projections.bean(ProductListResponseDTO.class, product.prodKey, product.prodName,
                        product.prodPrice, product.prodCount, product.prodDate, product.prodOpen))
                .from(product)
                .orderBy(product.prodOpen.desc(), product.prodDate.desc())
                .fetch();
    }

    // 상품 목록 조회 (구매순)
    // 정렬: 오픈판매상품 > 구매순
    @Override
    public List<ProductListResponseDTO> getProductListOrderByPurchase() {
        QProduct product = QProduct.product;
        QPurchaseProd purchaseProd = QPurchaseProd.purchaseProd;
        return query
                .select(Projections.bean(ProductListResponseDTO.class, product.prodKey, product.prodName,
                        product.prodPrice, product.prodCount, product.prodDate, product.prodOpen,
                        ExpressionUtils.as(
                                JPAExpressions.select(purchaseProd.purProdKey.count())
                                        .from(purchaseProd)
                                        .where(purchaseProd.product.prodKey.eq(product.prodKey))
                                , "orderTotal"
                        )))
                .from(product)
                .orderBy(product.prodOpen.desc(), Expressions.numberPath(Integer.class, "orderTotal").desc())
                .fetch();

    }

    // 상품 상세 조회
    @Override
    public Optional<ProductResponseDTO> getProductDetail(Long prodKey) {
        QProduct product = QProduct.product;
        QPurchaseProd purchaseProd = QPurchaseProd.purchaseProd;
        QOpenTime openTime = QOpenTime.openTime;
        return Optional.ofNullable(query
                .select(Projections.bean(ProductResponseDTO.class, product.prodKey, product.prodName, product.prodContent,
                        product.prodPrice, product.prodCount, product.prodDate, product.prodOpen, openTime.salesTime,
                        ExpressionUtils.as(
                                JPAExpressions.select(purchaseProd.purProdKey.count())
                                        .from(purchaseProd)
                                        .where(purchaseProd.product.prodKey.eq(product.prodKey))
                                , "orderTotal"
                        )))
                .from(product)
                .leftJoin(openTime).on(openTime.product.prodKey.eq(product.prodKey))
                .where(product.prodKey.eq(prodKey))
                .fetchOne());
    }

    // 구매 목록 조회 (최신순)
    @Override
    public List<PurchaseListResponseDTO> getPurchaseList(Long userKey) {
        QPurchase purchase = QPurchase.purchase;
        return query
                .select(Projections.bean(PurchaseListResponseDTO.class, purchase.purKey, purchase.purPrice,
                        purchase.purCount, purchase.purDate, purchase.purState))
                .from(purchase)
                .where(purchase.userKey.eq(userKey))
                .orderBy(purchase.purDate.desc())
                .fetch();
    }

    // 상세 구매 내역 조회
    @Override
    public Optional<PurchaseResponseDTO> getPurchaseDetail(Long purKey) {
        QPurchase purchase = QPurchase.purchase;
        QPurchaseProd purchaseProd = QPurchaseProd.purchaseProd;
        QProduct product = QProduct.product;
        Optional<PurchaseResponseDTO> purchaseResponseDTO = Optional.ofNullable(query
                .select(Projections.bean(PurchaseResponseDTO.class, purchase.purKey, purchase.purPrice,
                        purchase.purDate, purchase.purName, purchase.purAddress, purchase.purTel, purchase.purState))
                .from(purchase)
                .where(purchase.purKey.eq(purKey))
                .fetchOne());
        List<PurchaseProdResponseDTO> purchaseProdResponseDTO = query
                .select(Projections.bean(PurchaseProdResponseDTO.class, purchaseProd.purProdKey, product.prodKey,
                        product.prodName, product.prodPrice, purchaseProd.purProdPrice, purchaseProd.purProdCount))
                .from(purchaseProd)
                .leftJoin(product).on(purchaseProd.product.prodKey.eq(product.prodKey))
                .where(purchaseProd.purchase.purKey.eq(purKey))
                .fetch();
        purchaseResponseDTO.ifPresent(dto -> {
            dto.setPurchaseProd(purchaseProdResponseDTO);
            // 다른 로직 수행
        });
        return purchaseResponseDTO;
    }

    // 구매 -> 재고 감소
    @Override
    public void subtractProdCount(Long prodKey, int purProdCount) {
        QProduct product = QProduct.product;
        query.update(product)
                .set(product.prodCount, product.prodCount.subtract(purProdCount))
                .where(product.prodKey.eq(prodKey))
                .execute();
    }

    // 구매 중 취소 및 실패 -> 재고 증가
    @Override
    public void addProdCount(Long prodKey, int purProdCount) {
        QProduct product = QProduct.product;
        query.update(product)
                .set(product.prodCount, product.prodCount.add(purProdCount))
                .where(product.prodKey.eq(prodKey))
                .execute();
    }

    // 장바구니 조회
    @Override
    public List<CartListResponseDTO> getCartList(Long userKey) {
        QCart cart = QCart.cart;
        QProduct product = QProduct.product;
        return query
                .select(Projections.bean(CartListResponseDTO.class, cart.cartKey, product.prodKey,
                        product.prodName, product.prodPrice, product.prodCount, cart.cartCount))
                .from(cart)
                .leftJoin(product).on(cart.product.prodKey.eq(product.prodKey))
                .where(cart.userKey.eq(userKey))
                .fetch();
    }
}
