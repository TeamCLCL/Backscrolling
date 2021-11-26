package com.web.servlet;

import com.dao.impl.UserDaoImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

/**
 * 检测邮箱是否重复
 */
public class CheckEmailServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        // 获取前端提交的信息
        String email = request.getParameter("email");

        // 判断邮箱是否重复
        if (new UserDaoImpl().emailCheck(email) != 0) {
            out.print("[{emailRepeat : true}]");
        }else{
            out.print("[{emailRepeat : false}]");
        }
    }
}
