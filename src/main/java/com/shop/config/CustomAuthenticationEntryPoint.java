package com.shop.config;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*인증되지 않은 사용자가 리소스를 요청할 경우 "Unauthorized"에러를 발생하도록 Authentication EntryPoint 인터페이스를 구현*/
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override public void commence
            (HttpServletRequest request, HttpServletResponse response,
             AuthenticationException authenticationException) throws IOException, ServletException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }
}
