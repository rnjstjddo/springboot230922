package org.example.springboot230922.config.jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.example.springboot230922.domain.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class TokenProvider {

    private final JwtProperties jp;

    public String generateToken(User u, Duration expiredAt) {
        System.out.println("config-jwt클래스 TokenProvider클래스 generateToken() 진입");
        System.out.println("config-jwt클래스 TokenProvider클래스 generateToken() 진입 파라미터 User -> " + u.toString());
        System.out.println("config-jwt클래스 TokenProvider클래스 generateToken() 진입 파라미터 Duration -> " + expiredAt);

        Date now = new Date();
        return makeToken(new Date(now.getTime() + expiredAt.toMillis()), u);
    }

    //토큰생성메소드
    private String makeToken(Date expiry, User u){
        System.out.println("config-jwt클래스 TokenProvider클래스 makeToken() 진입");

        Date now = new Date();
        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer(jp.getIssuer())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .setSubject(u.getEmail())//내욜sub
                .claim("id", u.getId())//클레임id
                .signWith(SignatureAlgorithm.HS256, jp.getSecretKey())
                .compact();

    }


    //토큰 유효성검증메소드
    public boolean validToken(String token){
        System.out.println("config-jwt클래스 TokenProvider클래스 validToken() 진입 파라미터 token -> "+ token);

        try {
            System.out.println("config-jwt클래스 TokenProvider클래스 validToken() 진입 토큰secret_key복호화과정 에러 미발생 유효한토큰 ");
            Jwts.parser()
                    .setSigningKey(jp.getSecretKey())
                    .parseClaimsJws(token);
                    return true;
        }catch (Exception e){
            System.out.println("config-jwt클래스 TokenProvider클래스 validToken() 진입 토큰secret_key복호화과정 에러발생");

            return false;
        }
    }

    //토큰 기반 인증정보가져오는 메소드
    public Authentication getAuthentication(String token){
        System.out.println("config-jwt클래스 TokenProvider클래스 getAuthentication() 진입 파라미터 token -> "+ token);
        Claims c = getClaims(token);

        System.out.println("config-jwt클래스 TokenProvider클래스 getAuthentication() 진입 클레임Claims -> "+ c.toString());

//        {sub=user@email.com, iss=ajufresh@gmail.com, iat=1696043299, exp=1697252899}
        Set<SimpleGrantedAuthority> autho = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));

        System.out.println("config-jwt클래스 TokenProvider클래스 getAuthentication() 진입 권한 -> "+ autho );

        return new UsernamePasswordAuthenticationToken(new org.springframework.security.core.userdetails.User(c.getSubject(), "", autho), token, autho);

    }

    //토큰기반 사용자 id가져온다.
    public Long getUserId(String token){

        System.out.println("config-jwt클래스 TokenProvider클래스 getUserId() 진입 파라미터 token ->"+ token);

        Claims c = getClaims(token);
        return c.get("id", Long.class);

    }

    //클레임조회해서 반환
    private Claims getClaims(String token){

        System.out.println("config-jwt클래스 TokenProvider클래스 getClaims() Claims 반환 ");

        return Jwts.parser().setSigningKey(jp.getSecretKey())
                .parseClaimsJws(token)
                .getBody();

    }
}
