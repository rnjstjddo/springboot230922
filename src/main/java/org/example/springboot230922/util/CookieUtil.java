package org.example.springboot230922.util;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.SerializationUtils;

import java.util.Base64;

public class CookieUtil {

    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge){
        System.out.println("Util클래스 CookieUtil addCookie() 진입");

        Cookie c = new Cookie(name, value);
        System.out.println("Util클래스 CookieUtil addCookie() 진입 생성한 Cookie ->" +c.toString());

        c.setPath("/");
        c.setMaxAge(maxAge);
        response.addCookie(c);
    }

    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name){
        System.out.println("Util클래스 CookieUtil deleteCookie() 진입");
        System.out.println("Util클래스 CookieUtil deleteCookie() 진입 파라미터 name -> "+name);

        Cookie [] cs = request.getCookies();
        if(cs == null){
            System.out.println("Util클래스 CookieUtil deleteCookie() 진입 쿠키 존재하지 않는경우");

            return;
        }

        for(Cookie c: cs){
            if(name.equals(c.getName())){
                System.out.println("Util클래스 CookieUtil deleteCookie() 진입 쿠키있다면 삭제한다.");

                c.setValue("");
                c.setPath("/");
                c.setMaxAge(0);
                response.addCookie(c);
            }

        }
    }

    public static String serialize(Object o){
        System.out.println("Util클래스 CookieUtil serialize() 진입 객체 직렬화해서 쿠키값으로 변환 ");
        return Base64.getUrlEncoder().encodeToString(SerializationUtils.serialize(o));
    }

    public static <T> T deserialize(Cookie c, Class<T> cls){
        System.out.println("Util클래스 CookieUtil serialize() 진입 쿠키 역직렬화해서 객체로 변환 ");

        return cls.cast(SerializationUtils.deserialize(Base64.getUrlDecoder().decode(c.getValue())));

    }
}
