//主页面相关操作

/*collect = function(datas){
	for(var i = 0; i < datas.length; i++) {
		$("#"+datas[i].id).click(function(){
			//当要收藏时，收藏数+1；当要取消收藏时，收藏数-1
			//colType保存当前收藏状态：【收藏】或【取消收藏】
			//var colType = $("#"+datas[i].id).val();
			var colType = document.getElementById(datas[i].id).innerText;
			//colNum保存用户点击后的收藏数
			var colNum = (colType == '收藏') ? (datas[i].collect + 1) : (datas[i].collect - 1);
			var useroperres = (colType == '收藏') ? ("collect") : ("remove");

			//发送请求修改收藏数
			$.post("user",{"useroperres":useroperres,"resourceid":datas[i].id},function(resq) {
				//修改展示时的收藏数
				$("#"+datas[i].id+"_col").text(colNum);
				//进行【收藏】和【取消收藏】的切换
				$("#"+datas[i].id).val((colType == '收藏') ? ("取消收藏") : ("收藏"));
			},"text");
		})
	}
}*/

setCollect = function(id){

	$("#"+id).click(function(){
		//当要收藏时，收藏数+1；当要取消收藏时，收藏数-1
		//colType保存当前收藏状态：【收藏】或【取消收藏】
		var colType = $("#"+id).val();
		//colNum保存用户点击收藏按钮前的收藏数
		var colNum = parseInt($("#"+id+"_col").html());
		var useroperres = (colType == '收藏') ? ("collect") : ("remove");

		alert(colType);
		//发送请求修改收藏数
		$.post("user",{"useroperres":useroperres,"resourceid":id},function(resq) {
			//修改展示时的收藏数
			$("#"+id+"_col").html((colType == '收藏') ? (colNum + 1) : (colNum - 1));
			//进行【收藏】和【取消收藏】的切换
			$("#"+id).val((colType == '收藏') ? ("取消收藏") : ("收藏"));
			alert($("#"+id).val());
		},"text");
	})

}

showResource = function(page) {
	//传回来的json中包含了与分页相关的：
	//页码pageno，资源总数totalsize，每页条数pagesize，资源数组dataList
	//dataList中有id title link collect
	maxpageno = Math.ceil(page.totalsize / page.pagesize);	//全局变量最大页号
	var resource = "";
	var datas = page.dataList;
	$("#resource").html("");
	for(var i = 0; i < datas.length; i++) {

		resource += "<li><a href='"+datas[i].link+"' target='_blank'>"+datas[i].title+"</a>  ";
		resource += "<input id='"+datas[i].id+"' type='button' value='收藏' />  ";
		resource += "<span id='"+datas[i].id+"_col'>"+datas[i].collect+"</span></li>";

		$("#resource").append(resource);
		resource = "";

		setCollect(datas[i].id);

		/*$("#"+datas[i].id).click(function(){
			//当要收藏时，收藏数+1；当要取消收藏时，收藏数-1
			//colType保存当前收藏状态：【收藏】或【取消收藏】
			//var colType = $("#"+datas[i].id).text();
			var colType = document.getElementById(datas[i].id).innerText;
			//colNum保存用户点击后的收藏数
			var colNum = (colType == '收藏') ? (datas[i].collect + 1) : (datas[i].collect - 1);
			var useroperres = (colType == '收藏') ? ("collect") : ("remove");

			//发送请求修改收藏数
			$.post("user",{"useroperres":useroperres,"resourceid":datas[i].id},function(resq) {
				//修改展示时的收藏数
				$("#"+datas[i].id+"_col").text(colNum);
				//进行【收藏】和【取消收藏】的切换
				$("#"+datas[i].id).text((colType == '收藏') ? ("取消收藏") : ("收藏"));
			},"text");
		})*/
	}


	$("#whichPage").text(pageno+"/"+maxpageno);
	if(pageno == 1) {
		//第一页无法点击上一页按钮
		$("#lastPage").attr("disabled",true);
		if(maxpageno == 1) {
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

