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
        // 如果本次请求资源文件与登录/注册/忘记密码/欢迎页相关，无条件放行
        if(uri.contains("login") || uri.contains("Login") || uri.contains("logon") || uri.contains("Logon") || uri.contains("forget") || uri.contains("Reset") || "/GYW/".equals(uri) || uri.contains(".css") || uri.contains(".js") || uri.contains(".jpg") || uri.contains("png") || uri.contains(".ico") || uri.contains("check") || uri.contains("send") || uri.contains("Load")){
            chain.doFilter(request,response);
            return;
        }
        // 如果不是请求和登录、注册、忘记密码、欢迎页相关的资源文件，则需验证登录状态
        // 获取session对象
        HttpSession session = req.getSession(false);
        if(session != null && (session.getAttribute("user") != null || session.getAttribute("admin") != null)) {
            // 获取session对象中的用户对象，如果有则表示当前登录的为用户
            User user = (User)session.getAttribute("user");
            // 已登录者想访问登录页，提示先退出登录并调整到主页面
            if(uri.contains("login")){
                if(user == null){
                    // 如果是管理员，则重定向到管理员管理页面
                    resp.sendRedirect(req.getContextPath() + "/admin.html");
                }else{
                    // 如果是用户，则重定向到主页面
                    resp.sendRedirect(req.getContextPath() + "/index.html");
                }
            }else{
                // 放行
                chain.doFilter(request,response);
            }
        }else{
            // 未登录则重定向到登录页
            resp.sendRedirect(req.getContextPath() + "/login.html");
        }
    }
}
