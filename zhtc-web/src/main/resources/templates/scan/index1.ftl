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
                <div class="cartitle">请选择缴费车辆</div>
                <ul class="ul_input">
                    <li id="cp1" class="input_zim" data-sort="1"><span></span></li>
                    <li id="cp2" data-sort="2"><span></span></li>
                    <li id="cp3" data-sort="3"><span></span></li>
                    <li id="cp4" data-sort="4"><span></span></li>
                    <li id="cp5" data-sort="5"><span></span></li>
                    <li id="cp6" data-sort="6"><span></span></li>
                    <li id="cp7" data-sort="7"><span></span></li>
                    <li id="cp8" data-sort="8" style="display:none;"><span></span></li>
                    <li class="xinneng"><span><img src="img/xinweng.png"></span></li>
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
            window.location.href = "/scan/toOrder?parkId=${parkId}&carNo=" + carNo + "&carType=" + carType;
        }
    </script>
</body>

</html>