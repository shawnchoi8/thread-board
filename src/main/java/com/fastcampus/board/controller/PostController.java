package com.fastcampus.board.controller;

import com.fastcampus.board.model.Post;
import com.fastcampus.board.model.PostCreateRequestBody;
import com.fastcampus.board.model.PostUpdateRequestBody;
import com.fastcampus.board.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        Post findPost = postService.getPostByPostId(postId);
        return ResponseEntity.ok(findPost);
    }

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody PostCreateRequestBody postCreateRequestBody) {
        Post createdPost = postService.createPost(postCreateRequestBody);
        return ResponseEntity.ok(createdPost);
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<Post> updatePost(@PathVariable Long postId, @RequestBody PostUpdateRequestBody updateRequestBody) {
        Post updatedPost = postService.updatePost(postId, updateRequestBody);
        return ResponseEntity.ok(updatedPost);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.noContent().build(); // Client will receive 204 No Content
    }
}
