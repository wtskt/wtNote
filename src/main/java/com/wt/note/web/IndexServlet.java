package com.wt.note.web;

import com.wt.note.po.Note;
import com.wt.note.po.User;
import com.wt.note.service.NoteService;
import com.wt.note.util.Page;
import com.wt.note.vo.NoteVo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 查询日记
 * @author 王童
 */
@WebServlet("/index")
public class IndexServlet extends HttpServlet {
    private NoteService noteService = new NoteService();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 设置首页导航高亮
        request.setAttribute("menu_page", "index");

        // 得到用户行为（判断是说明条件查询: 标题查询，日期查询，类型查询）
        String actionName = request.getParameter("actionName");
        // 将用户行为设置到request作用域中（分页导航中需要要获取）
        request.setAttribute("action", actionName);

        // 判断用户行为
        if ("searchTitle".equals(actionName)) {

            // 获取查询条件: 标题
            String title = request.getParameter("title");
            // 将查询条件回显
            request.setAttribute("title", title);
            noteList(request, response, title, null, null);

        } else if ("searchDate".equals(actionName)) {

            // 获取查询条件: 日期
            String date = request.getParameter("date");
            // 将查询条件回显
            request.setAttribute("date", date);
            noteList(request, response, null, date, null);

        } else if ("searchType".equals(actionName)) {

            // 获取查询条件: 类型
            String typeId = request.getParameter("typeId");
            // 将查询条件回显
            request.setAttribute("typeId", typeId);
            noteList(request, response, null, null, typeId);

        } else {
            // 分页查询云记列表
            noteList(request, response, null, null, null);
        }

        // 设置动态包含的页面
        request.setAttribute("changePage", "note/list.jsp");
        // 请求转发到index.jsp更新信息
        request.getRequestDispatcher("index.jsp").forward(request,response);
    }



    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }

    /**
     * 查询云记列表
     * @param request
     * @param response
     * @param title
     */
    private void noteList(HttpServletRequest request, HttpServletResponse response, String title, String date, String typeId) {
        // 1. 接收参数（当前页， 每页显示的数量）
        String pageNum = request.getParameter("pageNum");
        String pageSize = request.getParameter("pageSize");

        // 2. 获取Session作用域中的user对象
        User user = (User) request.getSession().getAttribute("user");

        // 3. 调用Service层方法，返回Page对象
        Page<Note> page = new NoteService().findNoteListByPage(pageNum, pageSize, user.getUserId(), title, date, typeId);
        // 4. 将page对象设置到域中
        request.setAttribute("page", page);

        // 通过日期分组查询当前登录用户下的云记数量
        List<NoteVo> dateInfo = new NoteService().findNoteCountByDate(user.getUserId());
        // 设置集合存放在Session域中
        request.getSession().setAttribute("dateInfo", dateInfo);

        // 通过类型分组
        List<NoteVo> typeInfo = new NoteService().findNoteCountByType(user.getUserId());
        request.getSession().setAttribute("typeInfo", typeInfo);
    }
}
