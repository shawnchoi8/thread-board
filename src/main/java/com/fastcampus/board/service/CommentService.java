package com.fastcampus.board.service;

import com.fastcampus.board.exception.comment.CommentNotFoundException;
import com.fastcampus.board.exception.post.PostNotFoundException;
import com.fastcampus.board.exception.user.UserNotAllowedException;
import com.fastcampus.board.model.comment.Comment;
import com.fastcampus.board.model.comment.CreateCommentRequestBody;
import com.fastcampus.board.model.comment.UpdateCommentRequestBody;
import com.fastcampus.board.model.entity.CommentEntity;
import com.fastcampus.board.model.entity.PostEntity;
import com.fastcampus.board.model.entity.UserEntity;
import com.fastcampus.board.repository.CommentEntityRepository;
import com.fastcampus.board.repository.PostEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentEntityRepository commentEntityRepository;

    private final PostEntityRepository postEntityRepository;

    public List<Comment> getCommentsByPostId(Long postId) {
        PostEntity postEntity = postEntityRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        List<CommentEntity> comments = commentEntityRepository.findByPost(postEntity);
        return comments.stream().map(Comment::from).toList();
    }

    @Transactional
    public Comment createComment(Long postId, CreateCommentRequestBody requestBody, UserEntity currentUser) {
        PostEntity postEntity = postEntityRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        CommentEntity commentEntity = CommentEntity.of(requestBody.body(), currentUser, postEntity);
        commentEntityRepository.save(commentEntity);
        postEntity.setCommentCount(postEntity.getCommentCount() + 1); //댓글 생성 시 댓글 숫자 카운트 +1
        return Comment.from(commentEntity);
    }

    public Comment updateComment(Long postId, Long commentId, UpdateCommentRequestBody updateCommentRequest, UserEntity currentUser) {
        PostEntity postEntity = postEntityRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        CommentEntity commentEntity = commentEntityRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(commentId));

        if (!commentEntity.getUser().equals(currentUser)) { // TODO: Owner of the post is able to delete the comment...
            throw new UserNotAllowedException();
        }

        commentEntity.setBody(updateCommentRequest.body());
        commentEntityRepository.save(commentEntity); // Todo : 이거 save 꼭 해줘야하나? 그냥 update 하면 dirty checking 안되나
        return Comment.from(commentEntity);
    }

    @Transactional
    public void deleteComment(Long postId, Long commentId, UserEntity currentUser) {
        PostEntity postEntity = postEntityRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        CommentEntity commentEntity = commentEntityRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(commentId));

        if (!commentEntity.getUser().equals(currentUser)) {
            throw new UserNotAllowedException();
        }

        commentEntityRepository.delete(commentEntity);
        postEntity.setCommentCount(Math.max(0, postEntity.getCommentCount() - 1)); //댓글 삭제시 댓글 카운트 -1
        postEntityRepository.save(postEntity); //TODO: 이거 해줘야하나? save 안해도 자동으로 되는거 아닌가
    }
}
