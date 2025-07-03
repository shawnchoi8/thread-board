package com.fastcampus.board.repository;

import com.fastcampus.board.model.entity.CommentEntity;
import com.fastcampus.board.model.entity.PostEntity;
import com.fastcampus.board.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentEntityRepository extends JpaRepository<CommentEntity, Long> {

    List<CommentEntity> findByUser(UserEntity user);

    List<CommentEntity> findByPost(PostEntity post);
}
