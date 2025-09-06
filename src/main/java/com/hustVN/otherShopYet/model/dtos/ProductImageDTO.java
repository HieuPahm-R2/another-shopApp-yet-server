package com.hustVN.otherShopYet.model.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ProductImageDTO {
    @JsonProperty("product_id")
    @Min(value = 1, message = "Id must be > 0")
    private Long productId;

    @Size(min = 10, max = 255, message = "too short")
    @JsonProperty("image_url")
    private String imageUrl;
}
