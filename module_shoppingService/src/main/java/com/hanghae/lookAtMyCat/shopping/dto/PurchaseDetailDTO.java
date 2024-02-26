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
public class PurchaseDetailDTO {

    @NotEmpty
    private List<PurchaseProductDTO> purchaseProduct;

    @NotEmpty
    private String purName;

    @NotEmpty
    private String purAddress;

    @NotEmpty
    private String purTel;

    // 입력 json
//    {
//        "purchaseProduct": [
//            {
//                "prodKey": 1,
//                "purProdCount": 3
//            },
//            {
//                "prodKey": 2,
//                "purProdCount": 1
//            }
//        ],
//        "purName": "주문자이름",
//        "purAddress": "주문배송지",
//        "purTel": "주문자 전화번호"
//    }

}
