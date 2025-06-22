package com.fastcampus.board.model;

import com.fastcampus.board.model.entity.PostEntity;

import java.time.ZonedDateTime;

public record Post(Long postId,
                   String body,
                   ZonedDateTime createdDateTime,
                   ZonedDateTime updatedDateTime,
                   ZonedDateTime deletedDateTime) {

    public static Post from(PostEntity postEntity) {
        return new Post(
                postEntity.getPostId(),
                postEntity.getBody(),
                postEntity.getCreatedDateTime(),
                postEntity.getUpdatedDateTime(),
                postEntity.getDeletedDateTime()
        );
    }
}
