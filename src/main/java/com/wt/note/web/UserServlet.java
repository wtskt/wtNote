package com.wt.note.web;

import com.wt.note.po.User;
import com.wt.note.service.UserService;
import com.wt.note.util.VerifyCode;
import com.wt.note.vo.ResultInfo;
import org.apache.commons.io.FileUtils;

import javax.imageio.ImageIO;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;


@WebServlet("/user")
@MultipartConfig
public class UserServlet extends HttpServlet {

    private UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 设置首页导航高亮
        request.setAttribute("menu_page", "user");

        // 1.接收用户行为
        String actionName = request.getParameter("actionName");
        // 2.判断用户行为
        if ("checkCode".equals(actionName)) {
            // 验证码
            checkCodeImage(request, response);
        } else if ("logout".equals(actionName)) {
            // 用户退出
            userLogout(request, response);
        } else if ("userCenter".equals(actionName)) {
            // 进入个人中心
            userCenter(request, response);
        } else if ("userHead".equals(actionName)) {
            // 加载头像
            userHead(request, response);
        } else if ("checkNick".equals(actionName)) {
            // 验证昵称的唯一性
            checkNick(request, response);
        } else if ("updateUser".equals(actionName)) {
            // 修改用户信息
            updateUser(request, response);
        } else if ("register".equals(actionName)) {
            // 用户注册
            register(request, response);
        } else if ("checkUserName".equals(actionName)) {
            // 验证用户名的唯一性
            checkUserName(request, response);
        } else if ("login".equals(actionName)) {
            // 用户登录
            userLogin(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }

    /**
     * 生成验证码
     * @param request
     * @param response
     */
    private void checkCodeImage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int width = 200;
        int height = 69;
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        String randomText = VerifyCode.drawRandomText(width, height, bufferedImage);
        request.getServletContext().setAttribute("verifyCode", randomText);
        response.setContentType("image/png");

        try {
            OutputStream os = response.getOutputStream();
            ImageIO.write(bufferedImage, "png", os);
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        request.getRequestDispatcher("login.jsp");
    }

    /**
     * 验证用户名的唯一性
     * @param request
     * @param response
     */
    private void checkUserName(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String userName = request.getParameter("userName");
        int code = userService.checkUserName(userName);
        response.getWriter().write(code + "");
        response.getWriter().close();
    }

    /**
     * 用户注册
     * @param request
     * @param response
     */
    private void register(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String userName = request.getParameter("userName");
        String password = request.getParameter("passWord");
        userService.register(userName, password);
        response.sendRedirect("login.jsp");
    }

    /**
     * 修改用户个人信息
     * @param request
     * @param response
     */
    private void updateUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. 调用Service层的方法，传递request对象作为参数，得到resultInfo对象
        ResultInfo<User> resultInfo = userService.updateUser(request);
        // 2. 将resultInfo对象存到request作用域中
        request.setAttribute("resultInfo", resultInfo);
        // 3. 请求转发到个人中心页面（修改完毕后，重新回到个人中心页面）
        request.getRequestDispatcher("user?actionName=userCenter").forward(request, response);
    }

    /**
     * 验证昵称的唯一性
     * @param request
     * @param response
     * @throws IOException
     */
    private void checkNick(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 1.获取参数 （昵称，通过ajax传输）
        String nick = request.getParameter("nick");
        // 2. 从session中获取用户对象，得到用户id
        User user = (User) request.getSession().getAttribute("user");
        // 3. 调用Service层的方法，得到返回的结果
        int code = userService.checkNick(nick, user.getUserId());
        // 4. 通过字符输出流将结果响应给前台的ajax的回调函数
        response.getWriter().write(code+"");
        // 5. 关闭资源
        response.getWriter().close();
    }

    /**
     * 加载头像
     * @param request
     * @param response
     * @throws IOException
     */
    private void userHead(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 1. 获取参数（图片名称）--- 用户数据库中的图片名称
        String head = request.getParameter("imageName");
        // 2. 得到图片的存放路径 - 将
        String realPath = request.getServletContext().getRealPath("/WEB-INF/upload/");
        // 3. 通过完整的图片路径，得到file对象
        File file = new File(realPath + "/" + head);
        // 4. 通过截取，得到图片后缀
        String suffix = head.substring(head.lastIndexOf(".") + 1);
        // 5.setContentType() --- 设置响应数据类型, 使客户端浏览器区分不同种类的数据
        if ("PNG".equalsIgnoreCase(suffix)) {
            response.setContentType("image/png");
        } else if ("JPG".equalsIgnoreCase(suffix) || "JPEG".equalsIgnoreCase(suffix)) {
            response.setContentType("image/jpeg");
        } else if ("GIF".equalsIgnoreCase(suffix)) {
            response.setContentType("image/gif");
        }
        // 6. 将图片拷贝至响应输出流，输出至浏览器
        FileUtils.copyFile(file, response.getOutputStream());
    }

    /**
     * 进入个人中心
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void userCenter(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 设置首页动态包含的值
        request.setAttribute("changePage", "user/info.jsp");
        // 请求转发跳转到index
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }

    /**
     * 用户退出
     * @param request
     * @param response
     * @throws IOException
     */
    private void userLogout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 销毁Session
        request.getSession().invalidate();
        // 删除cookie
        Cookie cookie = new Cookie("user", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        // 跳转到登录页面
        response.sendRedirect("login.jsp");
    }

    /**
     * 用户登录
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void userLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1.获取用户输入的参数（用户名, 密码）
        String userName = request.getParameter("userName");
        String userPwd = request.getParameter("userPwd");


        // 2. 调用service层的方法，返回ResultInfo对象
        ResultInfo<User> resultInfo = userService.userLogin(userName, userPwd);

        // 登录成功
        if (resultInfo.getCode() == 1) {

            // 1. 将用户名信息设置到session作用域中
            request.getSession().setAttribute("user", resultInfo.getResult());
            // 2. 判断用户是否选择记住密码（rem的值为1）
            String rem = request.getParameter("rem");
            if ("1".equals(rem)) {
                // 得到cookie -- 将信息保存到cookie中
                Cookie cookie = new Cookie("user", userName+"-"+ userPwd);
                // 设置失效时间 (3天)
                cookie.setMaxAge(3*24*60*60);
                // 响应给客户端
                response.addCookie(cookie);
            } else {
                // 清空cookie对象
                Cookie cookie = new Cookie("user", null);
                cookie.setMaxAge(0);
            }
            // 3. 登录成功转发到index
            request.getRequestDispatcher("index").forward(request, response);
        } else {
            // 登录失败
            // 将resultInfo对象设置到request作用域中
            request.setAttribute("resultInfo", resultInfo);
            // 请求转发跳转到登录页面
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}
