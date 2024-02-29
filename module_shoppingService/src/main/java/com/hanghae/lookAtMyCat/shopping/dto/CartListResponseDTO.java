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
public class CartListResponseDTO {

    @NotEmpty
    private Long cartKey;

    @NotEmpty
    private Long prodKey;

    @NotEmpty
    private String prodName;

    @NotEmpty
    private int prodPrice;

    // 상품 재고
    @NotEmpty
    private int prodCount;

    // 구매 수량
    @NotEmpty
    private int cartCount;

}
