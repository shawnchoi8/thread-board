package com.fastcampus.board.controller;

import com.fastcampus.board.model.entity.UserEntity;
import com.fastcampus.board.model.post.Post;
import com.fastcampus.board.model.post.PostCreateRequestBody;
import com.fastcampus.board.model.post.PostUpdateRequestBody;
import com.fastcampus.board.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostService postService;

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
    public ResponseEntity<Post> createPost(@RequestBody PostCreateRequestBody postCreateRequestBody, Authentication authentication) {
        log.info("POST /api/v1/posts");
        Post createdPost = postService.createPost(postCreateRequestBody, (UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(createdPost);
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<Post> updatePost(
            @PathVariable Long postId,
            @RequestBody PostUpdateRequestBody updateRequestBody,
            Authentication authentication) {
        log.info("PATCH /api/v1/posts/{}", postId);
        Post updatedPost = postService.updatePost(postId, updateRequestBody, (UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(updatedPost);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId, Authentication authentication) {
        log.info("DELETE /api/v1/posts/{}", postId);
        postService.deletePost(postId, (UserEntity) authentication.getPrincipal());
        return ResponseEntity.noContent().build(); // Client will receive 204 No Content
    }

    /**
     * likes
     */
    @PostMapping("/{postId}/likes")
    public ResponseEntity<Post> toggleLike(@PathVariable Long postId, Authentication authentication) {
        Post post = postService.toggleLike(postId, (UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(post);
    }
}
