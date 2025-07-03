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
@SQLDelete(sql = "UPDATE \"comment\" SET deleteddatetime = CURRENT_TIMESTAMP WHERE commentid = ?")
@SQLRestriction("deleteddatetime IS NULL")
@Table(name = "comment")
@Entity
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

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

    public static CommentEntity of(String body, UserEntity user, PostEntity post) {
        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setBody(body);
        commentEntity.setUser(user);
        commentEntity.setPost(post);
        return commentEntity;
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
