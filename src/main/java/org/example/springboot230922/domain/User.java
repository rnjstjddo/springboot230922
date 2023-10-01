package org.example.springboot230922.domain;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Table(name="users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@ToString
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", updatable = false)
    private Long id;

    @Column(name="email", nullable = false, unique = true)
    private String email;

    @Column(name="password")
    private String password;

    @Builder
    public User(String email, String password, String auth, String nickname){
        System.out.println("엔티티 User 생성자 진입 ");

        this.email=email;
        this.password = password;
        this.nickname=nickname;
    }

    //OAuth 키추가
    @Column(name="nickname", unique = true)
    private String nickname;
    
    //사용자이름변경OAuth로그인
    public User update(String nickname){
        System.out.println("엔티티클래스 User update() 진입 파라미터 OAuth2이용한 nickname ->"+nickname);
        System.out.println("엔티티클래스 User update() 진입 기존 nickname ->"+this.nickname);
        this.nickname =nickname;
        return this;
    }
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        System.out.println("엔티티 User 오버라이딩 getAuthorities() 진입 사용자가 가진 권한 목록을 반환한다.");

        return List.of(new SimpleGrantedAuthority("user"));
    }

    @Override
    public String getUsername() {
        System.out.println("엔티티 User 오버라이딩 getAuthorities() 진입 사용자 아이디인 email 반환 ");
        return email;
    }

    @Override
    public String getPassword(){
        System.out.println("엔티티 User 오버라이딩 getPassword() 진입 사용자 패스워드반환");
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        System.out.println("엔티티 User 오버라이딩 isAccountNonExpired() 계정만료여부확인 true 만료되지 않음");
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        System.out.println("엔티티 User 오버라이딩 isAccountNonLocked() 계정잠금되었는지 true 잠금되지 않음 ");
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {

        System.out.println("엔티티 User 오버라이딩 isCredentialsNonExpired() 패스워드만료여부확인 true 만료되지 않음 ");

        return true;
    }

    @Override
    public boolean isEnabled() {
        System.out.println("엔티티 User 오버라이딩 isCredentialsNonExpired() 계정사용가능여부확인 true 사용가능 ");

        return true;
    }
}
