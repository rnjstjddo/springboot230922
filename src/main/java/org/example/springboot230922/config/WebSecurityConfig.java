package org.example.springboot230922.config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;
/*

@RequiredArgsConstructor
@Configuration
public class WebSecurityConfig {

    private final UserDetailsService us;

    @Bean
    public WebSecurityCustomizer configure(){
        System.out.println("Config클래스 WebSecurityConfig configure() 진입 스프링 시큐리티 기능 비활성화 - 반환타입 WebSecurtiyCustomizer ");
        return (web) -> web.ignoring().requestMatchers(toH2Console()).requestMatchers("/static/**");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        System.out.println("Config클래스 WebSecurityConfig filterChain() 진입 특정HTTP요청에 대한 웹 기반 보안구성 -반환타입 SecurityFilterChain ");
        return http.authorizeHttpRequests().requestMatchers("/login", "/signup", "/user").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/login")
                .defaultSuccessUrl("/articles")
                .and()
                .logout()
                .logoutSuccessUrl("/login")
                .invalidateHttpSession(true)
                .and()
                .csrf().disable().build();
    }


    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder encoder, UserDetailsService us)throws Exception{

        System.out.println("Config클래스 WebSecutiryConfig authenticationManager() 진입 인증관리자 관련설정 -반환타입 AuthenticationManager ");

        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(us)
                .passwordEncoder(encoder)
                .and()
                .build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        System.out.println("Config클래스 bCryptPasswordEncoder() 진입 패스워드 인코더로 사용할 빈등록 -반환타입 BCryptPasswordEncoder ");

        return new BCryptPasswordEncoder();
    }
}
*/
