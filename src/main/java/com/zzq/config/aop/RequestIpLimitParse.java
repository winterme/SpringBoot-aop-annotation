package com.zzq.config.aop;

import com.zzq.config.annotation.REQUERT_IP_LIMIT;
import com.zzq.config.cache.RequestIpLImitCache;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
public class RequestIpLimitParse {

    @Autowired
    private RequestIpLImitCache requestIpLImitCache;

    @Pointcut(value = "@annotation(com.zzq.config.annotation.REQUERT_IP_LIMIT)" )
    public void requestLimitPointCut(){};

    @Around(value = "requestLimitPointCut())")
    public Object requestLimitAround(ProceedingJoinPoint pjp) {

        MethodSignature methodSignature = (MethodSignature)pjp.getSignature();
        REQUERT_IP_LIMIT limit = methodSignature.getMethod().getAnnotation(REQUERT_IP_LIMIT.class);

        // 限制访问次数
        int count = limit.count();

        // 获取 request ， 然后获取访问 ip
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String requestIp = RequestIpLimitParse.getRequestIp(request);
        if(StringUtils.isEmpty(requestIp)){
            return "非法访问！ip不能为空！";
        }

        //获取request 的请求路径
        RequestMapping methodAnnotation = methodSignature.getMethod().getAnnotation(RequestMapping.class);
        String url = methodAnnotation.value()[0];

        String key = "_" + url + "_" + requestIp +"_";

        if( requestIpLImitCache.count(key) > count ){
            return "访问失败！超过访问限制！";
        }
        // 将访问存进缓存
        requestIpLImitCache.add(key+System.currentTimeMillis(), "1", limit.timeUnit(), limit.t());

        //记录开始时间
        long startTime = System.currentTimeMillis();

        //获取传入目标方法的参数
        Object[] args = pjp.getArgs();
        Object result = null;
        try {
            // 执行访问并返回数据
            result = pjp.proceed(args);
            return result;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        } finally {
//            System.out.println("request url:" + url);
//            System.out.println("request ip:" + requestIp);
//            //记录结束时间
//            long endTime = System.currentTimeMillis();
//            System.out.println("time use " + (endTime - startTime));
        }
    }

    public static String getRequestIp(HttpServletRequest request){
        // 获取请求IP
        String ip = request.getHeader("x-forwarded-for");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip) || "null".equals(ip)){
            ip = "" + request.getHeader("Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip) || "null".equals(ip)){
            ip = "" + request.getHeader("WL-Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip) || "null".equals(ip)){
            ip = "" + request.getRemoteAddr();
        }

        return ip;
    }

}
