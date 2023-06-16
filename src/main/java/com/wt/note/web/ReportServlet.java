package com.wt.note.web;

import com.wt.note.po.Note;
import com.wt.note.po.User;
import com.wt.note.service.NoteService;
import com.wt.note.util.JSONUtil;
import com.wt.note.vo.ResultInfo;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/report")
public class ReportServlet extends HttpServlet {

    private NoteService noteService = new NoteService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 设置高亮
        request.setAttribute("menu_page", "report");
        // 得到用户行为
        String actionName = request.getParameter("actionName");
        // 判断用户行为
        if ("info".equals(actionName)) {
            // 进入报表页面
            reportInfo(request, response);
        } else if ("month".equals(actionName)) {
            queryNoteCountByMonth(request, response);
        } else if ("location".equals(actionName)) {
            // 查询用户发布云记时的坐标
            queryNoteLonAndLat(request, response);
        }

    }




    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }

    /**
     * 查询用户发布云记时的坐标
     * @param request
     * @param response
     */
    private void queryNoteLonAndLat(HttpServletRequest request, HttpServletResponse response) {
        // 从Session 作用域中 获取用户对象
        User user = (User) request.getSession().getAttribute("user");
        // 调用Service的查询方法，返回resultInfo对象
        ResultInfo<List<Note>> resultInfo = noteService.queryNoteLonAndLat(user.getUserId());
        // 将resultInfo对象转换成json对象
        JSONUtil.toJson(response, resultInfo);
    }

    /**
     * 查询月份对应的云记数量
     * @param request
     * @param response
     */
    private void queryNoteCountByMonth(HttpServletRequest request, HttpServletResponse response) {
        // 从Session作用域中获取用户对象
        User user = (User) request.getSession().getAttribute("user");
        // 调用Service层查询方法，返回resultInfo对象
        ResultInfo<Map<String, Object>> resultInfo = noteService.queryNoteCountByMonth(user.getUserId());
        //
        JSONUtil.toJson(response, resultInfo);
    }

    /**
     * 进入报表页面
     * @param request
     * @param response
     */
    private void reportInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 设置首页动态包含的页面
        request.setAttribute("changePage", "report/info.jsp");
        // 请求转发跳转到jsp
        request.getRequestDispatcher("index.jsp").forward(request, response);

    }
}
