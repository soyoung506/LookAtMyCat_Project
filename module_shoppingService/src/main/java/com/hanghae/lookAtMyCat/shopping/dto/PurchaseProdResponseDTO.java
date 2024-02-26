package com.hanghae.lookAtMyCat.shopping.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class PurchaseProdResponseDTO {

    @NotEmpty
    private Long purProdKey;

    @NotEmpty
    private Long prodKey;

    @NotEmpty
    private String prodName;

    // 상품 가격
    @NotEmpty
    private int prodPrice;

    // 구매 개수에 따른 가격
    @NotEmpty
    private int purProdPrice;

    @NotEmpty
    private int purProdCount;

}
