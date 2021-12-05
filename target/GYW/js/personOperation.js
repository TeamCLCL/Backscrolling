//个人页面的相关操作

setCollect = function(id) {
	$("#"+id).click(function() {
		
		//colType保存当前收藏状态：【收藏】或【取消收藏】
		var colType = $("#"+id).val();
		var useroperres = (colType == '收藏') ? ("collect") : ("remove");
		
		//发送请求修改收藏状态
		$.post("user",{"useroperres":useroperres,"resourceid":id},function(resq) {
			//进行【收藏】和【取消收藏】的切换
			$("#"+id).val((colType == '收藏') ? ("取消收藏") : ("收藏"));
		},"text");
		
	})
}

show = function(page) {
	//传回来的json中包含了与分页相关的：
	//页码pageno，资源总数totalsize，每页条数pagesize，资源数组dataList
	//dataList中有id title link collect isCollect
	maxpageno = Math.ceil(page.totalsize / page.pagesize);	//全局变量最大页号
	var resource = "";
	var datas = page.dataList;
	$("#myStar").html("");
	for(var i = 0; i < datas.length; i++) {

		resource += "<div><a href='"+datas[i].link+"' target='_blank'>"+datas[i].title+"</a>  ";
		resource += "<input id='"+datas[i].id+"' type='button' value='取消收藏' /></div>";

		$("#myStar").append(resource);
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
	$.post("user",{"useroperres":"usercollects","pageno":toWhich},function(resq){
		eval("var page = " + resq);
		$("#lastPage").attr("disabled",false);
		$("#nextPage").attr("disabled",false);
		//展示资源
		show(page);
	},"text");
}

getMyStar = function() {
	//显示收藏页面，隐藏个人信息和浏览记录
	$("#getStar").css("display","block");
	$("#getMessage").css("display","none");
	$("#getHistory").css("display","none");

	$.post("user",{"useroperres":"usercollects","pageno":1},function(resq){
		eval("var page = " + resq);
		//展示收藏列表
		pageno = page.pageno;	//全局变量，当前页号，初始值为1
		show(page);
	},"text");
}

getMyMsg = function() {
	//隐藏收藏页面和浏览记录页面，显示个人信息页面
	$("#getStar").css("display","none");
	$("#getMessage").css("display","block");
	$("#getHistory").css("display","none");

	$.post("user",{"useropermsg":"msg"},function(resq){
		eval("var msg = " + resq);
		//展示个人信息
		$("#name").append(msg.name);
		$("#sex").append(msg.sex);
		$("#address").append(msg.address);
		$("#email").append(msg.email);
	},"text");
}

getMyHistory = function() {
	//隐藏收藏页面和个人信息页面，显示浏览记录页面
	$("#getStar").css("display","none");
	$("#getMessage").css("display","none");
	$("#getHistory").css("display","block");

	/*$.post("user",{"useropermsg":"msg"},function(resq){
		eval("var history = " + resq);

	},"text");*/
	$("#getHistory").text("history page!");
}

$(function(){
	//包括：
	//1、个人信息
	//2、我的收藏
	//3、浏览记录
	//4、更换记录
	var searchWhat = location.search.substr(1).split("=")[1];	//要查询什么
	//message star history
	if (searchWhat == "star") {
		getMyStar();	//查收藏列表
	} else if (searchWhat == "message") {
		getMyMsg();		//查询个人信息
	} else {
		getMyHistory();		//查询浏览历史
	}
	
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
	
	$("#resetBtn").click(function(){
		window.location = "reset.html";
	})
	
	$("#logoutBtn").click(function(){
		if (confirm("是否要退出登陆?")) {
			$.ajax({
				"url":"logout",
				"type":"post",
				"data":{"type":'logout'}
			});
			window.location = "index.html";
		}
	})

	$("#msgBtn").click(function(){
		getMyMsg();
	})

	$("#starBtn").click(function(){
		getMyStar();
	})

	$("#hisBtn").click(function(){
		getMyHistory();
	})

	$(".rights ul li a").click(function(){
		if (!checkStatus()) {
			$(".rights ul li a").attr("href","login.html");
		}
	})
})