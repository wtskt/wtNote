package com.wt.note.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.wt.note.dao.UserDao;
import com.wt.note.po.User;
import com.wt.note.vo.ResultInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

/**
 * @author 王童
 */
public class UserService {

    private UserDao userDao = new UserDao();


    public ResultInfo<User> userLogin (String userName, String userPwd) {
        ResultInfo<User> resultInfo = new ResultInfo<>();

        // 数据回显: 当登录失败时，将登录信息返回给页面
        User t = new User();
        t.setUname(userName);
        t.setUpwd(userPwd);
        // 设置到resultInfo对象中
        resultInfo.setResult(t);

        // 1. 判断参数是否为空
        if (StrUtil.isBlank(userName) || StrUtil.isBlank(userPwd)) {
            resultInfo.setCode(0);
            resultInfo.setMsg("用户名或密码不能为空!");
            return resultInfo;
        }
        // 2. 如果不为空，通过用户名查询用户对象
        User user = userDao.queryUserByName(userName);
        // 3. 用户名不存在
        if (user == null) {
            resultInfo.setCode(0);
            resultInfo.setMsg("用户名不存在!");
            return resultInfo;
        }
        // 4. 如果用户对象不为空，将数据库中查询到的用户对象的密码与前台传递的密码作比较
        // 将前台传递的密码按照MD5方式加密
        userPwd = DigestUtil.md5Hex(userPwd);
        // 判断加密后的密码是否与数据库一致
        if (!userPwd.equals(user.getUpwd())) {
            // 不一致
            resultInfo.setCode(0);
            resultInfo.setMsg("用户密码不正确!");
            return resultInfo;
        }
        // 5. 账号密码正确
        resultInfo.setCode(1);
        resultInfo.setResult(user);
        return resultInfo;
    }

    /**
     * 验证昵称
     * @param nick
     * @param userId
     * @return
     */
    public int checkNick(String nick, int userId) {
        // 1. 判断昵称是否为空
        if (StrUtil.isBlank(nick)) {
            return 0;
        }
        // 2. 调用Dao层，通过用户ID和昵称查询用户对象
        User user = userDao.queryUserByIdAndNick(nick, userId);
        // 3. 判断用户对象存在
        if (user != null) {
            // 存在
            return 0;
        }
        // 不存在
        return 1;
    }

    /**
     * 修改用户信息
     * @param request
     * @return
     */
    public ResultInfo<User> updateUser(HttpServletRequest request) {
        ResultInfo<User> resultInfo = new ResultInfo<>();
        // 1. 获取参数（昵称，心情）--- 头像参数通过文件域获取
        String nick = request.getParameter("nick");
        String mood = request.getParameter("mood");
        // 2. 参数的校验
        if (StrUtil.isBlank(nick)) {
            // 如果昵称为空，将状态码和错误信息设置resultInfo对象中，返回resultInfo对象
            resultInfo.setCode(0);
            resultInfo.setMsg("用户昵称不能为空！");
            return resultInfo;
        }
        // 3. 从session作用域中获取用户对象（获取用户的默认头像）
        User user = (User) request.getSession().getAttribute("user");
        // 设置修改的昵称和头像
        user.setNick(nick);
        user.setMood(mood);

        // 4. 实现文件上传
        try {
            // 1. 获取Part对象 request.getPart("name"); name 代表的是file文件域的name属性值
            /*
                Part 能获取所有的请求参数的参数名，而 Parameter 只能获取 非文件类型 的参数名
                Part 不能获得非文件类型参数的参数值，Part获得二进制的输入流
                Parameter 只能获得非文件类型参数值
            */

            Part img = request.getPart("img");
            // 2. 通过Part对象获取上传文件的文件名（从头部信息中获取用户上传的头像文件名）
            String header = img.getHeader("Content-Disposition");
            // 获取具体的请求头对应的值 (键值对属性)
            String str = header.substring(header.lastIndexOf("=")+2);
            // 获取上传的文件名
            String fileName = str.substring(0, str.length() - 1);
            // 3. 判断文件名是否为空, 即用户上传了头像
            if (!StrUtil.isBlank(fileName)) {
                // 更新用户对象中的头像数据
                user.setHead(fileName);
                // 4. 获取文件存放的路径（绝对路径）
                String filePath = request.getServletContext().getRealPath("/WEB-INF/upload");
                // 5. 上传文件到指定目录 (将选择的图片写入该目录)
                // TODO
                img.write(filePath + "/" + fileName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 6. 调用Dao层的更新方法，更新用户信息，返回受影响的行数
        int row = userDao.updateUser(user);
        // 7. 判断受影响的行数
        if (row > 0) {
            resultInfo.setCode(1);
            // 更新session中用户对象
            request.getSession().setAttribute("user", user);
        } else {
            resultInfo.setCode(0);
            resultInfo.setMsg("更新失败！");
        }
        return resultInfo;
    }

    /**
     * 验证用户名的唯一性
     * @param userName
     * @return
     */
    public int checkUserName(String userName) {
        int row = userDao.checkUserName(userName);
        if (row != 0) {
            return 1;
        }
        return 0;
    }

    /**
     * 用户注册
     * @param userName
     * @param password
     */
    public void register(String userName, String password) {
        userDao.registerUser(userName, password);
    }
}
