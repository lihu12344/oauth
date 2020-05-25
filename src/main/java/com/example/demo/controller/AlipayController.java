package com.example.demo.controller;

import com.example.demo.config.alipay.AlipayProperties;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.nio.charset.Charset;

@RestController
@RequestMapping("/alipay")
public class AlipayController {

    @Resource
    private AlipayProperties alipayProperties;

    @RequestMapping("/authorize")
    public void authorize(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String baseUri="https://openauth.alipaydev.com/oauth2/publicAppAuthorize.htm";

        String appId=alipayProperties.getAppId();
        String scope=request.getParameter("scope");
        String redirectUri=request.getParameter("redirect_uri");
        String state=request.getParameter("state");

        System.out.println(appId);
        System.out.println(scope);
        System.out.println(redirectUri);
        System.out.println(state);

        String uri=baseUri+"?app_id="+appId+ "&scope="+scope+
                "&redirect_uri="+ URLEncoder.encode(redirectUri, Charset.defaultCharset())+
                "&state="+state;
        System.out.println(uri);

        request.getRequestDispatcher(uri).forward(request,response);
        //response.sendRedirect(uri);
    }
}
