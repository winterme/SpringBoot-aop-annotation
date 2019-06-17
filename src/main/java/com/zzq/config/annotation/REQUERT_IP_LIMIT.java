package com.zzq.config.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * 限制每个ip对每个方法的访问限制，加上时间限制
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface REQUERT_IP_LIMIT {

    /**
     * 时间类型，默认毫秒
     * @return
     */
    TimeUnit timeUnit() default TimeUnit.MILLISECONDS ;

    /**
     * 多长时间内限制，默认 60
     * @return
     */
    long t () default 60;

    /**
     * 单位时间内能访问多少次，默认10次
     * @return
     */
    int count () default 10;

}
