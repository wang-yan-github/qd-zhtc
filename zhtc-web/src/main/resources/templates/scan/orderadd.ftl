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
    <link rel="stylesheet" href="css/iosSelect.css">
</head>

<body>
    <div class="wrap fillinfo">
        <header class="header headerwhitebg">
            <div class="topbar">
                <span class="btn-left"><i class="iconfont icon-back"></i></span>
                <div class="words ellipsis">申诉订单</div>
                <!-- <div class="rightword" id="feedbacklist"><i class="iconfont icon-fankuiyijianfankui-xianxing"></i>订单列表</div> -->
            </div>
        </header>
        <div class="contWrap">
            <div class="formbox">

                <div class="item-form">
                    <div class="label">申诉原因</div>
                    <div class="items borderbottom checkbox">
                        <div class="l">没有本次停车行为</div>
                        <div class="r"><i class="iconfont icon-xingzhuang-tuoyuanxing"></i></div>
                        <input type="hidden" value="0">
                    </div>
                    <div class="items borderbottom checkbox">
                        <div class="l">停车时间有误</div>
                        <div class="r"><i class="iconfont icon-xingzhuang-tuoyuanxing"></i></div>
                        <input type="hidden" value="0">
                    </div>
                    <div class="items borderbottom">
                        <div class="l">驶入时间</div>
                        <div class="r">2023-05-29 17:04</div>
                    </div>
                    <div class="items">
                        <div class="l">驶出时间</div>
                        <div class="r">2023-05-29 17:04</div>
                    </div>
                </div>
                <div class="item-form">
                    <div class="label"><text>图片（选填）</text><text class="small">（调用摄像头拍摄车辆前后照片，限制4张照片）</text></div>
                    <div class="uploader">
                        <!-- <div class="img-v">
                            <ul class="content-img-list">                                
                            </ul>
                            <div class="uploader__input-box pic" bindtap="chooseImg">+<input type="file" name="file"
                                    accept="image/*" id="upload" multiple=""></div>
                        </div> -->
                        <article class="upload-piclist">
                            <div class="upload-file">
                                +
                                <input type="file" id="file" accept="image/*" multiple
                                    onchange="imgChange('upload-Picitem')" />
                            </div>
                        </article>
                    </div>

                </div>

                <div class="item-form">
                    <div class="label">申诉原因<text class="small">（选填）</text></div>
                    <div class="input"><input placeholder="请填写申诉原因（100字以内）"></input></div>

                </div>

            </div>

        </div>

        <div class="bottom">
            <div class="submit" onclick="submit()">提交</div>
        </div>
    </div>

    <script src="js/data.js"></script>
    <script src="js/iosSelect.js"></script>
    <script src="js/iscroll.js"></script>
    <script type="text/javascript">


        $('.formbox').on('click', '.checkbox', function () {          
            
            $(this).find("input").val(1);
            $(this).find(".iconfont").removeClass("icon-xingzhuang-tuoyuanxing").addClass("active icon-dui")            
            $(this).siblings().find(".iconfont").removeClass("active icon-dui").addClass("icon-xingzhuang-tuoyuanxing")
            $(this).siblings().find("input").val(0);
           
        });




        // 时间选择

        var selectDateDom = $('#selectDate');
        var showDateDom = $('#showDate');
        // 初始化时间
        var now = new Date();
        var nowYear = now.getFullYear();
        var nowMonth = now.getMonth() + 1;
        var nowDate = now.getDate();
        showDateDom.attr('data-year', nowYear);
        showDateDom.attr('data-month', nowMonth);
        showDateDom.attr('data-date', nowDate);
        // 数据初始化
        function formatYear(nowYear) {
            var arr = [];
            for (var i = nowYear - 5; i <= nowYear + 5; i++) {
                arr.push({
                    id: i + '',
                    value: i + '年'
                });
            }
            return arr;
        }
        function formatMonth() {
            var arr = [];
            for (var i = 1; i <= 12; i++) {
                arr.push({
                    id: i + '',
                    value: i + '月'
                });
            }
            return arr;
        }
        function formatDate(count) {
            var arr = [];
            for (var i = 1; i <= count; i++) {
                arr.push({
                    id: i + '',
                    value: i + '日'
                });
            }
            return arr;
        }
        var yearData = function (callback) {
            callback(formatYear(nowYear))
        }
        var monthData = function (year, callback) {
            callback(formatMonth());
        };
        var dateData = function (year, month, callback) {
            if (/^(1|3|5|7|8|10|12)$/.test(month)) {
                callback(formatDate(31));
            }
            else if (/^(4|6|9|11)$/.test(month)) {
                callback(formatDate(30));
            }
            else if (/^2$/.test(month)) {
                if (year % 4 === 0 && year % 100 !== 0 || year % 400 === 0) {
                    callback(formatDate(29));
                }
                else {
                    callback(formatDate(28));
                }
            }
            else {
                throw new Error('month is illegal');
            }
        };
        var hourData = function (one, two, three, callback) {
            var hours = [];
            for (var i = 0, len = 24; i < len; i++) {
                hours.push({
                    id: i,
                    value: i + '时'
                });
            }
            callback(hours);
        };
        var minuteData = function (one, two, three, four, callback) {
            var minutes = [];
            for (var i = 0, len = 60; i < len; i++) {
                minutes.push({
                    id: i,
                    value: i + '分'
                });
            }
            callback(minutes);
        };
        selectDateDom.bind('click', function () {
            var oneLevelId = showDateDom.attr('data-year');
            var twoLevelId = showDateDom.attr('data-month');
            var threeLevelId = showDateDom.attr('data-date');
            var fourLevelId = showDateDom.attr('data-hour');
            var fiveLevelId = showDateDom.attr('data-minute');
            var iosSelect = new IosSelect(5,
                [yearData, monthData, dateData, hourData, minuteData],
                {
                    title: '日期选择',
                    itemHeight: 35,
                    itemShowCount: 9,
                    oneLevelId: oneLevelId,
                    twoLevelId: twoLevelId,
                    threeLevelId: threeLevelId,
                    fourLevelId: fourLevelId,
                    fiveLevelId: fiveLevelId,
                    callback: function (selectOneObj, selectTwoObj, selectThreeObj, selectFourObj, selectFiveObj) {
                        showDateDom.attr('data-year', selectOneObj.id);
                        showDateDom.attr('data-month', selectTwoObj.id);
                        showDateDom.attr('data-date', selectThreeObj.id);
                        showDateDom.attr('data-hour', selectFourObj.id);
                        showDateDom.attr('data-minute', selectFiveObj.id);
                        showDateDom.html(selectOneObj.value + '' + selectTwoObj.value + '' + selectThreeObj.value + ' ' + selectFourObj.value + '' + selectFiveObj.value);
                    }
                });
        });


    </script>
    <!-- <script src="js/uploadImg.js"></script> -->
    <script src="js/uploader.js"></script>
    <script>
        //提交
        function submit() {
            let imglist = []
            let piclist = document.querySelectorAll('.upload-Picitem');
            for (let i = 0; i < piclist.length; i++) {
                imglist.push(piclist[i].lastChild.src)
            }
            console.log("图片列表：", imglist)
        }
    </script>
</body>

</html>