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
public class PurchaseListResponseDTO {

    @NotEmpty
    private Long purKey;

    @NotEmpty
    private int purPrice;   
    
    // 구매 상품이 2개 이상이면, "-외 N개" 표시
    @NotEmpty
    private int purCount;

    @NotEmpty
    private LocalDateTime purDate;

    @NotEmpty
    private String purState;

}
