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
public class CartDTO {

    @NotEmpty
    private Long cartKey;

    // 구매 수량
    @NotEmpty
    private int cartCount;

}
