package com.example.vueadminjava.security;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.example.vueadminjava.common.lang.Result;
import com.example.vueadminjava.entity.SysUser;
import com.example.vueadminjava.service.SysUserService;
import com.example.vueadminjava.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends BasicAuthenticationFilter {
    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserDetailServiceImpl userDetailService;

    @Autowired
    SysUserService sysUserService;


    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String url=request.getRequestURI();
        if("/captcha".equals(url)&&request.getMethod().equals("GET")){
            chain.doFilter(request, response);
            return;
        }

        String str=request.getParameter("str");
        String username=request.getParameter("username");
        if(str.equals("sucess")){

            SysUser sysUser=sysUserService.getByUsername(username);
            //System.out.println(sysUser.getId());
            UsernamePasswordAuthenticationToken token
                    = new UsernamePasswordAuthenticationToken(username, null, userDetailService.getUserAuthority(sysUser.getId()));

            SecurityContextHolder.getContext().setAuthentication(token);

            chain.doFilter(request, response);
        } else{
            response.setContentType("application/json;charset=UTF-8");
            ServletOutputStream outputStream = response.getOutputStream();

            Result result = Result.fail("请先登陆");

            outputStream.write(JSONUtil.toJsonStr(result).getBytes("UTF-8"));

            outputStream.flush();
            outputStream.close();
        }

       /*String jwt = request.getHeader(jwtUtils.getHeader());

        if (StrUtil.isBlankOrUndefined(jwt)) {
            chain.doFilter(request, response);
            return;
        }

        Claims claim = jwtUtils.getClaimByToken(jwt);
        if (claim == null) {
            throw new JwtException("token 异常");
        }
        if (jwtUtils.isTokenExpired(claim)) {
            throw new JwtException("token已过期");
        }
        String username = claim.getSubject();
        // 获取用户的权限等信息

        SysUser sysUser=sysUserService.getByUsername(username);

        UsernamePasswordAuthenticationToken token
                = new UsernamePasswordAuthenticationToken(username, null, userDetailService.getUserAuthority(sysUser.getId()));

        SecurityContextHolder.getContext().setAuthentication(token);*/

        //chain.doFilter(request, response);

    }

}
