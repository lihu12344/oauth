package com.example.demo.oauth2.alipay;

import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationExchange;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class AlipayOAuth2AccessTokenResponseClient implements OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> {

    @Resource
    private AlipayClient alipayClient;

    @Override
    public OAuth2AccessTokenResponse getTokenResponse(OAuth2AuthorizationCodeGrantRequest oAuth2AuthorizationCodeGrantRequest) {
        System.out.println("进入alipay客户端");

        OAuth2AuthorizationExchange oAuth2AuthorizationExchange=oAuth2AuthorizationCodeGrantRequest.getAuthorizationExchange();
        String auth_code=oAuth2AuthorizationCodeGrantRequest.getAuthorizationExchange().getAuthorizationResponse().getCode();

        AlipaySystemOauthTokenRequest oauthTokenRequest=new AlipaySystemOauthTokenRequest();
        oauthTokenRequest.setCode(auth_code);
        oauthTokenRequest.setGrantType("authorization_code");

        try{
            AlipaySystemOauthTokenResponse oauthTokenResponse=alipayClient.execute(oauthTokenRequest);

            if (oauthTokenResponse.isSuccess()){
                return OAuth2AccessTokenResponse.withToken(oauthTokenResponse.getAccessToken())
                        .tokenType(OAuth2AccessToken.TokenType.BEARER)
                        .refreshToken(oauthTokenResponse.getRefreshToken())
                        .expiresIn(Long.parseLong(oauthTokenResponse.getExpiresIn()))
                        .scopes(oAuth2AuthorizationExchange.getAuthorizationRequest().getScopes())
                        .build();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }
}