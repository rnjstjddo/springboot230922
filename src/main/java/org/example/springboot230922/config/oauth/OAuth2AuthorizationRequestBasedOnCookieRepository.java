package org.example.springboot230922.config.oauth;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.springboot230922.util.CookieUtil;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.web.util.WebUtils;

public class OAuth2AuthorizationRequestBasedOnCookieRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    public final static String OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME="oauth2_auth_request";
    private final static int COOKIE_EXPIRE_SECONDS=18000;


    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        System.out.println("Config-oauth 정보저장클래스 OAuth2AuthorizationRequestBasedOnCookieRepository 오버라이딩 loadAuthorizationRequest() 진입");

        System.out.println("Config-oauth 정보저장클래스 OAuth2AuthorizationRequestBasedOnCookieRepository 오버라이딩 loadAuthorizationRequest() 진입 파라미터 HttpServletRequest-> "+ request);
        Cookie c = WebUtils.getCookie(request, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
        System.out.println("Config-oauth 정보저장클래스 OAuth2AuthorizationRequestBasedOnCookieRepository 오버라이딩 loadAuthorizationRequest() 진입 생성한 Cookie -> "+ c);

        return CookieUtil.deserialize(c, OAuth2AuthorizationRequest.class);
    }

    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("Config-oauth 정보저장클래스 OAuth2AuthorizationRequestBasedOnCookieRepository 오버라이딩 saveAuthorizationRequest() 진입 파라미터 OAuth2AuthorizationRequest -> "+ authorizationRequest);

        if(authorizationRequest ==null){
            System.out.println("OAuth2AuthorizationRequest 없을경우");
            removeAuthorizationRequestCookies(request, response);
            return;
        }

        CookieUtil.addCookie(response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME, CookieUtil.serialize(authorizationRequest), COOKIE_EXPIRE_SECONDS);

    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("Config-oauth 정보저장클래스 OAuth2AuthorizationRequestBasedOnCookieRepository 오버라이딩 removeAuthorizationRequest() 진입");

        return this.loadAuthorizationRequest(request);
    }


    public void removeAuthorizationRequestCookies(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("Config-oauth 정보저장클래스 OAuth2AuthorizationRequestBasedOnCookieRepository removeAuthorizationRequestCookies() 진입");

        CookieUtil.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
    }
}
