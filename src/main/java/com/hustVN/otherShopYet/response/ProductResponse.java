package com.hustVN.otherShopYet.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hustVN.otherShopYet.model.entity.Category;
import com.hustVN.otherShopYet.model.entity.Product;
import com.hustVN.otherShopYet.model.entity.ProductImage;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ProductResponse extends BaseResponse {
    private Long id;
    private String name;
    private Double price;
    private String description;
    private String thumbnail;
    private int totalPages;
    @JsonProperty("product_images")
    private List<ProductImage> productImages = new ArrayList<>();

    @JsonProperty("category_id")
    private Long categoryId;

    public static ProductResponse from(Product item){
        ProductResponse productRes = ProductResponse.builder()
                .id(item.getId())
                .name(item.getName())
                .price(item.getPrice())
                .thumbnail(item.getThumbnail())
                .description(item.getDescription())
                .categoryId(item.getCategory().getId())
                .productImages(item.getProductImages())
                .build();
        productRes.setCreatedAt(item.getCreatedAt());
        productRes.setUpdatedAt(item.getUpdatedAt());
        return productRes;
    }

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryDetail{
        private Long id;
        private String name;
    }
}
