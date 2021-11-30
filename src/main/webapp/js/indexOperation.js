//主页面相关操作

showResource = function(page) {
	//传回来的json中包含了与分页相关的：
	//页码pageno，资源总数totalsize，每页条数pagesize，资源数组dataList
	//dataList中有id title link collect
	var resource = "<li>序号  资源  操作 当前收藏量</li>";
	var datas = page.dataList;
	for(var i = 0; i < datas.length; i++) {
		resource += "<li>"+datas[i].id+"  <a href='"+datas[i].link+"'>"+datas[i].title+"</a>  "+"收藏"+"  "+datas[i].collect+"</li>";
	}
	resource += "<li>"+page.pageno+"/"+Math.ceil(page.totalsize/page.pagesize)+" 下一页</li>";
	$("#resource").html(resource);
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
		//处理传回来的资源信息
		showResource(json[1]);
	},"text");

	$("#loginStatus").on("click",function(){
		//如果是未登录状态，接下来要跳转到登陆页面进行登陆
		//如果是登陆状态，接下来要退出登陆，退出后留在原页
		alert(status);
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
})