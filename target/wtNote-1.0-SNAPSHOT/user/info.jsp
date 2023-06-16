<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>

<div class="col-md-9">


    <div class="data_list">
        <div class="data_list_title"><span class="glyphicon glyphicon-edit"></span>&nbsp;个人中心 </div>
        <div class="container-fluid">
            <div class="row" style="padding-top: 20px;">
                <div class="col-md-8">

                    <form class="form-horizontal" method="post" action="user" enctype="multipart/form-data" onsubmit="return checkUser();">
                        <div class="form-group">
                        <%-- 隐藏域, 存放用户操作 --%>
                            <input type="hidden" name="actionName" value="updateUser">

                            <label for="nickName" class="col-sm-2 control-label">昵称:</label>
                            <div class="col-sm-3">
                                <input class="form-control" name="nick" id="nickName" placeholder="昵称" value="${user.nick}">
                            </div>

                            <label for="img" class="col-sm-2 control-label">头像:</label>
                            <div class="col-sm-5">
                                <%-- 将图片上传到文件域 : file --%>
                                <input type="file" id="img" name="img">
                            </div>

                        </div>
                        <div class="form-group">

                            <label for="mood" class="col-sm-2 control-label">心情:</label>
                            <div class="col-sm-10">
                                <textarea class="form-control" name="mood" id="mood" rows="3">${user.mood}</textarea>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-sm-offset-2 col-sm-10">
<%--                                修改信息表单提交--%>
                                <button type="submit" id="btn" class="btn btn-success" onclick="return updateUser();" >修改</button>&nbsp;&nbsp;
                                <span style="color:red; font-size: 12px" id="msg"></span>
                            </div>
                        </div>
                    </form>

                </div>

                <div class="col-md-4">
                    <%--指定图片的src属性为后端资源路径--%>

                    <img style="width:240px;height:180px" src="user?actionName=userHead&imageName=${user.head}">
                </div>

            </div>
        </div>
    </div>
</div>

<script type="text/javascript">
    /**
     * 验证昵称
     */
    $('#nickName').blur(function () {
        // 1. 获取昵称文本框的值
        var nickName = $('#nickName').val();
        // 2. 判断是否为空
        if (isEmpty(nickName)) {
            // 如果为空，提示用户，禁用按钮
            $('#msg').html('用户昵称不能为空');
            // 禁用按钮
            $('#btn').prop('disabled', true);
            return ;
        }
        // 如果不为空
        // 1. 从session中获取用户昵称
        var nick = '${user.nick}';
        // 用户昵称与session中一致
        if (nickName == nick) {
            return ;
        }
        // 用户昵称与session中不一致，发送修改请求
        $.ajax({
            type: "get",
            url: "user",
            /**
             * ajax传输数据
             */
            data:{
                actionName:"checkNick",
                nick:nickName
            },
            success: function (code) {
                // 如果可用，清空提示信息，按钮可用
                if (code == 1) {
                    // 1. 清空提示信息
                    $('#msg').html('');
                    // 2. 设置按钮可用
                    $('#btn').prop('disabled', false);
                } else {
                    // 如果不可用，提示用户，并禁用按钮
                    // 1. 提示用户
                    $('#msg').html('该昵称已存在，请重新输入');
                    // 设置按钮禁用
                    $('#btn').prop('disabled', true);
                }
            }
        })
    }).focus(function () {
        // 1. 清空提示信息
        $('#msg').html('');
        // 2. 设置按钮可用
        $('#btn').prop('disabled', false);
    });

    /**
     *修改表单提交的校验
     *  满足条件，返回true，表示提交表单
     *  不满足条件，返回false，表示不提交表单
     */
    function updateUser() {
        // 判断昵称输入是否为空 --- 以防万一，再写一遍
        var nickName = $("#nickName").val();
        if (isEmpty(nickName)) {
            $("#msg").html("用户昵称不能为空");
            $("#btn").prop("disabled", true);
            return false;
        }
        // 唯一性
        return true;
    }
</script>
