package com.fastcampus.board.model.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.ZonedDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Table(name = "\"like\"", indexes = @Index(name = "like_userid_postid_idx", columnList = "userid, postid", unique = true))
@Entity
public class LikeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long likeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postid")
    private PostEntity post;

    @Column
    private ZonedDateTime createdDateTime;

    public static LikeEntity of(UserEntity user, PostEntity post) {
        LikeEntity likeEntity = new LikeEntity();
        likeEntity.setUser(user);
        likeEntity.setPost(post);
        return likeEntity;
    }

    @PrePersist
    public void prePersist() {
        this.createdDateTime = ZonedDateTime.now();
    }

}
