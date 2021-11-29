package com.web.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 处理用户请求
 */
public class UserServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        // 判断请求类型

    }

    /**
     * 用户以关键字检索资源
     */
    private void selectByKeyword(){

    }

    /**
     * 用户以资源类别检索资源
     */
    private void selectByType(){

    }

    /**
     * 用户获取自己所收藏资源
     */
    private void selectUserCollects(){

    }

    /**
     * 用户收藏资源
     */
    private void collect(){

    }

    /**
     * 用户移除收藏资源
     */
    private void removeCollect(){

    }

    /**
     * 用户修改个人信息
     */
    private void update(){

    }
}
