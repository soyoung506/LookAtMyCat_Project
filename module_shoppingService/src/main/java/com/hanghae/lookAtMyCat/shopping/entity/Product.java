package com.hanghae.lookAtMyCat.shopping.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@ToString
@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long prodKey;

    @Column(nullable = false, length = 150)
    private String prodName;

    @Column(nullable = false, length = 1000)
    private String prodContent;

    @Column(nullable = false)
    private int prodPrice;

    // 실시간 재고 수량은 redis
    @Column(nullable = false)
    private int prodCount;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime prodDate;

    // 일반판매: 0, 오픈판매: 1
    @Column(nullable = false)
    private int prodOpen;

    // 현재 주문건수 확인을 위해 양방향 참조
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PurchaseProd> purchaseProd;

    // 오픈판매 상품일 경우 판매 시간을 확인하기 위해 양방향 참조
    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private OpenTime openTime;

}
