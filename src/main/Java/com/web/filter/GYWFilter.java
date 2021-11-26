package com.web.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 *  过滤器 —— 拦截未登录访问除登录页、注册页、欢迎页外的其他资源文件
 */
public class GYWFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 增强 —— 设置服务器对请求体的编码方式
        request.setCharacterEncoding("UTF-8");

        HttpServletRequest req = (HttpServletRequest)request;
        // 调用请求对象读取请求包中的URI，了解用户访问的资源文件是谁
        String uri = req.getRequestURI();
        // 如果本次请求资源文件与登录/注册/欢迎页相关，无条件放行
        if(uri.contains("login") || uri.contains("Login") || uri.contains("logon") || uri.contains("Logon") || uri.contains("reset") || uri.contains("Reset") || "/GYW/".equals(uri)){
            chain.doFilter(request,response);
            return;
        }
        // 如果不是请求和登录相关的资源文件，则需验证登录状态
        // 获取session对象
        HttpSession session = req.getSession(false);
        if(session != null && (session.getAttribute("user") != null || session.getAttribute("admin") != null)) {
            chain.doFilter(request,response);
            return;
        }else{
            // 转发到登录页
            RequestDispatcher dispatcher = req.getRequestDispatcher("/login.html");
            dispatcher.forward(request,response);
        }
    }
}
