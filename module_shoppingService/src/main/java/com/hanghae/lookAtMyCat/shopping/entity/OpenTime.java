package com.hanghae.lookAtMyCat.shopping.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@Entity
public class OpenTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long salesKey;

    // 오픈판매 상품키
    @OneToOne
    @JoinColumn(nullable = false, name = "prodKey")
    private Product product;

    @Column(nullable = false)
    private LocalDateTime salesTime;

}
