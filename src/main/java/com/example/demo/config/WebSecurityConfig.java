package com.example.demo.config;

import com.example.demo.oauth2.CustomOAuth2AccessTokenResponseClient;
import com.example.demo.oauth2.CustomOAuth2UserService;
import com.example.demo.oauth2.alipay.AlipayOAuth2AccessTokenResponseClient;
import com.example.demo.oauth2.alipay.AlipayOAuth2UserService;
import com.example.demo.oauth2.gitee.GiteeOAuth2AccessTokenResponseClient;
import com.example.demo.oauth2.gitee.GiteeOAuth2UserService;
import com.example.demo.oauth2.weibo.WeiboOAuth2AccessTokenResponseClient;
import com.example.demo.oauth2.weibo.WeiboOAuth2UserService;
import com.example.demo.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;

import javax.annotation.Resource;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private UserService userService;

    @Resource
    private GiteeOAuth2AccessTokenResponseClient giteeOAuth2AccessTokenResponseClient;

    @Resource
    private GiteeOAuth2UserService giteeOAuth2UserService;

    @Resource
    private AlipayOAuth2AccessTokenResponseClient alipayOAuth2AccessTokenResponseClient;

    @Resource
    private AlipayOAuth2UserService alipayOAuth2UserService;

    @Resource
    private WeiboOAuth2AccessTokenResponseClient weiboOAuth2AccessTokenResponseClient;

    @Resource
    private WeiboOAuth2UserService weiboOAuth2UserService;

    @Bean
    public PasswordEncoder initPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin().loginPage("/login/gitee").loginProcessingUrl("/login/form")
                .and()
                .authorizeRequests()
                .antMatchers("/hello").hasAnyAuthority("ROLE_USER")
                .antMatchers("/**").permitAll()
                .and()
                .logout().deleteCookies("JSESSIONID")
                .logoutSuccessUrl("/").permitAll();

        http.oauth2Login().loginPage("/login/gitee")
                .tokenEndpoint().accessTokenResponseClient(this.initOAuth2AccessTokenResponseClient())
                .and()
                .userInfoEndpoint()
                .userService(this.initOAuth2UserService());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(initPasswordEncoder());
    }

    private OAuth2UserService<OAuth2UserRequest,OAuth2User> initOAuth2UserService(){
        CustomOAuth2UserService userService=new CustomOAuth2UserService();
        userService.getUserServiceMap().put("gitee",giteeOAuth2UserService);
        userService.getUserServiceMap().put("weibo",weiboOAuth2UserService);
        userService.getUserServiceMap().put("alipay",alipayOAuth2UserService);

        return userService;
    }

    private OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> initOAuth2AccessTokenResponseClient(){
        CustomOAuth2AccessTokenResponseClient client=new CustomOAuth2AccessTokenResponseClient();
        client.getClientMap().put("gitee",giteeOAuth2AccessTokenResponseClient);
        client.getClientMap().put("weibo",weiboOAuth2AccessTokenResponseClient);
        client.getClientMap().put("alipay",alipayOAuth2AccessTokenResponseClient);

        return client;
    }
}
