package com.web.servlet;

import com.dao.impl.UserDaoImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 检测用户名是否重复
 */
public class CheckNameServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        // 获取前端提交的信息
        String user = request.getParameter("user");
        // 判断用户名是否重复
        if (new UserDaoImpl().nameCheck(user) != 0) {
            out.print("[{nameRepeat : true}]");
        }else{
            out.print("[{nameRepeat : false}]");
        }
    }
}
