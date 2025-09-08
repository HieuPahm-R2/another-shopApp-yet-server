package com.hustVN.otherShopYet.response;

import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ProductListResponse {
    private List<ProductResponse> products;
    private int totalPages;
}
