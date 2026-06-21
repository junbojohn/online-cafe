package com.example.online.cafe.domain.manager.controller;

import com.example.online.cafe.domain.manager.auth.JwtUtil;
import com.example.online.cafe.domain.manager.entity.Manager;
import com.example.online.cafe.domain.manager.repository.ManagerRepository;
import com.example.online.cafe.domain.manager.service.CustomManagerService;
import com.example.online.cafe.domain.manager.service.ManagerService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/*
관리자 회원가입, 로그인, 그리고 로그아웃 기능을 담당한다.(POST 요청)

회원가입, 로그인을 하게 해주는 html 페이지를 띄우는 GET 요청들은 'ManagerController' 에 있다.
*/

//@RestController
@Controller
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    ManagerRepository managerRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtil jwtUtils;

    @Autowired
    CustomManagerService managerService;

    // 존재하는 관리자가 로그인을 시도하면 JWT 토큰을 생성한다.
    @PostMapping("/login")
    public String authenticateUser(
            //@RequestBody
            //Manager manager

            @RequestParam
            String username,

            @RequestParam
            String password,

            HttpServletResponse response
    ) {
        /*
        //System.out.println("Now entering AuthController\n");

        //System.out.println("Starting authentication\n");

        //System.out.println("Entered password: " + password);

        //UserDetails user = managerService.loadUserByUsername(username);

        //System.out.println("login attempt as: " + username);
        //System.out.println("login password: " + password);

        //System.out.println("encoded password in DB: " + user.getPassword());

        //System.out.println("encoded password: " + user.getPassword());

        //System.out.println("Does password and encode match?: " + encoder.matches(password, user.getPassword()));

        //System.out.println("does password and encode match using hardcode?: " + encoder.matches("2424", user.getPassword()));
        //System.out.println("does password and encode match using password parameter?: " + encoder.matches(password, user.getPassword()) + "\n");

        //System.out.println("encoder.getClass(): " + encoder.getClass());

        //System.out.println("[" + password + "]");
        //System.out.println("password length: " + password.length());

        //System.out.println("encoded password: " + user.getPassword());
        //System.out.println("does password and encode match using hardcode?: " + encoder.matches("2525", user.getPassword()));
        //System.out.println("does password and encode match using password parameter?: " + encoder.matches("2525", user.getPassword()) + "\n");

        //System.out.println("password equals?: " + password.equals("2424"));
        //System.out.println("password bytes length: " + password.getBytes().length);
        */

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            username,
                            password
                    )
            );

            System.out.println("Authentication succeeded");

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtUtils.generateToken(userDetails.getUsername());

            System.out.println("생성한 토큰: " + token);

            Cookie cookie = new Cookie("jwt", token);

            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60);

            response.addCookie(cookie);

        } catch (Exception e) {
            e.printStackTrace();
        }

        /*
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        username,
                        password
                )
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtUtils.generateToken(userDetails.getUsername());

        System.out.println("생성한 토큰: " + token);

        Cookie cookie = new Cookie("jwt", token);

        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60);

        response.addCookie(cookie);
        */

        return "redirect:/manager/menu";
        //return jwtUtils.generateToken(userDetails.getUsername());

        /*
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        manager.getUsername(),
                        manager.getPassword()
                )
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return jwtUtils.generateToken(userDetails.getUsername());
        */
    }

    // 로그인한 관리자에게 로그아웃을 하게 해준다.
    // 로그아웃 하면 쿠키가 삭제됨으로서 인증할때 필요한 JWT를 더 이상 들고 다니지 못하게 한다.
    @PostMapping("/logout")
    public String logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("jwt", null);

        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);

        response.addCookie(cookie);

        SecurityContextHolder.clearContext();

        return "redirect:/manager/login";
    }


    // 새로운 관리자가 가입하려고 하면 가입 작업을 진행한다.
    @PostMapping("/signup")
    public String registerUser(
            //@RequestBody
            //Manager manager

            @RequestParam
            String username,

            @RequestParam
            String password
    ) {

        // 가입하려는 username이 이미 존재하는지 확인한다.
        // 이미 존재한다면 가입 작업을 중단하고 가입 페이지로 그대로 리다이렉트 시킨다.
        if (managerRepository.existsByUsername(username)) {
            return "redirect:/manager/signup";
            //return "오류: 이미 사용되는 닉네임 입니다.";
        }

        System.out.println("[" + password + "]");
        System.out.println("password length: " + password.length());

        String encoded = encoder.encode(password);

        System.out.println("password: " + password);
        System.out.println("encoded: " + encoded);
        System.out.println(encoder.matches(password, encoded));

        // 새로운 관리자 데이터를 생성한다.
        Manager newManager = Manager.builder()
                .username(username)
                .password(encoder.encode(password))
                .build();

        // 새로 생성한 관리자 데이터를 'Manager' 테이블에 삽입한다.
        managerRepository.save(newManager);

        // 가입 작업 완료 후, 상품 목록 보기, 관리자 로그인, 관리자 가입 링크를 보여주는 첫 페이지인 "/welcome"으로 리다이렉트 시킨다.
        return "redirect:/welcome";
        //return "관리자 가입이 완료 되었습니다.";

        /*
        if (managerRepository.existsByUsername(manager.getUsername())) {
            return "오류: 이미 사용되는 닉네임 입니다.";
            //return "Error: Username is already taken!";
        }

        // Create new user's account
        Manager newManager = new Manager(
                null,
                manager.getUsername(),
                encoder.encode(manager.getPassword())
        );

        managerRepository.save(newManager);

        return "관리자 가입이 완료 되었습니다.";
        //return "User registered successfully!";
        */
    }
}
