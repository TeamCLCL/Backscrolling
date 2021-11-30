
			
// function welcome(){
// 	window.alert("此页面功能尚未开放");
// }

window.onload = function(){
	document.getElementById("b1").onclick = function(){
	document.getElementById("x1").style.display="block";
	document.getElementById("x2").style.display="none";
	document.getElementById("x3").style.display="none"}

	document.getElementById("b2").onclick = function(){
	document.getElementById("x2").style.display="block";
	document.getElementById("x1").style.display="none";
	document.getElementById("x3").style.display="none"}

	document.getElementById("b3").onclick = function(){
	document.getElementById("x3").style.display="block";
	document.getElementById("x1").style.display="none";
	document.getElementById("x2").style.display="none"}
}