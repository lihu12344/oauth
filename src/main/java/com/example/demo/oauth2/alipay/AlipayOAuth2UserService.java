package com.example.demo.oauth2.alipay;

import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayUserInfoShareRequest;
import com.alipay.api.response.AlipayUserInfoShareResponse;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class AlipayOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Resource
    private AlipayClient alipayClient;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        String accessToken=oAuth2UserRequest.getAccessToken().getTokenValue();

        AlipayUserInfoShareRequest userInfoShareRequest=new AlipayUserInfoShareRequest();

        try {
            AlipayUserInfoShareResponse userInfoShareResponse=alipayClient.execute(userInfoShareRequest,accessToken);

            if (userInfoShareResponse.isSuccess()){
                AlipayOAuth2User alipayOAuth2User=new AlipayOAuth2User();
                alipayOAuth2User.setUserId(userInfoShareResponse.getUserId());
                alipayOAuth2User.setGender(userInfoShareResponse.getGender());

                return alipayOAuth2User;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }
}
