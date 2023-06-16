package com.wt.note.filter;

import cn.hutool.core.util.StrUtil;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * 字符乱码过滤器
 */
@WebFilter("/*")
public class EncodingFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        // 处理post请求
        request.setCharacterEncoding("utf-8");

        String method = request.getMethod();

        // get请求
        if ("GET".equalsIgnoreCase(method)) {
            // 得到服务器版本 -- tomcat
            String serverInfo = request.getServletContext().getServerInfo();
            // 截取字符串得到具体版本号
            String version = serverInfo.substring(serverInfo.indexOf("/") + 1, serverInfo.indexOf("."));
            if (Integer.parseInt(version) < 8) {
                MyWapper myRequest = new MyWapper(request);
                // 放行资源, 将处理过的request请求放回
                filterChain.doFilter(myRequest, response);
                return ;
            }
        }
        filterChain.doFilter(request, response);

    }
    /**
     * 定义内部类 (本质是request对象)
     * 继承包装类
     * 重写getParameter方法
     * 对字符串重新编码
     */
    class MyWapper extends HttpServletRequestWrapper {
        // 提升构造器中request对象作用域
        private HttpServletRequest request;

        /**
         * 带参构造
         *  可以得到需要处理的request对象
         * @param request
         */
        public MyWapper (HttpServletRequest request) {
            super(request);
            this.request = request;
        }

        /**
         * 重写getParameter方法，处理乱码问题
         * @param name
         * @return
         */
        @Override
        public String getParameter(String name) {
            // 获取参数 （乱码的参数值）
            String value = request.getParameter(name);
            // 判断非空
            if (StrUtil.isBlank(value)) {
                return value;
            }
            try {
                // 转换编码
                value = new String(value.getBytes("ISO-8859-1"), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return value;
        }
    }
}
