package org.example.springboot230922.controller.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.springboot230922.config.jwt.JwtFactory;
import org.example.springboot230922.config.jwt.JwtProperties;
import org.example.springboot230922.domain.User;
import org.example.springboot230922.domain.jwt.RefreshToken;
import org.example.springboot230922.dto.jwt.CreateAccessTokenRequest;
import org.example.springboot230922.repository.UserRepository;
import org.example.springboot230922.repository.jwt.RefreshTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class TokenApiControllerTest {

    @Autowired
    protected MockMvc mm;

    @Autowired
    protected ObjectMapper om;

    @Autowired
    private WebApplicationContext wc;

    @Autowired
    JwtProperties jp;

    @Autowired
    UserRepository ur;

    @Autowired
    RefreshTokenRepository rr;

    @BeforeEach
    public void mockMvcSetUp(){
        System.out.println("테스트 TokenApiControllerTest @BeforeEach 진입");
        this.mm= MockMvcBuilders.webAppContextSetup(wc).build();
        ur.deleteAll();
    }

    @DisplayName("createNewAccessToken 새로운엑세스 토큰을 발급한다.")
    @Test
    public void createNewAccessToken() throws Exception{
        System.out.println("테스트 TokenApiControllerTest createNewAccessToken() 진입");

        final String url="/api/token";

        User u = ur.save(User.builder().email("user@gmail.com").password("test").build());

        System.out.println("테스트 TokenApiControllerTest createNewAccessToken() 진입 생성한 User객체 -> "+ u.toString());
// User(id=1, email=user@gmail.com, password=test)

//테스트 JwtFactory 생성자진입 -> JwtFactory(subject=test@email.com, issuedAt=Sat Sep 30 13:08:22 KST 2023, expiration=Sat Oct 14 13:08:22 KST 2023, map={id=1})
//테스트 JwtFactory클래스 createToken() 진입 파라미터  JwtProperties-> JwtProperties(issuer=ajufresh@gmail.com, secretKey=study-springboot)

        String refreshToken = JwtFactory.builder()
                .map(Map.of("id", u.getId()))
                .build()
                .createToken(jp);
        System.out.println("테스트 TokenApiControllerTest createNewAccessToken() 진입 생성한 리프레시토큰 -> "+ refreshToken);


        rr.save(new RefreshToken(u.getId() ,refreshToken));

        //엔티티클래스 RefreshToken 생성자진입
        //엔티티클래스 RefreshToken 생성자진입 파라미터 사용자ID->1, 리프레시토큰 -> eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QGVtYWlsLmNvbSIsImlzcyI6ImFqdWZyZXNoQGdtYWlsLmNvbSIsImlhdCI6MTY5NjA0NjkwMiwiZXhwIjoxNjk3MjU2NTAyLCJpZCI6MX0.aKZuLXKJvOZwImenSwhCl11MHoO3eYR1PFADzKSTnVQ
        CreateAccessTokenRequest request= new CreateAccessTokenRequest();
        request.setRefreshToken(refreshToken);

        final String requestBody = om.writeValueAsString(request);

        System.out.println("테스트 TokenApiControllerTest createNewAccessToken() 진입 컨트롤러 전달 requestBody -> "+ requestBody);
//{"refreshToken":"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QGVtYWlsLmNvbSIsImlzcyI6ImFqdWZyZXNoQGdtYWlsLmNvbSIsImlhdCI6MTY5NjA0NjkwMiwiZXhwIjoxNjk3MjU2NTAyLCJpZCI6MX0.aKZuLXKJvOZwImenSwhCl11MHoO3eYR1PFADzKSTnVQ"}
        ResultActions action = mm.perform(post(url).contentType(MediaType.APPLICATION_JSON_VALUE).content(requestBody));

//        JWT컨트롤러 TokenApiController createNewAccessToken() 진입 파라미터 DTO클래스 CreateAccessTokenRequest ->CreateAccessTokenRequest(refreshToken=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QGVtYWlsLmNvbSIsImlzcyI6ImFqdWZyZXNoQGdtYWlsLmNvbSIsImlhdCI6MTY5NjA0NjkwMiwiZXhwIjoxNjk3MjU2NTAyLCJpZCI6MX0.aKZuLXKJvOZwImenSwhCl11MHoO3eYR1PFADzKSTnVQ)
//        JWT컨트롤러 TokenApiController createNewAccessToken() 진입 파라미터 DTO클래스 CreateAccessTokenRequest getRefreshToken()->eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QGVtYWlsLmNvbSIsImlzcyI6ImFqdWZyZXNoQGdtYWlsLmNvbSIsImlhdCI6MTY5NjA0NjkwMiwiZXhwIjoxNjk3MjU2NTAyLCJpZCI6MX0.aKZuLXKJvOZwImenSwhCl11MHoO3eYR1PFADzKSTnVQ
//        서비스클래스 TokenService createNewAccessToken() 진입 파라미터 리프레시 토큰-> eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QGVtYWlsLmNvbSIsImlzcyI6ImFqdWZyZXNoQGdtYWlsLmNvbSIsImlhdCI6MTY5NjA0NjkwMiwiZXhwIjoxNjk3MjU2NTAyLCJpZCI6MX0.aKZuLXKJvOZwImenSwhCl11MHoO3eYR1PFADzKSTnVQ
//        config-jwt클래스 TokenProvider클래스 validToken() 진입 파라미터 token -> eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QGVtYWlsLmNvbSIsImlzcyI6ImFqdWZyZXNoQGdtYWlsLmNvbSIsImlhdCI6MTY5NjA0NjkwMiwiZXhwIjoxNjk3MjU2NTAyLCJpZCI6MX0.aKZuLXKJvOZwImenSwhCl11MHoO3eYR1PFADzKSTnVQ
//        config-jwt클래스 TokenProvider클래스 validToken() 진입 토큰secret_key복호화과정 에러 미발생 유효한토큰
//        서비스클래스 RefreshTokenService findByRefreshToken() 진입 파라미터 리프레시토큰 -> eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QGVtYWlsLmNvbSIsImlzcyI6ImFqdWZyZXNoQGdtYWlsLmNvbSIsImlhdCI6MTY5NjA0NjkwMiwiZXhwIjoxNjk3MjU2NTAyLCJpZCI6MX0.aKZuLXKJvOZwImenSwhCl11MHoO3eYR1PFADzKSTnVQ
//        서비스클래스 TokenService createNewAccessToken() 진입 리프레시토큰을 사용자id찾기 -> 1
//        서비스클래스 UserService findById() 진입 리프레시토큰으로 새로운 엑세스 토큰 발급 파라미터 사용자id -> 1
//엔티티 User 오버라이딩 getPassword() 진입 사용자 패스워드반환
//서비스클래스 TokenService createNewAccessToken() 진입 리프레시토큰을 사용자객체찾기 -> User(id=1, email=user@gmail.com, password=test)
//config-jwt클래스 TokenProvider클래스 generateToken() 진입
//엔티티 User 오버라이딩 getPassword() 진입 사용자 패스워드반환
//config-jwt클래스 TokenProvider클래스 generateToken() 진입 파라미터 User -> User(id=1, email=user@gmail.com, password=test)
//config-jwt클래스 TokenProvider클래스 generateToken() 진입 파라미터 Duration -> PT2H
//config-jwt클래스 TokenProvider클래스 makeToken() 진입
//JWT컨트롤러 TokenApiController createNewAccessToken() 진입 새롭게 생성한 엑세스토큰 ->eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJhanVmcmVzaEBnbWFpbC5jb20iLCJpYXQiOjE2OTYwNDY5MDIsImV4cCI6MTY5NjA1NDEwMiwic3ViIjoidXNlckBnbWFpbC5jb20iLCJpZCI6MX0.0VRy3luRXgZkJN-aCHoy1j2gseXtrTwgUryeKpoD2aU
        System.out.println("테스트 TokenApiControllerTest createNewAccessToken() 진입 ResultActions -> "+ action);

        action.andExpect(status().isCreated())
                .andExpect(jsonPath("$.accessToken").isNotEmpty());//CreateAccessTokenResponse 변수
    }
}