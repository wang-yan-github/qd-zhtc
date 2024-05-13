<!doctype html>
<html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport"
        content="user-scalable=0,width=device-width,initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=no,target-densitydpi=high-dpi" />
    <meta http-equiv="Pragma" content="no-cache">
    <meta http-equiv="Cache-Control" content="no-cache">
    <title>主页</title>
    <meta name="keywords" content="主页" />
    <meta name="description" content="主页" />
    <link rel="stylesheet" href="fonts/iconfont.css">
    <link rel="stylesheet" href="css/css.css">
    <script src="js/jquery-1.10.2.min.js" type="text/javascript" charset="utf-8"></script>
    <script src="js/main.js" type="text/javascript" charset="utf-8"></script>
</head>

<body>
    <div class="wrap fillinfo">
        <header class="header headerwhitebg">
            <div class="topbar">
                <span class="btn-left"><i class="iconfont icon-back"></i></span>
                <div class="words ellipsis">订单详情</div>
                <span class="btn-right"><i class="iconfont icon-zhuye6"></i></span>
            </div>
        </header>
        <div class="contWrap imageWraper">
            <ul class="tab-box orderlist orderdetail active">
                <li>
                    <div class="row1">
                        <div class="ordernum"><span>基本信息</span>
                        </div>
                        <div class="state green">未离场</div>
                    </div>
                    <div class="row2">
                        <div class="item-row">
                            <span class="p1">订单编号：</span>
                            <span class="p2">55545612356</span>
                        </div>
                        <div class="item-row">
                            <span class="p1">停车地点：</span>
                            <span class="p2">川垣四路北段西侧C</span>
                        </div>
                        <div class="item-row">
                            <span class="p1">停车位号：</span>
                            <span class="p2">15050837155</span>
                        </div>
                        <div class="item-row">
                            <span class="p1">车牌号码：</span>
                            <span class="p2">苏C-R876U</span>                            
                        </div>                      
                        <div class="item-row">
                            <span class="p1">停车时长：</span>
                            <span class="p2">5小时20分</span>
                        </div>
                    </div>
                   

                </li>
            </ul>

            <ul class="tab-box orderlist orderdetail active">
                <li>
                    <div class="row1">
                        <div class="ordernum"><span>进出场信息</span>
                        </div>
                    </div>
                    <div class="row2">
                        <div class="item-row">
                            <span class="p1">进场时间：</span>
                            <span class="p2">2023-05-29 17:04</span>
                        </div>
                        <div class="item-row">
                            <span class="p1">出场时间：</span>
                            <span class="p2">2023-05-29 17:04</span>
                        </div>
                        <div class="item-row">
                            <span class="p1">进场照片：</span>
                            <span class="p2">
                                <div class="piclist">
                                    <div class="li"><img src="img/mainbg.jpg" alt="" class="ximg"></div>
                                </div>
                            </span>
                        </div>
                        <div class="item-row">
                            <span class="p1">出场照片：</span>
                            <span class="p2">
                                <div class="piclist">
                                    <div class="li"><img src="img/mainbg.jpg" alt="" class="ximg"></div>
                                </div>
                            </span>
                        </div>

                    </div>

                </li>
            </ul>
            <ul class="tab-box orderlist orderdetail active">
                <li>
                    <div class="row1">
                        <div class="ordernum"><span>订单支付信息</span>
                        </div>
                    </div>
                    <div class="row2">
                        <div class="item-row">
                            <span class="p1">已支付：</span>
                            <span class="p2">0元</span>
                        </div>
                       
                        <div class="item-row">
                            <span class="p1">本次需支付：</span>
                            <span class="p2">
                                8元
                            </span>
                        </div>

                    </div>

                </li>
            </ul>           
           

        </div>
        <div class="bottom">
            <div class="submit" onclick="submit()">去支付</div>
        </div>

    </div>
    <script>
        let isMobile = /Android|webOS|iPhone|iPod|BlackBerry/i.test(navigator.userAgent);
        if (!isMobile) {
            if (confirm(`
                请在手机上浏览
            `)) {
            } else {
            };
        }

    </script>
    <script src="js/image-preview-iife.js"></script>
    <script>
        new imagePreviewModule.ImagePreview({
            selector: '.imageWraper img',
        })

    </script>
</body>

</html>