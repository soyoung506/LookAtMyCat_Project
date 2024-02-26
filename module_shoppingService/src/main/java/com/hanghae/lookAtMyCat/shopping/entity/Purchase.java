package com.hanghae.lookAtMyCat.shopping.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@ToString
@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@Entity
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long purKey;

    // userKey와 join
    @Column(nullable = false)
    private Long userKey;

    // 총결제금액
    @Column(nullable = false)
    private int purPrice;

    // 총상품개수
    @Column(nullable = false)
    private int purCount;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime purDate;

    @Column(nullable = false, length = 15)
    private String purName;

    @Column(nullable = false, length = 300)
    private String purAddress;

    @Column(nullable = false, length = 15)
    private String purTel;

    // 결제상태: 결제완료, 결제취소, 결제실패
    @Column(nullable = false, length = 20)
    private String purState;

    // 해당 주문 내역으로 상품들을 조회하기 위해 양방향 참조
    @OneToMany(mappedBy = "purchase", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PurchaseProd> purchaseProd;

    @Builder
    public Purchase(Long userKey, int purPrice, int purCount, String purName, String purAddress, String purTel, String purState) {
        this.userKey = userKey;
        this.purPrice = purPrice;
        this.purCount = purCount;
        this.purName = purName;
        this.purAddress = purAddress;
        this.purTel = purTel;
        this. purState = purState;
    }

}
