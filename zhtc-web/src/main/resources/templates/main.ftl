<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>停车场</title>
    <link rel="stylesheet" href="/sjfx/Lib/layui/css/layui.css" media="all">
    <script src="/sjfx/js/jquery.min.js?v=2.1.4"></script>
    <script type="text/javascript" src="/sjfx/js/echart/data-common.js"></script>
    <script type="text/javascript" src="/sjfx/js/echart/echarts.min.js"></script>
    <script type="text/javascript" src="/sjfx/js/echart/option/rankingChart.js"></script>
    <script type="text/javascript" src="/sjfx/js/echart/option/pieChart.js"></script>
    <script type="text/javascript" src="/sjfx/js/echart/option/columnChart.js"></script>
    <script type="text/javascript" src="/sjfx/js/echart/option/lineChart.js"></script>
    <script type="text/javascript" src="/sjfx/js/echart/option/itemStyle.js"></script>
    <script type="text/javascript" src="/sjfx/js/echart/option/china.js"></script>
    <script type="text/javascript" src="/sjfx/js/echart/option/geoCoordMap.js"></script>
    <script type="text/javascript" src="/sjfx/js/echart/option/chinaMap.js"></script>
    <script type="text/javascript" src="/sjfx/js/echart/option/doughnutChart.js"></script>
    <script type="text/javascript" src="/sjfx/js/echart/option/linearGradientColor.js"></script>
    <script type="text/javascript" src="/sjfx/js/jquery.timers-1.2.js"></script>
    <link href="/sjfx/css/css.css" rel="stylesheet">
    <link href="/sjfx/css/mobile.css" rel="stylesheet">
    <link rel="stylesheet" href="/sjfx/fonts/iconfont.css">
</head>

<body>

<div class="bodycontainer">
    <div class="box_car">
        <div class="bgbox">
            <div class="jiao1"></div>
            <div class="jiao2"></div>
            <div class="jiao3"></div>
            <div class="jiao4"></div>
            <h2 class="tips">停车场在停车辆前十排名</h2>
            <div id="carDiv"></div>

        </div>
    </div>
    <div class="box_two">
        <div class="top">
            <div class="box_k">
                <div class="box_t">
                    <div class="bgbox">
                        <div class="jiao1"></div>
                        <div class="jiao2"></div>
                        <div class="jiao3"></div>
                        <div class="jiao4"></div>
                        <h2 class="tips">支付方式</h2>
                        <div id="mydDiv"></div>
                    </div>
                </div>
                <div class="box_b">
                    <div class="bgbox">
                        <div class="jiao1"></div>
                        <div class="jiao2"></div>
                        <div class="jiao3"></div>
                        <div class="jiao4"></div>
                        <h2 class="tips">商家数量</h2>
                        <div id="speDiv"></div>
                    </div>
                </div>

            </div>
            <div class="box_n">
                <div class="bgbox">
                    <div class="jiao1"></div>
                    <div class="jiao2"></div>
                    <div class="jiao3"></div>
                    <div class="jiao4"></div>
                    <h2 class="tips">实时动态</h2>
                    <h3 class="tips_sec">（异常信息提醒）</h3>
                    <div class="cont">
                        <div class="layui-border-box layui-table-view" style="cursor:pointer;">
                            <div class="layui-table-box">
                                <div class="layui-table-body layui-table-main section-scroll">
                                    <table class="layui-table" >
                                        <colgroup>
                                            <col style="width:100px;" />
                                            <col style="width:120px;" />
                                            <col />
                                            <col />
                                            <col style="width:100px;" />
                                        </colgroup>
                                        <thead>
                                        <tr>
                                            <th class="table-th-css"><div align="center">车牌号码</div>
                                            </th>
                                            <th class="table-th-css"><div align="center">停车场名称</div>
                                            </th>
                                            <th class="table-th-css"><div align="center">驶入时间</div>
                                            </th>
                                            <th class="table-th-css"><div align="center">驶出时间</div>
                                            </th>
                                            <th class="table-th-css"><div align="center">车辆状态</div>
                                            </th>
                                        </tr>
                                        </thead>
                                        <tbody id="mybody">

                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="bot">
            <div class="box_k">
                <div class="bgbox">
                    <div class="jiao1"></div>
                    <div class="jiao2"></div>
                    <div class="jiao3"></div>
                    <div class="jiao4"></div>
                    <h2 class="tips">停车场收费前十排名</h2>
                    <div id="columnChart2" class="columnChart2"></div>
                </div>
            </div>
            <div class="box_n">
                <div class="bgbox">
                    <div class="jiao1"></div>
                    <div class="jiao2"></div>
                    <div class="jiao3"></div>
                    <div class="jiao4"></div>
                    <h2 class="tips">昨日停车时长占比</h2>
                    <div id="hxDiv"></div>

                </div>
            </div>
        </div>
    </div>
    <div class="box_tong">
        <div class="bgbox">
            <div class="jiao1"></div>
            <div class="jiao2"></div>
            <div class="jiao3"></div>
            <div class="jiao4"></div>
            <h2 class="tips">统计信息</h2>
            <ul class="one-box">
                <li>
                    <div class="icon iconfont icon-chongzhi2"></div>
                    <div class="word">
                        <div class="numbox"><span class="shuzifont" id="math1">0</span><i>元</i></div>
                        <div class="text">充值总金额</div>
                    </div>
                </li>
                <li>
                    <div class="icon iconfont icon-qianfeijine"></div>
                    <div class="word">
                        <div class="numbox"><span class="shuzifont" id="math2">0</span><i>元</i></div>
                        <div class="text">欠费总金额</div>
                    </div>
                </li>
                <li>
                    <div class="icon iconfont icon-chongzhichenggong"></div>
                    <div class="word">
                        <div class="numbox"><span class="shuzifont" id="math3">0</span><i>元</i></div>
                        <div class="text">开票总金额</div>
                    </div>
                </li>
                <li>
                    <div class="icon iconfont icon-yonghu"></div>
                    <div class="word">
                        <div class="numbox"><span class="num" id="math4">0</span><i>人</i></div>
                        <div class="text">用户总数</div>
                    </div>
                </li>
                <li>
                    <div class="icon iconfont icon-taizhanguzhang"></div>
                    <div class="word">
                        <div class="numbox"><span class="num" id="math5">0</span><i>个</i></div>
                        <div class="text">设备故障数</div>
                    </div>
                </li>
                <li>
                    <div class="icon iconfont icon-24gf-userGroup2"></div>
                    <div class="word">
                        <div class="numbox"><span class="shuzifont" id="math6">0</span><i>人</i></div>
                        <div class="text">包月用户数</div>
                    </div>
                </li>
                <li>
                    <div class="icon iconfont icon-nav_icon_cljk_spe"></div>
                    <div class="word">
                        <div class="numbox"><span class="num" id="math7">0</span><i>个</i></div>
                        <div class="text">车辆总泊位</div>
                    </div>
                </li>
                <li>
                    <div class="icon iconfont icon-shoufei"></div>
                    <div class="word">
                        <div class="numbox"><span class="num" id="math8">0</span><i>人</i></div>
                        <div class="text">收费员总数</div>
                    </div>
                </li>
            </ul>
        </div>
    </div>

</div>

<script type="text/javascript" src="/sjfx/js/echart/pages/main.js"></script>
<script type="text/javascript" src="/sjfx/js/countup.js"></script>
<script>
    var options={
        useEasing: true,  // 过渡动画效果，默认ture
        useGrouping: true,  // 千分位效果，例：1000->1,000。默认true
        separator: '',   // 使用千分位时分割符号
        decimal: '.',   // 小数位分割符号
        prefix: '',    // 前置符号
        suffix: ''    // 后置符号，可汉字
    };

</script>
<script>
    $(function (){
        statisticsInfo();
        getAppeal();
    })
    function statisticsInfo(){
        $.ajax({
            url: '/sjfx/parkStatisticsInfo.do',
            data: {},
            success: function (res){
                // $("#math1").html(res.data.recharge);
                // $("#math2").html(res.data.arrears);
                // $("#math3").html(res.data.invoiceAmount);
                // $("#math4").html(res.data.memberAmount);
                // $("#math5").html(res.data.deviceBreakdownCount);
                // $("#math6").html(res.data.merchantCount);
                // $("#math7").html(res.data.deviceCount);
                // $("#math8").html(res.data.inspectCopunt);
                var demo1 = new CountUp("math1",0,res.data.recharge,2,2,options);
                var demo2 = new CountUp("math2", 0,res.data.arrears,2,2,options);
                var demo3 = new CountUp("math3", 0,res.data.invoiceAmount,2,2,options);
                var demo4 = new CountUp("math4",0,res.data.memberAmount,0,2,options);
                var demo5 = new CountUp("math5",0,res.data.deviceBreakdownCount,0,2,options);
                var demo6 = new CountUp("math6",0,res.data.merchantCount,0,2,options);
                var demo7 = new CountUp("math7",0,res.data.deviceCount,0,2,options);
                var demo8 = new CountUp("math8",0,res.data.inspectCopunt,0,2,options);
                demo1.start();
                demo2.start();
                demo3.start();
                demo4.start();
                demo5.start();
                demo6.start();
                demo7.start();
                demo8.start();
            }
        })
    }

    function getAppeal(){
        $.ajax({
            url: '/sjfx/getAppeal.do',
            data: {type: '1'},
            success: function (res){
                if(res.code == 0){
                    var _html = "";
                    for(var i = 0; i < res.data.length; i ++){
                        _html += "<tr>"+
                            "<td align='center'>"+res.data[i]['car_no']+"</td>"+
                            "<td align='center'>"+res.data[i]['road_name']+"</td>"+
                            "<td align='center'>"+res.data[i]['drivein_time']+"</td>"+
                            "<td align='center'>"+res.data[i]['driveout_time']+"</td>"+
                            "<td align='center'><font color=#6bfc7f>"+res.data[i]['status']+"</font></td >"+
                            "</tr>";
                    }
                    $("#mybody").html(_html);
                    initCss();
                    var MyMarhq = '';
                    clearInterval(MyMarhq);
                    var tblTop = 0;
                    var speedhq = 50; // 数值越大越慢
                    var outerHeight = $('.layui-table tbody').find("tr").outerHeight();
                    //console.log(outerHeight);
                    function Marqueehq() {
                        if (tblTop <= -outerHeight * 6) {
                            tblTop = 0;
                        } else {
                            tblTop -= 1;
                        }
                        $('.layui-table tbody').css('top', tblTop + 'px');
                    }
                    MyMarhq = setInterval(Marqueehq(), speedhq);

                    // 鼠标移上去取消事件
                    $(".layui-table tbody").hover(function () {
                        clearInterval(MyMarhq);
                    }, function () {
                        clearInterval(MyMarhq);
                        MyMarhq = setInterval(Marqueehq(), speedhq);
                    })

                }
            }
        })
    }

    function initCss() {
        $(".layui-table thead").find("tr").each(function () {
            var tdArr = $(this).children();
            $(".layui-table tbody").find("tr").each(function () {
                var tdArr1 = $(this).children();
                tdArr1.eq(0).width(tdArr.eq(0).width());
                tdArr1.eq(1).width(tdArr.eq(1).width());
                tdArr1.eq(2).width(tdArr.eq(2).width());
                tdArr1.eq(3).width(tdArr.eq(3).width());
                tdArr1.eq(4).width(tdArr.eq(4).width());
                tdArr1.eq(5).width(tdArr.eq(5).width());
                tdArr1.eq(6).width(tdArr.eq(6).width());
                tdArr1.eq(7).width(tdArr.eq(7).width());
            });
        });
    }
</script>
</body>
</html>
