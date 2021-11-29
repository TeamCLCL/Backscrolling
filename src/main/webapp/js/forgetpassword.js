// 提示正确函数
function tripr(obj, trip) {
    document.getElementById(obj).innerHTML = "<c>" + trip + "</c>";
}

// 提示错误函数
function tripw(obj, trip) {
    document.getElementById(obj).innerHTML = "<b>" + trip + "</b>";
}

$(function(){
	var checkCode = "";
	var finEmail = "";
	var finpwd = "";
	var send=$("#send").get(0);	//获取发送验证码按钮
	var times=120;   			//设置倒计时的初值
	
	$("#email").on("focus",function(){
		if(this.value.trim() == "请输入邮箱") {
			this.value = "";
		}
		$("#send").attr("disabled",true);
		$("#send");
	})
	
	// 验证邮箱
	$("#email").on("blur",function() {
		var email = this.value.trim();
		var reg = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(\.[a-zA-Z0-9_-])+/;
		if (email == "") {
			tripw("emailMsg","邮箱不能为空");
			finEmail = "";
		} else if (!reg.test(email)) {
			tripw("emailMsg", "邮箱格式不正确");
			finEmail = "";
		} else {
			tripw("emailMsg","正在检测邮箱...");
			//表示邮箱合法，接下来验证该邮箱是否存在于数据库中
			$.post("checkemail",{"email":email},function(resq){
				eval("var json = " + resq);
					//需要后台对邮箱进行唯一性验证，并传回emailRepeat参数
					//true则为重复，表示该邮箱存在，可以进行下一步
					//false则为不重复，表示该邮箱不存在
					if (json.emailRepeat) {
						tripr("emailMsg", "√");
						$("#send").attr("disabled",false);
						finEmail = email;
					}else {
						tripw("emailMsg", "该邮箱未被绑定，请更换邮箱");
						finEmail = "";
					}
			},"text");
			
		}
	})
	
	//发送验证码
	$("#send").on("click",function(){
		// 只有在邮箱可用的情况下才能发送验证码
		if (finEmail != "") {
			//向后台发送【发出验证码】的请求，并从后台的响应中得到验证码
			$.post("sendmail",{"email":finEmail},function(resq){
				eval("var json = " + resq);
				checkCode = json.checkCode;
			},"text");

			//开启计时器
			DownToZero();
		}
	})
	
	//用于计时的函数
	function DownToZero() {
		send.disabled = true;
		if(times>0){
			send.value=(times--)+"s后重试";    //更新按钮的文本
			setTimeout(DownToZero,1000);    //通过定时器再次调用函数本身
		}else{
			send.value="发送验证码"; //将文本复原
			send.disabled = false;  //将按钮置为可用
			checkCode = "";		//将验证码置为空
			times=120;   //将数字还原为初值，以便再次计数
		}
	}
	
	//点击下一步按钮，检查验证码
	$("#next").on("click",function(){
		var btnContent = $("#next").val();
		if (btnContent == "下一步") {
			if ($("#checkcode").val() != checkCode || checkCode == "") {
				tripw("codeMsg","验证码错误或已失效");
			} else {
				$("#checkEmailDiv").css("display","none");
				$("#resetDiv").css("display","inline");
			}
		} 
		
	})
	
	// 验证密码格式
	$("#password").on("blur",function() {
		var password = this.value;
		var c = /^([a-zA-Z0-9]){8,16}$/;
		if(password == "") {
			tripw("pwdMsg", "密码不能为空");
		} else if (!c.test(password)) {
			tripw("pwdMsg", "密码长度为8-16位且只含有英语字母和数字");
		} else {
			tripr("pwdMsg", "√");
		}
		finpwd = "";
	})
	
	//在填写【确认密码】时要保证【密码】已填写
	$("#password2").on("focus",function(){
		
		if ($("#password").val() == ""){
			tripw("pwdMsg2", "请先设置密码");
		} else if($("#pwdMsg").text() != "√") {
			tripw("pwdMsg2", "请按要求设置密码");
		} else {
			tripw("pwdMsg2", "");
		}
		finpwd = "";
	})
	
	// 验证密码二次输入是否一致
	$("#password2").on("blur",function() {
		var password = $("#password").val();
		var password2 = this.value;
		if ($("#pwdMsg").text() == "√") {
			if(password != password2){
				tripw("pwdMsg2", "两次密码不一致");
				finpwd = "";
			} else {
				tripr("pwdMsg2", "√");
				finpwd = password;
			}
		} 
	})
	
	//发送修改密码的请求
	$("#resetPwd").on("click",function(){
		if (finpwd != "") {
			//表示可以修改密码了
			$.post("reset",{"email":finEmail,"password":finpwd},function(resq){
				eval("var json = " + resq);
				if (json.resetSuccess) {
					alert("密码修改成功");
					window.location = "login.html";
				} else {
					alert("服务器繁忙，请重试");
				}
			},"text")
		}
		
	})
})