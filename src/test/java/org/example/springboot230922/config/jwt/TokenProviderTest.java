package org.example.springboot230922.config.jwt;

import io.jsonwebtoken.Jwts;
import org.example.springboot230922.domain.User;
import org.example.springboot230922.repository.UserRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Duration;
import java.util.Date;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;



@SpringBootTest
class TokenProviderTest {

    @Autowired
    private TokenProvider tp;

    @Autowired
    private UserRepository ur;

    @Autowired
    private  JwtProperties jp;

    @DisplayName("generateToken() 유저정보와 만료기간전달해서 토큰생성한다.")
    @Test
    void generateToken(){

        User u = ur.save(User.builder().email("user@gmail.com").password("test").build());
        System.out.println("테스트 TokenProviderTest클래스 generateToken() 진입 생성한 User -> "+ u.toString());
        //when
        String token = tp.generateToken(u, Duration.ofDays(14));
        System.out.println("테스트 TokenProviderTest클래스 generateToken() 진입 생성한 Token -> "+ token);

        Long userId = Jwts.parser()
                .setSigningKey(jp.getSecretKey())
                .parseClaimsJws(token)
                .getBody()
                .get("id", Long.class);
        System.out.println("테스트 TokenProviderTest클래스 generateToken() jjwt라이브러리 사용해서 토큰 복호화한다." +
                "클레임으로 넣은 id값이 생성한User즉, DB의 값과 일치하는지 검증 -> "+ userId);


        assertThat(userId).isEqualTo(u.getId());

    }

    @DisplayName("validToken() 만료된 토큰일경우 유효성검증 실패")
    @Test
    void validToken_invalidToken(){
        System.out.println("테스트 TokenProviderTest클래스 validToken_invalidToken() 진입 ");

        String token = JwtFactory.builder().expiration(new Date(new Date().getTime()-Duration.ofDays(7).toMillis()))
                .build().createToken(jp);

        System.out.println("테스트 TokenProviderTest클래스 validToken_invalidToken() 진입 토큰생성하는데 이미만료된 토큰으로 생성한다. -> "+ token);

        //when
        boolean result = tp.validToken(token);

        System.out.println("테스트 TokenProviderTest클래스 validToken_invalidToken() 진입 유효한토큰인지 여부 -> "+ result);
        assertThat(result).isFalse();
    }
    
    @DisplayName("validToken()s 유효한 토큰일 경우 유효성검증 성공")
    @Test
    void validToken_validToken(){

        System.out.println("테스트 TokenProviderTest클래스 validToken_validToken() 진입 ");

        String token = JwtFactory.withDefaultValues().createToken(jp);

        System.out.println("테스트 TokenProviderTest클래스 validToken_validToken() 진입 유효한토큰생성 -> "+ token);

        boolean result = tp.validToken(token);
        assertThat(result).isTrue();
    }

    @DisplayName("getAuthentication() 토큰기반으로 인증정보 가져올수 있다.")
    @Test
    void getAuthentication(){
        System.out.println("테스트 TokenProviderTest클래스 getAuthentication() 진입 ");

        String userEmail = "user@email.com";
        String token = JwtFactory.builder()
                .subject(userEmail)
                .build().createToken(jp);
        System.out.println("테스트 TokenProviderTest클래스 getAuthentication() 진입 생성한 토큰-> "+ token);

        //when
        Authentication authen = tp.getAuthentication(token);

        System.out.println("테스트 TokenProviderTest클래스 getAuthentication() 진입 인증객체생성 toString() ->" +authen.toString());

        //UsernamePasswordAuthenticationToken
        //[
        // Principal=org.springframework.security.core.userdetails.User
        // [Username=user@email.com, Password=[PROTECTED], Enabled=true,
        // AccountNonExpired=true, credentialsNonExpired=true, AccountNonLocked=true,
        // Granted Authorities=[ROLE_USER]],
        // Credentials=[PROTECTED], Authenticated=true, Details=null, Granted Authorities=[ROLE_USER]
        // ]

        System.out.println("테스트 TokenProviderTest클래스 getAuthentication() 진입 인증객체생성 getPrincipal()->" +authen.getPrincipal());
        //org.springframework.security.core.userdetails.User
        // [
        // Username=user@email.com, Password=[PROTECTED], Enabled=true, AccountNonExpired=true
        // , credentialsNonExpired=true, AccountNonLocked=true, Granted Authorities=[ROLE_USER]
        // ]


        //then
        assertThat( ( (UserDetails) authen.getPrincipal()).getUsername()).isEqualTo(userEmail);

    }

    @DisplayName("getUserId() 토큰으로 사용자ID 가져올수있다.")
    @Test
    void getUserId(){
        Long userId =1L;
        String token = JwtFactory.builder()
                .map(Map.of("id", userId))
                .build().createToken(jp);


        System.out.println("테스트 TokenProviderTest클래스 getUserId() 진입 생성한 토큰 -> "+token);

        //when
        Long id = tp.getUserId(token);
        //then
        assertThat(id).isEqualTo(userId);

    }
}