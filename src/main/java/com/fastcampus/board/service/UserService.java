package com.fastcampus.board.service;

import com.fastcampus.board.exception.follow.FollowAlreadyExistsException;
import com.fastcampus.board.exception.follow.FollowNotFoundException;
import com.fastcampus.board.exception.follow.InvalidFollowException;
import com.fastcampus.board.exception.user.UserAlreadyExistsException;
import com.fastcampus.board.exception.user.UserNotAllowedException;
import com.fastcampus.board.exception.user.UserNotFoundException;
import com.fastcampus.board.model.entity.FollowEntity;
import com.fastcampus.board.model.entity.UserEntity;
import com.fastcampus.board.model.user.User;
import com.fastcampus.board.model.user.UserAuthenticationResponse;
import com.fastcampus.board.model.user.UserPatchRequestBody;
import com.fastcampus.board.repository.FollowEntityRepository;
import com.fastcampus.board.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {

    private final UserEntityRepository userEntityRepository;

    private final FollowEntityRepository followEntityRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private final JwtService jwtService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userEntityRepository
                .findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    /**
     * Sign Up
     */
    public User signUp(String username, String password) {
        userEntityRepository
                .findByUsername(username)
                .ifPresent(user -> {
                    throw new UserAlreadyExistsException();
                });

        UserEntity userEntity = UserEntity.of(username, passwordEncoder.encode(password));
        UserEntity savedUserEntity = userEntityRepository.save(userEntity);

        return User.from(savedUserEntity);
    }

    /**
     * Login
     */
    public UserAuthenticationResponse authenticate(String username, String password) {
        UserEntity userEntity = userEntityRepository
                .findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        if (passwordEncoder.matches(password, userEntity.getPassword())) { //client 로부터 전달받은 password 와 암호화된 password를 비교
            //일치한다면 -> 정상적인 사용자라는 거니까 -> jwt 발급해줘
            String accessToken = jwtService.generateAccessToken(userEntity);
            return new UserAuthenticationResponse(accessToken);
        } else {
            throw new UserNotFoundException();
        }
    }

    /**
     * Users 리스트 조회
     */
    public List<User> getUsers(String query) {
        List<UserEntity> userEntities;

        if (query != null && !query.isBlank()) {
            userEntities = userEntityRepository.findByUsernameContaining(query); //query 검색어 기반으로 해당 검색아가 username에 포함되어 있는 유저목록 가져오기
        } else {
            userEntities = userEntityRepository.findAll();
        }

        return userEntities.stream().map(User::from).toList();
    }

    /**
     * User 단건 조회
     */
    public User getUser(String username) {
        UserEntity userEntity = userEntityRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        return User.from(userEntity);
    }

    /**
     * User Description update
     */
    public User updateUser(String username, UserPatchRequestBody userPatchRequestBody, UserEntity currentUser) {
        UserEntity userEntity = userEntityRepository.findByUsername(username) //username에 있는지 검사 후 조회
                .orElseThrow(() -> new UserNotFoundException(username));

        if (!userEntity.equals(currentUser)) { //user와 지금 접속하려는 user가 일치하는지 검사
            throw new UserNotAllowedException();
        }

        if (userPatchRequestBody.description() != null) { //변경하기 위해 받아온 userPatchRequestBody가 null 이 아닐때만
            userEntity.setDescription(userPatchRequestBody.description()); // 변경
        }

        return User.from(userEntityRepository.save(userEntity));
    }

    @Transactional
    public User follow(String username, UserEntity currentUser) {
        UserEntity following = userEntityRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        if (following.equals(currentUser)) {
            throw new InvalidFollowException("you can't follow yourself");
        }

        followEntityRepository.findByFollowerAndFollowing(currentUser, following)
                .ifPresent(follow -> {
                    throw new FollowAlreadyExistsException(currentUser, following);
                });
        followEntityRepository.save(FollowEntity.of(currentUser, following));

        following.setFollowerCount(following.getFollowerCount() + 1);
        currentUser.setFollowingCount(currentUser.getFollowingCount() + 1);

        userEntityRepository.saveAll(List.of(following, currentUser));

        return User.from(following);
    }

    @Transactional
    public User unFollow(String username, UserEntity currentUser) {
        UserEntity following = userEntityRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        if (following.equals(currentUser)) {
            throw new InvalidFollowException("you can't unfollow yourself");
        }

        FollowEntity followEntity = followEntityRepository.findByFollowerAndFollowing(currentUser, following)
                .orElseThrow(() -> new FollowNotFoundException(currentUser, following));

        followEntityRepository.delete(followEntity);

        following.setFollowerCount(Math.max(0, following.getFollowerCount() - 1));
        currentUser.setFollowingCount(Math.max(0, currentUser.getFollowingCount() - 1));

        userEntityRepository.saveAll(List.of(following, currentUser));

        return User.from(following);
    }

    public List<User> getFollowersByUsername(String username) {
        UserEntity following = userEntityRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        List<FollowEntity> followers = followEntityRepository.findByFollowing(following);

        return followers
                .stream()
                .map(follow -> User.from(follow.getFollower()))
                .toList();
    }

    public List<User> getFollowingsByUsername(String username) {
        UserEntity follower = userEntityRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        List<FollowEntity> followings = followEntityRepository.findByFollower(follower);

        return followings
                .stream()
                .map(follow -> User.from(follow.getFollowing()))
                .toList();
    }

}
