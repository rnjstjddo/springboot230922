package org.example.springboot230922.service.jwt;


import lombok.RequiredArgsConstructor;
import org.example.springboot230922.domain.jwt.RefreshToken;
import org.example.springboot230922.repository.jwt.RefreshTokenRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository rr;
    

    //리프레시토큰 DB찾기
    public RefreshToken findByRefreshToken(String refreshToken){
        System.out.println("서비스클래스 RefreshTokenService findByRefreshToken() 진입 파라미터 리프레시토큰 -> "+ refreshToken);
        return rr.findByRefreshToken(refreshToken).orElseThrow( () -> new IllegalArgumentException("존재하지 않는 리프레시토큰입니다."));
    }
}
