package com.fastcampus.board.model.user;

import com.fastcampus.board.model.entity.UserEntity;

import java.time.ZonedDateTime;

public record User(
        Long userId,
        String username,
        String profile,
        String description,
        Long followerCount,
        Long followingCount,
        ZonedDateTime createdDateTime,
        ZonedDateTime updatedDateTime) {

    public static User from(UserEntity userEntity) {
        return new User(
                userEntity.getUserId(),
                userEntity.getUsername(),
                userEntity.getProfile(),
                userEntity.getDescription(),
                userEntity.getFollowerCount(),
                userEntity.getFollowingCount(),
                userEntity.getCreatedDateTime(),
                userEntity.getUpdatedDateTime());
    }
}
