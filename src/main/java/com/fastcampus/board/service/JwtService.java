package com.fastcampus.board.service;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * 1. 특정 데이터를 넣어서 token을 생성한다 -> generateToken()
 * 2. 해당 token을 통해 넣었던 데이터를 추출한다 -> getSubject()
 * 3. 실제로 JWT 인증에 사용할 public method를 만든다 -> username을 데이터로 쓸 계획이다
 */
@Slf4j
@Service
public class JwtService {

    private static final SecretKey key = Jwts.SIG.HS256.key().build();

    /**
     * 사용자 정보를 담고있는 userDetails를 전달 받아서
     * token을 생성한다
     */
    public String generateAccessToken(UserDetails userDetails) {
        return generateToken(userDetails.getUsername());
    }

    /**
     * accessToken 으로부터 username을 추출
     */
    public String getUsername(String accessToken) {
        return getSubject(accessToken);
    }

    /**
     * this method generates JWT Token by using jjwt library
     */
    private String generateToken(String subject) {
        // setting expiration time
        Date now = new Date();
        Date exp = new Date(now.getTime() + (1000 * 60 * 60) * 3); // 3 hours

        return Jwts.builder().subject(subject).signWith(key)
                .issuedAt(now)
                .expiration(exp)
                .compact();
    }

    /**
     * get subject using generated token
     * 토큰을 만들기 위해서 subject를 넣어줬으니까
     * 반대로 토큰을 통해서 subject를 추출할 수도 있음
     */
    private String getSubject(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
        } catch (JwtException e) {
            log.error("JwtException", e);
            throw e;
        }
    }
}
