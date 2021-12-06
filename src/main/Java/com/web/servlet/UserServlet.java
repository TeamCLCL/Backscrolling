package com.web.servlet;

import com.dao.UserDao;
import com.dao.impl.ResourceDaoImpl;
import com.dao.impl.UserDaoImpl;
import com.model.*;
import com.utils.JsonUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * 处理用户请求
 */
public class UserServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        // 判断是否为查询操作
        String selectBy = request.getParameter("selectBy");
        if(selectBy != null){
            select(request, response);
        }

        // 检查用户登录状态
        HttpSession session = request.getSession(false);
        if(session != null && session.getAttribute("user") != null){
            // 判断是否为用户对资源的操作（需用户登录）
            String useroperres = request.getParameter("useroperres");
            if(useroperres != null){
                useroperres(request, response);
            }

            // 判断是否为用户对个人信息的操作（需用户登录）
            String useropermsg = request.getParameter("useropermsg");
            if(useropermsg != null){
                useropermsg(request, response);
            }
        }
    }

    /**
     * 用户检索资源（普通、关键字、资源类别）
     * @param request
     * @param response
     * @throws IOException
     */
    private void select(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        // 页码
        Integer pageno = Integer.valueOf(request.getParameter("pageno"));
        // 包装为页面对象
        Page page = new Page(pageno);
        // 判断查询类型
        String selectBy = request.getParameter("selectBy");
        // 获取资源
        List<Resource> list = null;
        // 获取总资源数
        Integer totalsize = null;
        if("common".equals(selectBy)){
            // 用户普通检索资源（分页查询）
            // 获取对应页码的页面资源
            list = new ResourceDaoImpl().getAllResource(page);
            // 获取总资源数（让分页失效）
            page = new Page(1, Integer.MAX_VALUE);
            List<Resource> resources = new ResourceDaoImpl().getAllResource(page);
            if(resources != null){
                totalsize = resources.size();
            }
        }else if("keyword".equals(selectBy)){
            // 用户以关键字检索资源（分页查询）
            String keyword = request.getParameter("content");
            CriteriaResource cr = new CriteriaResource(keyword);
            // 获取对应页码的页面资源
            list = new ResourceDaoImpl().getResourceByKeyword(cr, page);
            // 获取总资源数（让分页失效）
            page = new Page(1, Integer.MAX_VALUE);
            List<Resource> resources = new ResourceDaoImpl().getResourceByKeyword(cr, page);
            if(resources != null){
                totalsize = resources.size();
            }
        }else if("type".equals(selectBy)){
            // 用户以资源类别检索资源（分页查询）
            Type type = new Type(request.getParameter("content"));
            // 获取对应页码的页面资源
            list = new ResourceDaoImpl().getResourceByType(type, page);
            // 获取总资源数（让分页失效）
            page = new Page(1, Integer.MAX_VALUE);
            List<Resource> resources = new ResourceDaoImpl().getResourceByType(type, page);
            if(resources != null){
                totalsize = resources.size();
            }
        }
        // 用户是否登录，未登录不需要做任何处理
        HttpSession session = request.getSession(false);
        if(session != null && session.getAttribute("user") != null){
            // 用户已登录，获取当前登录的用户对象
            User user = (User)session.getAttribute("user");
            // 获取用户收藏的资源
            List<Resource> userCollects = new ResourceDaoImpl().getUserCollects(user, page);
            // 循环变量当前页面资源
            for(int i = 0;i < list.size();++i){
                Resource resource = list.get(i);
                // 判断当前页面中资源用户是否已收藏
                if(userCollects != null){
                    for(int j = 0;j < userCollects.size();++j){
                        if(userCollects.get(j).getId() == resource.getId()){
                            // 若收藏，将isCollect属性修改为true
                            resource.setIsCollect(true);
                            list.set(i, resource);
                        }
                    }
                }
            }
        }
        // 包装为页面对象
        page = new Page(pageno, totalsize, list);
        // 转换为Json字符串
        String json = JsonUtil.toJson(page);
        // 将数据发送给前端
        out.print(json);
    }

    /**
     * 用户操作资源（查询所收藏资源、收藏、移除收藏）
     * @param request
     * @param response
     * @throws IOException
     */
    private void useroperres(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        // 判断用户对资源的操作类型
        String useroperres = request.getParameter("useroperres");
        // 获取当前登录用户id
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute("user");
        Integer user_id = user.getId();
        // 不同操作类型对应不同处理方式
        if("collect".equals(useroperres)){
            // 获取用户收藏的资源id
            Integer resource_id = Integer.valueOf(request.getParameter("resourceid"));
            // 用户收藏资源
            new UserDaoImpl().collect(user_id, resource_id);
        }else if("usercollects".equals(useroperres)){
            // 获取页码
            Integer pageno = Integer.valueOf(request.getParameter("pageno"));
            Page page = new Page(pageno, 5);
            // 用户获取所收藏的资源
            List<Resource> list = new ResourceDaoImpl().getUserCollects(user, page);
            // 获取用户收藏的总资源数（让分页失效）
            page = new Page(1, Integer.MAX_VALUE);
            List<Resource> collects = new ResourceDaoImpl().getUserCollects(user, page);
            Integer totalsize = null;
            if(collects != null){
                totalsize = collects.size();
            }
            for(int i = 0;list != null && i < list.size();++i){
                list.get(i).setIsCollect(true);
            }
            // 将获取专业装入页面对象
            page = new Page(pageno, 5, totalsize, list);
            // 将获取的资源包装为json字符串
            String json = JsonUtil.toJson(page);
            // 将资源传送给前端
            out.print(json);
        }else if("remove".equals(useroperres)){
            // 获取用户收藏的资源id
            Integer resource_id = Integer.valueOf(request.getParameter("resourceid"));
            // 用户移除收藏的资源
            new UserDaoImpl().removeCollect(user_id, resource_id);
        }
    }

    /**
     * 用户对个人信息的操作
     * @param request
     * @param response
     */
    private void useropermsg(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        // 判断用户的操作类型
        String useropermsg = request.getParameter("useropermsg");
        // 获取当前登录用户id
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute("user");
        // 不同操作类型对应不同处理方式
        if("msg".equals(useropermsg)){
            // 获取用户个人信息对应的json字符串
            String json = JsonUtil.toJson(user);
            out.print(json);
        }else if("history".equals(useropermsg)){
            // 获取用户浏览历史
            // 功能待开发
        }else if("upimage".equals(useropermsg)){
            // 上传图片
            // 获取新图片路径
            String image = request.getParameter("image");
            new UserDaoImpl().updateMessage(user, image);
        }else if("update".equals(useropermsg)){
            // 修改个人信息
            // 获取更新的用户名、性别、地址
            String name = request.getParameter("name");
            String sex = request.getParameter("sex");
            String address = request.getParameter("address");
            // 返回更新的数据条数
            int flag = new UserDaoImpl().updateMessage(user, name, sex, address);
            // 更新保存到session中的用户对象
            session.removeAttribute("user");
            session.setAttribute("user", new UserDaoImpl().getUser(user.getId()));
            // 告知前端是否保存成功
            if(flag == 1){
                out.print("{updateSuccess : true}");
            }else{
                out.print("{updateSuccess : false}");
            }
        }
    }
}
