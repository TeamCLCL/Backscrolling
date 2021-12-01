package com.web.servlet;

import com.dao.impl.ResourceDaoImpl;
import com.dao.impl.TypeDaoImpl;
import com.model.Page;
import com.model.Resource;
import com.model.Type;
import com.model.User;
import com.utils.JsonUtil;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * 主页面加载请求
 */
public class IndexLoadServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        // 判断请求类型
        String type = request.getParameter("type");
        if("indexload".equals(type)){
            // 主页面加载
            indexLoad(request, response);
        }else if("type".equals(type)){
            loadType(response);
        }else{
            // 退出登录
            logout(request);
        }
    }

    /**
     * 主页面加载（登录状态检查、资源加载）
     * @param request
     * @param response
     * @throws IOException
     */
    private void indexLoad(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 检查用户是否已登录
        HttpSession session = request.getSession(false);
        // 记录登录状态
        boolean isLogin = false;
        String username = "";
        if(session != null && session.getAttribute("user") != null){
            // 用户已登录
            isLogin = true;
            // 用户名
            User user = (User)session.getAttribute("user");
            username = user.getName();
        }

        // 包装为页面对象
        Page<Resource> page = new Page(1);
        // 查询数据库，加载第一页的资源
        // 资源数据
        List<Resource> list = new ResourceDaoImpl().getAllResource(page);
        // 资源总条数
        Integer totalsize = list.size();
        // 将资源包装为对象
        page = new Page(1, totalsize, list);
        // 将对象转换为
        String json = JsonUtil.toJson(page);
        // 查询结果
        String result = "[{isLogin : " + isLogin + ",username : \"" + username + "\"}," + json + "]";

        response.getWriter().print(result);
    }

    /**
     * 加载资源类别列表
     * @param response
     * @throws IOException
     */
    private void loadType(HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        // 获取所有资源类别
        List<Type> list = new TypeDaoImpl().getAllType();
        // 将资源类别转换为json字符串
        String json = JsonUtil.toJson(list);
        // 将资源类别传送给前端
        out.print(json);
    }

    /**
     * 退出登录
     * @param request
     */
    private void logout(HttpServletRequest request) throws IOException, ServletException {
        // 判断用户是否已登录
        HttpSession session = request.getSession(false);
        // 如果用户已登录，删除用户登录状态
        if(session != null && session.getAttribute("user") != null){
            session.removeAttribute("user");
        }
    }
}
