package com.web.servlet;

import com.dao.impl.UserDaoImpl;
import com.model.User;
import sun.misc.BASE64Encoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 重置密码
 */
public class ResetServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        // 获取前端提交数据
        String password = request.getParameter("password");
        String email = request.getParameter("email");

        // 密码加密
        try {
            // 确定计算方法
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            BASE64Encoder base64en = new BASE64Encoder();
            // 加密后的密码
            password = base64en.encode(md5.digest(password.getBytes("utf-8")));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        // 包装为实体类对象
        User user = new User(null, password, email);
        // 更新密码
        if(new UserDaoImpl().resetPassword(user) == 1){
            out.print("[{resetSuccess : true}]");
        }else{
            out.print("[{resetSuccess : false}]");
        }
    }
}
