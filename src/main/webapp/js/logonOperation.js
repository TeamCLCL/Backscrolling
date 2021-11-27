//对注册页面的操作

// 提示正确函数
function tripr(obj, trip) {
    document.getElementById(obj).innerHTML = "<c>" + trip + "</c>";
}

// 提示错误函数
function tripw(obj, trip) {
    document.getElementById(obj).innerHTML = "<b>" + trip + "</b>";
}

$(function(){
	var finUser = "";	//最终用于注册用户名
	var finEmail = "";	//最终用于注册的邮箱
	var finpwd = "";	//最终用于注册的密码
	var send=$("#send").get(0)	//获取发送验证码按钮
	var times=120;   		//设置倒计时的初值
	var checkCode="";		//验证码
	
	$("#user").on("focus",function(){
		if(this.value.trim() == "请输入用户名") {
			this.value = "";
		}
	}) 
	
	//验证用户名是否合法
	$("#user").on("blur",function() {
		var user = this.value.trim();
		var reg = /^([a-zA-Z]|[0-9]|[\u4e00-\u9fa5]){2,8}$/;
		if (user == "") {
			tripw("userMsg","用户名不能为空");
			finUser = "";
		} else if (!reg.test(user)) {
			tripw("userMsg", "用户名由2-8位的中文、英语字母或数字构成");
			finUser = "";
		} else {
			//此时用户名已合法
			//接下来验证用户名是否已被占用
			$.post("checkname",{"user":user},function(resq){
				eval("var json = " + resq);
				if(json.nameRepeat) {
					//表示用户名重复
					tripw("userMsg", "该用户名已被占用");
					finUser = "";
				} else {
					//用户名可用
					tripr("userMsg", "√");
					finUser = user;
				}
			},"text")
		}
	});
	
	
	$("#email").on("focus",function(){
		if(this.value.trim() == "请输入邮箱") {
			this.value = "";
		}
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
			//表示邮箱合法，接下来验证该邮箱是否已被绑定
			$.post("checkemail",{"email":email},function(resq){
				eval("var json = " + resq);
					//需要后台对邮箱进行唯一性验证，并传回emailRepeat参数
					//true则为重复，表示该邮箱已被绑定
					//false则为不重复，表示该邮箱可用
					if (json.emailRepeat) {
						tripw("emailMsg", "该邮箱已被绑定，请更换邮箱");
						finEmail = "";
					}else {
						tripr("emailMsg", "√");
						finEmail = email;
					}
			},"text");
			
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
		 tripr("pwdMsg", "√")
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
	
	
	//发送验证码
	$("#send").on("click",function(){
	  // 只有在邮箱可用的情况下才能发送验证码
	  if (finEmail != "") {
		//向后台发送【发出验证码】的请求，并从后台的响应中得到验证码
		$.post("url",{"email":finEmail},function(resq){
			eval("var json = " + resq);
			checkCode = json.checkCode;
		},"text");
		
		//开启计时器
		DownToZero();
	  } else {
		tripw("codeMsg","请保证邮箱的正确性");
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
	
	//注册
	$("#regBtn").on("click",function(){
		if(finUser == "" || finpwd == "" || finEmail == "") {
			//表示此时的用户名、密码、邮箱中有填写不正确的信息
			tripw("codeMsg","请正确填写注册信息");
		} else {
			//表示此时注册信息都已正确填写
			//接下来检查验证码是否正确
			if ($("#checkcode").val() == checkCode && checkCode != "") {
				//由于验证码会在倒计时结束时自动清空，因此不用考虑验证码是否失效
				var data = [{
					"user":finUser,
					"password":finpwd,
					"email":finEmail
				}];
				$.post("url",data,function(resq){
					eval("var json = " + resq);
					if (json.logonSuccess) {
						alert("注册成功");
						window.location = "login.html";
					} else {
						alert("服务器繁忙，请重试");
					}
					
				},"text");
			} else {
				//验证码错误或失效
				tripw("codeMsg","验证码错误或已失效");
			}
		}
		
		
	})
	
})

