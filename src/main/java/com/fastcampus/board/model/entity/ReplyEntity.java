package com.fastcampus.board.model.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.ZonedDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@SQLDelete(sql = "UPDATE \"reply\" SET deleteddatetime = CURRENT_TIMESTAMP WHERE replyid = ?")
@SQLRestriction("deleteddatetime IS NULL")
@Table(name = "reply",
        indexes = {@Index(name = "reply_userid_idx", columnList = "userid"), @Index(name = "reply_postid_idx", columnList = "postid")})
@Entity
public class ReplyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long replyId;

    @Column(columnDefinition = "TEXT")
    private String body;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postid")
    private PostEntity post;

    @Column
    private ZonedDateTime createdDateTime;

    @Column
    private ZonedDateTime updatedDateTime;

    @Column
    private ZonedDateTime deletedDateTime;

    public static ReplyEntity of(String body, UserEntity user, PostEntity post) {
        ReplyEntity replyEntity = new ReplyEntity();
        replyEntity.setBody(body);
        replyEntity.setUser(user);
        replyEntity.setPost(post);
        return replyEntity;
    }

    @PrePersist
    public void prePersist() {
        this.createdDateTime = ZonedDateTime.now();
        this.updatedDateTime = ZonedDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedDateTime = ZonedDateTime.now();
    }
}
