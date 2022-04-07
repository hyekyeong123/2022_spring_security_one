package com.codingcat.securityone.config;

import com.codingcat.securityone.config.oauth.PrincipalOauth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity // 스프링 시큐리티 필터가 스프링 필터 체인에 등록이 된다.
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) // securedEnabled = true : secured 어노테이션 활성화, prePostEnabled = true : preAuthorize, PostAuthorize 어노테이션 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // 비밀번호 암호화
    @Bean // 해당 메서드의 리턴되는 오브젝트를 IOC로 등록해준다.
    public BCryptPasswordEncoder encodePwd(){
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private PrincipalOauth2UserService principalOauth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
            .antMatchers("/user/**").authenticated() // 로그인 필요

            .antMatchers("/manager/**").
                access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")

            .antMatchers("/admin/**").
                access("hasRole('ROLE_ADMIN')")

            .anyRequest().permitAll()

            .and()
            .formLogin().loginPage("/loginForm") // 권한 실패시 로그인 페이지로 이동하게

            // login 주소가 호출이 되면 시큐리티가 낚아채서 대신 로그인을 진행해줍니다.
            .loginProcessingUrl("/login")

            .defaultSuccessUrl("/") // 로그인 성공하면 메인페이지로 이동

            .and()
            .oauth2Login().loginPage("/loginForm")
            .userInfoEndpoint().userService(principalOauth2UserService);

            // 구글 로그인 완료 된 후 후처리 필요함
            // 일반적이라면 1. 코드받기(인증), 2. 엑세스 토큰(권한), 3. 사용자 프로필 정보를 가져오기
            // oauth의 경우 코드 X, 액세스 토근 + 사용자 프로필 정보 동시에
            // 4-1. 그 정보를 토대로 회원가입을 자동으로 진행시키기도 함
            // 4-2. 그 정보가 모자르다면 추가로 더 입력하게 할 수 있음

    }
}
