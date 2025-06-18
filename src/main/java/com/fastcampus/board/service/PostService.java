package com.fastcampus.board.service;

import com.fastcampus.board.model.Post;
import org.springframework.stereotype.Service;

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
}
