<%--
  Created by IntelliJ IDEA.
  User: 王童
  Date: 2023/5/13
  Time: 13:30
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>注册页面</title>
    <script src="statics/js/jquery-1.11.3.js" ></script>
    <script src="statics/js/util.js"></script>
    <link rel="stylesheet" type="text/css" href="statics/css/style.css">
    <style type="text/css">
        body {
            background: linear-gradient(to bottom, #4e54c8, #8f94fb);
            color: #fff;
            font-family: 'Roboto', sans-serif;
            margin: 0;
            padding: 0;
            /*background-image: url("");*/
            /*background-repeat: no-repeat;*/
            background-size: cover;
        }
        .container {
            display: flex;
            align-items: center;
            justify-content: center;
            height: 100vh;
        }
    </style>

</head>
<body>
<!--login box 用户登录模块-->
<div class="container">
    <div class="form-container">
        <h3 style="color: #0f0f0f" align="center">注册</h3>
        <form method="post" action="user?actionName=register">
            <label for="username" class="form-label">用户名</label>
            <input type="text" id="username" placeholder="请输入用户名" name="userName" required>

            <label for="password" class="form-label">密码</label>
            <input type="password" id="password" placeholder="请输入密码" name="passWord" required>

            <label for="confirmPassword" class="form-label">确认密码</label>
            <input type="password" id="confirmPassword" placeholder="请再次输入密码" required>

            <br/>
            <span style="color:red; font-size: 12px" id="msg"></span>
            <br/>

            <button type="submit" id="btn" >注册</button>
        </form>
    </div>
</div>

<script type="text/javascript">
    /**
     * 检验用户名是否可用 --- 失去焦点时
     */
    $('#username').blur(function () {
        let userName = $('#username').val();
        if (userName != '') {
            $.ajax({
                type: "post",
                url: "user",
                data: {
                    actionName: "checkUserName",
                    userName: userName
                },
                success: function(code) {
                    if (code == 0) {
                        // 如果可用，清空提示信息
                        // 1. 清空提示信息
                        $('#msg').html('');
                        $('#btn').prop('disabled', false);
                    } else {
                        // 如果不可用，提示用户
                        // 1. 提示用户
                        $('#msg').html('用户名已存在');
                        $('#btn').prop('disabled', true);
                    }
                }
            });
        } else {
            $('#msg').html('');
            $('#btn').prop('disabled', false);
        }
    });

    /**
     * 注册按钮点击事件
     */
    $('#btn').click(function () {
        let password = $('#password').val();
        let confirmPassword = $('#confirmPassword').val();
        let username = $('#username').val();
        if (isEmpty(username)) {
            $('#msg').html('请输入账号');
            return false;
        }
        if (isEmpty(password)) {
            $('#msg').html('请输入密码');
            return false;
        }
        if (isEmpty(confirmPassword)) {
            $('#msg').html('请确认密码');
            return false;
        }
        return true;
    });
</script>

</body>
</html>