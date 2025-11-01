package com.hustVN.otherShopYet.service;

import com.hustVN.otherShopYet.exception.DataNotFoundException;
import com.hustVN.otherShopYet.model.dtos.CommentDTO;
import com.hustVN.otherShopYet.model.entity.Comment;
import com.hustVN.otherShopYet.response.CommentResponse;

import java.util.List;

public interface ICommentService {
    Comment insertComment(CommentDTO comment);

    void deleteComment(Long commentId);

    void updateComment(Long id, CommentDTO commentDTO) throws DataNotFoundException;

    List<CommentResponse> getCommentsByUserAndProduct(Long userId, Long productId);

    List<CommentResponse> getCommentsByProduct(Long productId);

    interface ICouponService {
    }
}
