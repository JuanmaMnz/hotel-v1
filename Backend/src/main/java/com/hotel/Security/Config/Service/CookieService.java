package com.hotel.Security.Config.Service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

@Service
public class CookieService {

    private String cookieDomain = "localhost";
    private String cookiePath = "/";

    public void addCookie(HttpServletResponse response, String cookieName, String cookieValue, int maxAgeInSeconds){
        Cookie cookie = new Cookie(cookieName, cookieValue);
        cookie.setDomain(cookieDomain);
        cookie.setPath(cookiePath);
        cookie.setMaxAge(maxAgeInSeconds);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        response.addCookie(cookie);
    }

    public void addCookie(HttpServletResponse response, String cookieName, String cookieValue, int maxAgeInSeconds, String path){
        Cookie cookie = new Cookie(cookieName, cookieValue);
        cookie.setDomain(cookieDomain);
        cookie.setPath(path);
        cookie.setMaxAge(maxAgeInSeconds);
        cookie.setHttpOnly(false);
        cookie.setSecure(false);
        response.addCookie(cookie);
    }

    public void expireCookie(HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setDomain(cookieDomain);
        cookie.setPath(cookiePath);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    public String getCookieValue(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

}