package com.wt.note.web;

import com.wt.note.po.NoteType;
import com.wt.note.po.User;
import com.wt.note.service.NoteTypeService;
import com.wt.note.util.JSONUtil;
import com.wt.note.vo.ResultInfo;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/type")
public class  NoteTypeServlet extends HttpServlet {

    private NoteTypeService typeService = new NoteTypeService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 设置首页导航的高亮值
        request.setAttribute("menu_page", "type");
        // 得到用户行为
        String actionName = request.getParameter("actionName");
        // 判断用户行为
        if ("list".equals(actionName)) {
            // 查询类型列表
            typeList(request, response);
        } else if ("delete".equals(actionName)) {
            // 删除类型
            deleteType(request, response);
        } else if ("addOrUpdate".equals(actionName)) {
            // 添加 或 修改类型
            addOrUpdate(request, response);
        }
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }

    /**
     * 添加/修改类型
     * @param request
     * @param response
     */
    private void addOrUpdate(HttpServletRequest request, HttpServletResponse response) {
        // 1. 获取参数
        String typeName = request.getParameter("typeName");
        String typeId = request.getParameter("typeId");
        // 2. 获取Session作用域中的user对象，得到用户ID
        User user = (User) request.getSession().getAttribute("user");
        // 3. 调用Service层的更新方法，返回ResultInfo对象
        ResultInfo<Integer> resultInfo = typeService.addOrUpdate(typeName, user.getUserId(), typeId);
        // 4. 将ResultInfo对象转换成JSON格式的字符串，响应给ajax的回调函数
        JSONUtil.toJson(response, resultInfo);
    }

    /**
     * 删除类型
     * @param request
     * @param response
     */
    private void deleteType(HttpServletRequest request, HttpServletResponse response) {
        // 1. 接收参数（类型ID）
        String typeId = request.getParameter("typeId");
        // 2. 调用Service的更新操作，返回ResultInfo对象
        ResultInfo<NoteType> resultInfo = typeService.deleteType(typeId);
        // 3. 将ResultInfo对象转换成JSON格式的字符串，响应给ajax的回调函数
        JSONUtil.toJson(response, resultInfo);
    }

    /**
     * 查询类型列表
     * @param request
     * @param response
     */
    private void typeList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 或获取Session作用域的User对象
        User user = (User) request.getSession().getAttribute("user");
        // 调用Service层的查询方法
        List<NoteType> typeList = typeService.findTypeList(user.getUserId());
        // 将类型列表设置到request请求域中
        request.setAttribute("typeList", typeList);
        // 设置首页动态包含的页面
        request.setAttribute("changePage", "type/list.jsp");
        // 请求转发到index.jsp页面
        request.getRequestDispatcher("index.jsp").forward(request,response);
    }
}
