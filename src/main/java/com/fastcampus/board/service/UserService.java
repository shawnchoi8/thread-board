package com.fastcampus.board.service;

import com.fastcampus.board.exception.user.UserAlreadyExistsException;
import com.fastcampus.board.exception.user.UserNotFoundException;
import com.fastcampus.board.model.entity.UserEntity;
import com.fastcampus.board.model.user.User;
import com.fastcampus.board.model.user.UserAuthenticationResponse;
import com.fastcampus.board.repository.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userEntityRepository
                .findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

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
}
