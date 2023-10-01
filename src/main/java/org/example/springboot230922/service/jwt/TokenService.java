package org.example.springboot230922.service.jwt;


import lombok.RequiredArgsConstructor;
import org.example.springboot230922.config.jwt.TokenProvider;
import org.example.springboot230922.domain.User;
import org.example.springboot230922.service.UserService;
import org.springframework.stereotype.Service;

import java.time.Duration;

@RequiredArgsConstructor
@Service
public class TokenService {

    private final TokenProvider tp;
    private final RefreshTokenService rs;
    private final UserService us;

    public String createNewAccessToken(String refreshToken){
        System.out.println("서비스클래스 TokenService createNewAccessToken() 진입 파라미터 리프레시 토큰-> "+ refreshToken);

        if(!tp.validToken(refreshToken)){

            System.out.println("서비스클래스 TokenService createNewAccessToken() 진입 리프레시 토큰이 유효하지 않기에 에러발생");
            throw new IllegalArgumentException("유효하지 않은 리프레시토큰입니다.");
        }

        Long userId= rs.findByRefreshToken(refreshToken).getUserId();
        System.out.println("서비스클래스 TokenService createNewAccessToken() 진입 리프레시토큰을 사용자id찾기 -> " +userId);

        User u = us.findById(userId);
        System.out.println("서비스클래스 TokenService createNewAccessToken() 진입 리프레시토큰을 사용자객체찾기 -> " +u.toString());

        return tp.generateToken(u, Duration.ofHours(2));
    }

}
