package com.hustVN.otherShopYet.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hustVN.otherShopYet.model.entity.OrderDetail;
import jakarta.validation.constraints.Min;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDetailResponse {
    private Long id;
    @JsonProperty("order_id")
    private Long orderId;

    @JsonProperty("product_id")
    private Long productId;
    @JsonProperty("price")
    private Float price;
    @JsonProperty("number_of_products")
    private int numberOfProducts;
    @JsonProperty("total_money")
    private Float totalMoney;

    public static OrderDetailResponse from(OrderDetail item){
        return OrderDetailResponse.builder()
                .id(item.getId())
                .orderId(item.getOrder().getId())
                .productId(item.getProduct().getId())
                .price(item.getPrice())
                .numberOfProducts(item.getNumberOfProducts())
                .totalMoney(item.getTotalMoney())
                .build();
    }
}
