package com.fastcampus.board.model.reply;

import com.fastcampus.board.model.entity.ReplyEntity;
import com.fastcampus.board.model.post.Post;
import com.fastcampus.board.model.user.User;

import java.time.ZonedDateTime;

public record Reply(
        Long replyId,
        String body,
        User user,
        Post post,
        ZonedDateTime createdDateTime,
        ZonedDateTime updatedDateTime,
        ZonedDateTime deletedDateTime) {

    public static Reply from(ReplyEntity replyEntity) {
        return new Reply(
                replyEntity.getReplyId(),
                replyEntity.getBody(),
                User.from(replyEntity.getUser()),
                Post.from(replyEntity.getPost()),
                replyEntity.getCreatedDateTime(),
                replyEntity.getUpdatedDateTime(),
                replyEntity.getDeletedDateTime()
        );
    }
}
