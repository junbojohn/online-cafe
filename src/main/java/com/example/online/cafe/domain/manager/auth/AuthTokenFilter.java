package com.example.online.cafe.domain.manager.auth;

import com.example.online.cafe.domain.manager.service.CustomManagerService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.*;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

// 매 요청마다 JWT를 검사해서 사용자가 로그인한 상태인지 확인하는 역할을 담당한다.

//@Component
public class AuthTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtils;

    @Autowired
    private CustomManagerService managerService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            System.out.println("필터 실행");

            String jwt = parseJwt(request);

            System.out.println("jwt: " + jwt + "\n");

            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                String username = jwtUtils.getUsernameFromToken(jwt);
                UserDetails userDetails = managerService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        } catch (Exception e) {
            System.out.println("관리자 인증을 설정할 수 없습니다: " + e);
            //System.out.println("Cannot set user authentication: " + e);
        }

        filterChain.doFilter(request, response);
    }

    // 주소에 "/api/auth"로 시작하는 요청들에는 filter를 건너띄게 한다.
    // 현재로선 로그인 및 관리자 가입 페이지에서 정상적으로 다음으로 넘어갈때 쓰이는 요청들의 주소가 "/api/auth"로 시작된다.
    // 로그인은 JWT가 없는 사용자가 로그인하여 JWT를 발급받는 과정인데 filter를 건너띄게 하지 않으면 filter는 항상 JWT 여부를
    // 확인하려는 무의미한 짓을 하게 된다. 따라서 이 의미없는 과정을 생략시키기 위해 'shouldNotFilter' 함수를 사용한다.
    // 쉽게 말하자면 "/api/auth/login"와 "/api/auth/signup"은 아직 JWT가 없는 사용자가 접근하는 곳이기에 이런 요청들은
    // 필터를 건너띄게 하는게 효율적이다.
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();

        return path.startsWith("/api/auth");
    }

    private String parseJwt(HttpServletRequest request) {

        // chatGPT 'JWT 로그인 상태 유지' 채팅에서 나온 답변 내용:

        // 프로젝트가 무엇이냐에 따라 Header와 Cookie 둘 중 무엇을 먼저 확인하고 다른 하나를 그 다음에 확인할지 다르다.
        // 프로젝트가 React, Vue, Angular, Flutter, Android, iOS 같은 프로젝트라면 Header를 먼저 확인, 그 다음에 Cookie를 확인한다.
        // 프로젝트가 Spring MVC, Thymeleaf, JSP 같은 프로젝트라면 반대로 Cookie를 먼저 확인, 그 다음에 Header를 확인한다.

        // Header 방식: 브라우저 -> JS가 localStorage에서 JWT 읽음 -> Authorization Header에 직접 넣음 -> 서버
        // 즉 JavaScript가 매번 Header를 만들어야 한다.

        // Cookie 방식: 로그인 -> 브라우저가 Cookie 저장 -> 다음 요청 -> 브라우저가 자동 전송
        // JavaScript가 할 일은 별로 없다.

        // 본 프로젝트는 Spring MVC + Thymeleaf 기반인 관계로 Cookie를 먼저 확인하고 그 다음에 Header를 확인한다.

        // 1.) 먼저 Cookie를 확인한다.
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("jwt".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        // 2.) 그 다음, Authorization Header을 확인한다.
        String headerAuth = request.getHeader("Authorization");

        if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }



        return null;
    }
}
