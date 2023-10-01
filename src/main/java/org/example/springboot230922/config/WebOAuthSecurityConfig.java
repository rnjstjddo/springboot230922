package org.example.springboot230922.config;


import lombok.RequiredArgsConstructor;
import org.example.springboot230922.config.jwt.TokenAuthenticationFilter;
import org.example.springboot230922.config.jwt.TokenProvider;
import org.example.springboot230922.config.oauth.OAuth2AuthorizationRequestBasedOnCookieRepository;
import org.example.springboot230922.config.oauth.OAuth2SuccessHandler;
import org.example.springboot230922.config.oauth.OAuth2UserCustomService;
import org.example.springboot230922.repository.jwt.RefreshTokenRepository;
import org.example.springboot230922.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.rmi.server.ExportException;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@RequiredArgsConstructor
@Configuration
public class WebOAuthSecurityConfig {

    private final OAuth2UserCustomService os;
    private final TokenProvider tp;
    private final RefreshTokenRepository rr;
    private final UserService us;

    @Bean
    public WebSecurityCustomizer configure(){
        System.out.println("Cofig클래스 WebOAuthSecurityConfig configure() 진입 시큐리티 기능 비활성화");
        return (web) -> web.ignoring().requestMatchers(toH2Console())
                .requestMatchers("/img/**", "/css/**", "/js/**", "/");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        System.out.println("Cofig클래스 WebOAuthSecurityConfig filterChain() 진입 기존폼로그인과 세션 비활성화");
        http.csrf().disable()
                .httpBasic().disable()
                .formLogin().disable()
                .logout().disable();

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        http.authorizeHttpRequests().requestMatchers("/api/token").permitAll()
                .requestMatchers("/api/**").authenticated().anyRequest().permitAll();

        http.oauth2Login().loginPage("/login").authorizationEndpoint()
                .authorizationRequestRepository(oAuth2AuthorizationRequestBasedOnCookieRepository())
                .and()
                .successHandler(oAuth2SuccessHandler())
                .userInfoEndpoint()
                .userService(os);

        http.logout().logoutSuccessUrl("/login");

        http.exceptionHandling().defaultAuthenticationEntryPointFor(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED), new AntPathRequestMatcher("/api/**"));

        return http.build();

    }

    @Bean
    public OAuth2SuccessHandler oAuth2SuccessHandler(){
        System.out.println("Config클래스 WebOAuthSecurityConfig oAuth2SuccessHandler() 진입");

        return new OAuth2SuccessHandler(tp, rr, oAuth2AuthorizationRequestBasedOnCookieRepository(), us);
    }

    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter(){
        System.out.println("Config클래스 WebOAuthSecurityConfig tokenAuthenticationFilter() 진입");

        return new TokenAuthenticationFilter(tp);
    }

    @Bean
    public OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository(){
        System.out.println("Config클래스 WebOAuthSecurityConfig oAuth2AuthorizationRequestBasedOnCookieRepository() 진입");
        return new OAuth2AuthorizationRequestBasedOnCookieRepository();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        System.out.println("Config클래스 WebOAuthSecurityConfig bCryptPasswordEncoder() 진입");
        return new BCryptPasswordEncoder();
    }
}
