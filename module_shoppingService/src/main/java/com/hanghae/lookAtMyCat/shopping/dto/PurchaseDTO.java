package com.hanghae.lookAtMyCat.shopping.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class PurchaseDTO {

    @NotEmpty
    private List<PurchaseProductDTO> purchaseProduct;

    // 입력 json
//    {
//        "purchaseProduct": [
//        {
//            "prodKey": 1,
//            "purProdCount": 3
//        },
//        {
//            "prodKey": 2,
//            "purProdCount": 1
//        }
//  ]
//    }

}
