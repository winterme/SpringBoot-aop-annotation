package com.zzq.controller;

import com.zzq.config.annotation.REQUERT_IP_LIMIT;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.concurrent.TimeUnit;

@Controller
public class DemoController {

    @REQUERT_IP_LIMIT( t = 60 , count = 10 , timeUnit = TimeUnit.SECONDS)
    @RequestMapping("/get1234")
    @ResponseBody
    public Object get1(){
        return "get1";
    }

    @REQUERT_IP_LIMIT( t = 30 , count = 5 , timeUnit = TimeUnit.SECONDS)
    @RequestMapping("/get2")
    @ResponseBody
    public Object get2(){
        return "get2";
    }

    @RequestMapping("/get3")
    @ResponseBody
    public Object get3(){
        return "get3";
    }



}
