package com.fastcampus.board.model.comment;

import com.fastcampus.board.model.entity.CommentEntity;
import com.fastcampus.board.model.post.Post;
import com.fastcampus.board.model.user.User;

import java.time.ZonedDateTime;

public record Comment(
        Long CommentId,
        String body,
        User user,
        Post post,
        ZonedDateTime createdDateTime,
        ZonedDateTime updatedDateTime,
        ZonedDateTime deletedDateTime) {

    public static Comment from(CommentEntity commentEntity) {
        return new Comment(
                commentEntity.getCommentId(),
                commentEntity.getBody(),
                User.from(commentEntity.getUser()),
                Post.from(commentEntity.getPost()),
                commentEntity.getCreatedDateTime(),
                commentEntity.getUpdatedDateTime(),
                commentEntity.getDeletedDateTime()
        );
    }
}
