package com.hustVN.otherShopYet.model.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ProductDTO {
    @NotBlank(message = "name must not be empty")
    @Size(min = 3, max = 255, message = "name must be between 3 and 255 characters")
    private String name;

    private String description;
    @NotBlank(message = "price must not be empty")
    @Min(value = 0, message = "price must be greater than or equal to 0")
    @Max(value = 999999999, message = "price must be less than or equal to 999,999,999 VND")
    private Float price;
    private String thumbnail;

    @JsonProperty("category_id")
    private long categoryId;
}
