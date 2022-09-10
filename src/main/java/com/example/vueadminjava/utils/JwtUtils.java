package com.example.vueadminjava.utils;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;

@Data
@Component
@ConfigurationProperties(prefix = "markerhub.jwt")
public class JwtUtils {

    private long expire;
    private String secret;
    private String header;

    // 生成jwt
    public String generateToken(String username) {

         Date nowDate = new Date();
         Date expireDate = new Date(nowDate.getTime() + 1000 * expire);

        SignatureAlgorithm signatureAlgorithm=SignatureAlgorithm.HS512;

         // return "sucess";
        byte[] bytes = "ji8n3439n439n43ld9ne9343fdfer49h".getBytes();

        JwtBuilder builder=Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(username)
                .setIssuedAt(nowDate)
                .setExpiration(expireDate)
                .signWith(signatureAlgorithm,bytes)
                ;



       // builder.signWith(signatureAlgorithm,secret);
        // String token=builder.compact();

        return "sucess";
        //return builder.compact();



    }

    // 解析jwt
    public Claims getClaimByToken(String jwt) {
        try {

            return Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(jwt)
                    .getBody();
        } catch (Exception e) {
            return null;
        }
    }

    // jwt是否过期
    public boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }

}
/*
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;

@Data
@Component
@ConfigurationProperties(prefix = "markerhub.jwt")
public class JwtUtils {
    private long expire;
    private String secret;
    private String header;

    //生成jwt
    public String generateToken(String username){
        Date nowDate=new Date();
        Date expireDate=new Date(nowDate.getTime()+1000*expire);

        return Jwts.builder()
                .setHeaderParam("typ","JWT")
                .setSubject(username)
                .setIssuedAt(nowDate)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    //解析jwt
    public Claims getClaimByToken(String jwt){
        try{
            return Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(jwt)
                    .getBody();

        }catch (Exception e){
            return null;
        }
    }

    //判断jwt是否过期
    public boolean isTokenExpired(Claims claims){
        return claims.getExpiration().before(new Date());
    }

}*/
