package com.example.demo.oauth2.gitee;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service
public class GiteeOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Resource
    private RestTemplate restTemplate;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        String access_token=oAuth2UserRequest.getAccessToken().getTokenValue();
        System.out.println(access_token);

        Map<String,String> params=new HashMap<>();
        params.put("access_token",access_token);

        HttpHeaders httpHeaders=new HttpHeaders();
        httpHeaders.add("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36");

        HttpEntity<Object> entity=new HttpEntity<>(httpHeaders);

        String url=oAuth2UserRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUri()+
                "?access_token={access_token}";

        return restTemplate.exchange(url, HttpMethod.GET,entity, GiteeOAuth2User.class,params).getBody();
    }
}
