package org.example.springboot230922.service;


import lombok.RequiredArgsConstructor;
import org.example.springboot230922.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDetailService implements UserDetailsService {

    private final UserRepository ur;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("서비스클래스 UserDetailService 오버라이딩 loadUserByUsername() 진입 파라미터 email -> "+ email);

        return ur.findByEmail(email).orElseThrow( () -> new IllegalArgumentException("해당 사용자는 존재하지 않습니다. -> "+email));
    }
}
