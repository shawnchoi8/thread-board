package com.fastcampus.board.service;

import com.fastcampus.board.exception.reply.ReplyNotFoundException;
import com.fastcampus.board.exception.post.PostNotFoundException;
import com.fastcampus.board.exception.user.UserNotAllowedException;
import com.fastcampus.board.model.reply.Reply;
import com.fastcampus.board.model.reply.CreateReplyRequestBody;
import com.fastcampus.board.model.reply.UpdateReplyRequestBody;
import com.fastcampus.board.model.entity.ReplyEntity;
import com.fastcampus.board.model.entity.PostEntity;
import com.fastcampus.board.model.entity.UserEntity;
import com.fastcampus.board.repository.ReplyEntityRepository;
import com.fastcampus.board.repository.PostEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ReplyService {

    private final ReplyEntityRepository replyEntityRepository;

    private final PostEntityRepository postEntityRepository;

    public List<Reply> getRepliesByPostId(Long postId) {
        PostEntity postEntity = postEntityRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        List<ReplyEntity> replyEntities = replyEntityRepository.findByPost(postEntity);
        return replyEntities.stream().map(Reply::from).toList();
    }

    @Transactional
    public Reply createReply(Long postId, CreateReplyRequestBody requestBody, UserEntity currentUser) {
        PostEntity postEntity = postEntityRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        ReplyEntity replyEntity = replyEntityRepository.save(ReplyEntity.of(requestBody.body(), currentUser, postEntity));
        postEntity.setReplyCount(postEntity.getReplyCount() + 1); //댓글 생성 시 댓글 숫자 카운트 +1
        return Reply.from(replyEntity);
    }

    public Reply updateReply(Long postId, Long replyId, UpdateReplyRequestBody updateReplyRequest, UserEntity currentUser) {
        PostEntity postEntity = postEntityRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        ReplyEntity replyEntity = replyEntityRepository.findById(replyId)
                .orElseThrow(() -> new ReplyNotFoundException(replyId));

        if (!replyEntity.getUser().equals(currentUser)) { // TODO: Owner of the post is able to delete the reply...
            throw new UserNotAllowedException();
        }

        replyEntity.setBody(updateReplyRequest.body());
        replyEntityRepository.save(replyEntity); // Todo : 이거 save 꼭 해줘야하나? 그냥 update 하면 dirty checking 안되나
        return Reply.from(replyEntity);
    }

    @Transactional
    public void deleteReply(Long postId, Long replyId, UserEntity currentUser) {
        PostEntity postEntity = postEntityRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        ReplyEntity replyEntity = replyEntityRepository.findById(replyId)
                .orElseThrow(() -> new ReplyNotFoundException(replyId));

        if (!replyEntity.getUser().equals(currentUser)) {
            throw new UserNotAllowedException();
        }

        replyEntityRepository.delete(replyEntity);
        postEntity.setReplyCount(Math.max(0, postEntity.getReplyCount() - 1)); //댓글 삭제시 댓글 카운트 -1
        postEntityRepository.save(postEntity); //TODO: 이거 해줘야하나? save 안해도 자동으로 되는거 아닌가
    }
}
