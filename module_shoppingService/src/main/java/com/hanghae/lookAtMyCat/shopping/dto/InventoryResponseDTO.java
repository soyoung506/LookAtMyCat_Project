package com.hanghae.lookAtMyCat.shopping.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class InventoryResponseDTO {

    private Long prodKey;

    private Long stock;

    @Builder
    private InventoryResponseDTO(Long prodKey, Long stock) {
        this.prodKey = prodKey;
        this.stock = stock;
    }

}
