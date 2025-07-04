package com.fastcampus.board.model.post;

import com.fastcampus.board.model.entity.PostEntity;
import com.fastcampus.board.model.user.User;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.ZonedDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record Post(Long postId,
                   String body,
                   Long commentCount,
                   User user,
                   ZonedDateTime createdDateTime,
                   ZonedDateTime updatedDateTime,
                   ZonedDateTime deletedDateTime) {

    public static Post from(PostEntity postEntity) {
        return new Post(
                postEntity.getPostId(),
                postEntity.getBody(),
                postEntity.getCommentCount(),
                User.from(postEntity.getUser()),
                postEntity.getCreatedDateTime(),
                postEntity.getUpdatedDateTime(),
                postEntity.getDeletedDateTime()
        );
    }
}
