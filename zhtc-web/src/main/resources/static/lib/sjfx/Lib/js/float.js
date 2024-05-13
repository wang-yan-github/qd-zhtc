$(function () {

	var llheight = $(".fixedgg").offset().top + $(".fixedgg").height();
	var mb = myBrowser();
	var w;
	if ("IE" == mb) {
		w=$("#table-list").width()-2
	}
	if ("FF" == mb) {
		w=$("#table-list").width()
	}
	if ("Chrome" == mb) {
		w=$("#table-list").width()-1
	}
	//var w=$("#dataTable").width();
	//alert(w);
	$(window).scroll(function() {
		var sTop = document.documentElement.scrollTop == 0 ? document.body.scrollTop : document.documentElement.scrollTop;

		var scrollLeft = $("#ovlTable").scrollLeft()-31;		
		if (sTop >= llheight) {
			//alert(sTop+'|'+llheight+'|'+$(".fixedgg").height()+'|'+ $(".fixedgg").offset().top);
			
			$("#fixedHeader").css({
				position: "fixed",
				top: "0px",
				width:w+"px"
				
			});
			$("#shadowbg").show();
		} else {
			$("#fixedHeader").css({
				position: "",
				top: "",
				width:""
				
			});
			$("#shadowbg").hide();
		}



	});

	$("#ovlTable").scroll(function() {
		
		var scrollLeft = $("#ovlTable").scrollLeft()-31;
		//alert(scrollLeft);
		
		if(scrollLeft>0){
			$("#fixedHeader").css({				
				left:"-"+scrollLeft + "px"
			});
		}else{
			$("#fixedHeader").css({				
				left:""
			});
		}

	});
});

function myBrowser(){
	var userAgent = navigator.userAgent; //取得浏览器的userAgent字符串
	var isOpera = userAgent.indexOf("Opera") > -1;
	if (isOpera) {
		return "Opera"
	}; //判断是否Opera浏览器
	if (userAgent.indexOf("Firefox") > -1) {
		return "FF";
	} //判断是否Firefox浏览器
	if (userAgent.indexOf("Chrome") > -1){
		return "Chrome";
	}
	if (userAgent.indexOf("Safari") > -1) {
		return "Safari";
	} //判断是否Safari浏览器
	if (userAgent.indexOf("compatible") > -1 && userAgent.indexOf("MSIE") > -1 && !isOpera) {
		return "IE";
	}; //判断是否IE浏览器
}	