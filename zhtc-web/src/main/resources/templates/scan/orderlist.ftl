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
    <link rel="stylesheet" href="css/common.css">
    <link rel="stylesheet" href="fonts/iconfont.css">
    <link rel="stylesheet" href="css/css.css">
    <link rel="stylesheet" href="css/sellTicket.css">
    <script src="js/jquery-1.10.2.min.js" type="text/javascript" charset="utf-8"></script>
    <script src="js/main.js" type="text/javascript" charset="utf-8"></script>
    <script src="js/common.js"></script>
    <script src="js/iscroll-probe.js"></script>
    <script src="layer/layer.js" type="text/javascript" charset="utf-8"></script>
</head>

<body>
    <div class="wrap">
        <header class="header headerwhitebg">
            <div class="topbar">
                <span class="btn-left"><i class="iconfont icon-back"></i></span>
                <div class="words ellipsis">我的订单</div>
            </div>
        </header>
        <div class="contWrapScoll">
            <ul class="tabs-panel">
                <li>全部</li>
                <li>进行中</li>
                <li>已完成</li>
                <li class="active">已欠费</li>
            </ul>
            <div class="tabs-content">
                <div class="tab-box orderlist">
                    <div class="">

                        <ul class="dataLine">

                            <#list list as item>
                                <li>
                                    <div class="row1">
                                        <div class="ordernum"><span>${item.order_no}-${item.id}</span>
                                        </div>
                                        <div class="state green">${item.state_name}</div>
                                    </div>
                                    <div class="row2">
                                        <div class="item-row">
                                            <span class="p1">车牌号码：</span>
                                            <span class="p2">${item.car_no}</span>
                                        </div>
                                        <div class="item-row">
                                            <span class="p1">停车地点：</span>
                                            <span class="p2">${item.park_name}</span>
                                        </div>
                                        <div class="item-row">
                                            <span class="p1">进场时间</span>
                                            <span class="p2">${item.drivein_time?string('yyyy-MM-dd HH:mm:ss')}</span>
                                        </div>
                                        <div class="item-row">
                                            <span class="p1">出场时间：</span>
                                            <span class="p2"></span>
                                        </div>
                                    </div>
                                    <div class="row3">
                                        <div class="jiner"><i>￥</i>${item.sum_amount}</div>
                                        <div class="btns" onclick="pay('${item.id?c}')">支付</div>
                                    </div>

                                </li>
                            </#list>

                        </ul>

                    </div>
                </div>

                <div class="tab-box">
                    <div class="has-no-order">
                        <div class="bg">
                            <span class="iconfont icon-dingdan6"></span>
                        </div>
                        <div class="text">还没有订单</div>
                    </div>
                </div>

                <div class="tab-box orderlist">
                    <div class="scrollbox">
                        <p class="pullDown">下拉刷新...</p>
                        <ul class="dataLine">
                            <li>
                                <div class="row1">
                                    <div class="ordernum"><span>11641280231331504346173</span>
                                    </div>
                                    <div class="state green">已支付</div>
                                </div>
                                <div class="row2">
                                    <div class="item-row">
                                        <span class="p1">车牌号码：</span>
                                        <span class="p2">苏C-R876U</span>
                                    </div>
                                    <div class="item-row">
                                        <span class="p1">停车地点：</span>
                                        <span class="p2">川垣四路北段西侧C</span>
                                    </div>
                                    <div class="item-row">
                                        <span class="p1">进场时间</span>
                                        <span class="p2">2023-05-29 15:04</span>
                                    </div>
                                    <div class="item-row">
                                        <span class="p1">出场时间：</span>
                                        <span class="p2">2023-05-29 17:04</span>
                                    </div>
                                </div>
                                <div class="row3">
                                    <div class="jiner"><i>￥</i>852.00</div>
                                </div>

                            </li>
                    </div>

                </div>
                <div class="tab-box active orderlist">
                    <div class="scrollbox">
                        <p class="pullDown">下拉刷新...</p>
                        <ul class="dataLine">
                            <li>
                                <div class="row1">
                                    <div class="ordernum"><span
                                            class="checkbox iconfont icon-xingzhuang-tuoyuanxing"></span><span>11641280231331504346173</span>
                                    </div>
                                    <div class="state green">待支付</div>
                                </div>
                                <div class="row2">
                                    <div class="item-row">
                                        <span class="p1">车牌号码：</span>
                                        <span class="p2">苏C-R876U</span>
                                    </div>
                                    <div class="item-row">
                                        <span class="p1">停车地点：</span>
                                        <span class="p2">川垣四路北段西侧C川垣四路北段西侧C川垣四路北段西侧C</span>
                                    </div>
                                    <div class="item-row">
                                        <span class="p1">进场时间</span>
                                        <span class="p2">2023-05-29 15:04</span>
                                    </div>
                                    <div class="item-row">
                                        <span class="p1">出场时间：</span>
                                        <span class="p2">2023-05-29 17:04</span>
                                    </div>
                                </div>
                                <div class="row3">
                                    <div class="jiner"><i>￥</i><span class="money">5.00</span></div>
                                    <div class="btns" onclick="location.href='orderdetail.html'">订单详情</div>
                                </div>

                            </li>
                            <li>
                                <div class="row1">
                                    <div class="ordernum"><span
                                            class="checkbox iconfont icon-xingzhuang-tuoyuanxing"></span><span>11641280231331504346173</span>
                                    </div>
                                    <div class="state green">未离场</div>
                                </div>
                                <div class="row2">
                                    <div class="item-row">
                                        <span class="p1">车牌号码：</span>
                                        <span class="p2">苏C-R876U</span>
                                    </div>
                                    <div class="item-row">
                                        <span class="p1">停车地点：</span>
                                        <span class="p2">川垣四路北段西侧C</span>
                                    </div>
                                    <div class="item-row">
                                        <span class="p1">进场时间</span>
                                        <span class="p2">2023-05-29 15:04</span>
                                    </div>
                                    <div class="item-row">
                                        <span class="p1">出场时间：</span>
                                        <span class="p2">2023-05-29 17:04</span>
                                    </div>
                                </div>
                                <div class="row3">
                                    <div class="jiner"><i>￥</i><span class="money">5.00</span></div>
                                    <div class="btns" onclick="location.href='orderdetail.html'">订单详情</div>
                                </div>

                            </li>
                            <li>
                                <div class="row1">
                                    <div class="ordernum"><span
                                            class="checkbox iconfont icon-xingzhuang-tuoyuanxing"></span><span>11641280231331504346173</span>
                                    </div>
                                    <div class="state green">未离场</div>
                                </div>
                                <div class="row2">
                                    <div class="item-row">
                                        <span class="p1">车牌号码：</span>
                                        <span class="p2">苏C-R876U</span>
                                    </div>
                                    <div class="item-row">
                                        <span class="p1">停车地点：</span>
                                        <span class="p2">川垣四路北段西侧C</span>
                                    </div>
                                    <div class="item-row">
                                        <span class="p1">进场时间</span>
                                        <span class="p2">2023-05-29 15:04</span>
                                    </div>
                                    <div class="item-row">
                                        <span class="p1">出场时间：</span>
                                        <span class="p2">2023-05-29 17:04</span>
                                    </div>
                                </div>
                                <div class="row3">
                                    <div class="jiner"><i>￥</i><span class="money">5.00</span></div>
                                    <div class="btns" onclick="location.href='orderdetail.html'">订单详情</div>
                                </div>

                            </li>
                            <li>
                                <div class="row1">
                                    <div class="ordernum"><span
                                            class="checkbox iconfont icon-xingzhuang-tuoyuanxing"></span><span>11641280231331504346173</span>
                                    </div>
                                    <div class="state green">未离场</div>
                                </div>
                                <div class="row2">
                                    <div class="item-row">
                                        <span class="p1">车牌号码：</span>
                                        <span class="p2">苏C-R876U</span>
                                    </div>
                                    <div class="item-row">
                                        <span class="p1">停车地点：</span>
                                        <span class="p2">川垣四路北段西侧C</span>
                                    </div>
                                    <div class="item-row">
                                        <span class="p1">进场时间</span>
                                        <span class="p2">2023-05-29 15:04</span>
                                    </div>
                                    <div class="item-row">
                                        <span class="p1">出场时间：</span>
                                        <span class="p2">2023-05-29 17:04</span>
                                    </div>
                                </div>
                                <div class="row3">
                                    <div class="jiner"><i>￥</i><span class="money">5.00</span></div>
                                    <div class="btns" onclick="location.href='orderdetail.html'">订单详情</div>
                                </div>

                            </li>
                            <li>
                                <div class="row1">
                                    <div class="ordernum"><span
                                            class="checkbox iconfont icon-xingzhuang-tuoyuanxing"></span><span>11641280231331504346173</span>
                                    </div>
                                    <div class="state green">未离场</div>
                                </div>
                                <div class="row2">
                                    <div class="item-row">
                                        <span class="p1">车牌号码：</span>
                                        <span class="p2">苏C-R876U</span>
                                    </div>
                                    <div class="item-row">
                                        <span class="p1">停车地点：</span>
                                        <span class="p2">川垣四路北段西侧C</span>
                                    </div>
                                    <div class="item-row">
                                        <span class="p1">进场时间</span>
                                        <span class="p2">2023-05-29 15:04</span>
                                    </div>
                                    <div class="item-row">
                                        <span class="p1">出场时间：</span>
                                        <span class="p2">2023-05-29 17:04</span>
                                    </div>
                                </div>
                                <div class="row3">
                                    <div class="jiner"><i>￥</i><span class="money">5.00</span></div>
                                    <div class="btns" onclick="location.href='orderdetail.html'">订单详情</div>
                                </div>

                            </li>

                        </ul>
                        <p class="pullUp">上拉加载...</p>
                    </div>
                </div>

            </div>
        </div>
        <div class="footer">
            <div class="allsel" id="checkAll"><i class="iconfont icon-xingzhuang-tuoyuanxing"></i><span
                    class="p1">全选</span><span class="p2">共 <i class="totalnum" id="totalnum">3</i> 单</span></div>
            <div class="paybox">
                <div class="prices">总计：<span class="money" id="totalmoney">52.00</span><input type="hidden"
                        id="realtotal" value="0.00" readonly></div>
                <div class="paybtn" onclick="pay()">支付</div>
            </div>
        </div>
    </div>
    <script>
        var totalnum = 0;
        var totlmoney = 0;
        $(function () {
            $("#totalnum").text("0");
            $("#totalmoney").text("0.00");
            // $(".footer").hide();
            $(".tabs-panel li").mouseover(function () {
                $(this).addClass('active').siblings().removeClass('active');
                var index = $(this).index();
                if (index == 3) {
                    $(".footer").show();
                } else {
                    $(".footer").hide();
                }
                //console.log(index);
                $('.tabs-content .tab-box:eq(' + index + ')').addClass('active').siblings().removeClass('active');
            });

            $(".row1").on("click", function () {
                //console.log(111);
                var _self = $(this).find(".iconfont");
                if (!_self.hasClass('active')) {
                    _self.addClass('active icon-dui').removeClass('icon-xingzhuang-tuoyuanxing');
                    totalnum++
                    var money = _self.parents("li").find(".money").text();
                    totlmoney += parseFloat(money);
                    $("#totalmoney").text(totlmoney.toFixed(2))
                    $("#realtotal").val(totlmoney.toFixed(2))
                    $("#totalnum").text(totalnum);
                    localStorage.setItem("totlmoney",totlmoney.toFixed(2));
                    //$("#totalmoney").text("0.00");
                } else {
                    _self.addClass('icon-xingzhuang-tuoyuanxing').removeClass('active icon-dui');
                    $("#checkAll").find(".iconfont").addClass('icon-xingzhuang-tuoyuanxing').removeClass('active icon-dui');
                    totalnum--
                    var money = _self.parents("li").find(".money").text();
                    totlmoney -= parseFloat(money);
                    $("#totalmoney").text(totlmoney.toFixed(2))
                    $("#realtotal").val(totlmoney.toFixed(2))
                    localStorage.setItem("totlmoney",totlmoney.toFixed(2));
                    $("#totalnum").text(totalnum);
                }


                //判断是否已经全选，若全选，全选按钮置选择中状态
                var k = 0
                $(".checkbox").each(function (i, ck) {
                    if ($(ck).hasClass('active')) {
                        k++
                    }
                })
                //console.log(k)
                if ($(".tab-box.active.orderlist .dataLine li").length == k) {
                    $("#checkAll").find(".iconfont").addClass('active icon-dui').removeClass('icon-xingzhuang-tuoyuanxing');
                }


            });


            $("#checkAll").on("click", function () {
                // var flag = $(this).find('iconfont');
                //根据全选按钮的状态设置选项的状态
                var _self = $(this).find(".iconfont");
                if (!_self.hasClass('active')) {
                    $(".iconfont").addClass('active icon-dui').removeClass('icon-xingzhuang-tuoyuanxing');
                } else {
                    $(".iconfont").addClass('icon-xingzhuang-tuoyuanxing').removeClass('active icon-dui');
                }
                tongji()
            })

        })
        function tongji() {
            totalnum = 0;
            totlmoney = 0;

            //遍历所有选择按钮，查询状态是否被选中
            $(".checkbox").each(function (i, ck) {
                if ($(ck).hasClass('active')) {
                    //console.log(i)
                    //如果被选中，获取相应的小计的值
                    var money = $(ck).parents("li").find(".money").text();
                    totlmoney += parseFloat(money);
                    totalnum = i + 1
                    // alert(total);
                } else {
                    totlmoney = 0;
                    totalnum = 0
                }

            })
            $("#totalnum").text(totalnum);
            $("#totalmoney").text(totlmoney.toFixed(2))
            localStorage.setItem("totlmoney",totlmoney.toFixed(2));
            $("#realtotal").val(totlmoney.toFixed(2))
        }

        function pay(id) {
            window.location.href = "/scan/pay?orderId=" + id;
        }
    </script>

</body>

</html>