package com.fastcampus.board.controller;

import com.fastcampus.board.model.Post;
import com.fastcampus.board.model.PostCreateRequestBody;
import com.fastcampus.board.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping
    public ResponseEntity<List<Post>> getPosts() {
        List<Post> posts = postService.getPosts();
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<Post> getPostByPostId(@PathVariable Long postId) {
        Optional<Post> findPost = postService.getPostByPostId(postId);

        return findPost
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody PostCreateRequestBody postCreateRequestBody) {
        Post post = postService.createPost(postCreateRequestBody);
        return ResponseEntity.ok(post);
    }
}
