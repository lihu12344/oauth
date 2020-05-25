package com.example.demo.oauth2.alipay;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlipayOAuth2User implements OAuth2User {

    private String userId;
    private String gender;

    private List<GrantedAuthority> authorities= AuthorityUtils.createAuthorityList("ROLE_USER");
    private Map<String,Object> attributes;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public String getName() {
        return this.getUserId();
    }

    @Override
    public Map<String, Object> getAttributes() {
        if (attributes==null){
            attributes=new HashMap<>();

            attributes.put("userId",this.getUserId());
            attributes.put("gender",this.getGender());
        }

        return attributes;
    }

    @Override
    public List<GrantedAuthority> getAuthorities() {
        return authorities;
    }
}
