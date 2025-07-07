package com.fastcampus.board.service;

import com.fastcampus.board.exception.post.PostNotFoundException;
import com.fastcampus.board.exception.user.UserNotAllowedException;
import com.fastcampus.board.exception.user.UserNotFoundException;
import com.fastcampus.board.model.entity.LikeEntity;
import com.fastcampus.board.model.entity.UserEntity;
import com.fastcampus.board.model.post.Post;
import com.fastcampus.board.model.post.PostCreateRequestBody;
import com.fastcampus.board.model.post.PostUpdateRequestBody;
import com.fastcampus.board.model.entity.PostEntity;
import com.fastcampus.board.repository.LikeEntityRepository;
import com.fastcampus.board.repository.PostEntityRepository;
import com.fastcampus.board.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostEntityRepository postEntityRepository;

    private final UserEntityRepository userEntityRepository;

    private final LikeEntityRepository likeEntityRepository;

    public List<Post> getPosts() {
        List<PostEntity> postEntities = postEntityRepository.findAll();
        return postEntities.stream().map(Post::from).toList();
    }

    public Post getPostByPostId(Long postId) {
        PostEntity postEntity = postEntityRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));
        return Post.from(postEntity);
    }

    public Post createPost(PostCreateRequestBody postCreateRequestBody, UserEntity currentUser) {
        PostEntity postEntity = PostEntity.of(postCreateRequestBody.getBody(), currentUser);
        PostEntity savedPostEntity = postEntityRepository.save(postEntity);
        return Post.from(savedPostEntity);
    }

    public Post updatePost(Long postId, PostUpdateRequestBody updateRequestBody, UserEntity currentUser) {
        PostEntity postEntity = postEntityRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        if (!postEntity.getUser().equals(currentUser)) { // postEntity 의 user 와 currentUser 가 일치해야 update 가능
            throw new UserNotAllowedException();
        }

        postEntity.setBody(updateRequestBody.getBody());
        PostEntity updatedPostEntity = postEntityRepository.save(postEntity);
        // TODO: remove save() after @Transactional -> dirty checking in Service class

        return Post.from(updatedPostEntity);
    }

    public void deletePost(Long postId, UserEntity currentUser) {
        PostEntity postEntity = postEntityRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        if (!postEntity.getUser().equals(currentUser)) { // postEntity 의 user 와 currentUser 가 일치해야 delete 가능
            throw new UserNotAllowedException();
        }

        postEntityRepository.delete(postEntity);
    }

    public List<Post> getPostsByUsername(String username) {
        UserEntity userEntity = userEntityRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        List<PostEntity> posts = postEntityRepository.findByUser(userEntity);
        return posts.stream().map(Post::from).toList();
    }

    @Transactional
    public Post toggleLike(Long postId, UserEntity currentUser) {
        PostEntity postEntity = postEntityRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        Optional<LikeEntity> likeEntity = likeEntityRepository.findByUserAndPost(currentUser, postEntity);

        if (likeEntity.isPresent()) {
            likeEntityRepository.delete(likeEntity.get());
            postEntity.setLikeCount(Math.max(0, postEntity.getLikeCount() - 1));
        } else {
            likeEntityRepository.save(LikeEntity.of(currentUser, postEntity));
            postEntity.setLikeCount(postEntity.getLikeCount() + 1);
        }

        return Post.from(postEntityRepository.save(postEntity));
    }
}
