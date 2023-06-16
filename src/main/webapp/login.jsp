<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: 王童
  Date: 2023/3/22
  Time: 20:24
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>云R记</title>
    <link href="statics/css/login.css" rel="stylesheet" type="text/css"/>
    <script src="statics/js/jquery-1.11.3.js" type=text/javascript></script>
    <script src="statics/js/config.js" type=text/javascript></script>
    <script src="statics/js/util.js" type=text/javascript></script>

    <link rel="stylesheet" type="text/css" href="statics/css/style.css">
    <style type="text/css">
        body {
            background: linear-gradient(to right, #ff00cc, #333399, #66ccff, #ff6699);
            background-size: 800% 800%;
            animation: gradient 15s ease infinite;
            color: #fff;
            font-family: 'Roboto', sans-serif;
            margin: 0;
            padding: 0;
        }
        .container {
            display: flex;
            align-items: center;
            justify-content: center;
            height: 100vh;

            position: absolute;
            bottom: 0;
            left: 65%;
            transform: translateX(-10%);
        }
    </style>
</head>
<body>

<!--head-->
<div id="head">
    <div class="top">
        <div class="fl yahei18">开启移动办公新时代！</div>
    </div>
</div>

<div id="flash" style="position: relative">
    <div class="pos">
        <a bgUrl="images/banner-bg1.jpg" id="flash1" style="display:block;"><img src="statics/images/banner_pic1.png"></a>
        <a bgUrl="images/banner-bg2.jpg" id="flash2"                       ><img src="statics/images/banner-pic2.jpg"></a>
    </div>
    <div class="flash_bar">
        <div class="dq" id="f1" onclick="changeflash(1)"></div>
        <div class="no" id="f2" onclick="changeflash(2)"></div>
    </div>
</div>

<!--login box 用户登录模块-->
<div class="container">
    <div class="form-container">
        <h1>Login</h1>
        <form action="user" method="post" id="loginForm">
            <input type="hidden" id="revCode">

            <!--表示用户行为，通过这个参数在service里判断用户操作-->
            <input type="hidden" name="actionName" value="login"/>

            <!--通过resultInfo回显-->
            <label for="userName">用户名:</label>
            <input type="text" id="userName" name="userName" placeholder="请输入用户名"  value="${resultInfo.result.uname}" required>

            <label for="userPwd">密码:</label>
            <input type="password" id="userPwd" name="userPwd" placeholder="输入你的密码"  value="${resultInfo.result.upwd}" required>

            <label for="checkCode">验证码:</label>
            <input type="text" name="checkCode" id="checkCode" placeholder="请输入验证码" required>
            <img src="user?actionName=checkCode" alt="" id="code_image" width="100px" height="30px"/> <br/>

            <span><input name="rem" type="checkbox" value="1" class="inputcheckbox"/> <label style="font-size: 8px">记住我</label></span>
            <!-- 将后端resultInfo中的msg信息取出 -->
            <span id="msg" style="color: red; font-size: 12px;">${resultInfo.msg}</span>
            <button type="submit" onclick="return checkLogin()" id="login_btn" > 登录 </button>
        </form>
        <div class="register-link">
            <p style="color: #3dd5f3">没有账号? <a href="register.jsp">在这里注册</a></p>
        </div>
    </div>
</div>

<!--bottom-->
<div id="bottom">
    <div id="copyright">
        <div class="text">
            Copyright © 2006-2026 <a href="https://www.shsxt.com">wt</a> All Rights Reserved
        </div>
    </div>
</div>

<script type="text/javascript">

    $('#userName').focus(function () {
        $('#msg').html('');
    });
    $('#userPwd').focus(function () {
        $('#msg').html('');
    });
    $("#checkCode").focus(function(){
        $('#msg').html('');

    });

    // 给验证码的图片， 绑定单击事件
     $("#code_image").click(function () {
         // 在最后添加一个随便的参数，避免浏览器从缓存中获取资源
         this.src = "user?actionName=checkCode&amp;" + new Date();
     });

    /**
     * 失去焦点后对验证码的校验
     */
    $("#checkCode").blur(function () {
         let checkCode = $("#checkCode").val();
         if (isEmpty(checkCode)) {
             $('#msg').html('请输入验证码');
         } else {
             $.ajax({
                 type: "get",
                 url: "check",
                 data:{
                     code: checkCode,
                     actionName: "checkVerifyCode"
                 },
                 success: function (code) {
                     if (code == 0) {
                         $('#msg').html('验证码错误');
                         $("#login_btn").prop("disabled", true);
                     } else {
                         $("#login_btn").prop("disabled", false);
                     }
                 }
             });
         }
     });

</script>
</body>

</html>
