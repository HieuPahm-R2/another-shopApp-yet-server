package com.hustVN.otherShopYet.response;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@MappedSuperclass
public class BaseResponse {

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
