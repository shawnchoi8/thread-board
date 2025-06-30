package com.fastcampus.board.model.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.ZonedDateTime;

@Getter
@Setter // TODO: delete setter later
@EqualsAndHashCode
@SQLDelete(sql = "UPDATE \"post\" SET deleteddatetime = CURRENT_TIMESTAMP WHERE postid = ?") // deletedDateTime을 현재 시각으로 update 해줌
@SQLRestriction("deleteddatetime IS NULL") // 조회할 때 deleteDatetime이 null인 게시물만 보여줘 (삭제된적이 없는)
@Table(name = "post", indexes = @Index(name = "post_userid_idx", columnList = "userid"))
@Entity
public class PostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //key 생성 전략 : identity
    private Long postId;

    @Column(columnDefinition = "TEXT")
    private String body;

    @ManyToOne
    @JoinColumn(name = "userid")
    private UserEntity user;

    @Column
    private ZonedDateTime createdDateTime;

    @Column
    private ZonedDateTime updatedDateTime;

    @Column
    private ZonedDateTime deletedDateTime; //soft delete

    @PrePersist
    private void prePersist() {
        this.createdDateTime = ZonedDateTime.now();
        this.updatedDateTime = this.createdDateTime;
    }

    @PreUpdate
    private void preUpdate() {
        this.updatedDateTime = ZonedDateTime.now();
    }
}
