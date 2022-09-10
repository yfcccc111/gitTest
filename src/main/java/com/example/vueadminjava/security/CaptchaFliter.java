package com.example.vueadminjava.security;
//自定义图片验证码过滤器


import com.example.vueadminjava.common.exception.CaptchaException;
import com.example.vueadminjava.common.lang.Const;
import com.example.vueadminjava.utils.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component

public class CaptchaFliter extends OncePerRequestFilter {
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    LoginFailureHandler loginFailureHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
          String url=httpServletRequest.getRequestURI();


          if("/login".equals(url)&&httpServletRequest.getMethod().equals("POST")){
             //校验验证码
              try {
                  //System.out.println("开始校验验证码");
                  validate(httpServletRequest);


              }catch (CaptchaException e){
                  //认证失败，交给认证失败处理器
                  //System.out.println("bad");
                  loginFailureHandler.onAuthenticationFailure(httpServletRequest,httpServletResponse,e);
              }
              // 不正确，跳转到认证失败管理器

          }
       /* System.out.println("开始验证用户和密码");
        String username=httpServletRequest.getParameter("username");
        String password=httpServletRequest.getParameter("password");
        System.out.println(username+"  "+password);*/

        filterChain.doFilter(httpServletRequest, httpServletResponse);

    }
     //校验逻辑
    private void validate(HttpServletRequest httpServletRequest) {
        String code=httpServletRequest.getParameter("code");
        String key=httpServletRequest.getParameter("token");

        System.out.println(code +"   "+ key);

        if(StringUtils.isBlank(code)||StringUtils.isBlank(key)){

           throw new CaptchaException("验证码错误");
            //System.out.println("验证码错误");
        }

        if(!code.equals(redisUtil.hget(Const.CAPTCHA_KEY,key))){
            throw new CaptchaException("验证码错误");
            //System.out.println("验证码错误");
        }

        //去掉验证码，一次性使用

        redisUtil.hdel(Const.CAPTCHA_KEY,key);
    }
}
