<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>停车场</title>
    <link rel="stylesheet" href="/sjfxNew/Lib/layui/css/layui.css" media="all">
    <link href="/sjfxNew/css/css.css" rel="stylesheet">
    <link href="/sjfxNew/css/mobile.css" rel="stylesheet">
    <link href="/sjfxNew/css/mapInfoWindow.css" rel="stylesheet">
    <link rel="stylesheet" href="/sjfxNew/fonts/iconfont.css">
    <link rel="stylesheet" type="text/css" href="/sjfxNew/font/iconfont.css"/>

    <script src="/sjfxNew/js/jquery.min.js?v=2.1.4"></script>
    <script type="text/javascript" src="/sjfxNew/js/echart/data-common.js"></script>
    <script type="text/javascript" src="/sjfxNew/js/echart/echarts.min.js"></script>
    <script type="text/javascript" src="/sjfxNew/js/echart/echarts-liquidfill.js"></script>
    <script type="text/javascript" src="/sjfxNew/js/echart/option/pieChart.js"></script>
    <script type="text/javascript" src="/sjfxNew/js/echart/option/itemStyle.js"></script>
    <script type="text/javascript" src="/sjfxNew/js/jquery.timers-1.2.js"></script>
    <script type="text/javascript" src="https://api.map.baidu.com/api?type=webgl&v=1.0&ak=1aHhIdt8HG26HgFqpMUTqY25QG5XLMPX"></script>
    <script type="text/javascript" src="/sjfxNew/js/yanmou.js"></script>
    <script type="text/javascript" src="/sjfxNew/js/countup.js"></script>
</head>

<body>
<div class="maincontainerNew">
    <!-- 地图 -->
    <div id="mapDiv" style="width: 100%;height: 100%;"></div>
    <!-- 左侧图表 -->
    <div class="leftbox">
        <div class="left-box1 bgbox">
            <div class="jiao1"></div>
            <div class="jiao2"></div>
            <div class="jiao3"></div>
            <div class="jiao4"></div>
            <h2 class="tips">今日营收</h2>
            <div class="revenue-box">
                <div class="revenue-left">
                    <div class="revenue-lf-icon"></div>
                    <div class="revenue-lf-text">
                        <h4 class="red"><span>￥</span><span id="today_moneys" class="shuzifont f18">53,456</span></h4>
                        <p>总营收</p>
                    </div>
                </div>
                <div class="revenue-center">
                    <div class="revenue-ct-item">
                        <p>停车收费</p>
                        <h4 class="yellows"><span>￥</span><span id="today_paidAmount" class="shuzifont">53,456</span></h4>
                        <i>昨日：<em>￥<span id="yesterday_paidAmount" class="shuzifont">2345</span></em></i>
                    </div>
                    <div class="revenue-ct-item">
                        <p>包期</p>
                        <h4><span>￥</span><span id="today_month_moneys" class="shuzifont">53,456</span></h4>
                        <i>昨日：<em>￥<span id="yesterday_month_moneys" class="shuzifont">2345</span></em></i>
                    </div>
                    <div class="revenue-ct-item">
                        <p>退款</p>
                        <h4><span>￥</span><span id="today_refund_amount" class="shuzifont">53,456</span></h4>
                        <i>昨日：<em>￥<span id="yesterday_refund_amount" class="shuzifont">2345</span></em></i>
                    </div>
                </div>
                <div class="revenue-right">
                    <p>欠费:<em class="red">￥<span id="today_unpaidAmount" class="shuzifont">545</span></em></p>
                    <p>优惠:<em class="green">￥<span id="today_discount_money" class="shuzifont">124</span></em></p>
                    <p>充值:<em class="green">￥<span id="today_czMoney" class="shuzifont">300</span></em></p>
                </div>
            </div>
            <div id="revenueLine"></div>
        </div>
        <div class="left-box2 bgbox">
            <div class="jiao1"></div>
            <div class="jiao2"></div>
            <div class="jiao3"></div>
            <div class="jiao4"></div>
            <h2 class="tips">车场盘点</h2>
            <div id="inventoryLine"></div>
            <div class="inventory-bot">
                <div class="inventory-bot-small">
                    <div id="occupyPie"></div>
                    <p><span>\</span>满位率</p>
                </div>
                <div class="inventory-bot-small">
                    <div class="stopping-car-num" id="ztCount">5769</div>
                    <p><span>\</span>在停车辆</p>
                </div>
                <div class="num-sort" id="ztTopList">
                    <div class="num-sort-item num-sort-item-1">
                        <p title="泉山区东坡休闲广场停车场">泉山区东坡休闲广场停车场</p>
                        <span class="shuzifont">45656<i>次</i></span>
                    </div>
                    <div class="num-sort-item num-sort-item-2">
                        <p title="泉山区东坡休闲广场停车场">泉山区东坡休闲广</p>
                        <span class="shuzifont">4556<i>次</i></span>
                    </div>
                    <div class="num-sort-item num-sort-item-3">
                        <p title="泉山区东坡休闲广场停车场">泉山区东坡休闲停车场</p>
                        <span class="shuzifont">4156<i>次</i></span>
                    </div>
                </div>
            </div>
        </div>
        <div class="left-box3 bgbox">
            <div class="jiao1"></div>
            <div class="jiao2"></div>
            <div class="jiao3"></div>
            <div class="jiao4"></div>
            <h2 class="tips">车主分析</h2>
            <div class="analyse-con">
                <div class="analyse-left">
                    <div class="analyse-lf-top">
                        <span class=""></span>
                        <div class="analyse-lf-item">
                            <p><i class="user-icon"></i>用户总数</p>
                            <span class="yellow-text shuzifont" id="user_count">16,345</span>
                        </div>
                        <div class="analyse-lf-item">
                            <p><i class="huoyue-icon"></i>今日活跃数</p>
                            <span id="today_count" class=" shuzifont">6,345</span>
                        </div>
                        <div class="analyse-lf-item">
                            <p><i class="time-icon"></i>7日活跃数</p>
                            <span id="days_count" class=" shuzifont">6,345</span>
                        </div>
                    </div>
                    <div class="analyse-lf-bottom">
                        <div class="analyse-bot-item">
                            <div id="loaclCar"></div>
                            <p>本地车占比</p>
                        </div>
                        <div class="analyse-bot-item">
                            <div id="bindCar"></div>
                            <p>绑定车占比</p>
                        </div>
                    </div>
                </div>
                <div class="analyse-right">
                    <#--<p class="simple-title">支付方式/金额</p>-->
                    <div id="payStyle"></div>
                </div>
            </div>
        </div>
    </div>
    <!-- 中间数据展示 -->
    <div class="centerbox">
        <div class="top-c">
            <div class="top-c-item">
                <div class="top-c-num top-c-num-a shuzifont" id="road_count">2387</div>
                <h4>停车道路数量</h4>
            </div>
            <div class="top-c-item">
                <div class="top-c-num top-c-num-b shuzifont" id="placeTotal">2387</div>
                <h4>车位总数</h4>
            </div>
            <div class="top-c-item">
                <div class="top-c-num top-c-num-c shuzifont" id="freeCount">2387</div>
                <h4>空余数</h4>
            </div>
            <div class="top-c-item">
                <div class="top-c-num top-c-num-d shuzifont" id="park_count">2387</div>
                <h4>停车场数量</h4>
            </div>
        </div>
        <div class="bottom-c">
            <div class="bottom-c-item">
                <h5>停车场</h5>
                <div class="bottom-c-detail">
                    <div class="bot-c-d-item">
                        <div class="c-icon">
                            <span class="p-1"></span>
                        </div>
                        <div class="c-text">
                            <h4 class="yellows"><span id="parking_placeTotal" class="shuzifont f16">23,457</span><span>个</span></h4>
                            <p>车位总数</p>
                        </div>
                    </div>
                    <div class="bot-c-d-item">
                        <div class="c-icon">
                            <span class="p-3"></span>
                        </div>
                        <div class="c-text">
                            <h4 class="yellows"><span id="parking_freeCount" class="shuzifont f16">23,457</span><span>个</span></h4>
                            <p>空余数</p>
                        </div>
                    </div>
                    <div class="bot-c-d-item">
                        <div class="c-icon">
                            <span class="p-4"></span>
                        </div>
                        <div class="c-text">
                            <h4 class="yellows"><span id="parking_rjcwzzl" class="shuzifont f16">57</span><span>%</span></h4>
                            <p>日均车位周转率</p>
                        </div>
                    </div>
                    <div class="bot-c-d-item">
                        <div class="c-icon">
                            <span class="p-2"></span>
                        </div>
                        <div class="c-text">
                            <h4 class="yellows"><span id="parking_today_ztCount" class="shuzifont f16">23,457</span><span>次</span></h4>
                            <p>今日停车量</p>
                        </div>
                    </div>
                </div>
            </div>
            <div class="bottom-c-item">
                <h5>路侧停车</h5>
                <div class="bottom-c-detail">
                    <div class="bot-c-d-item">
                        <div class="c-icon">
                            <span class="p-1"></span>
                        </div>
                        <div class="c-text">
                            <h4 class="yellows"><span id="road_placeTotal" class="shuzifont f16">23,457</span><span>个</span></h4>
                            <p>车位总数</p>
                        </div>
                    </div>
                    <div class="bot-c-d-item">
                        <div class="c-icon">
                            <span class="p-3"></span>
                        </div>
                        <div class="c-text">
                            <h4 class="yellows"><span id="road_freeCount" class="shuzifont f16">23,457</span><span>个</span></h4>
                            <p>空余数</p>
                        </div>
                    </div>
                    <div class="bot-c-d-item">
                        <div class="c-icon">
                            <span class="p-4"></span>
                        </div>
                        <div class="c-text">
                            <h4 class="yellows"><span id="road_rjcwzzl" class="shuzifont f16">57</span><span>%</span></h4>
                            <p>日均车位周转率</p>
                        </div>
                    </div>
                    <div class="bot-c-d-item">
                        <div class="c-icon">
                            <span class="p-2"></span>
                        </div>
                        <div class="c-text">
                            <h4 class="yellows"><span id="road_today_ztCount" class="shuzifont f16">23,457</span><span>次</span></h4>
                            <p>今日停车量</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!-- 右侧图表 -->
    <div class="rightbox">
        <div class="left-box1 bgbox bg-right">
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
        <div class="left-box2 bgbox bg-right">
            <div class="jiao1"></div>
            <div class="jiao2"></div>
            <div class="jiao3"></div>
            <div class="jiao4"></div>
            <h2 class="tips">客户服务</h2>
            <div class="serve-list">
                <div class="serve-list-item">
                    <p class="s-title">车主反馈</p>
                    <div class="serve-all-num">
                        <h4 id="today_feedbackcount" class="shuzifont">3456</h4>
                        <p>
                            <span>总反馈量</span>
                            <span>昨日：<span id="yesterday_feedbackcount" class="shuzifont">234</span></span>
                        </p>
                    </div>
                </div>
                <div class="serve-list-item">
                    <p class="s-title">订单申诉</p>
                    <div class="serve-all-num">
                        <h4 id="today_appealcount" class="shuzifont">4663</h4>
                        <p>
                            <span>总申诉量</span>
                            <span>昨日：<span id="yesterday_appealcount" class="shuzifont">234</span></span>
                        </p>
                    </div>
                </div>
                <div class="serve-list-item">
                    <p class="s-title">车牌申诉</p>
                    <div class="serve-all-num">
                        <h4 id="today_carnocount" class="shuzifont">4623</h4>
                        <p>
                            <span>总申诉量</span>
                            <span>昨日：<span id="yesterday_carnocount" class="shuzifont">234</span></span>
                        </p>
                    </div>
                </div>
            </div>
            <div class="serve-charts">
                <div class="serve-left">
                    <p class="simple-title">近七日服务类型趋势</p>
                    <div id="serveLine"></div>
                </div>
                <div class="serve-right">
                    <p class="simple-title">订单申诉处置统计</p>
                    <div class="serve-num-sort" id="appealHandleRecordData">
                        <div class="serve-num-sort-item serve-num-sort-item-1">
                            <p>结束时间处理</p>
                            <span class="shuzifont">45656<i>条</i></span>
                        </div>
                        <div class="serve-num-sort-item serve-num-sort-item-2">
                            <p>订单费用处理</p>
                            <span class="shuzifont">4556<i>条</i></span>
                        </div>
                        <div class="serve-num-sort-item serve-num-sort-item-3">
                            <p>退款处理</p>
                            <span class="shuzifont">4156<i>条</i></span>
                        </div>
                        <div class="serve-num-sort-item serve-num-sort-item-4">
                            <p>修正车牌处理</p>
                            <span class="shuzifont">4156<i>条</i></span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="left-box3 bgbox bg-right">
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

<script type="text/javascript" src="/sjfxNew/js/echart/pages/main.js"></script>

<script type="text/javascript" src="/sjfxNew/js/baidumapGL.js"></script>


<script>
    // var pointJson = [
    //     {
    //         "lat": "34.360028",
    //         "lng": "118.331378",
    //         "icon": myIcon,
    //         "name": "建国路停车场",
    //         "type": "0",
    //         "a": "234",
    //         "b": "233",
    //         "c": "452",
    //         "d": "454",
    //         "e": "766",
    //         "f": "853",
    //         id: "525864852458"
    //     },
    //     {
    //         "lat": "34.366882",
    //         "lng": "118.323257",
    //         "icon": myIcon,
    //         "name": "东坡休闲广场停车场",
    //         "type": "0",
    //         "a": "234",
    //         "b": "233",
    //         "c": "452",
    //         "d": "454",
    //         "e": "766",
    //         "f": "853",
    //         id: "525864852458"
    //     },
    //     {
    //         "lat": "34.365034",
    //         "lng": "118.370832",
    //         "icon": myIcon1,
    //         "name": "建国路路侧",
    //         "type": "1",
    //         "a": "666",
    //         "b": "233",
    //         "c": "452",
    //         "d": "454",
    //         "e": "766",
    //         "f": "853",
    //         id: "525864852458"
    //     },
    //     {
    //         "lat": "34.360028",
    //         "lng": "118.356171",
    //         "icon": myIcon1,
    //         "name": "东坡休闲广场路侧",
    //         "type": "1",
    //         "a": "888",
    //         "b": "233",
    //         "c": "452",
    //         "d": "454",
    //         "e": "766",
    //         "f": "853",
    //         id: "525864852458"
    //     },
    //     {
    //         "lat": "34.36575",
    //         "lng": "118.35574",
    //         "icon": myIcon2,
    //         "name": "张伟伟",
    //         "type": "2",
    //         "id": "J568569",
    //         position: "建国西路26号",
    //         tel: "15865256878"
    //     },
    //     {
    //         "lat": "34.351028",
    //         "lng": "118.369107",
    //         "icon": myIcon2,
    //         "name": "刘文武",
    //         "type": "2",
    //         "id": "J584748",
    //         position: "淮海西路25号",
    //         tel: "15865256878"
    //     },
    // ];
    //
    // creatMarker(pointJson);

    var options = {
        useEasing: true,  // 过渡动画效果，默认ture
        useGrouping: true,  // 千分位效果，例：1000->1,000。默认true
        separator: '',   // 使用千分位时分割符号
        decimal: '.',   // 小数位分割符号
        prefix: '',    // 前置符号
        suffix: ''    // 后置符号，可汉字
    };

    $(function () {
        //初始化地图
        DataInit();

        /**
         * 首页
         * 停车道路数量（停车场数量+路侧街道数量）、车位总数（停车场车位总数+路侧泊位总数）、空余数、停车场数量
         * 停车场：车位总数、空余数、日均车位周转率、今日停车量
         * 路侧停车：车位总数、空余数、日均车位周转率、今日停车量
         * 停车场/路段坐标集合
         */
        getMainIndexData();

        /**
         * 左图1
         * 今日营收
         * 金额单位：元
         * 今日总营收
         * 停车收费 今日/昨日
         * 包期 今日/昨日
         * 退款 今日/昨日
         * 今日欠费、今日优惠、今日充值
         */
        getMainLeft1Data();

        /**
         * 定时任务
         * 每个整点重新数据渲染
         */
        nextIntegralPointAfterLogin();
    });

    /**
     * 首页
     * 停车道路数量（停车场数量+路侧街道数量）、车位总数（停车场车位总数+路侧泊位总数）、空余数、停车场数量
     * 停车场：车位总数、空余数、日均车位周转率、今日停车量
     * 路侧停车：车位总数、空余数、日均车位周转率、今日停车量
     * 停车场/路段坐标集合
     */
    function getMainIndexData() {
        $.ajax({
            url: '/sjfxNew/getMainIndexData.json',
            type: "POST",
            data: {},
            success: function (res) {
                console.log("统计信息")
                console.log(res)

                // 停车道路数量（停车场数量+路侧街道数量）、road_count
                var road_count = new CountUp("road_count", 0, res.data.road_count, 0, 2, options);
                // 车位总数（停车场车位总数+路侧泊位总数）、placeTotal
                var placeTotal = new CountUp("placeTotal", 0, res.data.placeTotal, 0, 2, options);
                // 空余数、freeCount
                var freeCount = new CountUp("freeCount", 0, res.data.freeCount, 0, 2, options);
                // 停车场数量 park_count
                var park_count = new CountUp("park_count", 0, res.data.park_count, 0, 2, options);

                //停车场：车位总数 parking_placeTotal、空余数 parking_freeCount、日均车位周转率 parking_rjcwzzl、今日停车量 parking_today_ztCount
                var parking_placeTotal = new CountUp("parking_placeTotal", 0, res.data.parking_placeTotal, 0, 2, options);
                var parking_freeCount = new CountUp("parking_freeCount", 0, res.data.parking_freeCount, 0, 2, options);
                var parking_rjcwzzl = new CountUp("parking_rjcwzzl", 0, res.data.parking_rjcwzzl, 0, 2, options);
                var parking_today_ztCount = new CountUp("parking_today_ztCount", 0, res.data.parking_today_ztCount, 0, 2, options);

                //路侧停车：车位总数 road_placeTotal、空余数 road_freeCount、日均车位周转率 road_rjcwzzl、今日停车量 road_today_ztCount
                var road_placeTotal = new CountUp("road_placeTotal", 0, res.data.road_placeTotal, 0, 2, options);
                var road_freeCount = new CountUp("road_freeCount", 0, res.data.road_freeCount, 0, 2, options);
                var road_rjcwzzl = new CountUp("road_rjcwzzl", 0, res.data.road_rjcwzzl, 0, 2, options);
                var road_today_ztCount = new CountUp("road_today_ztCount", 0, res.data.road_today_ztCount, 0, 2, options);

                road_count.start();
                placeTotal.start();
                freeCount.start();
                park_count.start();

                parking_placeTotal.start();
                parking_freeCount.start();
                parking_rjcwzzl.start();
                parking_today_ztCount.start();

                road_placeTotal.start();
                road_freeCount.start();
                road_rjcwzzl.start();
                road_today_ztCount.start();

                //路侧地图打点
                creatMarker(res.data.roadList, "0");
                //停车场地图打点
                creatMarker(res.data.parkList, "1");
            }
        })
    }

    /**
     * 左图1
     * 今日营收
     * 金额单位：元
     * 今日总营收
     * 停车收费 今日/昨日
     * 包期 今日/昨日
     * 退款 今日/昨日
     * 今日欠费、今日优惠、今日充值
     */
    function getMainLeft1Data() {
        $.ajax({
            url: '/sjfxNew/getMainLeft1Data.json',
            type: "POST",
            data: {},
            success: function (res) {
                // console.log(res)

                // 今日 总营收
                var today_moneys = new CountUp("today_moneys", 0, res.data.today_moneys, 0, 2, options);

                //今日 停车收费收入 = 停车场/已付金额
                var today_paidAmount = new CountUp("today_paidAmount", 0, res.data.today_paidAmount, 0, 2, options);
                //今日 包月收入
                var today_month_moneys = new CountUp("today_month_moneys", 0, res.data.today_month_moneys, 0, 2, options);
                //今日 退款金额
                var today_refund_amount = new CountUp("today_refund_amount", 0, res.data.today_refund_amount, 0, 2, options);


                //昨日 停车收费收入 = 停车场/已付金额
                var yesterday_paidAmount = new CountUp("yesterday_paidAmount", 0, res.data.yesterday_paidAmount, 0, 2, options);
                //昨日 包月收入
                var yesterday_month_moneys = new CountUp("yesterday_month_moneys", 0, res.data.yesterday_month_moneys, 0, 2, options);
                //昨日 退款金额
                var yesterday_refund_amount = new CountUp("yesterday_refund_amount", 0, res.data.yesterday_refund_amount, 0, 2, options);

                // 欠费
                var today_unpaidAmount = new CountUp("today_unpaidAmount", 0, res.data.today_unpaidAmount, 0, 2, options);
                // 优惠
                var today_discount_money = new CountUp("today_discount_money", 0, res.data.today_discount_money, 0, 2, options);
                // 充值收入
                var today_czMoney = new CountUp("today_czMoney", 0, res.data.today_czMoney, 0, 2, options);

                today_moneys.start();

                today_paidAmount.start();
                today_month_moneys.start();
                today_refund_amount.start();

                yesterday_paidAmount.start();
                yesterday_month_moneys.start();
                yesterday_refund_amount.start();

                today_unpaidAmount.start();
                today_discount_money.start();
                today_czMoney.start();
            }
        })
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
        } else if (index == 2) {
            trendLine.resize()
        } else {
            turnoverClum.resize()
        }

    })

    function mapToggle() {
        $('.leftbox').animate({left: 'toggle', opacity: 'toggle'}, 500);
        $('.rightbox').animate({right: 'toggle', opacity: 'toggle'}, 500);
        $('.centerbox').animate({top: 'toggle', opacity: 'toggle'}, 500);
    }

    /**
     * 定时任务
     * 每个整点重新数据渲染
     */
    function nextIntegralPointAfterLogin() {
        console.log("-----总览 定时任务------");

        // IntegralPointExecute();//在整点执行的函数，在每个整点都调用该函数
        setInterval("IntegralPointExecute();", 60 * 60 * 1000);//一个小时执行一次，那么下一个整点，下下一个整点都会执行
    }

    function IntegralPointExecute() {
        console.log("-----总览 每个整点重新数据渲染------");
        console.log(new Date());
        console.log(new Date().getHours());

        /**
         * 首页
         * 停车道路数量（停车场数量+路侧街道数量）、车位总数（停车场车位总数+路侧泊位总数）、空余数、停车场数量
         * 停车场：车位总数、空余数、日均车位周转率、今日停车量
         * 路侧停车：车位总数、空余数、日均车位周转率、今日停车量
         * 停车场/路段坐标集合
         */
        getMainIndexData();

        /**
         * 左图1
         * 今日营收
         * 金额单位：元
         * 今日总营收
         * 停车收费 今日/昨日
         * 包期 今日/昨日
         * 退款 今日/昨日
         * 今日欠费、今日优惠、今日充值
         */
        getMainLeft1Data();

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
