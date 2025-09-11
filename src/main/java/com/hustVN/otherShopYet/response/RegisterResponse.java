package com.hustVN.otherShopYet.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hustVN.otherShopYet.model.entity.User;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterResponse {
    @JsonProperty("message")
    private String message;

    @JsonProperty("user")
    private User user;
}
