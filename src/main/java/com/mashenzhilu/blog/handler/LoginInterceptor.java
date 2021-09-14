package com.mashenzhilu.blog.handler;

import com.alibaba.fastjson.JSON;
import com.mashenzhilu.blog.dao.pojo.SysUser;
import com.mashenzhilu.blog.service.LoginService;
import com.mashenzhilu.blog.vo.ErrorCode;
import com.mashenzhilu.blog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private LoginService loginService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        //再执行controller方法之前进行执行
        /*
            1.判断请求的接口路径是否为HandlerMethod，确实是controller里面的方法
            2.判断token是否为空 空说明没有登录
            3.不为空， 登录验证
            4.认证成功就放行
         */

        if (!(handler instanceof HandlerMethod)){
            //说明不是handler方法，可以放行
            //如果是的话，可能是恶意程序，直接访问的静态资源
            return true;
        }

        String token = request.getHeader("Authorization");
        if (token == null){
            Result result = Result.fail(ErrorCode.NO_LOGIN.getCode(), "未登录");
            response.setContentType("application/json;charset=utf-8");
            try {
                response.getWriter().print(JSON.toJSONString(result));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
        SysUser sysUser = loginService.checkToken(token);
        if (sysUser == null){
            Result result = Result.fail(ErrorCode.NO_LOGIN.getCode(), "未登录");
            response.setContentType("application/json;charset=utf-8");
            try {
                response.getWriter().print(JSON.toJSONString(result));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
        //是登录状态，放行


        return true;
    }
}
