package com.web.servlet;

import com.dao.impl.ResourceDaoImpl;
import com.dao.impl.UserDaoImpl;
import com.model.CriteriaResource;
import com.model.Page;
import com.model.Resource;
import com.model.Type;
import com.utils.JsonUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

        // 判断是否为用户对资源的操作
        String operation = request.getParameter("operation");
        if(operation != null){
            operation(request, response);
        }

        // 判断用户对个人信息的操作类型
        String msg = request.getParameter("");
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
        if("common".equals(selectBy)){
            // 普通分页查询
            list = selectByCommon(page);
        }else if("keyword".equals(selectBy)){
            // 按关键字分页查询
            String keyword = request.getParameter("keyword");
            CriteriaResource cr = new CriteriaResource(keyword);
            list = selectByKeyword(cr, page);
        }else if("type".equals(selectBy)){
            // 按资源类别分页查询
            String type = request.getParameter("type");
            Type t = new Type();
            list = selectByType(t, page);
        }
        // 获取资源总数
        Long totalsize = new ResourceDaoImpl().getNum();
        // 包装为页面对象
        page = new Page(pageno, totalsize, list);
        // 转换为Json字符串
        String json = JsonUtil.toJson(page);
        // 将数据发送给前端
        out.print(json);
    }

    /**
     * 用户操作资源（增、删、查）
     * @param request
     * @param response
     */
    private void operation(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();

        // 判断用户对资源的操作类型
        String operation = request.getParameter("operation");
        // 操作是否成功
        boolean isSuccess = false;
        if("collect".equals(operation)){
            // 用户获取所收藏的资源
            selectCollects(request,response);
        }else if("remove".equals(operation)){
            // 用户收藏资源
            collect();
        }else if("select".equals(operation)){
            // 用户移除收藏的资源
            removeCollect();
        }
    }

    /**
     * 用户检索资源（分页查询）
     * @param page
     * @throws IOException
     * @return
     */
    private List<Resource> selectByCommon(Page page) throws IOException {
        // 获取对应页码的页面资源
        return new ResourceDaoImpl().getAllResource(page);
    }

    /**
     * 用户以关键字检索资源（分页查询）
     * @param cr
     * @param page
     * @return
     */
    private List<Resource> selectByKeyword(CriteriaResource cr, Page page){
        return new ResourceDaoImpl().getResourceByKeyword(cr, page);
    }

    /**
     * 用户以资源类别检索资源（分页查询）
     * @param type
     * @param page
     * @return
     */
    private List<Resource> selectByType(Type type, Page page){
        return new ResourceDaoImpl().getResourceByType(type, page);
    }

    /**
     * 用户获取自己所收藏资源（分页查询）
     * @param request
     * @param response
     * @return
     */
    private List<Resource> selectCollects(HttpServletRequest request, HttpServletResponse response){
        return new ResourceDaoImpl().getUserCollects(1,1);
    }

    /**
     * 用户收藏资源
     */
    private void collect(){
        /*new UserDaoImpl().collect();*/
    }

    /**
     * 用户移除收藏资源
     */
    private void removeCollect(){
        /*new UserDaoImpl().removeCollect();*/
    }

    /**
     * 用户修改个人信息
     */
    private void update(){

    }
}
