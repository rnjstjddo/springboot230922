package org.example.springboot230922.config.jwt;


import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.Duration;
import java.util.Date;
import java.util.Map;

import static java.util.Collections.emptyMap;

@Getter
@ToString
public class JwtFactory {

    private String subject="test@email.com";
    private Date issuedAt= new Date();
    private Date expiration = new Date(new Date().getTime() + Duration.ofDays(14).toMillis());
    private Map<String, Object> map = emptyMap();

    @Builder
    public JwtFactory(String subject, Date issuedAt, Date expiration, Map<String, Object> map){
        System.out.println("테스트 JwtFactory 생성자진입");
        this.subject = subject!= null? subject: this.subject;
        this.issuedAt = issuedAt!=null? issuedAt: this.issuedAt;
        this.expiration= expiration != null? expiration : this.expiration;
        this.map =map !=null? map: this.map;
        System.out.println("테스트 JwtFactory 생성자진입 -> "+ this.toString());

    }

    public static JwtFactory withDefaultValues(){
        System.out.println("테스트 JwtFactory클래스 withDefaultValues() 진입");
        return JwtFactory.builder().build();
    }


    //jwt라이브러리 사용해서 jwt토큰 생성
    public String createToken(JwtProperties jp){
        System.out.println("테스트 JwtFactory클래스 createToken() 진입");
        System.out.println("테스트 JwtFactory클래스 createToken() 진입 파라미터  JwtProperties-> "+jp.toString());

        return Jwts.builder().setSubject(subject)
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer(jp.getIssuer())
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .addClaims(map)
                .signWith(SignatureAlgorithm.HS256, jp.getSecretKey())
                .compact();
    }
}
