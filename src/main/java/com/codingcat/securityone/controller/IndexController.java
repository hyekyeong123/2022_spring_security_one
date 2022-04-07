package com.codingcat.securityone.controller;

import com.codingcat.securityone.config.auth.PrincipalDetails;
import com.codingcat.securityone.model.User;
import com.codingcat.securityone.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
//  ************************************

    @GetMapping({"", "/"})
    public String indexView(){
        return "index";
    }

    // 일반로그인이든 oauth 로그인이든 principalDetails로 받음
    @GetMapping("/user")
    public @ResponseBody String user(
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ){
        System.out.println("[JHG] principalDetails : "+principalDetails.getUser());
        return "user";

    }

    @GetMapping("/admin")
    public @ResponseBody String admin(){
        return "admin";
    }

    @GetMapping("/manager")
    public @ResponseBody String manager(){
        return "manager";
    }
//  START ******************************** 로그인 *************************************

    // 현재 스프링 시큐리티와 이름이 겹침
    @GetMapping("/loginForm")
    public String loginView(){
        return "loginForm";
    }

    // 일반 로그인 - 유저 정보 가져오는 두가지 방법
    @GetMapping("/test/login")
    public String loginTest(
            Authentication authentication,
            @AuthenticationPrincipal PrincipalDetails principalDetails
            ){ // DI(의존성 주입)

        /*
        방법 1
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        System.out.println("[JHG] authentication.getUser :" + principalDetails.getUser());
        */

        // 방법 2
        System.out.println("[JHG] userDetails : "+principalDetails.getUser());
        return "loginTest";

    }

    // 구글 로그인 - 유저 정보 가져오는 두가지 방법
    @GetMapping("/test/oauth/login")
    public String oauthLoginTest(
            Authentication authentication,
            @AuthenticationPrincipal OAuth2User oAuth2User
    ){ // DI(의존성 주입)


        // 방법 1
//        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
//        System.out.println("[JHG] authentication.getUser :" + oAuth2User.getAttributes());

        // [JHG] authentication.getUser :{sub=110272766628283343927, name=정ᄋᄋ, given_name=ᄋᄋ, family_name=정, picture=https://lh3.googleusercontent.com/a-/AOh14GhowIByYBu93BzWNEZSMLi3NLEivamFUYT4JRa49g=s96-c, email=jhg507677@gmail.com, email_verified=true, locale=ko}

        // 방법 2
        System.out.println("[JHG] oAuth2User.getAttributes()"+ oAuth2User.getAttributes());

        return "oauthLoginTest";

    }
//  END ******************************** 로그인 *************************************

//  START ******************************** 회원가입 *************************************

    @GetMapping("/joinForm")
    public String joinView(){
        return "joinForm";
    }

    @PostMapping("/join")
    public String joinAction(User user){

        user.setRole("ROLE_USER");

        // 비밀번호 암호화 하기기
        String oldPassword = user.getPassword();
        String newPassword = bCryptPasswordEncoder.encode(oldPassword);
        user.setPassword(newPassword);

        userRepository.save(user);
        return "redirect:/loginForm";
    }
//   END ******************************** 회원가입 *************************************

    @Secured("ROLE_ADMIN") //특정 메서드에 간단하게 걸고 싶을때
    @GetMapping("/info")
    public @ResponseBody String infoView(){
        return "개인정보";
    }

    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')") // 메서드가 실행되기 직전에(조건 두개 이상일때 사용)
    @GetMapping("/data")
    public @ResponseBody String dataView(){
        return "data";
    }
}
