package com.codingcat.securityone.config.oauth.provider;

// OAuth에 따라 가져오는 양식이 다르기 때문에 interface 정의
public interface OAuth2UserInfo {
    String getProviderId();
    String getProvider(); // google, facebook
    String getEmail();
    String getName();
}
