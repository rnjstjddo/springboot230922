package org.example.springboot230922.config.oauth;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.el.parser.Token;
import org.example.springboot230922.config.jwt.TokenProvider;
import org.example.springboot230922.domain.User;
import org.example.springboot230922.domain.jwt.RefreshToken;
import org.example.springboot230922.repository.jwt.RefreshTokenRepository;
import org.example.springboot230922.service.UserService;
import org.example.springboot230922.util.CookieUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.Duration;

@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {


    public static final String REFRESH_TOKEN_COOKIE_NAME="refresh_token";
    public static final Duration REFRESH_TOKEN_DURATION=Duration.ofDays(14);
    public static final Duration ACCESS_TOKEN_DURATION =Duration.ofDays(1);

    public static final String REDIRECT_PATH="/articles";

    private final TokenProvider tp;
    private final RefreshTokenRepository rr;
    private final OAuth2AuthorizationRequestBasedOnCookieRepository or;
    private final UserService us;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        System.out.println("cofing-oauth 성공핸들러 OAuth2SuccessHandler 오버라이딩 onAuthenticationSuccess() 진입 파라미터 Authentication -> "+ authentication);

        OAuth2User ou = (OAuth2User) authentication.getPrincipal();

        System.out.println("cofing-oauth 성공핸들러 OAuth2SuccessHandler 오버라이딩 onAuthenticationSuccess() 진입 OAuth2User -> "+ ou.toString());

        User u = us.findByEmail( (String) ou.getAttributes().get("email"));
        System.out.println("cofing-oauth 성공핸들러 OAuth2SuccessHandler 오버라이딩 onAuthenticationSuccess() 진입 User -> "+u.toString());

        //리프레시 토큰 생성 저장 쿠키 저장
        String refreshToken = tp.generateToken(u, REFRESH_TOKEN_DURATION);
        System.out.println("cofing-oauth 성공핸들러 OAuth2SuccessHandler 오버라이딩 onAuthenticationSuccess() 진입 리프레시토큰생성" +refreshToken);

        saveRefreshToken(u.getId(), refreshToken);
        addRefreshTokenToCookie(request, response, refreshToken);

        String accessToken = tp.generateToken(u, ACCESS_TOKEN_DURATION);

        System.out.println("cofing-oauth 성공핸들러 OAuth2SuccessHandler 오버라이딩 onAuthenticationSuccess() 진입 엑세스토큰생성" +accessToken);
        String url = getTargetUrl(accessToken);

        System.out.println("cofing-oauth 성공핸들러 OAuth2SuccessHandler 오버라이딩 onAuthenticationSuccess() 진입 url -> "+ url);

        clearAuthenticationAttributes(request, response);
        
        //리다이렉트
        getRedirectStrategy().sendRedirect(request, response, url);

    }

    private void saveRefreshToken( Long userId, String newRefreshToken){
        System.out.println("cofing-oauth 성공핸들러 OAuth2SuccessHandler saveRefreshToken() 진입 새로생성한 리프레시토큰 DB저장");

        RefreshToken rt = rr.findByUserId(userId)
                .map(entity -> entity.update(newRefreshToken)).orElse(new RefreshToken(userId, newRefreshToken ));

        System.out.println("cofing-oauth 성공핸들러 OAuth2SuccessHandler saveRefreshToken() 진입 새로생성한 리프레시토큰 DB저장 생성한 리프레시토큰 -> "+ rt);

        rr.save(rt);
    }

    private void addRefreshTokenToCookie( HttpServletRequest request,HttpServletResponse response, String rt){
        System.out.println("cofing-oauth 성공핸들러 addRefreshTokenToCookie() 진입 새로생성한 리프레시토큰 쿠키에 저장");

        int cookieMaxAge =(int) REFRESH_TOKEN_DURATION.toSeconds();
        CookieUtil.deleteCookie(request, response, REFRESH_TOKEN_COOKIE_NAME);

        CookieUtil.addCookie(response, REFRESH_TOKEN_COOKIE_NAME, rt, cookieMaxAge);

    }

    private void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response){
        System.out.println("cofing-oauth 성공핸들러 clearAuthenticationAttributes() 진입 인증관련 설정값 쿠키제거");

        super.clearAuthenticationAttributes(request);
        or.removeAuthorizationRequestCookies(request, response);

    }

    //엑세스 토큰 패스추가
    private String getTargetUrl(String token){
        System.out.println("cofing-oauth 성공핸들러 getTargetUrl() 진입 엑세스토큰 PATH추가 파라미터 엑세스토큰 -> "+ token);

        return UriComponentsBuilder.fromUriString(REDIRECT_PATH)
                .queryParam("token", token)
                .build().toUriString();
    }
}


