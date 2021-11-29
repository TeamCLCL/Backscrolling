package com.web.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 处理管理员请求
 */
public class AdminServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        // 判断请求类型
    }

    /**
     * 管理员查看资源（分页）
     */
    private void selectResource(){

    }

    /**
     * 管理员插入资源
     */
    private void insertResource(){

    }

    /**
     * 管理员删除资源
     */
    private void deleteResource(){

    }

    /**
     * 管理员修改资源
     */
    private void updateResource(){

    }

    /**
     * 管理员查看用户
     */
    private void selectUser(){

    }
}
