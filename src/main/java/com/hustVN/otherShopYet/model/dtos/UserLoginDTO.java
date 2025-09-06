package com.hustVN.otherShopYet.model.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserLoginDTO {
    @JsonProperty("phone_number")
    @NotBlank(message = "phone number not be blank")
    private String phoneNumber;

    @NotBlank(message = "Password not can be blank")
    private String password;
}
