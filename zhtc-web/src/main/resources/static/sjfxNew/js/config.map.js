window.isflsgrn = false;//ie11以下是否进入全屏标志，true为全屏状态，false为非全屏状态
window.ieIsfSceen = false;//ie11是否进入全屏标志，true为全屏状态，false为非全屏状态
//跨浏览器返回当前 document 是否进入了可以请求全屏模式的状态
function fullscreenEnable() {
    var isFullscreen = document.fullscreenEnabled ||
        window.fullScreen ||
        document.mozFullscreenEnabled ||
        document.webkitIsFullScreen;
    return isFullscreen;
}
//全屏
var fScreen = function () {
    var docElm = document.documentElement;
    if (docElm.requestFullscreen) {
        docElm.requestFullscreen();
    }
    else if (docElm.msRequestFullscreen) {
        docElm.msRequestFullscreen();
        ieIsfSceen = true;
    }
    else if (docElm.mozRequestFullScreen) {
        docElm.mozRequestFullScreen();
    }
    else if (docElm.webkitRequestFullScreen) {
        docElm.webkitRequestFullScreen();
    } else {//对不支持全屏API浏览器的处理，隐藏不需要显示的元素
        window.parent.hideTopBottom();
        isflsgrn = true;
        $("#fsbutton").text("恢复");		
		if ($("#fsbutton").hasClass('fullSc')) {			
            $("#fsbutton").removeClass('fullSc').addClass('closeSc');
        }
    }
}
//恢复
var cfScreen = function () {
    if (document.exitFullscreen) {
        document.exitFullscreen();
    }
    else if (document.msExitFullscreen) {
        document.msExitFullscreen();
    }
    else if (document.mozCancelFullScreen) {
        document.mozCancelFullScreen();
    }
    else if (document.webkitCancelFullScreen) {
        document.webkitCancelFullScreen();
    } else {
        window.parent.showTopBottom();
        isflsgrn = false;
        $("#fsbutton").text("全屏");
		if ($("#fsbutton").hasClass('fullSc')) {	
			//alert(111);
            $("#fsbutton").removeClass('fullSc').addClass('closeSc');
        }
    }
}
//全屏按钮点击事件
$("#header").click(function () {
	//document.getElementById('header').slideToggle(300);
	//$(".header").toggleClass("hideTop");
	$("#footer").toggleClass("hideFooter");
	//$(".content").toggleClass("hidemenu");	
	
    var isfScreen = fullscreenEnable();
    if (!isfScreen && isflsgrn == false) {
        if (ieIsfSceen == true) {
            document.msExitFullscreen();
            ieIsfSceen = false;
            return;
        }
        fScreen();
    } else {
        cfScreen();
    }
})

$("#fsbutton").click(function () {
	//document.getElementById('header').slideToggle(300);
	//$(".header").toggleClass("hideTop");
	$("#footer").toggleClass("hideFooter");
	//$(".content").toggleClass("hidemenu");	
	
    var isfScreen = fullscreenEnable();
    if (!isfScreen && isflsgrn == false) {
        if (ieIsfSceen == true) {
            document.msExitFullscreen();
            ieIsfSceen = false;
            return;
        }
        fScreen();
    } else {
        cfScreen();
    }
})
//键盘操作
$(document).keydown(function (event) {
    if (event.keyCode == 27 && ieIsfSceen == true) {
        ieIsfSceen = false;		
    }
});
//监听状态变化
if (window.addEventListener) {
    document.addEventListener('fullscreenchange', function () {
        if ($("#fsbutton").text() == "全屏") {
            $("#fsbutton").text("恢复");
			$("#fsbutton").removeClass('fullSc').addClass('closeSc');
        } else {
            $("#fsbutton").text("全屏");
			$("#fsbutton").removeClass('closeSc').addClass('fullSc');
			
        }
    });
    document.addEventListener('webkitfullscreenchange', function () {
        if ($("#fsbutton").text() == "全屏") {
            $("#fsbutton").text("恢复");
			$("#fsbutton").removeClass('fullSc').addClass('closeSc');
        } else {
            $("#fsbutton").text("全屏");
			$("#fsbutton").removeClass('closeSc').addClass('fullSc');
        }
    });
    document.addEventListener('mozfullscreenchange', function () {
        if ($("#fsbutton").text() == "全屏") {
            $("#fsbutton").text("恢复");
			$("#fsbutton").removeClass('fullSc').addClass('closeSc');
        } else {
            $("#fsbutton").text("全屏");
			$("#fsbutton").removeClass('closeSc').addClass('fullSc');
        }
    });
    document.addEventListener('MSFullscreenChange', function () {
        if ($("#fsbutton").text() == "全屏") {
            $("#fsbutton").text("恢复");
			$("#fsbutton").removeClass('fullSc').addClass('closeSc');
        } else {
            $("#fsbutton").text("全屏");
			$("#fsbutton").removeClass('closeSc').addClass('fullSc');
        }
    });
}