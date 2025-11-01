package com.hustVN.otherShopYet.service.implement;

import com.hustVN.otherShopYet.exception.DataNotFoundException;
import com.hustVN.otherShopYet.model.dtos.CommentDTO;
import com.hustVN.otherShopYet.model.entity.Comment;
import com.hustVN.otherShopYet.model.entity.Product;
import com.hustVN.otherShopYet.model.entity.User;
import com.hustVN.otherShopYet.repo.CommentRepository;
import com.hustVN.otherShopYet.repo.ProductRepository;
import com.hustVN.otherShopYet.repo.UserRepository;
import com.hustVN.otherShopYet.response.CommentResponse;
import com.hustVN.otherShopYet.service.ICommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService implements ICommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Override
    public Comment insertComment(CommentDTO dto) {
        User user = userRepository.findById(dto.getUserId()).orElseThrow(
                () -> new IllegalArgumentException("User or product not found")
        );
        Product product = productRepository.findById(dto.getProductId()).orElseThrow(
                () -> new IllegalArgumentException("User or product not found")
        );
        Comment newComment = Comment.builder()
                .user(user)
                .product(product)
                .content(dto.getContent())
                .build();
        return commentRepository.save(newComment);
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    @Override
    public void updateComment(Long id, CommentDTO commentDTO) throws DataNotFoundException {
        Comment existComment = commentRepository.findById(id).orElseThrow(
                () -> new DataNotFoundException("Comment not found")
        );
        existComment.setContent(commentDTO.getContent());
        commentRepository.save(existComment);
    }

    @Override
    public List<CommentResponse> getCommentsByUserAndProduct(Long userId, Long productId) {
        List<Comment> comments = commentRepository.findByUserIdAndProductId(userId, productId);
        return comments.stream()
                .map(CommentResponse::from).collect(Collectors.toList());
    }

    @Override
    public List<CommentResponse> getCommentsByProduct(Long productId) {
        List<Comment> comments = commentRepository.findByProductId(productId);
        return comments.stream().map(CommentResponse::from).collect(Collectors.toList());
    }
}
