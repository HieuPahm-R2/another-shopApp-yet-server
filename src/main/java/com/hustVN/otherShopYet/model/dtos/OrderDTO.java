package com.hustVN.otherShopYet.model.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class OrderDTO {
    @JsonProperty("user_id")
    @Min(value = 1, message = "user's id must be > 0")
    private Long userId;

    @JsonProperty("fullname")
    private String fullName;
    private String email;

    @JsonProperty("phone_number")
    @NotBlank(message = "phone number not be blank")
    private String phoneNumber;

    private String address;

    private String note;
    @JsonProperty("total_money")
    @Min(value = 0, message = "must be greater than 0")
    private Float totalMoney;
    @JsonProperty("shipping_method")
    private String shippingMethod;

    @JsonProperty("shipping_address")
    private String ShippingAddress;

    @JsonProperty("payment_method")
    private String paymentMethod;
}
