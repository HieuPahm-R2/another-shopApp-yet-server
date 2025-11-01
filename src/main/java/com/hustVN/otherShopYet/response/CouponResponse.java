package com.hustVN.otherShopYet.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CouponResponse {
    @JsonProperty("result")
    private Double result;

    // errorCode ?
    @JsonProperty("errorMessage")
    private String errorMessage;
}
