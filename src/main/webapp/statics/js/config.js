//登录页图片切换 --- 轮播图
var currentindex=1;

$(function(){
	$("#flash").css("background-image","url("+$("#flash1").attr("bgUrl")+")");//设置banner背景颜色名称 这里的flash就是div的ID
});


function changeflash(i) {
	currentindex=i;
	for (j=1;j<=2;j++){//此处的2代表你想要添加的幻灯片的数量与下面的5相呼应
		if (j==i){
			$("#flash"+j).fadeIn("normal");
			$("#flash"+j).css("display","block");
			$("#f"+j).removeClass();
			$("#f"+j).attr("class","dq");
			//alert("#f"+j+$("#f"+j).attr("class"))
			$("#flash").css("background-image","url("+$("#flash"+j).attr("bgUrl")+")");
		}else{
			$("#flash"+j).css("display","none");
			$("#f"+j).removeClass();
			$("#f"+j).attr("class","no");
		}
	}
}
function startAm(){
	timerID = setInterval("timer_tick()",3000);//8000代表间隔时间设置
}
function stopAm(){
	clearInterval(timerID);
}
function timer_tick() {
	currentindex = currentindex >=2 ? 1 : currentindex + 1;//此处的5代表幻灯片循环遍历的次数
	changeflash(currentindex);
}
$(document).ready(function(){
	$(".flash_bar div").mouseover(function(){
		stopAm();
	}).mouseout(function(){
		startAm();
	});
	startAm();
});


// input
// $(function(){
// 	//鼠标焦点
// 	$(":input.user").focus(function(){
// 		$(this).addClass("userhover");
// 	}).blur(function(){
// 		$(this).removeClass("userhover")
// 	});
// 	$(":input.pwd").focus(function(){
// 		$(this).addClass("pwdhover");
// 	}).blur(function(){
// 		$(this).removeClass("pwdhover")
// 	});
//
// 	//输入用户名
// 	$(".user").focus(function(){
// 		var username = $(this).val();
// 		if(username == ''){
// 			$(this).val('');
// 		}
// 	});
// 	$(".user").blur(function(){
// 		var username = $(this).val();
// 		if(username == ''){
// 			$(this).val('');
// 		}
// 	});
//
// 	//输入密码
// 	$(".pwd").focus(function(){
// 		var password = $(this).val();
// 		if(password == ''){
// 			$(this).val('');
// 		}
// 	});
// 	$(".pwd").blur(function(){
// 		var password = $(this).val();
// 		if(password == ''){
// 			$(this).val('');
// 		}
// 	});
//
//
// });
// 用户登录表单校验
function checkLogin() {
	// 1. 获取用户名和密码及验证码
	var userName = $("#userName").val();
	var userPwd = $("#userPwd").val();
	let checkCode = $("#checkCode").val();
	var info = 0;
	// 2， 判断非空
	if (isEmpty(userName)) {
		$("#msg").html("用户名不能为空");
		return false;
	}
	if (isEmpty(userPwd)) {
		$("#msg").html("密码不能为空");
		return false;
	}


	if (isEmpty(checkCode)) {
		$("#msg").html("请输入验证码");
		return false;
	} else {

	}

	// 3. 提交表单、
	return true;
}