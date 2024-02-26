package com.hanghae.lookAtMyCat.shopping.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ListTypeDTO {

    // "최신순", "구매순"
    private String listType;

}
