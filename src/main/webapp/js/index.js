
// 点击猫猫返回
window.onload = function(){
    var oTop = document.getElementById("to_top");
    var screenw = document.documentElement.clientWidth || document.body.clientWidth;
    var screenh = document.documentElement.clientHeight || document.body.clientHeight;
    oTop.style.left = screenw - oTop.offsetWidth +"px";
    oTop.style.top = screenh - oTop.offsetHeight + "px";
    window.onscroll = function(){
      var scrolltop = document.documentElement.scrollTop || document.body.scrollTop;
      oTop.style.top = screenh - oTop.offsetHeight + scrolltop +"px";
    }
    oTop.onclick = function(){
      document.documentElement.scrollTop = document.body.scrollTop =0;
    }
  }  

  $(function(){
    var li = $('#sel');
    var cc=$('#sel option'); 
    var len=cc.length;
    li.change(function(){
    //  alert(123);
    var t = parseInt(li.get(0).selectedIndex);
     // var id=$('#chanpin'+t);
    //  alert(t) ;
  for(var i= 1;i<len;i++){
     if(t==i){
     // alert(i);
      $('#s'+t).show();

     }else{
       $('#s'+i).hide();
     }
     if(t==0){$('#s'+i).show();}
     }
   
    });   
});