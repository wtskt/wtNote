package com.wt.note.filter;

import cn.hutool.crypto.digest.DigestUtil;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * 非法访问拦截
 * 拦截的资源：
 *      所有的资源
 * 需要放行的资源：
 *      1. 指定页面
 *          不需要登录能访问的页面
 *              登录页面, 注册页面
 *      2. 静态资源
 *          存在static目录下的资源
 *              js，css，image
 *      3. 指定行为
 *          无需登录即可执行的操作
 *      4. 登录状态
 *          判断session作用域中是否存在user对象
 *
 *      5. 免登录（自动登录）直接访问首页
 *              通过Cookie对象实现
 *              什么时候使用免登录
 *                  用户处于未登录状态，且去请求登录才能访问的资源时，调用自动登录
 *              目的
 *                  让用户处于登录状态（自动调用登录方法）
 *              实现
 *                  从Cookie中获取用户的姓名与密码，自动执行登录操作
 *                      1. 获取Cookie数组 request。getCookies()
 *                      2. 判断Cookie数组
 *                      3. 遍历Cookie数组，获取指定的Cookie对象（name为user的Cookie对象）
 *                      4. 得到对应的Cookie对象的value（姓名与密码：userName-userPwd）
 *                      5. 分别得到对应的姓名与密码值
 *                      6. 请求转发到登录操作 user?actionName=login&userName=xxx&userPwd=xxx
 * 如果以上判断都不满足，则拦截跳转到登录页面
 * @author 王童
 */
@WebFilter("/*")
public class LoginAccessFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // 基于http
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // 放行的资源
        String[] resource = {"login.jsp", "register.jsp", "statics", "check"};

        // 得到访问的路径
        // 格式: 项目路径/资源路径
        String path = request.getRequestURI();
        for (String s : resource) {
            if (path.contains(s)) {
                filterChain.doFilter(request,response);
                return ;
            }
        }

        if (path.contains("user")) {
            // 得到用户行为
            String actionName = request.getParameter("actionName");
            // 判断是否为登录 或 注册
            if ("login".equals(actionName) || "register".equals(actionName)
                    || "checkUserName".equals(actionName) || "checkCode".equals(actionName)) {
                filterChain.doFilter(request,response);
                return ;
            }
        }
        if (request.getSession().getAttribute("user") != null) {
            filterChain.doFilter(request, response);
            return ;
        }

        // 免登录 - 用户勾选了面登录，自动为用户执行登录操作(检查cookie)
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("user".equals(cookie.getName())) {
                    String value = cookie.getValue();
                    String[] split = value.split("-");
                    String userName = split[0];
                    String userPwd = split[1];
                    // 请求转发到登录的servlet, 记得将rem参数设置为1, 否则免登录过程中会将cookie清除，下次就不会执行自动登录了
                    String url = "user?actionName=login&rem=1&userName="+userName+"&userPwd="+userPwd;
                    request.getRequestDispatcher(url).forward(request, response);
                    // !!!
                    return ;
                }
            }
        }
        response.sendRedirect("login.jsp");
    }

    @Override
    public void destroy() {

    }
}
