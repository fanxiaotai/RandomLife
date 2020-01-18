package com.fyt.rlife.rlife.interceptor;

import com.fyt.rlife.rlife.annotation.RoleRequire;
import com.fyt.rlife.rlife.util.CookieUtil;
import com.fyt.rlife.rlife.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Component
public class AuthInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        //判断被拦截的请求的方法的注解是否需要被拦截
        //强转子类
        if (handler instanceof ResourceHttpRequestHandler){
            return true;
        }
        HandlerMethod hm = (HandlerMethod) handler;
        //反射获取注解
        RoleRequire methodAnnotation = hm.getMethodAnnotation(RoleRequire.class);

        if (methodAnnotation==null){
            return true;
        }

        String token = "";
        String oldToken = CookieUtil.getCookieValue(request,"oldToken",true);
        if (StringUtils.isNotBlank(oldToken)){
            token = oldToken;
        }
        String newToken = request.getParameter("token");
        if (StringUtils.isNotBlank(newToken)){
            token = newToken;
        }

        //调用认证中心进行验证
        int rolesToken = 0;
        Map<String,Object> successMap = new HashMap<>();
        if(StringUtils.isNotBlank(token)){

            JwtUtil jwtUtil = new JwtUtil();
            Claims claims = null;
            try {
                claims = jwtUtil.parseJWT(token);
            } catch (Exception e) {
                e.printStackTrace();
                StringBuffer ReturnUrl = request.getRequestURL();
                response.sendRedirect("http://www.life.com:80/toLogin?ReturnUrl="+ReturnUrl);
                return false;
            }
            rolesToken = Integer.parseInt((String)claims.get("roles"));
            String id = claims.getId();
            successMap.put("memberId",id);
            String subject = claims.getSubject();
            successMap.put("nickname",subject);

        }else {
            //重定向passport登陆
            StringBuffer ReturnUrl = request.getRequestURL();
            response.sendRedirect("http://www.life.com:80/toLogin?ReturnUrl="+ReturnUrl);
            return false;
        }

        //获得该请求是否需要成功登陆
        int roles = methodAnnotation.roles();

        if (rolesToken>=roles){
            //验证通过
            //将token携带的用户信息写入
            request.setAttribute("memberId",successMap.get("memberId"));
            request.setAttribute("nickname",successMap.get("nickname"));
        }else {
            //权限不足，重定向到一个权限不足的提示页面，暂时先重定向到登陆页面
            StringBuffer ReturnUrl = request.getRequestURL();
            response.sendRedirect("http://www.life.com:80/toLogin?ReturnUrl="+ReturnUrl);
            return false;
        }

        //验证提供，覆盖cookie中的token
        if(StringUtils.isNotBlank(token)){
            CookieUtil.setCookie(request,response,"oldToken",token,60*60*2,true);
        }
        //拦截代码
        System.out.println("经过拦截器");

        return true;
    }
}