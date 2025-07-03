package com.fastcampus.board.controller;

import com.fastcampus.board.model.comment.Comment;
import com.fastcampus.board.model.comment.CreateCommentRequestBody;
import com.fastcampus.board.model.comment.UpdateCommentRequestBody;
import com.fastcampus.board.model.entity.UserEntity;
import com.fastcampus.board.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/posts/{postId}/comments")
public class CommentController {

    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<List<Comment>> getComments(@PathVariable Long postId) {
        List<Comment> comments = commentService.getCommentsByPostId(postId);
        return ResponseEntity.ok(comments);
    }

    @PostMapping
    public ResponseEntity<Comment> createComment(
            @PathVariable Long postId,
            @RequestBody CreateCommentRequestBody requestBody,
            Authentication authentication) {

        Comment comment = commentService.createComment(postId, requestBody, (UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(comment);
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<Comment> updateComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @RequestBody UpdateCommentRequestBody updateCommentRequest,
            Authentication authentication) {

        Comment comment = commentService
                .updateComment(postId, commentId, updateCommentRequest, (UserEntity) authentication.getPrincipal());

        return ResponseEntity.ok(comment);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            Authentication authentication) {

        commentService.deleteComment(postId, commentId, (UserEntity) authentication.getPrincipal());

        return ResponseEntity.noContent().build();
    }
}
