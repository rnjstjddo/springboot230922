package org.example.springboot230922.service;


import lombok.RequiredArgsConstructor;
import org.example.springboot230922.domain.User;
import org.example.springboot230922.dto.security.AddUserRequest;
import org.example.springboot230922.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository ur;

//oauth2+jwt이용시 주석처리
//    private final BCryptPasswordEncoder encoder;

    public Long save(AddUserRequest dto){
        System.out.println("서비스클래스 UserService save() 진입 User객체생성 후 DB저장 ");

        //추가
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        return ur.save(User.builder().email(dto.getEmail()).password(encoder.encode(dto.getPassword()))
                .build()).getId();
    }
    
    //리프레시토큰 가지고 있다면 새로운 엑세스토큰 발급
    public User findById(Long userId){
        System.out.println("서비스클래스 UserService findById() 진입 리프레시토큰으로 새로운 엑세스 토큰 발급 파라미터 사용자id -> "+ userId);
        return ur.findById(userId).orElseThrow( () -> new IllegalArgumentException(("존재하지 사용자입니다. -> "+ userId )));
    }
    
    //추가 oauth+jwt
    public User findByEmail(String email){
        System.out.println("서비스클래스 UserService findByEmail() 진입 oauth2 로그인 이메일을 이용해서 User엔티티에서사용자정보찾는다 email -> "+ email);
        return ur.findByEmail(email).orElseThrow( () -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
    }
}
