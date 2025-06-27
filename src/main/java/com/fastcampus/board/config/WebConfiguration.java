package com.fastcampus.board.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class WebConfiguration {

    @Autowired
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private final JwtExceptionFilter jwtExceptionFilter;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration(); //CORS 설정 틀 생성
        configuration.setAllowedOrigins(List.of("http://localhost:3000", "http://127.0.0.1:3000")); //CORS 정책을 통해 허용하고자 하는 Origin들 세팅
        configuration.setAllowedMethods(List.of("GET", "POST", "PATCH", "DELETE")); //CORS 설정을 통해서 API가 호출될 때 어떤 Http method를 허용할지 설정
        configuration.setAllowedHeaders(List.of("*")); //나중에 Header를 통해 JWT token도 받을 예정. 일단은 모두 열어놔

        //위에서 만든 CORS 설정을 어떤 API 패턴에 대해 적용할지, 아래와 같이 URL 베이스로 지정할 수 있다
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/v1/**", configuration); //특정 API 패턴에 대해서만 제한적으로 CORS 설정 적용 가능

        return source;
    }

    /**
     * 1. 모든 request에 대해서 인증 처리를 할건데
     * 2. basic auth를 사용 할 것이고
     * 3. csrf는 아예 제외를 시킬 것이다
     * 4. 나는 REST API를 개발하기 때문에, Session 자체가 필요 없음 -> 세션 생성을 아예 안해
     */
    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors(Customizer.withDefaults()) //CORS 설정 추가
                .authorizeHttpRequests((requests) -> requests.anyRequest().authenticated()) // 1
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 4
                .csrf(CsrfConfigurer::disable) // 3
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtExceptionFilter, jwtAuthenticationFilter.getClass())
                .httpBasic((Customizer.withDefaults())); // 2

        return httpSecurity.build();
    }
}
