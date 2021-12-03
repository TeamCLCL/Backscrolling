//对登陆页面的操作

$(function(){
	
	var identity = 0;	//用来区分普通用户或管理员
	var type = "error";	//用来保存账号的类型：email/username，方便后台验证
	var pwd = "";	//用来保存密码，和type共同作为是否向后台发起账户验证的依据
	
	//更改
	$("#idBtn").on("click",function(){
		//identity为0表示普通用户
		identity = (identity == 0) ? 1 : 0;
		$("#idBtn").text((identity == 0) ? "管理员登陆" : "用户登陆");
		$("#user").val((identity == 0) ? "请输入用户名或邮箱" : "请输入管理员账号");
		$("p").text("");
		$(":password").val("");
	})
	
	//当账号框的内容为提示信息时，选中则清空
	$("#user").on("focus",function(){
		var userContent = this.value.trim();
		if ((userContent == "请输入用户名或邮箱" && identity == 0) ||
			(userContent == "请输入管理员账号" && identity == 1)) {
			this.value = "";	
		}
	})
	
	//用户登陆时填写的账号可以是账号名或邮箱
	$("#user").on("blur",function(){
		var mailReg = /^[A-Za-z\d]+([-_.][A-Za-z\d]+)*@([A-Za-z\d]+[-.])+[A-Za-z\d]{2,4}$/;
		var nameReg = /^([a-zA-Z]|[0-9]|[\u4e00-\u9fa5]){2,8}$/;
		var user = this.value.trim();
		var massage;	//提示信息
		if(user === "") {
			massage = "账号不能为空";
			user.value = (identity == 0) ? "请输入管理员账号" : "请输入用户名或邮箱";
			type = "error";
		}else if(mailReg.test(user) && identity == 0) {
			//只有普通用户可以采用邮箱登陆，管理员只能用用户名登陆
			massage = "";
			type = "email";
		}else if(nameReg.test(user)) {
			massage = "";
			type = "username";
		}else {
			massage = "请输入正确格式的账号";
			type = "error";
		}
		$("#userMsg").text(massage);
	})
	
	//若密码只有空格，则选中密码的时候清空
	$("#password").on("focus",function(){
		var pwdvalue = this.value;
		if(pwdvalue != "" && pwdvalue.trim() == ""){
			//表示密码全是空格
			$("#password").val("");
		}
	})
	
	//登陆时密码不能为空
	$("#password").on("blur",function(){
		var password = this.value.trim();
		if(password === "") {
			$("#pwdMsg").text("密码不能为空");
			pwd = "";
		}else {
			//密码不为空
			$("#pwdMsg").text("");
			pwd = password;
		}
	})
	
	//当按下登陆按钮时，判断账号和密码是否都已合法
	//若不合法，提示正确填写；若合法，向后台发起请求
	$("#submitBtn").on("click",function(){
		if(type == "error" || pwd == "") {
			//表示账号或密码没有正确填写
			alert("请正确填写账号或密码");
		}else{
			var data = {
				"identity":identity,
				"type":type,
				"user":$("#user").val().trim(),
				"password":pwd.trim()};
			$.post("login",data,function(resq){
				eval("var json = " + resq);
				if(!json.loginSuccess) {
					//表示登陆失败
					alert("账号或密码错误，请重新登陆");
				} else {
					//登陆成功则按身份跳转
					if(identity == 0) {
						window.location = "index.html";
					} else {
						window.location = "manage.html";
					}
				}
			},"text");
			//alert("登陆中...身份为"+identity+"账号类型为"+type+"，账号为"+$("#user").val().trim()+"，密码为"+pwd.trim());
		}
	})
})

