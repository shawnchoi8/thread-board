package com.fastcampus.board.service;

import com.fastcampus.board.model.Post;
import com.fastcampus.board.model.PostCreateRequestBody;
import com.fastcampus.board.model.PostUpdateRequestBody;
import com.fastcampus.board.model.entity.PostEntity;
import com.fastcampus.board.repository.PostEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PostService {

    @Autowired
    private final PostEntityRepository postEntityRepository;

    public List<Post> getPosts() {
        List<PostEntity> postEntities = postEntityRepository.findAll();
        return postEntities.stream().map(Post::from).toList();
    }

    public Post getPostByPostId(Long postId) {
        PostEntity postEntity = postEntityRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
        return Post.from(postEntity);
    }

    public Post createPost(PostCreateRequestBody postCreateRequestBody) {
        Long newPostId = posts.stream().mapToLong(Post::getPostId).max().orElse(0L) + 1; //새로 생성될 post의 postId 추출

        Post newPost = new Post(newPostId, postCreateRequestBody.getBody(), ZonedDateTime.now());
        posts.add(newPost);

        return newPost;
    }

    public Post updatePost(Long postId, PostUpdateRequestBody updateRequestBody) {
        Optional<Post> postOptional = posts.stream().filter(post -> postId.equals(post.getPostId())).findFirst();

        if (postOptional.isPresent()) {
            Post findPost = postOptional.get();
            findPost.setBody(updateRequestBody.getBody()); //body update
            return findPost;
        } else { // post가 없으면
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found");
        }
    }

    public void deletePost(Long postId) {
        Optional<Post> postOptional = posts.stream().filter(post -> postId.equals(post.getPostId())).findFirst();

        if (postOptional.isPresent()) {
            posts.remove(postOptional.get());
        } else { // post가 없으면
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found");
        }
    }
}
