package com.example.vueadminjava.controller;

import com.example.vueadminjava.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;


public class BaseController {

    @Autowired
   HttpServletRequest req;

    @Autowired
    RedisUtil redisUtil;
}
