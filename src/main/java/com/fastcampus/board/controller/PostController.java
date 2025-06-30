package com.fastcampus.board.controller;

import com.fastcampus.board.model.post.Post;
import com.fastcampus.board.model.post.PostCreateRequestBody;
import com.fastcampus.board.model.post.PostUpdateRequestBody;
import com.fastcampus.board.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping
    public ResponseEntity<List<Post>> getPosts() {
        log.info("GET /api/v1/posts");
        List<Post> posts = postService.getPosts();
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<Post> getPostByPostId(@PathVariable Long postId) {
        log.info("GET /api/v1/posts/{}", postId);
        Post findPost = postService.getPostByPostId(postId);
        return ResponseEntity.ok(findPost);
    }

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody PostCreateRequestBody postCreateRequestBody) {
        log.info("POST /api/v1/posts");
        Post createdPost = postService.createPost(postCreateRequestBody);
        return ResponseEntity.ok(createdPost);
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<Post> updatePost(@PathVariable Long postId, @RequestBody PostUpdateRequestBody updateRequestBody) {
        log.info("PATCH /api/v1/posts/{}", postId);
        Post updatedPost = postService.updatePost(postId, updateRequestBody);
        return ResponseEntity.ok(updatedPost);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        log.info("DELETE /api/v1/posts/{}", postId);
        postService.deletePost(postId);
        return ResponseEntity.noContent().build(); // Client will receive 204 No Content
    }
}
