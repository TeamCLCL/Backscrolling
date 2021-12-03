package com.web.filter;

import com.model.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
        HttpServletResponse resp = (HttpServletResponse)response;
        // 调用请求对象读取请求包中的URI，了解用户访问的资源文件是谁
        String uri = req.getRequestURI();
        // 获取session对象，判断管理员或用户是否已登录
        HttpSession session = req.getSession(false);
        if(session != null && (session.getAttribute("user") != null || session.getAttribute("admin") != null)) {
            // 获取session对象中的用户对象，如果有则表示当前登录的为用户
            User user = (User)session.getAttribute("user");
            // 已登录者想访问登录页，管理员跳转到管理员页面，用户跳转到主页面
            if(uri.contains("login")){
                if(user == null){
                    // 如果是管理员，则重定向到管理员管理页面
                    resp.sendRedirect(req.getContextPath() + "/manage.html");
                }else{
                    // 如果是用户，则重定向到主页面
                    resp.sendRedirect(req.getContextPath() + "/index.html");
                }
            }else{
                // 放行
                chain.doFilter(request,response);
            }
        }else{
            // 若本次请求的资源文件与欢迎页/登录/注册/忘记密码/静态资源（css、js、图片...）相关，无条件放行
            if(uri.contains("/GYW/") || uri.contains("login") || uri.contains("logon") || uri.contains("reset") || uri.contains("check") || uri.contains("send") || uri.contains("load") || uri.contains("user") || uri.contains("admin") || uri.contains(".css") || uri.contains(".js") || uri.contains(".jpg") || uri.contains("png") || uri.contains(".ico")){
                // 放行
                chain.doFilter(request,response);
                return;
            }else{
                // 如果不是以上资源文件且未登录，则重定向到登录页
                resp.sendRedirect(req.getContextPath() + "/login.html");
            }
        }
    }
}
