package org.example.springboot230922.domain.jwt;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Getter
@Entity
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", updatable = false)
    private Long id;

    @Column(name="user_id", nullable = false, unique = true)
    private Long userId;

    @Column(name="refresh_token", nullable = false)
    private String refreshToken;

    public RefreshToken(Long userId, String refreshToken){
        System.out.println("엔티티클래스 RefreshToken 생성자진입");

        System.out.println("엔티티클래스 RefreshToken 생성자진입 파라미터 사용자ID->"+ userId+ ", 리프레시토큰 -> "+ refreshToken);

        this.userId=userId;
        this.refreshToken=refreshToken;
    }

    public RefreshToken update(String newRefreshToken){
        System.out.println("엔티티클래스 RefreshToken update() 진입 파라미터 새로운 리프레시토큰-> "+ newRefreshToken);
        this.refreshToken=newRefreshToken;
        return this;

    }
}
