package com.fastcampus.board.controller;

import com.fastcampus.board.model.reply.Reply;
import com.fastcampus.board.model.reply.CreateReplyRequestBody;
import com.fastcampus.board.model.reply.UpdateReplyRequestBody;
import com.fastcampus.board.model.entity.UserEntity;
import com.fastcampus.board.service.ReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/posts/{postId}/replies")
public class ReplyController {

    private final ReplyService replyService;

    @GetMapping
    public ResponseEntity<List<Reply>> getReplies(@PathVariable Long postId) {
        List<Reply> replies = replyService.getRepliesByPostId(postId);
        return ResponseEntity.ok(replies);
    }

    @PostMapping
    public ResponseEntity<Reply> createReply(
            @PathVariable Long postId,
            @RequestBody CreateReplyRequestBody requestBody,
            Authentication authentication) {

        Reply reply = replyService.createReply(postId, requestBody, (UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(reply);
    }

    @PatchMapping("/{replyId}")
    public ResponseEntity<Reply> updateReply(
            @PathVariable Long postId,
            @PathVariable Long replyId,
            @RequestBody UpdateReplyRequestBody updateReplyRequest,
            Authentication authentication) {

        Reply reply = replyService
                .updateReply(postId, replyId, updateReplyRequest, (UserEntity) authentication.getPrincipal());

        return ResponseEntity.ok(reply);
    }

    @DeleteMapping("/{replyId}")
    public ResponseEntity<Void> deleteReply(
            @PathVariable Long postId,
            @PathVariable Long replyId,
            Authentication authentication) {

        replyService.deleteReply(postId, replyId, (UserEntity) authentication.getPrincipal());

        return ResponseEntity.noContent().build();
    }
}
