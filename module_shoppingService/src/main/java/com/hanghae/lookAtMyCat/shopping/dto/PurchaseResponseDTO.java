package com.hanghae.lookAtMyCat.shopping.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class PurchaseResponseDTO {

    @NotEmpty
    private Long purKey;

    @NotEmpty
    private int purPrice;

    @NotEmpty
    private LocalDateTime purDate;

    @NotEmpty
    private String purName;

    @NotEmpty
    private String purAddress;

    @NotEmpty
    private String purTel;

    @NotEmpty
    private String purState;

    @NotEmpty
    private List<PurchaseProdResponseDTO> purchaseProd;

}
