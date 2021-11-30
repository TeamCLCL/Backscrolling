//主页面相关操作

showResource = function(page) {
	//传回来的json中包含了与分页相关的：
	//页码pageno，资源总数totalsize，每页条数pagesize，资源数组dataList
	//dataList中有id title link collect
	maxpageno = Math.ceil(page.totalsize/page.pagesize);	//全局变量最大页号
	var resource = "<li>序号  资源  操作 当前收藏量</li>";
	var datas = page.dataList;
	for(var i = 0; i < datas.length; i++) {
		resource += "<li>"+datas[i].id+"  <a href='"+datas[i].link+"' target=_blank>"+datas[i].title+"</a>  "+"收藏"+"  "+datas[i].collect+"</li>";
	}

	$("#resource").html(resource);
	$("#whichPage").text(pageno+"/"+maxpageno);
	if(pageno == 1) {
		//第一页无法点击上一页按钮
		$("#lastPage").attr("disabled",true);
	} else if(pageno == maxpageno) {
		//最后一页无法点击下一页按钮
		$("#nextPage").attr("disabled",true);
	}


}

//前往指定页
toSomePage = function(toWhich) {
	alert("即将前往第"+toWhich+"页");
	//请求拿到对应页的资源
	$.post("user",{"selectBy":"common","pageno":toWhich},function(resq){
		eval("var page = " + resq);
		$("#lastPage").attr("disabled",false);
		$("#nextPage").attr("disabled",false);
		//展示资源
		showResource(page);
	},"text");
	
}

$(function(){
	
	//页面加载完就发出请求
	//1、发出请求查看是否已经有用户登陆，若已登录则应该在页面上有所展现，若未登陆则应有登陆按钮
	//2、加载第一页的资源(后续资源需要在用户访问时才加载)
	
	var status;		//保存用户的状态，0表示未登录，1表示已登录

	$.post("indexload",{"type":"indexload"},function(resq){
		//传回来的json包括json[0]登陆状态和json[1]第一页资源相关的信息
		//处理登陆状态
		eval("var json = " + resq);
		if(json[0].isLogin) {
			//已登录
			status = 1;
			$("#msg").text(json[0].username + "，欢迎您，点此");
			$("#loginStatus").val("退出登录");
		} else {
			//未登录
			status = 0;
			$("#msg").text("欢迎您，点此");
			$("#loginStatus").val("登录");
		}
		//第一次访问index页面的时候展示第一页资源
		pageno = json[1].pageno;	//全局变量，当前页号，初始值为1
		showResource(json[1]);
	},"text");

	$("#loginStatus").on("click",function(){
		//如果是未登录状态，接下来要跳转到登陆页面进行登陆
		//如果是登陆状态，接下来要退出登陆，退出后留在原页
		if (status == 1) {
			$.ajax({
				"url":"logout",
				"type":"post",
				"data":{"type":'logout'}
			});
			status = 0;
			$("#msg").text("欢迎您，点此");
			$("#loginStatus").val("登录");
		} else {
			window.location = "login.html";
		}
	})
	
	$("#lastPage").click(function(){
		//前往上一页，当前页面pageno应减一
		toSomePage(--pageno);
	})
	
	$("#nextPage").click(function(){
		//前往下一页，当前页面pageno应加一
		toSomePage(++pageno);
	})
	
	$("#goto").click(function(){
		//前往指定页，当前页面pageno应和文本框中的页号一致
		var targetPage = $("#target").val().trim();
		var reg = /^[0-9]+$/;
		if (reg.test(targetPage)) {
			//先匹配是否为由【数字】构成的字符串，是则转换为数字
			targetPage = parseInt(targetPage);
			//在判断数字方位
			if (1 <= targetPage && targetPage <= maxpageno) {
				pageno = targetPage;
				toSomePage(targetPage);
			} else {
				alert("请输入【1~"+maxpageno+"】的数字");
			}
		} else {
			alert("请输入【1~"+maxpageno+"】的数字");
		}
		$("#target").val("");
		
		
	})
})

