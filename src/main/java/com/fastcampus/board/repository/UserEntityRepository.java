package com.fastcampus.board.repository;

import com.fastcampus.board.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUsername(String username);

    List<UserEntity> findByUsernameContaining(String username); //전달받은 문자열이 부분적으로 포함되어있는 결과를 반환 받을 수 있음
}
