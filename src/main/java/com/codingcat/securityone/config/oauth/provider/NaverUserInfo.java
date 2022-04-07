package com.codingcat.securityone.config.oauth.provider;

import java.util.Map;

public class NaverUserInfo implements OAuth2UserInfo {
// [JHG] getAttributes : {
//  resultcode=00,
//  message=success,
//  response={id=dQLt7c39VQbvTY4aaVN2iyXAVfifwOUm2tAc6xlUWPc, email=jhg097@jr.naver.com, name=정혜경}
// }
    private Map<String, Object> attributes; // oauth2User.getAttributes

    public NaverUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProviderId() {
        return (String) attributes.get("id");
    }

    @Override
    public String getProvider() {
        return "naver";
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }
}
