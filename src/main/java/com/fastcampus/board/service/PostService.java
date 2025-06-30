package com.fastcampus.board.service;

import com.fastcampus.board.exception.post.PostNotFoundException;
import com.fastcampus.board.model.post.Post;
import com.fastcampus.board.model.post.PostCreateRequestBody;
import com.fastcampus.board.model.post.PostUpdateRequestBody;
import com.fastcampus.board.model.entity.PostEntity;
import com.fastcampus.board.repository.PostEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostEntityRepository postEntityRepository;

    public List<Post> getPosts() {
        List<PostEntity> postEntities = postEntityRepository.findAll();
        return postEntities.stream().map(Post::from).toList();
    }

    public Post getPostByPostId(Long postId) {
        PostEntity postEntity = postEntityRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));
        return Post.from(postEntity);
    }

    public Post createPost(PostCreateRequestBody postCreateRequestBody) {
        PostEntity postEntity = new PostEntity();
        postEntity.setBody(postCreateRequestBody.getBody());
        PostEntity savedPostEntity = postEntityRepository.save(postEntity);
        return Post.from(savedPostEntity);
    }

    public Post updatePost(Long postId, PostUpdateRequestBody updateRequestBody) {
        PostEntity postEntity = postEntityRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        postEntity.setBody(updateRequestBody.getBody());
        PostEntity updatedPostEntity = postEntityRepository.save(postEntity);
        // TODO: remove save() after @Transactional -> dirty checking in Service class

        return Post.from(updatedPostEntity);
    }

    public void deletePost(Long postId) {
        PostEntity postEntity = postEntityRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        postEntityRepository.delete(postEntity);
    }
}
