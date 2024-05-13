var map;
//创建图片对象
//            var icon = new BMapGL.Icon({
//                iconUrl: "../img/marker/btnwater2.png",
//                iconSize: new BMapGL.Point(30, 34),
//                iconAnchor: new BMapGL.Point(10, 25)
//            });
//var myIcon2 = new BMapGL.Icon({
//        iconUrl: '../img/marker/btnjk.png',
//        iconSize: new BMapGL.Point(30, 36),
//        iconAnchor: new BMapGL.Point(10, 25)
//    });
//var myIcon3 = new BMapGL.Icon({
//        iconUrl: '../img/marker/btnwarning.png',
//        iconSize: new BMapGL.Point(40, 35),
//        iconAnchor: new BMapGL.Point(10, 25)
//    });
var myIcon = new BMapGL.Icon("../sjfxNew/img/pack.png", new BMapGL.Size(40, 40), {
    imageOffset: new BMapGL.Size(0, 0),
    offset: new BMapGL.Size(0, 0)
});
var myIcon1 = new BMapGL.Icon("../sjfxNew/img/road.png", new BMapGL.Size(40, 40), {
    imageOffset: new BMapGL.Size(0, 0),
    offset: new BMapGL.Size(0, 0)
});
var myIcon2 = new BMapGL.Icon("../sjfxNew/img/worker.png", new BMapGL.Size(40, 40), {
    imageOffset: new BMapGL.Size(0, 0),
    offset: new BMapGL.Size(0, 0)
});

function maponLoad(lng, lat, zoom) {
    // console.log("maponLoad")

    // //初始化地图对象
    // map = new BMapGL.Map("mapDiv");
    // //设置显示地图的中心点和级别
    // map.centerAndZoom(new BMapGL.Point(lng, lat), zoom);
    // // map.addControl(new BMapGL.NavigationControl());
    // //允许鼠标双击放大地图
    // //map.addControl(new BMapGL.ScaleControl());    //添加左下方比例尺控件
    // map.enableScrollWheelZoom(true);     //开启鼠标滚轮缩放
    // //          map.enableInertia();
    // //          map.enableKeyboard();
    //
    // map.setHeading(0);//角度
    // map.setTilt(60);//倾斜角
    // map.setMapStyleV2({ styleJson: yanmou });
    // //map.setMapStyle({style:'midnight'});

    // GL版命名空间为BMapGL
    // 按住鼠标右键，修改倾斜角和角度
    map = new BMapGL.Map("mapDiv");    // 创建Map实例
    map.centerAndZoom(new BMapGL.Point(lng, lat), zoom);  // 初始化地图,设置中心点坐标和地图级别
    map.enableScrollWheelZoom(true);     //开启鼠标滚轮缩放
    map.addControl(new BMapGL.NavigationControl());
    map.setHeading(0);//角度
    map.setTilt(60);//倾斜角
    map.setMapStyleV2({styleJson: yanmou});
}

function DataInit() {
    // console.log("DataInit")
    maponLoad(117.266777, 34.260551, 18);
    // maponLoad(117.18, 34.27, 15);
    // maponLoad(123.362222, 41.773469, 18);
    // setTimeout(function () {
    //     $('.BMapGL_cpyCtrl').remove();
    //     $('.anchorBL').remove();
    // }, 500);
}

function creatMarker(pointJson, type) {
    // console.log(pointJson);
    // $.each(pointJson, function (n, value) {
    var iconType = myIcon;
    pointJson.forEach((value, n) => {
        if (type == "1") {
            iconType = myIcon1;
        }
        //创建标注对象
        var marker = new BMapGL.Marker(new BMapGL.Point(value.longitude, value.latitude), {icon: iconType});
        //向地图上添加标注
        map.addOverlay(marker); // 将标注添加到地图中
        //marker.setAnimation(BMapGL_ANIMATION_BOUNCE);
        //labelInfo(value.lng, value.lat,value.name);

        //地图图标点击事件
        marker.addEventListener("click", function () {
            // console.log("marker.addEventListener")
            getMainIndexView(marker, value, type)
            // marker.openInfoWindow(infoWin1);
            // map.panTo(new BMapGL.Point(value.longitude, value.latitude));// map.panTo方法，把点击的点设置为地图中心点
            // map.setHeading(0);//角度
            // map.setTilt(60);//倾斜角
            // map.setMapStyleV2({styleJson: yanmou});
        });// 将标注添加到地图中

    });
}

//点击地图图标，显示停车场/路侧 总泊位数、空闲数、今日营收、昨日营收、今日停车数量、昨日停车数量
function getMainIndexView(marker, value, type) {
    $.ajax({
        url: '/sjfxNew/getMainIndexView.json',
        type: "POST",
        data: {id: value.id, type: type},
        success: function (res) {
            // console.log(res)
            var infoWin1 = new BMapGL.InfoWindow();
            var sContent = "";
            if (type == 2) {
                // sContent = "<div class='p10 f-cb'><p class='titles'>" + value.name + "<i>编号：" + value.id + "</i></p><p class='conts'><em class='items'><i class='iconfont icon-dianhua1'></i>" + value.tel + "</em><em class='items'><i class='iconfont icon-weizhi11'></i>" + value.position + "</em></p></div>";
            } else if (type == "1") {
                //停车场详情
                sContent = "<div class='p10 f-cb'><p class='titles colorlan tips'>" + value.park_name + "</p>"
                    + "<div class='map-list-con'>"
                    + "<div class='map-list-item'>"
                    + "<h3>" + res.data.parkNum + "</h3>"
                    + "<p>总泊位数</p>"
                    + "</div>"
                    + "<div class='map-list-item'>"
                    + "<h3>" + res.data.parkingFreeCount + "</h3>"
                    + "<p>空闲数</p>"
                    + "</div>"
                    + "<div class='map-list-item'>"
                    + "<h3><i>￥</i>" + res.data.today_money + "</h3>"
                    + "<p>今日停车营收</p>"
                    + "</div>"
                    + "<div class='map-list-item'>"
                    + "<h3><i>￥</i>" + res.data.yesterday_money + "</h3>"
                    + "<p>昨日停车营收</p>"
                    + "</div>"
                    + "<div class='map-list-item'>"
                    + "<h3>" + res.data.parking_today_ztCount + "</h3>"
                    + "<p>今日停车</p>"
                    + "</div>"
                    + "<div class='map-list-item'>"
                    + "<h3>" + res.data.parking_yesterday_ztCount + "</h3>"
                    + "<p>昨日停车</p>"
                    + "</div>"
                    + "</div>"
                    + "<p class='btnbox tc'>"
                    + "<button class='btns close' onclick='closePup()'>关闭</button>"
                    + "<button class='btns' onclick='openDetailPark(" + value.id + ")'>查看详情</button>"
                    + "</p>";

            } else {
                //路侧详情
                sContent = "<div class='p10 f-cb'><p class='titles colorlan tips'>" + value.road_name + "</p>"
                    + "<div class='map-list-con'>"
                    + "<div class='map-list-item'>"
                    + "<h3>" + res.data.roadNum + "</h3>"
                    + "<p>总泊位数</p>"
                    + "</div>"
                    + "<div class='map-list-item'>"
                    + "<h3>" + res.data.roadFreeCount + "</h3>"
                    + "<p>空闲数</p>"
                    + "</div>"
                    + "<div class='map-list-item'>"
                    + "<h3><i>￥</i>" + res.data.today_money + "</h3>"
                    + "<p>今日停车营收</p>"
                    + "</div>"
                    + "<div class='map-list-item'>"
                    + "<h3><i>￥</i>" + res.data.yesterday_money + "</h3>"
                    + "<p>昨日停车营收</p>"
                    + "</div>"
                    + "<div class='map-list-item'>"
                    + "<h3>" + res.data.road_today_ztCount + "</h3>"
                    + "<p>今日停车</p>"
                    + "</div>"
                    + "<div class='map-list-item'>"
                    + "<h3>" + res.data.road_yesterday_ztCount + "</h3>"
                    + "<p>昨日停车</p>"
                    + "</div>"
                    + "</div>"
                    + "<p class='btnbox tc'>"
                    + "<button class='btns close' onclick='closePup()'>关闭</button>"
                    + "<button class='btns' onclick='openDetail(" + value.id + ")'>查看详情</button>"
                    + "</p>";
            }

            infoWin1.setContent(sContent);

            marker.openInfoWindow(infoWin1);
            map.panTo(new BMapGL.Point(value.longitude, value.latitude));// map.panTo方法，把点击的点设置为地图中心点
        }
    })
}

function closePup() {
    map.closeInfoWindow(); //关闭信息窗口
}
function openDetail(id) {
    window.parent.$('.menu').hide()
    window.parent.$('#fMap').hide()
    window.parent.$('#backTo').show()
    window.location.href = '/sjfxNew/roadView.do?id=' + id
}

function openDetailPark(id) {
    window.parent.$('.menu').hide()
    window.parent.$('#fMap').hide()
    window.parent.$('#backTo').show()
    window.location.href = '/sjfxNew/parkingView.do?id=' + id
}
