<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>路侧总览</title>
    <link rel="stylesheet" href="/sjfxNew/Lib/layui/css/layui.css" media="all">
    <script src="/sjfxNew/js/jquery.min.js?v=2.1.4"></script>
    <script type="text/javascript" src="/sjfxNew/js/echart/data-common.js"></script>
    <script type="text/javascript" src="/sjfxNew/js/echart/echarts.min.js"></script>
    <script type="text/javascript" src="/sjfxNew/js/echart/echarts-liquidfill.js"></script>
    <script type="text/javascript" src="/sjfxNew/js/jquery.timers-1.2.js"></script>
    <link href="/sjfxNew/css/css.css" rel="stylesheet">
    <link href="/sjfxNew/css/mobile.css" rel="stylesheet">
    <link rel="stylesheet" href="/sjfxNew/fonts/iconfont.css">

    <script type="text/javascript" src="/sjfxNew/js/countup.js"></script>
</head>

<body>
<div class="maincontainerNew maincontainerNewRoad">
    <div class="road-top-con">

        <div class="road-top-center bgbox">
            <div class="jiao1"></div>
            <div class="jiao2"></div>
            <div class="jiao3"></div>
            <div class="jiao4"></div>
            <h2 class="tips">车位周转率</h2>
            <div class="tab-title">
                <a href="javascript:;" class="tab-btn active" data-index="0">日均车位周转率</a>
                <a href="javascript:;" class="tab-btn" data-index="1">停车时长</a>
                <a href="javascript:;" class="tab-btn" data-index="2">出入趋势</a>
            </div>
            <div class="tab-sub">
                <div class="tab-sub-item show" id="turnoverClum"></div>
                <div class="tab-sub-item" id="stopTimePie"></div>
                <div class="tab-sub-item" id="trendLine"></div>
            </div>
        </div>

        <div class="road-bot-center">
            <div class="gather-box">
                <div class="gather-item1">
                    <h4 id="road_count">3456</h4>
                    <p>停车道路数量</p>
                </div>
                <div class="gather-item2">
                    <h4 id="road_placeTotal">3456</h4>
                    <p>车位总数</p>
                </div>
                <div class="gather-item3">
                    <h4 id="road_devicecount">3456</h4>
                    <p>路侧设备</p>
                </div>
                <div class="gather-item4">
                    <h4 id="road_freeCount">3456</h4>
                    <p>空余数</p>
                </div>
            </div>
        </div>
        <div class="road-top-right bgbox">
            <div class="jiao1"></div>
            <div class="jiao2"></div>
            <div class="jiao3"></div>
            <div class="jiao4"></div>
            <h2 class="tips">统计信息</h2>
            <ul class="ul-box">
                <li>
                    <div class="icon iconfont icon-chongzhi2"></div>
                    <div class="word">
                        <div class="numbox"><span class="shuzifont" id="math1">105</span><i>元</i></div>
                        <div class="text">充值总金额</div>
                    </div>
                </li>
                <li>
                    <div class="icon iconfont icon-qianfeijine"></div>
                    <div class="word">
                        <div class="numbox"><span class="shuzifont" id="math2">1385</span><i>元</i></div>
                        <div class="text">欠费总金额</div>
                    </div>
                </li>
                <li>
                    <div class="icon iconfont icon-chongzhichenggong"></div>
                    <div class="word">
                        <div class="numbox"><span class="shuzifont" id="math3">36527</span><i>元</i></div>
                        <div class="text">开票总金额</div>
                    </div>
                </li>
                <li>
                    <div class="icon iconfont icon-yonghu"></div>
                    <div class="word">
                        <div class="numbox"><span class="num" id="math4">2687</span><i>人</i></div>
                        <div class="text">用户总数</div>
                    </div>
                </li>
                <li>
                    <div class="icon iconfont icon-taizhanguzhang"></div>
                    <div class="word">
                        <div class="numbox"><span class="num" id="math5">23</span><i>个</i></div>
                        <div class="text">设备故障数</div>
                    </div>
                </li>
                <li>
                    <div class="icon iconfont icon-24gf-userGroup2"></div>
                    <div class="word">
                        <div class="numbox"><span class="shuzifont" id="math6">257</span><i>人</i></div>
                        <div class="text">商家用户数</div>
                    </div>
                </li>
                <li>
                    <div class="icon iconfont icon-nav_icon_cljk_spe"></div>
                    <div class="word">
                        <div class="numbox"><span class="num" id="math7">3218</span><i>个</i></div>
                        <div class="text">车辆总泊位</div>
                    </div>
                </li>
                <li>
                    <div class="icon iconfont icon-shoufei"></div>
                    <div class="word">
                        <div class="numbox"><span class="num" id="math8">26</span><i>人</i></div>
                        <div class="text">收费员总数</div>
                    </div>
                </li>
            </ul>
        </div>
    </div>
    <div class="road-center-con">
        <div class="road-center-left bgbox">
            <div class="jiao1"></div>
            <div class="jiao2"></div>
            <div class="jiao3"></div>
            <div class="jiao4"></div>
            <h2 class="tips">在停车辆前五排名</h2>
            <div id="stopCarFiveLine"></div>
        </div>
        <div class="road-center-center bgbox">
            <div class="jiao1"></div>
            <div class="jiao2"></div>
            <div class="jiao3"></div>
            <div class="jiao4"></div>
            <h2 class="tips">收费金额前五排名</h2>
            <div id="chargeFiveLine"></div>
        </div>
        <div class="road-center-right bgbox">
            <div class="jiao1"></div>
            <div class="jiao2"></div>
            <div class="jiao3"></div>
            <div class="jiao4"></div>
            <h2 class="tips">欠费车辆前十排名</h2>
            <div id="arrearsTen"></div>
        </div>
    </div>
    <div class="road-bot-con">
        <div class="road-bot-left bgbox">
            <div class="jiao1"></div>
            <div class="jiao2"></div>
            <div class="jiao3"></div>
            <div class="jiao4"></div>
            <h2 class="tips">车位满位率</h2>
            <div id="inventoryRoadLine"></div>
        </div>
        <div class="road-top-left bgbox">
            <div class="jiao1"></div>
            <div class="jiao2"></div>
            <div class="jiao3"></div>
            <div class="jiao4"></div>
            <h2 class="tips">昨日停车时长占比</h2>
            <div id="yesterdayStopCarPie"></div>
        </div>

        <div class="road-bot-right bgbox">
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
</div>

<script type="text/javascript" src="/sjfxNew/js/echart/pages/road.js"></script>
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
    $(function () {

        /**
         * 路侧
         * 首页
         * 充值总金额、开票总金额、车辆总泊位、设备故障数、欠费总金额、用户总数、收费员总数、商家用户数
         * 停车场道路数量、路侧设备总数、车位总数、空闲数
         */
        getRoadIndexData();

        /**
         * 定时任务
         * 每个整点重新数据渲染
         */
        nextIntegralPointAfterLogin();

    })

    $('.tab-title .tab-btn').on('click', function () {
        var index = $(this).attr('data-index');
        $('.tab-btn').removeClass('active')
        $(this).addClass('active');
        $('.tab-sub .tab-sub-item').hide()
        $('.tab-sub .tab-sub-item').eq(index).show()
        $('.tab-sub .tab-sub-item').eq(index).css({'height': "100%", 'width': "100%",})
        if (index == 1) {
            stopTimePie.resize()
        } else if (index == 2) {
            trendLine.resize()
        } else {
            turnoverClum.resize()
        }

    })

    /**
     * 路侧
     * 首页
     * 充值总金额、开票总金额、车辆总泊位、设备故障数、欠费总金额、用户总数、收费员总数、商家用户数
     * 停车场道路数量、路侧设备总数、车位总数、空闲数
     */
    function getRoadIndexData() {
        $.ajax({
            url: '/sjfxNew/getRoadIndexData.json',
            type: "POST",
            data: {},
            success: function (res) {
                console.log("路侧 统计信息")
                console.log(res)

                //充值总金额、开票总金额、车辆总泊位、设备故障数、欠费总金额、用户总数、收费员总数、商家用户数
                var demo1 = new CountUp("math1", 0, res.data.road_info.recharge, 0, 2, options);
                var demo2 = new CountUp("math2", 0, res.data.road_info.arrears, 0, 2, options);
                var demo3 = new CountUp("math3", 0, res.data.road_info.invoiceAmount, 0, 2, options);
                var demo4 = new CountUp("math4", 0, res.data.road_info.memberAmount, 0, 2, options);
                var demo5 = new CountUp("math5", 0, res.data.road_info.deviceBreakdownCount, 0, 2, options);
                var demo6 = new CountUp("math6", 0, res.data.businessCount, 0, 2, options);
                var demo7 = new CountUp("math7", 0, res.data.road_info.deviceCount, 0, 2, options);
                var demo8 = new CountUp("math8", 0, res.data.road_info.inspectCopunt, 0, 2, options);

                var road_count = new CountUp("road_count", 0, res.data.road_count, 0, 2, options);
                var road_devicecount = new CountUp("road_devicecount", 0, res.data.road_placeTotal, 0, 2, options);
                var road_placeTotal = new CountUp("road_placeTotal", 0, res.data.road_placeTotal, 0, 2, options);
                var road_freeCount = new CountUp("road_freeCount", 0, res.data.road_freeCount, 0, 2, options);

                demo1.start();
                demo2.start();
                demo3.start();
                demo4.start();
                demo5.start();
                demo6.start();
                demo7.start();
                demo8.start();
                road_count.start();
                road_devicecount.start();
                road_placeTotal.start();
                road_freeCount.start();
            }
        })
    }

    /**
     * 定时任务
     * 每个整点重新数据渲染
     */
    function nextIntegralPointAfterLogin() {
        console.log("-----定时任务 每个整点重新数据渲染------");

        // IntegralPointExecute();//在整点执行的函数，在每个整点都调用该函数
        setInterval("IntegralPointExecute();", 60 * 60 * 1000);//一个小时执行一次，那么下一个整点，下下一个整点都会执行
    }

    function IntegralPointExecute() {
        console.log(new Date().getHours());

        getRoadIndexData();

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
