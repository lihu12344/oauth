package com.example.demo.oauth2;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.HashMap;
import java.util.Map;

public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    Map<String,OAuth2UserService<OAuth2UserRequest,OAuth2User>> userServiceMap;

    private final String default_key="default";

    public CustomOAuth2UserService(){
        userServiceMap=new HashMap<>();
        userServiceMap.put(default_key,new DefaultOAuth2UserService());
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest,OAuth2User> userService=userServiceMap.get(oAuth2UserRequest.getClientRegistration().getRegistrationId());

        if (userService==null){
            userService=userServiceMap.get(default_key);
        }

        return userService.loadUser(oAuth2UserRequest);
    }

    public Map<String, OAuth2UserService<OAuth2UserRequest, OAuth2User>> getUserServiceMap() {
        return userServiceMap;
    }
}
