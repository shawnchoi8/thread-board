package com.fastcampus.board.model;

import lombok.*;

import java.time.ZonedDateTime;
import java.util.Objects;

@Getter
@Setter
@ToString //TODO: 나중에 getter setter constructor 관련 annotation 정리할 예정
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    private Long postId;
    private String body;
    private ZonedDateTime createdDateTime;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Post post)) return false;
        return Objects.equals(getPostId(), post.getPostId()) && Objects.equals(getBody(), post.getBody()) && Objects.equals(getCreatedDateTime(), post.getCreatedDateTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPostId(), getBody(), getCreatedDateTime());
    }
}
