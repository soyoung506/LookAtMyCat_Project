package com.hanghae.lookAtMyCat.shopping.entity;

import jakarta.persistence.*;
import lombok.*;

@ToString
@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@Entity
public class PurchaseProd {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long purProdKey;

    // orderKey와 join
    @ManyToOne
    @JoinColumn(nullable = false, name = "purKey")
    private Purchase purchase;

    // proKey와 join
    @ManyToOne
    @JoinColumn(nullable = false, name = "prodKey")
    private Product product;

    @Column(nullable = false)
    private int purProdPrice;

    @Column(nullable = false)
    private int purProdCount;

    @Builder
    public PurchaseProd(Purchase purchase, Product product, int purProdPrice, int purProdCount) {
        this.purchase = purchase;
        this.product = product;
        this.purProdPrice = purProdPrice;
        this.purProdCount = purProdCount;
    }

}
