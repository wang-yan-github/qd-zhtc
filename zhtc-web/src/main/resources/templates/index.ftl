﻿<!doctype html>
<html xmlns="http://www.w3.org/1999/html">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>智慧停车数据分析平台</title>
    <script type="text/javascript" src="/sjfx/js/jquery.min.js?v=2.1.4"></script>
    <link href="/sjfx/css/css.css" rel="stylesheet">
    <link href="/sjfx/css/mobile.css" rel="stylesheet">
    <link href="/sjfx/css/loaders.css" rel="stylesheet">
</head>

<body oncontextmenu="return true;">
<iframe frameborder="0" id="mainbody" class="frmWin"></iframe>
<div class="headBox">
    <p class="head">智慧停车数据分析平台</p>
    <!--
              <div class="weatherbox">
                 <iframe id="fancybox-frame" name="fancybox-frame1593480476992" frameborder="0" scrolling="no" hspace="0"  src="http://i.tianqi.com/index.php?c=code&a=getcode&id=34&h=25&w=280&color=%23FFFFFF&site=14" style="color:#fff;"></iframe></div>
    -->
    <div id="getTime"></div>
    <script type="text/javascript">
        function time() {
            dt = new Date();
            var y = dt.getFullYear();
            var ms = ("0" + (dt.getMonth() + 1)).slice(-2);
            var d = ("0" + dt.getDate()).slice(-2);
            var h = dt.getHours();
            var m = ("0" + dt.getMinutes()).slice(-2);
            var s = dt.getSeconds();
            var zhou = '星期' + '日一二三四五六'.charAt(dt.getDay());
            var str = "数据截止至：" + y + "年" + ms + "月" + d + "日" + " " + h + ":" + m + ":" + s + " " + zhou;
            document.getElementById("getTime").innerHTML = str;
        }

        setInterval(time, 1000);
    </script>

    <!--<ul class="useradmin f-cb">
       <li><a href="#"><i class="user"></i>admin</a></li>
       <li><a href="#"><i class="quick"></i>退出</a></li>
    </ul>-->


    <!--
               <div class="nav">
        <div class="nav-main">
            <ul class="nav-ul">
                <li><a href="yyjj.html" class="first">选择统筹区<span></span></a>
                    <div class="subnav" style="display: none;">
                        <a href="yyjj.html" class="one">市区</a>
                        <a href="yyhj.html" class="one">邳州</a>
                        <a href="feedyy1.html" class="one">睢宁</a>
                        <a href="contact.html" class="one">云龙</a>

                    </div>
                </li>
            </ul>
        </div>
    </div>
    -->
    <button class="fullSc" id="fsbutton" onclick="fScreen()">全屏</button>
</div>
<div class="menu">
    <ul class="f-cb">
        <li class="active"><a href="javascript:;" frmurl="/sjfx/parking.do">路侧</a></li>
        <li><a href="javascript:;" frmurl="/sjfx/main.do">停车场</a></li>

    </ul>
</div>


<div id="overlay" class="overlay"></div>
<div id="loading">
    <div class="line-scale">
        <div></div>
        <div></div>
        <div></div>
        <div></div>
        <div></div>
    </div>
</div>

<!-- page-wrapper -->
<script>
    $(function () {
        $("#mainbody").attr("src", "/sjfx/parking.do");
        $('.nav-ul li').hover(function () {
            $(this).find('.subnav').stop(true, true).slideDown(400)
            $(this).children("a").addClass("on");
        }, function () {
            $(this).find('.subnav').stop(true, true).slideUp(400);
            $(this).children("a").removeClass("on");
        })
        //fScreen();
        $(".menu li").click(function () {
            $(this).addClass("active").siblings().removeClass("active");
            $(".menu1 li").removeClass("active");
        })
        $(".menu1 li").click(function () {
            $(this).addClass("active").siblings().removeClass("active");
            $(".menu li").removeClass("active");
        })
        //页面跳转
        $('body').on('click', '*[frmurl]', function () {
            var othis = $(this)
                , href = othis.attr('frmurl')
            othis.siblings().removeClass("cur");
            othis.addClass("cur");
            $("#mainbody").attr("src", href);
        });

    })

    function showLoading() {
        document.getElementById("loading").className = "show";
    }

    function hideLoading() {
        document.getElementById("loading").className = "hide";
    }

    function showoverlay() {
        document.getElementById("overlay").style.opacity = 1;
    }

    function hideoverlay() {
        document.getElementById("overlay").className = "hide"
    }
</script>
<script src="/sjfx/js/config.map.js" charset="utf-8"></script>
</body>
</html>
