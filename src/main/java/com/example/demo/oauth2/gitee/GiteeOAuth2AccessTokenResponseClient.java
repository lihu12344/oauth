package com.example.demo.oauth2.gitee;

import com.alibaba.fastjson.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationExchange;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service
public class GiteeOAuth2AccessTokenResponseClient implements OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> {

    @Resource
    private RestTemplate restTemplate;

    @Override
    public OAuth2AccessTokenResponse getTokenResponse(OAuth2AuthorizationCodeGrantRequest oAuth2AuthorizationCodeGrantRequest) {
        System.out.println("进入code 客户端");

        ClientRegistration clientRegistration=oAuth2AuthorizationCodeGrantRequest.getClientRegistration();

        System.out.println("registerId："+clientRegistration.getRegistrationId());
        OAuth2AuthorizationExchange oAuth2AuthorizationExchange=oAuth2AuthorizationCodeGrantRequest.getAuthorizationExchange();

        Map<String,String> params=new HashMap<>();
        params.put("client_id",clientRegistration.getClientId());
        params.put("client_secret",clientRegistration.getClientSecret());
        params.put("grant_type",clientRegistration.getAuthorizationGrantType().getValue());
        params.put("code",oAuth2AuthorizationExchange.getAuthorizationResponse().getCode());
        params.put("redirect_uri",oAuth2AuthorizationExchange.getAuthorizationResponse().getRedirectUri());

        HttpHeaders httpHeaders=new HttpHeaders();
        httpHeaders.add("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36");

        HttpEntity<Map<String,String>> entity=new HttpEntity<>(params,httpHeaders);

        String accessTokenResponse=restTemplate.postForObject(clientRegistration.getProviderDetails().getTokenUri(), entity,String.class);

        JSONObject object=JSONObject.parseObject(accessTokenResponse);
        String accessToken=object.getString("access_token");
        String refresh_token=object.getString("refresh_token");
        String expires_in=object.getString("expires_in");

        System.out.println(accessToken);
        System.out.println(refresh_token);
        System.out.println(expires_in);

        return OAuth2AccessTokenResponse.withToken(accessToken)
                .refreshToken(refresh_token)
                .expiresIn(Long.parseLong(expires_in))
                .tokenType(OAuth2AccessToken.TokenType.BEARER)
                .scopes(oAuth2AuthorizationExchange.getAuthorizationRequest().getScopes())
                .build();
    }
}
