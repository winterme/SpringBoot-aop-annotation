package com.zzq.config.aop;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AopInit {

    @Bean
    public RequestIpLimitParse requestIpLimitParse(){
        return new RequestIpLimitParse();
    }

}
