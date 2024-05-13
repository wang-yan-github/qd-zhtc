<!doctype html>
<html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport"
        content="user-scalable=0,width=device-width,initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=no,target-densitydpi=high-dpi" />
    <meta http-equiv="Pragma" content="no-cache">
    <meta http-equiv="Cache-Control" content="no-cache">
    <title>停车缴费</title>
    <meta name="keywords" content="停车缴费" />
    <meta name="description" content="停车缴费" />
    <link rel="stylesheet" href="/scan/fonts/iconfont.css">
    <link rel="stylesheet" href="/scan/css/css.css">
    <script src="/scan/js/jquery-1.10.2.min.js" type="text/javascript" charset="utf-8"></script>
    <script src="/scan/js/main.js" type="text/javascript" charset="utf-8"></script>
    <style>
        .contWrap{
            height: calc(100% - 1vw);
            padding-bottom: 20vw;
        }
    </style>
</head>

<body>
    <div class="wrap fillinfo">
        <header class="header header-green">
            <div class="topbar">
                <!-- <span class="btn-left"><i class="iconfont icon-back"></i></span> -->
                <div class="words white ellipsis">停车缴费</div>
            </div>
        </header>
        <div class="contWrap">
            
            <div class="car_input">
                <div class="cartitle">输入车牌查询订单</div>
                <ul class="ul_input">
                    <li id="cp1" class="input_zim" data-sort="1"><span></span></li>
                    <li id="cp2" data-sort="2"><span></span></li>
                    <li id="cp3" data-sort="3"><span></span></li>
                    <li id="cp4" data-sort="4"><span></span></li>
                    <li id="cp5" data-sort="5"><span></span></li>
                    <li id="cp6" data-sort="6"><span></span></li>
                    <li id="cp7" data-sort="7"><span></span></li>
                    <li id="cp8" data-sort="8" style="display:none;"><span></span></li>
                    <li class="xinneng"><span><img src="/scan/img/xinweng.png"></span></li>
                </ul>
                <div class="check-group">
                    <div class="check-box" id="check-box">
                        <input type="hidden" class="Ischeck" value="0" />
                    </div>
                    <div class="check-right"><span>是否黄牌</span></div>
                </div>
                <!-- <div class="zhuyi">请输入完成车牌号码</div> -->
                <button class="combtn" onclick="toOrder()">查询</button>
            </div>
            
            <!-- 查询结果 -->
            <div class="car-list-order">
                <div class="no-data">
                    <span></span>
                    <p>暂无此车辆信息</p>
                </div>
                <#--<div class="car-order-number">
                    <h2>苏C-R876U</h2>
                    <!-- 如果是新能源 则显示 &ndash;&gt;
                    <p><span></span>新能源</p>
                </div>

                <div class="no-data">
                    <span></span>
                    <p>暂无此车辆信息</p>
                </div>
                <!-- 订单列表 开始&ndash;&gt;
                <div class="car-order-item">
                    <div class="order-title">
                        <p>徐州市泉山区建国西路23号停车场</p>
                        <!-- 订单状态 status-a 红 status-b 绿 status-c 蓝 status-d 黄&ndash;&gt;
                        <span class="status-a">待支付</span>
                    </div>
                    <div class="order-number">
                        <span></span>
                        <p>123423480808095345</p>
                    </div>
                    <div class="order-layout">
                        <div class="order-layout-item">
                            <span>入场时间</span>
                            <p>09-01 13:12:24</p>
                        </div>
                        <div class="order-layout-item">
                            <span>出场时间</span>
                            <p>09-01 14:12:24</p>
                        </div>
                    </div>
                    <div class="order-layout">
                        <div class="order-layout-item">
                            <span>停车费用</span>
                            <p><b>30.00</b>元</p>
                        </div>
                        <div class="order-layout-item">
                            <div class="order-btn" onclick="">支 付</div>
                        </div>
                    </div>
                </div>
                <!-- 订单列表 结束&ndash;&gt;
                
                <!-- 订单列表 开始&ndash;&gt;
                <div class="car-order-item">
                    <div class="order-title">
                        <p>徐州市泉山区建国西路23号停车场</p>
                        <!-- 订单状态 status-a 红 status-b 绿 status-c 蓝 status-d 黄&ndash;&gt;
                        <span class="status-a">待支付</span>
                    </div>
                    <div class="order-number">
                        <span></span>
                        <p>123423480808095345</p>
                    </div>
                    <div class="order-layout">
                        <div class="order-layout-item">
                            <span>入场时间</span>
                            <p>09-01 13:12:24</p>
                        </div>
                        <div class="order-layout-item">
                            <span>出场时间</span>
                            <p>09-01 14:12:24</p>
                        </div>
                    </div>
                    <div class="order-layout">
                        <div class="order-layout-item">
                            <span>停车费用</span>
                            <p><b>30.00</b>元</p>
                        </div>
                        <div class="order-layout-item">
                            <div class="order-btn" onclick="">支 付</div>
                        </div>
                    </div>
                </div>-->
                <!-- 订单列表 结束-->


            </div>
        </div>
        <!-- <div class="bottom">
            <div class="submit" onclick="submit()">查询</div>
        </div> -->
    </div>
    <script src="/scan/js/slide.js"></script>
    <script>
        //一般直接写在一个js文件中
        $(function () {

            $('.car_input li').on('click', function () {
                document.activeElement.blur();  // 阻止弹出系统软键盘
                var _cliss = $(this).attr("class");
                var _sort = $(this).data("sort");
                $(this).addClass("input_zim").siblings().removeClass("input_zim");

                if (_sort == 1) {
                    $('body').keyboard({
                        defaults: 'symbol',    //键盘显示类型   English 字母  number 数字  symbol 符号
                        inputClass: _cliss,        //输入框Class
                    });
                } else {
                    $('body').keyboard({
                        defaults: 'English',    //键盘显示类型   English 字母  number 数字  symbol 符号
                        inputClass: _cliss,        //输入框Class
                    });
                }
            });

            $(document).on("click", '#keyboard .keyContent li', function (event) {

                $(".input_zim span").html($(this).text());
                var _sort = $(".input_zim").data("sort") + 1;
                if (_sort == 2) {
                    $('body').keyboard({
                        defaults: 'English',    //键盘显示类型   English 字母  number 数字  symbol 符号
                    });
                }
                $("#cp" + _sort).addClass("input_zim").siblings().removeClass("input_zim");
            });

            $(document).on("click", '.del', function (event) {
                $(".input_zim span").text('');
                var _sort = $(".input_zim").data("sort") - 1;
                $("#cp" + _sort).addClass("input_zim").siblings().removeClass("input_zim");
            });

            $(document).on("click", '.xinneng', function (event) {
                $(".xinneng").remove();
                $("#cp8").show();


            });



            $('.check-group').on('click', function () {
                var _self = $(this).find(".check-box");
                if (!_self.hasClass('checked')) {
                    _self.addClass('checked');
                    _self.siblings("div").find("span").addClass("yellow");
                    $(this).find("input").val(1);
                    $('#btnSure').removeClass("disb1");
                    $('#btnSure').removeAttr("disabled");
                }
                else {
                    _self.removeClass('checked');
                    $(this).find("input").val(0);
                    _self.siblings("div").find("span").removeClass("yellow");
                    $('#btnSure').addClass("disb1");
                    $('#btnSure').attr("disabled", "disabled");
                }
            });
        });

        function toOrder(){
            var isCheck = $(".Ischeck").val();

            var carNo = $("#cp1")[0].innerText.trim() +
                $("#cp2")[0].innerText.trim() +
                $("#cp3")[0].innerText.trim() +
                $("#cp4")[0].innerText.trim() +
                $("#cp5")[0].innerText.trim() +
                $("#cp6")[0].innerText.trim() +
                $("#cp7")[0].innerText.trim() +
                $("#cp8")[0].innerText.trim();

            var carType = 1;
            if(1 == isCheck){
                carType = 3;
            }else{
                if(7 == carNo.length){
                    carType = 1;
                }else {
                    carType = 2;
                }
            }
            $.ajax({
                url: '/scan/getOrder',
                data: {
                    parkId: ${parkId},
                    carNo: carNo,
                    carType: carType
                },
                success: function (res){
                    console.log(res);
                    $(".car-list-order").html("");
                    var _html = "";
                    _html += "<div class='car-order-number'>"+
                    "<h2>"+ carNo +"</h2>";
                    if(8 == carNo.length){
                        _html += "<p><span></span>新能源</p>";
                    }
                    _html += "</div>";
                    if(res.data.length == 0){
                        _html += "<div class='no-data'>"+
                        "<span></span>"+
                        "<p>暂无此车辆信息</p>"+
                    "</div>";
                    }else{
                        for(var i = 0; i < res.data.length; i++){
                            var order = res.data[i];
                            _html += "<div class='car-order-item'>"
                                +"<div class='order-title'>"
                                +"<p>"+order.park_name+"</p>"
                                +"<span class='status-c'>"+order.state_name+"</span>"
                                +"</div>"
                                +"<div class='order-number'>"
                                +"<span></span>"
                                +"<p>"+order.order_no+"</p>"
                                +"</div>"
                                +"<div class='order-layout'>"
                                +"<div class='order-layout-item'>"
                                +"<span>入场时间</span>"
                                +"<p>"+order.drivein_time+"</p>"
                                +"</div>"
                                +"</div>"
                                +"<div class='order-layout'>"
                                +"<div class='order-layout-item'>"
                                +"<span>停车费用</span>"
                                +"<p><b>"+order.sum_amount+"</b>元</p>"
                                +"</div>"
                                +"<div class='order-layout-item' onclick='pay("+order.id+")'>";
                                if(order.sum_amount > 0){
                                    _html += "<div class='order-btn' >支 付</div>";
                                }
                                _html+=
                                    "</div>"
                                    +"</div>"
                                    +"</div>";
                        }
                    }
                    $(".car-list-order").append(_html)
                }
            })
            //window.location.href = "/scan/toOrder?parkId=${parkId}&carNo=" + carNo + "&carType=" + carType;
        }

        function pay(id) {
            window.location.href = "/scan/pay?orderId=" + id;
        }
    </script>
</body>

</html>