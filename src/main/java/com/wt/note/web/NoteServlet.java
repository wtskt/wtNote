package com.wt.note.web;

import cn.hutool.core.util.StrUtil;
import com.wt.note.po.Note;
import com.wt.note.po.NoteType;
import com.wt.note.po.User;
import com.wt.note.service.NoteService;
import com.wt.note.service.NoteTypeService;
import com.wt.note.vo.ResultInfo;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/note")
public class NoteServlet extends HttpServlet {
    private NoteService noteService = new NoteService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 设置首页导航栏高亮
        request.setAttribute("menu_page", "note");

        // 得到用户行为
        String actionName = request.getParameter("actionName");
        if ("view".equals(actionName)) {
            noteView(request, response);
        } else if ("addOrUpdate".equals(actionName)) {
            // 添加或修改云记
            addOrUpdate(request, response);
        } else if ("detail".equals(actionName)) {
            // 加载云记详情
            noteDetail(request, response);
        } else if ("delete".equals(actionName)) {
            // 删除云记
            noteDelete(request, response);
        }
    }



    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }

    /**
     * 删除云记
     * @param request
     * @param response
     */
    private void noteDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 接收参数
        String noteId = request.getParameter("noteId");
        int code = noteService.deleteNote(noteId);
        response.getWriter().write(code + "");
        response.getWriter().close();
    }

    /**
     * 查询云记详情
     * @param request
     * @param response
     */
    private void noteDetail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String noteId = request.getParameter("noteId");
        Note note = noteService.findNoteById(noteId);
        request.setAttribute("note", note);
        // 设置首页动态包含得页面
        request.setAttribute("changePage", "note/detail.jsp");
        // 请求转发跳转到index.jsp页面
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }

    /**
     * 添加或修改云记
     * @param request
     * @param response
     */
    private void addOrUpdate(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        // 接收参数（类型ID, 标题, 内容）
        String typeId = request.getParameter("typeId");
        String title = request.getParameter("title");
        String content = request.getParameter("content");

        // 获取经纬度
        String lon = request.getParameter("lon");
        String lat = request.getParameter("lat");

        // 如果是修改操作，需要接收noteId
        String noteId = request.getParameter("noteId");

        // 调用service层方法，返回resultInfo对象
        ResultInfo<Note> resultInfo = noteService.addOrUpdate(typeId, title, content, noteId, lon, lat);
        // 判断成功或失败，resultInfo的code值
        if (resultInfo.getCode() == 1) {
            // 重定向跳转到首页 index
            response.sendRedirect("index");
        } else {
            request.setAttribute("resultInfo", resultInfo);
            String url = "note?actionName=view";
            // 修改操作需要传递noteId
            if (!StrUtil.isBlank(noteId)) {
                url += "&noteId=" + noteId;
            }
            request.getRequestDispatcher(url).forward(request, response);
        }
    }

    /**
     * 进入发布云记的页面
     * @param request
     * @param response
     */
    private void noteView(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 修改操作的查询云记对象
        String noteId = request.getParameter("noteId");
        // 通过noteId查询云记对象
        Note note = noteService.findNoteById(noteId);
        // 将我们的note对象设置到请求域中
        request.setAttribute("noteInfo", note);


        // 获取用户对象
        User user = (User) request.getSession().getAttribute("user");
        // 通过用户ID查询对应 类型列表
        List<NoteType> typeList = new NoteTypeService().findTypeList(user.getUserId());
        // 将用户的 云记类型集合 储存在域中
        request.setAttribute("typeList", typeList);
        // 设置首页动态包含的页面值
        request.setAttribute("changePage", "note/view.jsp");
        // 转发到index.jsp（首页）
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }
}
