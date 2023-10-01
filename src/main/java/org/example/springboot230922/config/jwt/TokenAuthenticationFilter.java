package org.example.springboot230922.config.jwt;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.SelectBeforeUpdate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;



@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tp;
    private final static String HEADER_AUTHORIZATION ="Authorization";
    private final static String TOKEN_PREFIX="Bearer ";


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("Config-jwt 필터클래스 TokenAuthenticationFilter 오버라이딩 doFilterInternal() 진입");

        String autho = request.getHeader(HEADER_AUTHORIZATION);
        System.out.println("Config-jwt 필터클래스 TokenAuthenticationFilter 오버라이딩 doFilterInternal() 진입 헤더의 키값조회-> " + autho);

        String token = getAccessToken(autho);

        System.out.println("Config-jwt 필터클래스 TokenAuthenticationFilter 오버라이딩 doFilterInternal() 진입 Bearer 의 값만 추출 -> " + token);

        if (tp.validToken(token)) {
            System.out.println("Config-jwt 필터클래스 TokenAuthenticationFilter 오버라이딩 doFilterInternal() 진입 유효한토큰이므로 인증정보 설정");

            Authentication authen = tp.getAuthentication(token);
            System.out.println("Config-jwt 필터클래스 TokenAuthenticationFilter 오버라이딩 doFilterInternal() 진입 유효한토큰이므로 인증정보 설정 인증객채 -> " + authen);

            SecurityContextHolder.getContext().setAuthentication(authen);
        }

        filterChain.doFilter(request, response);
    }

        private String getAccessToken(String autho){
            System.out.println("Config-jwt 필터클래스 TokenAuthenticationFilter getAccessToken() 진입 Bearer 값만 반환 ");

            if(autho != null && autho.startsWith(TOKEN_PREFIX)){
                return autho.substring(TOKEN_PREFIX.length());
            }

            System.out.println("Config-jwt 필터클래스 TokenAuthenticationFilter getAccessToken() 진입 Bearer 값만 반환 없기에 null반환");

            return null;
    }
}
