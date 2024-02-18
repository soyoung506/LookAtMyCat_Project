package com.hanghae.lookAtMyCat.activity.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class FollowDTO {

    @NotEmpty
    private Long toUserKey;
}
