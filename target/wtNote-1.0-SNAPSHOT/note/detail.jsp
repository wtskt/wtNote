<%--
  Created by IntelliJ IDEA.
  User: 王童
  Date: 2023/5/10
  Time: 16:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<style type="text/css">
    .note_content {
        background-color: #F5F5F5; /* 设置背景色，与白色环境区分 */
        border: 1px solid #DDDDDD; /* 设置边框 */
        border-radius: 5px; /* 设置圆角 */
        box-shadow: 0 1px 2px rgba(0, 0, 0, 0.5); /* 设置阴影 */
        padding: 20px; /* 设置内边距 */
        margin-bottom: 20px; /* 设置与下一个元素的距离 */
    }
</style>

<div class="col-md-9">
    <div class="data_list">
        <div class="data_list_title">
            <span class="glyphicon glyphicon-eye-open"></span>&nbsp;查看云记
        </div>

        <div>
            <div class="note_title"><h2>${note.title}</h2></div>
            <div class="note_info">
                发布时间：『 <fmt:formatDate value="${note.pubTime}" pattern="yyyy-MM-dd HH:mm"/>』&nbsp;&nbsp;云记类别：语录
            </div>
            <div class="note_content">
                <p>${note.content}</p>
            </div>
            <div class="note_btn">
                <button class="btn btn-primary" type="button" onclick="updateNote(${note.noteId})">修改</button>
                <button class="btn btn-danger" type="button" onclick="deleteNote(${note.noteId})">删除</button>
            </div>
        </div>
    </div>

    <script type="text/javascript">
        function deleteNote(noteId) {
            swal({
                title: "", 						        // 标题
                text: "<h3>您确认要删除该记录吗？</h3>",	// 内容
                type: "warning", 						// 图标  error  success	info  warning
                showCancelButton: true,  				// 是否显示取消按钮
                confirmButtonColor: "orange", 		    // 确认按钮的颜色
                confirmButtonText: "确定", 			    // 确认按钮的文本
                cancelButtonText: "取消" 				// 取消按钮的文本
            }).then(function(){
                // 确认删除后，发送ajax请求后台（类型ID）
                $.ajax({
                    type: "post",
                    url: "note",
                    data:{
                        actionName: "delete",
                        noteId: noteId
                    },
                   success:function (code) {
                        // 判断是否删除成功
                       if (code == 1) {
                           // 跳转到首页
                           window.location.href = "index";
                       } else {
                           // 提示失败
                           swal({
                               title: "",
                               text: "<h3>删除失败！</h3>",
                               type: "error"
                           })
                       }
                   }
                });
            });
        }

        /**
         * 进入发布云记的页面
         */
        function updateNote(noteId) {
            window.location.href = "note?actionName=view&noteId=" + noteId;
        }
    </script>

</div>