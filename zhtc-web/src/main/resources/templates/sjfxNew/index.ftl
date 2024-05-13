<!doctype html>
<html xmlns="http://www.w3.org/1999/html">
<head>
    <meta charset="utf-8">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>智慧停车数据分析平台</title>
    <script type="text/javascript" src="/sjfxNew/js/jquery.min.js?v=2.1.4"></script>
    <link href="/sjfxNew/css/css.css" rel="stylesheet">
    <link href="/sjfxNew/css/mobile.css" rel="stylesheet">
    <link href="/sjfxNew/css/loaders.css" rel="stylesheet">
    <link rel="shortcut icon" href="/sjfxNew/img/favicon.ico" type="image/x-icon" />
</head>

<body oncontextmenu="return true;">
<iframe frameborder="0" id="mainbody" name="myFrame" class="frmWin"></iframe>
<div class="headBox">
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
            var str = y + "年" + ms + "月" + d + "日" + " " + h + ":" + m + ":" + s + " " + zhou;
            document.getElementById("getTime").innerHTML = str;
        }

        setInterval(time, 1000);
    </script>

    <button class="map-btn" id="fMap" onclick="fMap()" title="地图"></button>
    <button class="back-btn" id="backTo" onclick="backTo()" title="返回"></button>
    <button class="fullSc" id="fsbutton" onclick="fScreen()" title="全屏"></button>
</div>
<div class="menu">
    <ul>
        <li class="active"><a href="javascript:;" frmurl="/sjfxNew/main.do">运营总览</a></li>
        <li><a href="javascript:;" frmurl="/sjfxNew/road.do">路侧</a></li>
        <li><a href="javascript:;" frmurl="/sjfxNew/parking.do">停车场</a></li>
    </ul>
</div>


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
        $("#mainbody").attr("src", "/sjfxNew/main.do");
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
            $('.map-btn').removeClass("toggle-btn")
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

    function backTo() {
        $("#mainbody").attr("src", '/sjfxNew/main.do');
        $('#backTo').hide()
        $('#fMap').show()
        $('.menu').show()
    }

    function showLoading() {
        document.getElementById("loading").className = "show";
    }

    function hideLoading() {
        document.getElementById("loading").className = "hide";
    }

    function fMap() {
        myFrame.window.mapToggle()
        $('.map-btn').toggleClass("toggle-btn")
    }

</script>
<script src="/sjfxNew/js/config.map.js" charset="utf-8"></script>
</body>
</html>
