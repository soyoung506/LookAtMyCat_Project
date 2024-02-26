package com.hanghae.lookAtMyCat.shopping.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ProductListResponseDTO {

    @NotEmpty
    private Long prodKey;

    @NotEmpty
    private String prodName;

    @NotEmpty
    private int prodPrice;

    @NotEmpty
    private int prodCount;

    @NotEmpty
    private LocalDateTime prodDate;

    @NotEmpty
    private int prodOpen;

    // PurchaseProd 테이블에서 해당 상품의 참조수를 리턴하여 상품구매수를 조회
    @NotEmpty
    private Long orderTotal;

}
