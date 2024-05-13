<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>路侧详情</title>
    <link rel="stylesheet" href="/sjfxNew/Lib/layui/css/layui.css" media="all">
    <script src="/sjfxNew/js/jquery.min.js?v=2.1.4"></script>
    <script type="text/javascript" src="/sjfxNew/js/echart/data-common.js"></script>
    <script type="text/javascript" src="/sjfxNew/js/echart/echarts.min.js"></script>
    <script type="text/javascript" src="/sjfxNew/js/echart/echarts-liquidfill.js"></script>
    <script type="text/javascript" src="/sjfxNew/js/jquery.timers-1.2.js"></script>
    <script type="text/javascript"
            src="https://api.map.baidu.com/api?type=webgl&v=1.0&ak=1aHhIdt8HG26HgFqpMUTqY25QG5XLMPX"></script>
    <script type="text/javascript" src="/sjfxNew/js/yanmou.js"></script>
    <link href="/sjfxNew/css/css.css" rel="stylesheet">
    <link href="/sjfxNew/css/mobile.css" rel="stylesheet">
    <link rel="stylesheet" href="/sjfxNew/fonts/iconfont.css">
    <link href="/sjfxNew/css/mapInfoWindow.css" rel="stylesheet">

    <script type="text/javascript" src="/sjfxNew/js/countup.js"></script>
</head>

<body>
<div class="maincontainerNew maincontainerNewDetail">
    <div class="detail-left">
        <div class="detail-data-con">
            <div class="detail-data-inside">
                <div class="p-icon"></div>
                <div class="p-des">
                    <h4 id="name">泉山区东坡休闲广场路侧停车</h4>
                    <#--<p>编号：<span id="code">08893323</span></p>-->
                </div>
            </div>
        </div>
        <div class="detail-left-top">
            <div id="mapDiv"></div>
        </div>
        <div class="detail-left-center bgbox">
            <div class="jiao1"></div>
            <div class="jiao2"></div>
            <div class="jiao3"></div>
            <div class="jiao4"></div>
            <h2 class="tips">车位周转率</h2>
            <div class="tab-title">
                <a href="javascript:;" class="tab-btn active" data-index="0">日均车位周转率</a>
                <a href="javascript:;" class="tab-btn" data-index="1">停车时长</a>
            </div>
            <div class="tab-sub">
                <div class="tab-sub-item show" id="turnoverClum"></div>
                <div class="tab-sub-item" id="stopTimePie"></div>
            </div>
        </div>
        <div class="detail-left-bot bgbox">
            <div class="jiao1"></div>
            <div class="jiao2"></div>
            <div class="jiao3"></div>
            <div class="jiao4"></div>
            <h2 class="tips">路边停车</h2>
            <div class="roadside-list">
                <div class="roadside-item">
                    <div id="roadsideA"></div>
                    <p>非现金支付占比</p>
                </div>
                <div class="roadside-item">
                    <div id="roadsideB"></div>
                    <p>自助停车率</p>
                </div>
                <div class="roadside-item">
                    <div id="roadsideC"></div>
                    <p>订单收费率</p>
                </div>
                <div class="roadside-item">
                    <div id="roadsideD"></div>
                    <p>15分钟内自助</p>
                </div>
                <div class="roadside-item">
                    <div id="roadsideE"></div>
                    <p>平均周转率</p>
                </div>
                <div class="roadside-item">
                    <div id="roadsideF"></div>
                    <p>超24H占位率</p>
                </div>
                <div class="roadside-item">
                    <div id="roadsideG"></div>
                    <p>发票发放率</p>
                </div>
            </div>
        </div>
    </div>

    <div class="detail-center">
        <div class="detail-data-con">
            <div class="revenue-box">
                <div class="revenue-left">
                    <div class="revenue-lf-icon revenue-lf-icon-b"></div>
                    <div class="revenue-lf-text">
                        <h4 id="road_today_orderCount">53,456</h4>
                        <p>
                            今停车次数
                            <span>昨停车次数：<span id="road_yesterday_orderCount">30</span></span>
                        </p>
                    </div>
                </div>
                <div class="revenue-left">
                    <div class="revenue-lf-icon revenue-lf-icon-c"></div>
                    <div class="revenue-lf-text">
                        <h4 id="road_ztCount">53,456</h4>
                        <p>
                            在停车辆数
                            <span>空余泊位数：<span id="road_freeCount">30</span></span>
                        </p>
                    </div>
                </div>
            </div>
        </div>
        <div class="detail-center-top bgbox">
            <div class="jiao1"></div>
            <div class="jiao2"></div>
            <div class="jiao3"></div>
            <div class="jiao4"></div>
            <h2 class="tips">今日进出</h2>
            <div id="todayOutLine"></div>
        </div>
        <div class="detail-center-center bgbox">
            <div class="jiao1"></div>
            <div class="jiao2"></div>
            <div class="jiao3"></div>
            <div class="jiao4"></div>
            <h2 class="tips">今日进出记录</h2>
            <div class="cont">
                <div class="layui-border-box layui-table-view" style="cursor:pointer;">
                    <div class="layui-table-box">
                        <div class="layui-table-body layui-table-main section-scroll">
                            <table class="layui-table">
                                <colgroup>
                                    <col style="width:100px;"/>
                                    <col/>
                                    <col style="width:160px;"/>
                                </colgroup>
                                <thead>
                                <tr>
                                    <th class="table-th-css">
                                        <div align="center">车牌号码</div>
                                    </th>
                                    <th class="table-th-css">
                                        <div align="center">路侧名称</div>
                                    </th>
                                    <th class="table-th-css">
                                        <div align="center">驶入驶出时间</div>
                                    </th>
                                </tr>
                                </thead>
                                <tbody style="position:absolute;" id="inOutList">
                                <#--<tr>-->
                                    <#--<td align="center">苏C15687</td>-->
                                    <#--<td align="center">A路侧</td>-->
                                    <#--<td align="center">8:00:00 至 18:00:00</td>-->
                                <#--</tr>-->
                                <#--<tr>-->
                                    <#--<td align="center">苏C15687</td>-->
                                    <#--<td align="center">A路侧</td>-->
                                    <#--<td align="center">8:00:00 至 18:00:00</td>-->
                                <#--</tr>-->
                                <#--<tr>-->
                                    <#--<td align="center">苏C15687</td>-->
                                    <#--<td align="center">A路侧</td>-->
                                    <#--<td align="center">8:00:00 至 18:00:00</td>-->
                                <#--</tr>-->
                                <#--<tr>-->
                                    <#--<td align="center">苏C15687</td>-->
                                    <#--<td align="center">A路侧</td>-->
                                    <#--<td align="center">8:00:00 至 18:00:00</td>-->
                                <#--</tr>-->
                                <#--<tr>-->
                                    <#--<td align="center">苏C15687</td>-->
                                    <#--<td align="center">A路侧</td>-->
                                    <#--<td align="center">8:00:00 至 18:00:00</td>-->
                                <#--</tr>-->
                                <#--<tr>-->
                                    <#--<td align="center">苏C15687</td>-->
                                    <#--<td align="center">A路侧</td>-->
                                    <#--<td align="center">8:00:00 至 18:00:00</td>-->
                                <#--</tr>-->
                                <#--<tr>-->
                                    <#--<td align="center">苏C15687</td>-->
                                    <#--<td align="center">A路侧</td>-->
                                    <#--<td align="center">8:00:00 至 18:00:00</td>-->
                                <#--</tr>-->
                                <#--<tr>-->
                                    <#--<td align="center">苏C15687</td>-->
                                    <#--<td align="center">A路侧</td>-->
                                    <#--<td align="center">8:00:00 至 18:00:00</td>-->
                                <#--</tr>-->
                                <#--<tr>-->
                                    <#--<td align="center">苏C15687</td>-->
                                    <#--<td align="center">A路侧</td>-->
                                    <#--<td align="center">8:00:00 至 18:00:00</td>-->
                                <#--</tr>-->
                                <#--<tr>-->
                                    <#--<td align="center">苏C15687</td>-->
                                    <#--<td align="center">A路侧</td>-->
                                    <#--<td align="center">8:00:00 至 18:00:00</td>-->
                                <#--</tr>-->
                                <#--<tr>-->
                                    <#--<td align="center">苏C15687</td>-->
                                    <#--<td align="center">A路侧</td>-->
                                    <#--<td align="center">8:00:00 至 18:00:00</td>-->
                                <#--</tr>-->
                                <#--<tr>-->
                                    <#--<td align="center">苏C15687</td>-->
                                    <#--<td align="center">A路侧</td>-->
                                    <#--<td align="center">8:00:00 至 18:00:00</td>-->
                                <#--</tr>-->
                                <#--<tr>-->
                                    <#--<td align="center">苏C15687</td>-->
                                    <#--<td align="center">A路侧</td>-->
                                    <#--<td align="center">8:00:00 至 18:00:00</td>-->
                                <#--</tr>-->
                                <#--<tr>-->
                                    <#--<td align="center">苏C15687</td>-->
                                    <#--<td align="center">A路侧</td>-->
                                    <#--<td align="center">8:00:00 至 18:00:00</td>-->
                                <#--</tr>-->
                                <#--<tr>-->
                                    <#--<td align="center">苏C15687</td>-->
                                    <#--<td align="center">A路侧</td>-->
                                    <#--<td align="center">8:00:00 至 18:00:00</td>-->
                                <#--</tr>-->


                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="detail-center-bot bgbox">
            <div class="jiao1"></div>
            <div class="jiao2"></div>
            <div class="jiao3"></div>
            <div class="jiao4"></div>
            <h2 class="tips">近30日进出</h2>
            <div id="thirtyOutLine"></div>
        </div>
    </div>
    <div class="detail-right">
        <div class="detail-data-con">
            <div class="revenue-box">
                <div class="revenue-left">
                    <div class="revenue-lf-icon"></div>
                    <div class="revenue-lf-text">
                        <h4><span>￥</span><span id="today_paidAmount">53,456</span></h4>
                        <p>今日停车营收</p>
                    </div>
                </div>
                <div class="revenue-center">
                    <div class="revenue-ct-item">
                        <p>停车欠费</p>
                        <h4><span>￥</span><span id="today_unpaid_amount">53,456</span></h4>
                        <i>昨日：￥<span id="yesterday_unpaid_amount">2345</span></i>
                    </div>
                    <#--<div class="revenue-ct-item">-->
                        <#--<p>包期</p>-->
                        <#--<h4><span>￥</span><span id="today_month_amount">53,456</span></h4>-->
                        <#--<i>昨日：￥<span id="yesterday_month_amount">2345</span></i>-->
                    <#--</div>-->
                    <div class="revenue-ct-item">
                        <p>退款</p>
                        <h4><span>￥</span><span id="today_refund_amount">53,456</span></h4>
                        <i>昨日：<span id="yesterday_refund_amount">2345</span></i>
                    </div>
                </div>
            </div>

        </div>
        <div class="detail-right-top bgbox">
            <div class="jiao1"></div>
            <div class="jiao2"></div>
            <div class="jiao3"></div>
            <div class="jiao4"></div>
            <h2 class="tips">今日停车营收</h2>
            <div id="todayRevenueLine"></div>
        </div>
        <div class="detail-right-center bgbox">
            <div class="jiao1"></div>
            <div class="jiao2"></div>
            <div class="jiao3"></div>
            <div class="jiao4"></div>
            <h2 class="tips">欠费分析</h2>
            <div class="arrears-box">
                <div class="arrears-left">
                    <div class="arrears-num" id="today_unpaid_amounts">56</div>
                    <p>今日欠费</p>
                </div>
                <div id="arrearsLine"></div>
            </div>

        </div>
        <div class="detail-right-bot bgbox">
            <div class="jiao1"></div>
            <div class="jiao2"></div>
            <div class="jiao3"></div>
            <div class="jiao4"></div>
            <h2 class="tips">车位满位率</h2>
            <div id="inventoryRoadLine"></div>
        </div>
    </div>
</div>
<input type="hidden" id="id" value="${id}">
<script type="text/javascript" src="/sjfxNew/js/echart/pages/roadDetail.js"></script>
<#--<script type="text/javascript" src="/sjfxNew/js/countup.js"></script>-->
<script>
    var options = {
        useEasing: true,  // 过渡动画效果，默认ture
        useGrouping: true,  // 千分位效果，例：1000->1,000。默认true
        separator: '',   // 使用千分位时分割符号
        decimal: '.',   // 小数位分割符号
        prefix: '',    // 前置符号
        suffix: ''    // 后置符号，可汉字
    };

    var map;
    var myIcon = new BMapGL.Icon("../sjfxNew/img/detail.png", new BMapGL.Size(16, 16), {
        imageOffset: new BMapGL.Size(0, 0),
        offset: new BMapGL.Size(0, 0)
    });

    function maponLoad(lng, lat, zoom) {
        // //初始化地图对象
        // map = new BMapGL.Map("mapDiv");
        // //设置显示地图的中心点和级别
        // map.centerAndZoom(new BMapGL.Point(lng, lat), zoom);
        // map.addControl(new BMapGL.NavigationControl());
        // //允许鼠标双击放大地图
        // //map.addControl(new BMapGL.ScaleControl());    //添加左下方比例尺控件
        // map.enableScrollWheelZoom(true);     //开启鼠标滚轮缩放
        // //          map.enableInertia();
        // //          map.enableKeyboard();
        // map.setHeading(0);//角度
        // map.setTilt(60);//倾斜角
        // map.setMapStyleV2({styleJson: yanmou});
        // //map.setMapStyle({style:'midnight'});

        // GL版命名空间为BMapGL
        // 按住鼠标右键，修改倾斜角和角度
        map = new BMapGL.Map("mapDiv");    // 创建Map实例
        map.centerAndZoom(new BMapGL.Point(lng, lat), zoom);  // 初始化地图,设置中心点坐标和地图级别
        map.enableScrollWheelZoom(true);     //开启鼠标滚轮缩放
        map.addControl(new BMapGL.NavigationControl());
        // map.setHeading(0);//角度
        // map.setTilt(60);//倾斜角
        map.setMapStyleV2({styleJson: yanmou});
    }

    // var point = new BMapGL.Point(116.404, 39.915);

    function creatMarker(pointJson) {   //console.log(pointJson);
        // $.each(pointJson, function (n, value) {
        pointJson.forEach((value, n) => {
            //创建标注对象
            var marker = new BMapGL.Marker(new BMapGL.Point(value.longitude, value.latitude), {icon: myIcon});
            //向地图上添加标注
            map.addOverlay(marker);
            map.panTo(new BMapGL.Point(value.longitude, value.latitude));// 将标注添加到地图中
        });
    }


    $(function () {
        /**
         * 路侧详情
         * 首页
         * 今日/昨日停车次数、在停车辆数、空余泊位数
         * 金额：今日营收、今日/昨日停车收费、今日/昨日包月、今日/昨日退款
         */
        getParkViewIndexData();

        /**
         * 路侧/停车场详情
         * 今日进出记录
         */
        getInOutList();

        /**
         * 定时任务
         * 每个整点重新数据渲染
         */
        nextIntegralPointAfterLogin();

        // // 地图
        // maponLoad(118.355354, 34.349881, 15);
        //
        // var pointJson = [{
        //     "lat": "34.360028",
        //     "lng": "118.331378",
        //     "icon": myIcon,
        //     "name": "建国路停车场",
        //     id: "525864852458"
        // }];
        //
        // creatMarker(pointJson);


        // 表格滚动
        // initCss();




    })

    function initCss() {
        $(".layui-table thead").find("tr").each(function () {
            var tdArr = $(this).children();
            $(".layui-table tbody").find("tr").each(function () {
                var tdArr1 = $(this).children();
                tdArr1.eq(0).width(tdArr.eq(0).width());
                tdArr1.eq(1).width(tdArr.eq(1).width());
                tdArr1.eq(2).width(tdArr.eq(2).width());
                // tdArr1.eq(3).width(tdArr.eq(3).width());
                // tdArr1.eq(4).width(tdArr.eq(4).width());
                // tdArr1.eq(5).width(tdArr.eq(5).width());
                // tdArr1.eq(6).width(tdArr.eq(6).width());
                // tdArr1.eq(7).width(tdArr.eq(7).width());
            });
        });
    }

    $('.tab-title .tab-btn').on('click', function () {
        var index = $(this).attr('data-index');
        $('.tab-btn').removeClass('active')
        $(this).addClass('active');
        $('.tab-sub .tab-sub-item').hide()
        $('.tab-sub .tab-sub-item').eq(index).show()
        $('.tab-sub .tab-sub-item').eq(index).css({'height': "100%", 'width': "100%",})
        if (index == 1) {
            stopTimePie.resize()
        } else {
            turnoverClum.resize()
        }

    })

    /**
     * 路侧详情
     * 首页
     * 今日/昨日停车次数、在停车辆数、空余泊位数
     * 金额：今日营收、今日/昨日停车收费、今日/昨日包月、今日/昨日退款
     */
    function getParkViewIndexData() {
        $.ajax({
            url: '/sjfxNew/getParkViewIndexData.json',
            type: "POST",
            data: {id: $("#id").val(), type: "0"},
            success: function (res) {
                console.log("路侧详情 统计信息")
                console.log(res)

                $("#name").html(res.data.road_info.road_name);
                // $("#code").html(res.data.road_info.park_code);

                // 地图
                maponLoad(res.data.road_info.longitude, res.data.road_info.latitude, 18);

                var pointJson = [];
                pointJson.push(res.data.road_info)
                console.log(pointJson)
                creatMarker(pointJson);

                // 今日/昨日停车次数、在停车辆数、空余泊位数
                var road_ztCount = new CountUp("road_ztCount", 0, res.data.road_ztCount, 0, 2, options);
                var road_freeCount = new CountUp("road_freeCount", 0, res.data.road_freeCount, 0, 2, options);
                var road_today_orderCount = new CountUp("road_today_orderCount", 0, res.data.road_today_orderCount, 0, 2, options);
                var road_yesterday_orderCount = new CountUp("road_yesterday_orderCount", 0, res.data.road_yesterday_orderCount, 0, 2, options);

                //金额：今日营收、今日/昨日停车收费、今日/昨日包月、今日/昨日退款
                // var today_moneys = new CountUp("today_moneys", 0, res.data.today_moneys, 0, 2, options);
                var today_paidAmount = new CountUp("today_paidAmount", 0, res.data.today_paidAmount, 0, 2, options);
                // var yesterday_paidAmount = new CountUp("yesterday_paidAmount", 0, res.data.yesterday_paidAmount, 0, 2, options);
                var today_refund_amount = new CountUp("today_refund_amount", 0, res.data.today_refund_amount, 0, 2, options);
                var yesterday_refund_amount = new CountUp("yesterday_refund_amount", 0, res.data.yesterday_refund_amount, 0, 2, options);
                // var today_month_amount = new CountUp("today_month_amount", 0, res.data.today_month_amount, 0, 2, options);
                // var yesterday_month_amount = new CountUp("yesterday_month_amount", 0, res.data.yesterday_month_amount, 0, 2, options);

                var today_unpaid_amount = new CountUp("today_unpaid_amount", 0, res.data.today_unpaid_amount, 0, 2, options);
                var yesterday_unpaid_amount = new CountUp("yesterday_unpaid_amount", 0, res.data.yesterday_unpaid_amount, 0, 2, options);

                road_ztCount.start();
                road_freeCount.start();
                road_today_orderCount.start();
                road_yesterday_orderCount.start();

                // today_moneys.start();
                today_paidAmount.start();
                // yesterday_paidAmount.start();
                today_refund_amount.start();
                yesterday_refund_amount.start();
                // today_month_amount.start();
                // yesterday_month_amount.start();

                today_unpaid_amount.start();
                yesterday_unpaid_amount.start();

            }
        })
    }

    /**
     * 路侧/停车场详情
     * 今日进出记录
     */
    function getInOutList() {
        $.ajax({
            url: '/sjfxNew/getInOutList.json',
            type: "POST",
            data: {id: $("#id").val(), type: "0"},
            success: function (res) {
                console.log("路侧详情 今日进出记录")
                console.log(res)

                $("#inOutList").empty();
                var _html = "";
                for (var i = 0; i < res.data.list.length; i++) {
                    var driveinTime = res.data.list[i].driveinTime;
                    var driveoutTime = res.data.list[i].driveoutTime;
                    if (driveinTime == null) {
                        driveinTime = "";
                    }
                    if (driveoutTime == null) {
                        driveoutTime = "";
                    }
                    _html += "<tr>" +
                        "<td align='center'>" + res.data.list[i].carNo + "</td>" +
                        "<td align='center'>" + res.data.list[i].placeName + "</td>" +
                        "<td align='center'>" + driveinTime + " 至 " + driveoutTime + "</td>" +
                        "</tr>";
                }
                $("#inOutList").html(_html);
                initCss();
                var MyMarhq = '';
                clearInterval(MyMarhq);
                //  $('.layui-table tbody').html($('.tbl-body tbody').html() + $('.tbl-body tbody').html());
                //$('.layui-table').css('top', '0');
                var tblTop = 0;
                var speedhq = 50; // 数值越大越慢
                var outerHeight = $('.layui-table tbody').find("tr").outerHeight();

                function Marqueehq() {
                    if (tblTop <= -outerHeight * 6) {
                        tblTop = 0;
                    } else {
                        tblTop -= 1;
                    }
                    $('.layui-table tbody').css('top', tblTop + 'px');
                }

                MyMarhq = setInterval(Marqueehq, speedhq);

                // 鼠标移上去取消事件
                $(".layui-table tbody").hover(function () {
                    clearInterval(MyMarhq);
                }, function () {
                    clearInterval(MyMarhq);
                    MyMarhq = setInterval(Marqueehq, speedhq);
                })
            }
        })
    }

    /**
     * 定时任务
     * 每个整点重新数据渲染
     */
    function nextIntegralPointAfterLogin() {
        console.log("-----路侧 详情 定时任务------");

        // IntegralPointExecute();//在整点执行的函数，在每个整点都调用该函数
        setInterval("IntegralPointExecute();", 60 * 60 * 1000);//一个小时执行一次，那么下一个整点，下下一个整点都会执行
    }

    function IntegralPointExecute() {
        console.log("-----路侧 详情 每个整点重新数据渲染------");
        console.log(new Date().getHours());

        /**
         * 路侧详情
         * 首页
         * 今日/昨日停车次数、在停车辆数、空余泊位数
         * 金额：今日营收、今日/昨日停车收费、今日/昨日包月、今日/昨日退款
         */
        getParkViewIndexData();

        /**
         * 路侧/停车场详情
         * 今日进出记录
         */
        getInOutList();

        var date = new Date();//现在时刻
        var dateIntegralPoint = new Date();//用户登录时刻的下一个整点，也可以设置成某一个固定时刻
        dateIntegralPoint.setHours(date.getHours() + 1);//小时数增加1
        dateIntegralPoint.setMinutes(0);
        dateIntegralPoint.setSeconds(0);
        setTimeout("nextIntegralPointAfterLogin();", dateIntegralPoint - date);//用户登录后的下一个整点执行。
    }
</script>

</body>
</html>
