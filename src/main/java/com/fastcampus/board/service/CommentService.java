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

    public Comment createComment(Long postId, CreateCommentRequestBody requestBody, UserEntity currentUser) {
        PostEntity postEntity = postEntityRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        CommentEntity commentEntity = CommentEntity.of(requestBody.body(), currentUser, postEntity);
        commentEntityRepository.save(commentEntity);
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
        commentEntityRepository.save(commentEntity);
        return Comment.from(commentEntity);
    }

    public void deleteComment(Long postId, Long commentId, UserEntity currentUser) {
        PostEntity postEntity = postEntityRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        CommentEntity commentEntity = commentEntityRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(commentId));

        if (!commentEntity.getUser().equals(currentUser)) {
            throw new UserNotAllowedException();
        }

        commentEntityRepository.delete(commentEntity);
    }
}
