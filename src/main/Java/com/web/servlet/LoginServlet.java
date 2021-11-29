package com.web.servlet;

import com.dao.impl.AdminDaoImpl;
import com.dao.impl.UserDaoImpl;
import com.model.Admin;
import com.model.User;
import sun.misc.BASE64Encoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 用户登录
 */
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        // 用户为0，管理员为1
        String identity = request.getParameter("identity");
        // 密码
        String password = request.getParameter("password");
        // 判断用户/管理员
        if("0".equals(identity)){
            // 用户
            // 用户登录使用的账号类型: 用户名/电子邮箱
            String accountType = request.getParameter("type");
            // 密码加密
            try {
                // 确定计算方法
                MessageDigest  md5 = MessageDigest.getInstance("MD5");
                BASE64Encoder base64en = new BASE64Encoder();
                // 加密后的密码
                password = base64en.encode(md5.digest(password.getBytes("utf-8")));
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            // 账号类型为用户名
            User user = null;
            if("username".equals(accountType)){
                String username = request.getParameter("user");
                // 包装为实体类对象
                user = new User(username, password, null);
            }else{
                String email = request.getParameter("email");
                // 包装为实体类对象
                user = new User(null, password, email);
            }
            // 判断登录是否成功
            User dbUser = new UserDaoImpl().loginCheck(user, accountType);
            if(dbUser != null){
                out.print("{loginSuccess : true}");
                // 获取session对象
                HttpSession session = request.getSession();
                // 将用户登录状态存入session范围
                session.setAttribute("user", dbUser);
            }else{
                out.print("{loginSuccess : false}");
            }
        }else{
            // 管理员
            // 账号类型为用户名
            String username = request.getParameter("user");
            // 包装为实体类对象
            Admin admin = new Admin(0, username, password);
            // 判断登录是否成功
            Admin dbAdmin = new AdminDaoImpl().loginCheck(admin);
            if(dbAdmin != null){
                out.print("{loginSuccess : true}");
                // 获取session对象
                HttpSession session = request.getSession();
                // 将用户登录状态存入session范围
                session.setAttribute("admin", dbAdmin);
            }else{
                out.print("{loginSuccess : false}");
            }
        }
    }
}
