package com.fastcampus.board.service;

import com.fastcampus.board.model.Post;
import com.fastcampus.board.model.PostCreateRequestBody;
import com.fastcampus.board.model.PostUpdateRequestBody;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private static final List<Post> posts = new ArrayList<>();

    static {
        posts.add(new Post(1L, "hello 1", ZonedDateTime.now()));
        posts.add(new Post(2L, "hello 2", ZonedDateTime.now()));
        posts.add(new Post(3L, "hello 3", ZonedDateTime.now()));
    }

    public List<Post> getPosts() {
        return posts;
    }

    public Optional<Post> getPostByPostId(Long postId) {
        return posts.stream().filter(post -> postId.equals(post.getPostId())).findFirst();
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
}
