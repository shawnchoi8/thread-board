package com.fastcampus.board.model.post;

import com.fastcampus.board.model.entity.PostEntity;
import com.fastcampus.board.model.user.User;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.ZonedDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record Post(
        Long postId,
        String body,
        Long replyCount,
        Long likeCount,
        User user,
        Boolean isLiking,
        ZonedDateTime createdDateTime,
        ZonedDateTime updatedDateTime,
        ZonedDateTime deletedDateTime) {

    public static Post from(PostEntity postEntity) {
        return new Post(
                postEntity.getPostId(),
                postEntity.getBody(),
                postEntity.getReplyCount(),
                postEntity.getLikeCount(),
                User.from(postEntity.getUser()),
                null,
                postEntity.getCreatedDateTime(),
                postEntity.getUpdatedDateTime(),
                postEntity.getDeletedDateTime()
        );
    }

    public static Post from(PostEntity postEntity, Boolean isLiking) {
        return new Post(
                postEntity.getPostId(),
                postEntity.getBody(),
                postEntity.getReplyCount(),
                postEntity.getLikeCount(),
                User.from(postEntity.getUser()),
                isLiking,
                postEntity.getCreatedDateTime(),
                postEntity.getUpdatedDateTime(),
                postEntity.getDeletedDateTime()
        );
    }
}
