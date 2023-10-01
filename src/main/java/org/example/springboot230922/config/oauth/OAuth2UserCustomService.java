package org.example.springboot230922.config.oauth;


import lombok.RequiredArgsConstructor;
import org.example.springboot230922.domain.User;
import org.example.springboot230922.repository.UserRepository;
import org.hibernate.annotations.SelectBeforeUpdate;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class OAuth2UserCustomService extends DefaultOAuth2UserService {

    private final UserRepository ur;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("OAuth서비스클래스 OAuth2UserCustomService 오버라이딩 loadUser() 진입 파라미터 OAuth2UserReqeust -> "+ userRequest.toString());

        OAuth2User ou = super.loadUser(userRequest);
        saveOrUpdate(ou);
        return ou;
    }

    //유저있으면 업데이트 없으면 유저생성
    private User saveOrUpdate(OAuth2User ou){
        System.out.println("OAuth서비스클래스 OAuth2UserCustomService saveOrUpdate() 진입 파라미터 OAuth2User -> "+ ou.toString() );

        Map<String, Object> map = ou.getAttributes();
        String email =(String) map.get("email");
        String name=(String) map.get("name");
        System.out.println("OAuth서비스클래스 OAuth2UserCustomService saveOrUpdate() 진입 파라미터 OAuth2User get(name) 추출 =nickname-> "+ name );


        User u = ur.findByEmail(email).map(entity ->  entity.update(name))
                .orElse(User.builder().email(email).nickname(name).build());
        return ur.save(u);


    }
}
