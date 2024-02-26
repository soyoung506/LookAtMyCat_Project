package com.hanghae.lookAtMyCat.shopping.entity;

import jakarta.persistence.*;
import lombok.*;

@ToString
@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@Entity
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartKey;

    // userKey와 join
    @Column(nullable = false)
    private Long userKey;

    @Column(nullable = false, length = 30)
    private String userNick;

    // proKey와 join
    @ManyToOne
    @JoinColumn(nullable = false, name = "prodKey")
    private Product product;

    @Column(nullable = false)
    private int cartCount;

    @Builder
    public Cart(Long userKey, String userNick, Product product, int cartCount) {
        this.userKey = userKey;
        this.userNick = userNick;
        this.product = product;
        this.cartCount = cartCount;
    }

}
