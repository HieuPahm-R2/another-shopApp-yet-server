package com.hustVN.otherShopYet.domain.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class OrderDetailDTO {
    @JsonProperty("order_id")
    @Min(value = 1, message = "must be > 1")
    private Long orderId;
    @JsonProperty("product_id")
    @Min(value = 1, message = "must be > 0")
    private Long productId;
    @Min(value = 0, message = "must be greater than or equals 0")
    private Float price;
    @JsonProperty("number_of_products")
    private int numberOfProducts;

    @JsonProperty("total_money")
    private int totalMoney;
}
