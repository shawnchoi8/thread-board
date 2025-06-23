package com.fastcampus.board.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebConfiguration {

    /**
     * 1. 모든 request에 대해서 인증 처리를 할건데
     * 2. basic auth를 사용 할 것이고
     * 3. csrf는 아예 제외를 시킬 것이다
     * 4. 나는 REST API를 개발하기 때문에, Session 자체가 필요 없음 -> 세션 생성을 아예 안해
     */
    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests((requests) -> requests.anyRequest().authenticated()) // 1
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 4
                .csrf(CsrfConfigurer::disable) // 3
                .httpBasic((Customizer.withDefaults())); // 2

        return httpSecurity.build();
    }
}
