package com.wt.note.web;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/check")
public class CheckServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String actionName = request.getParameter("actionName");
        if ("checkVerifyCode".equals(actionName)) {
            // 生成验证码
            checkVerifyCode(request, response);
        }
    }

    /**
     * 检验验证码
     * @param request
     * @param response
     */
    private void checkVerifyCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String verifyCode = (String) request.getServletContext().getAttribute("verifyCode");
        String code = request.getParameter("code");
        int info = 0;
        if (verifyCode.equalsIgnoreCase(code)) {
            info = 1;
        }
        response.getWriter().write(info + "");
        response.getWriter().close();
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }
}
