package com.fastcampus.board.config;

import com.fastcampus.board.exception.jwt.JwtTokenNotFoundException;
import com.fastcampus.board.service.JwtService;
import com.fastcampus.board.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JwtService를 개발했으니 -> 이 서비스를 이용해서 -> 실제로 사용자를 검증하고 처리하는 로직을 만들어야함
 * Spring Security를 통해서 JWT를 인증을 처리할 수 있게끔 Filter를 하나 추가하는 클래스
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private final JwtService jwtService;

    @Autowired
    private final UserService userService;

    /**
     * JWT Token을 검증하는 로직 작성
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        //JWT 기반으로 인증하기 + 해당 인증된 사용자 정보를 -> securityContext에 설정해놓기
        String BEARER_PREFIX = "Bearer ";
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION); //request의 header 에서 authorization 값을 받는다
        SecurityContext securityContext = SecurityContextHolder.getContext();

        if (ObjectUtils.isEmpty(authorization) || authorization.startsWith(BEARER_PREFIX)) {
            throw new JwtTokenNotFoundException();
        }

        if (!ObjectUtils.isEmpty(authorization)
                && authorization.startsWith(BEARER_PREFIX)
                && securityContext.getAuthentication() == null) {

            String accessToken = authorization.substring(BEARER_PREFIX.length()); // authorization 에서 BEARER_PREFIX 길이만큼 잘라내면 accessToken이 추출된다
            String username = jwtService.getUsername(accessToken); // token 으로 username 추출
            UserDetails userDetails = userService.loadUserByUsername(username); // username 으로 db 조회해서 userDetails 추출

            // 사용자 인증 정보가 담긴 authenticationToken 생성
            UsernamePasswordAuthenticationToken authenticationToken
                    = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            // 만들어진 인증 토큰 정보에다가 http request 정보도 설정
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // securityContext에 인증정보를 세팅하고, SecurityContextHolder로 해당 securityContext를 저장함
            securityContext.setAuthentication(authenticationToken);
            SecurityContextHolder.setContext(securityContext);
        }

        filterChain.doFilter(request, response); //검증이 완료되면, doFilter를 호출해서 이후에 Filter 들이 정상적으로 실행되게 해준다
    }
}
