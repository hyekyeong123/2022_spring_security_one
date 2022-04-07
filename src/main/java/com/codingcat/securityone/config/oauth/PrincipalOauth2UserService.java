package com.codingcat.securityone.config.oauth;

import com.codingcat.securityone.config.auth.PrincipalDetails;
import com.codingcat.securityone.config.oauth.provider.FacebookUserInfo;
import com.codingcat.securityone.config.oauth.provider.GoogleUserInfo;
import com.codingcat.securityone.config.oauth.provider.NaverUserInfo;
import com.codingcat.securityone.config.oauth.provider.OAuth2UserInfo;
import com.codingcat.securityone.model.User;
import com.codingcat.securityone.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

// PrincipalOauth2UserService는 Spring Security의 DefaultOAuth2UserService를 상속받고 loadUser() 메소드를 구현합니다. 이 메서드는 OAuth2 공급자로부터 액세스 토큰을 얻은 후에 호출됩니다.
@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private UserRepository userRepository;

    // 구글로부터 userRequest 데이터에 대한 후처리 되는 함수
    // 액세스 토큰, 프로필 정보 가져올 수 있음
    // 함수 종료시 @AuthenticationPrincipal 어노테이션이 만들어진다.
    @Override
    public OAuth2User loadUser(
            OAuth2UserRequest userRequest
    ) throws OAuth2AuthenticationException {
        System.out.println("[JHG] userRequest : "+userRequest);

        // 구글로부터 회원 정보 가져오기
        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println("[JHG] getAttributes : "+oAuth2User.getAttributes());

        // 회원가입을 강제로 진행
        OAuth2UserInfo oAuth2UserInfo = null;
        String provider = userRequest.getClientRegistration().getRegistrationId(); // google
        if(provider.equals("google")){
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        }else if(provider.equals("facebook")){
            oAuth2UserInfo = new FacebookUserInfo(oAuth2User.getAttributes());
        }else if(provider.equals("naver")){
            oAuth2UserInfo = new NaverUserInfo((Map)oAuth2User.getAttributes().get("response"));
        }

        String providerId = oAuth2UserInfo.getProviderId();
        String userName = provider + "_"+providerId;
        String password = bCryptPasswordEncoder.encode("JHG"); // 임의로 패스워드 생성
        String email = oAuth2UserInfo.getEmail();
        String role = "ROLE_USER";

        // 이미 가입한 정보가 있는지 확인하고 없다면 유저 저장하기
        User user = userRepository.findByUsername(userName);
        if(user == null){
            user = User.builder()
                    .username(userName)
                    .password(password)
                    .email(email)
                    .role(role)
                    .provider(provider)
                    .providerId(providerId)
                    .build();
            userRepository.save(user);
        }

        return new PrincipalDetails(user, oAuth2User.getAttributes());
    }
}
/*
userRequest.getClientRegistration(); // 어떤 OAuth로 로그인하였는지 확인
userRequest.getAttributes()
*/
