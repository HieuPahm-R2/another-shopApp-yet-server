package com.hustVN.otherShopYet.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hustVN.otherShopYet.model.entity.Comment;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentResponse {
    private String content;

    @JsonProperty("user")
    private UserResponse userRes;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    public static CommentResponse from(Comment comment) {
        return CommentResponse.builder()
                .content(comment.getContent())
                .userRes(UserResponse.from(comment.getUser()))
                .updatedAt(comment.getUpdatedAt())
                .build();
    }
}
