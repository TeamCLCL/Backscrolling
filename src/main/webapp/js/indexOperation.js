//主页面相关操作

checkStatus = function() {
	//检查登陆状态，若未登录则提示,并直接前往登陆页面，若已登录则可继续操作
	if (status == 0) {
		//未登录
		alert("请在登录后进行操作");
		window.location = "login.html";
	} else {
		return true;
	}
}

setCollect = function(id) {
	$("#"+id).click(function() {
		//只有当用户已登录的情况下才能进行收藏操作
		if (checkStatus()) {
			//当要收藏时，收藏数+1；当要取消收藏时，收藏数-1
			//colType保存当前收藏状态：【收藏】或【取消收藏】
			var colType = $("#"+id).val();
			//colNum保存用户点击收藏按钮前的收藏数
			var colNum = parseInt($("#"+id+"_col").html());
			var useroperres = (colType == '收藏') ? ("collect") : ("remove");
			
			//发送请求修改收藏数
			$.post("user",{"useroperres":useroperres,"resourceid":id},function(resq) {
				//修改展示时的收藏数
				$("#"+id+"_col").html((colType == '收藏') ? (colNum + 1) : (colNum - 1));
				//进行【收藏】和【取消收藏】的切换
				$("#"+id).val((colType == '收藏') ? ("取消收藏") : ("收藏"));
			},"text");
		}
	})
}

showResource = function(page) {
	//传回来的json中包含了与分页相关的：
	//页码pageno，资源总数totalsize，每页条数pagesize，资源数组dataList
	//dataList中有id title link collect isCollect
	maxpageno = Math.ceil(page.totalsize / page.pagesize);	//全局变量最大页号
	var resource = "";
	var datas = page.dataList;
	$("#resource").html("");
	for(var i = 0; i < datas.length; i++) {

		resource += "<li><a href='"+datas[i].link+"' target='_blank'>"+datas[i].title+"</a>  ";
		resource += "<input id='"+datas[i].id+"' type='button' value='"+(datas[i].isCollect) ? ("取消收藏") : ("收藏")+"' />  ";
		resource += "<span id='"+datas[i].id+"_col'>"+datas[i].collect+"</span></li>";

		$("#resource").append(resource);
		resource = "";

		setCollect(datas[i].id);
	}

	$("#whichPage").text(pageno+"/"+maxpageno);
	if(pageno == 1) {
		//第一页无法点击上一页按钮
		$("#lastPage").attr("disabled",true);
		if(maxpageno == 1) {
			//只有一页时，【上一页】和【下一页】都无法点击
			$("#nextPage").attr("disabled",true);
		}
	} else if (pageno == maxpageno) {
		//最后一页无法点击下一页按钮
		$("#nextPage").attr("disabled",true);
	}
}

//前往指定页
toSomePage = function(toWhich) {
	alert("即将前往第"+toWhich+"页");
	//请求拿到对应页的资源
	$.post("user",{"selectBy":selectBy,"pageno":toWhich},function(resq){
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
	//3、加载所有的资源类别，留以【按类别搜索】时使用
	
	status = 0;		//保存用户的状态，0表示未登录，1表示已登录，默认未登录
	selectBy = "common";	//搜索资源的类型，common表示普通资源，keyword表示按关键字搜索，type表示按类型搜索
	var allType = "<option value='0' disabled selected>请选择类型...</option>";	//保存所有的资源类型
	

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
	
	//页面加载完毕后发出请求，获取所有的资源类别，留以后面使用
	$.post("indexload",{"type":"type"},function(resq){
		//得到全部类型，包括【类型在数据库表中的id】和【类型名】
		eval("var getType = " + resq);
		for(var i = 0; i < getType.length; i++) {
			allType += "<option value='"+getType[i].id+"'>"+getType[i].type+"</option>";
		}
	},"text");
	
	//每次切换查询类型，后面的框都要随之改变
	$("#selectchange").change(function(){
		var selectType = $("#selectchange>option:selected").val();
		//每次切换都要将框中的内容变为原样
		if (selectType == "keyword") {
			//按关键字搜索，显示搜索框，隐藏选择框
			$("#keyword").css("display","inline");
			$("#type").css("display","none");
			$("#getKeyword").val("");
		} else {
			//按类型搜索，隐藏搜索框，显示选择框
			$("#keyword").css("display","none");
			$("#type").css("display","inline");
			$("#getType").html(allType);
		}
	})
	
	//搜索
	$("#searchRes").click(function(){
		//查询类型：关键字keyword或类别type
		selectBy = $("#selectchange>option:selected").val();
		//要查询的内容
		var searchContent = (selectType == "keyword") ? ($("#getKeyword").val()) : ($("#getType>option:selected").val());
		//发送请求搜索
		$.post("user",{"selectBy":selectBy,"keyword":searchContent},function(resq){
			eval("var page = " + resq);
			pageno = page.pageno;	//当前页号，初始值为1
			showResource(page);
		},"text")
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
	
	
})

