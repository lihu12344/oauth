package com.example.demo.oauth2;

import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;

import java.util.HashMap;
import java.util.Map;

public class CustomOAuth2AccessTokenResponseClient implements OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> {

    Map<String,OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest>> clientMap;

    private final String default_key="default";

    public CustomOAuth2AccessTokenResponseClient(){
        clientMap=new HashMap<>();
        clientMap.put(default_key,new DefaultAuthorizationCodeTokenResponseClient());
    }

    @Override
    public OAuth2AccessTokenResponse getTokenResponse(OAuth2AuthorizationCodeGrantRequest oAuth2AuthorizationCodeGrantRequest) {
        OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> client=clientMap.get(oAuth2AuthorizationCodeGrantRequest.getClientRegistration().getRegistrationId());

        if (client==null){
            client=clientMap.get(default_key);
        }

        return client.getTokenResponse(oAuth2AuthorizationCodeGrantRequest);
    }

    public Map<String, OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest>> getClientMap() {
        return clientMap;
    }
}
