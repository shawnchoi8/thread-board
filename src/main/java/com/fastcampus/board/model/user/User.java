package com.fastcampus.board.model.user;

import com.fastcampus.board.model.entity.UserEntity;

import java.time.ZonedDateTime;

public record User(
        Long userId,
        String username,
        String profile,
        String description,
        Long followersCount,
        Long followingsCount,
        Boolean isFollowing,
        ZonedDateTime createdDateTime,
        ZonedDateTime updatedDateTime) {

    public static User from(UserEntity userEntity) {
        return new User(
                userEntity.getUserId(),
                userEntity.getUsername(),
                userEntity.getProfile(),
                userEntity.getDescription(),
                userEntity.getFollowersCount(),
                userEntity.getFollowingsCount(),
                null,
                userEntity.getCreatedDateTime(),
                userEntity.getUpdatedDateTime());
    }

    public static User from(UserEntity userEntity, Boolean isFollowing) {
        return new User(
                userEntity.getUserId(),
                userEntity.getUsername(),
                userEntity.getProfile(),
                userEntity.getDescription(),
                userEntity.getFollowersCount(),
                userEntity.getFollowingsCount(),
                isFollowing,
                userEntity.getCreatedDateTime(),
                userEntity.getUpdatedDateTime());
    }
}
