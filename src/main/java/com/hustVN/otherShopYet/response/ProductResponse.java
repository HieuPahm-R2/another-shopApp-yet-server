package com.hustVN.otherShopYet.response;

import com.hustVN.otherShopYet.model.entity.Category;
import com.hustVN.otherShopYet.model.entity.Product;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ProductResponse extends BaseResponse {

    private String name;
    private Double price;
    private String description;
    private String thumbnail;

    private Long categoryId;

    public static ProductResponse from(Product item){
        ProductResponse productRes = ProductResponse.builder()
                .name(item.getName())
                .price(item.getPrice())
                .thumbnail(item.getThumbnail())
                .description(item.getDescription())
                .categoryId(item.getCategory().getId())
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
