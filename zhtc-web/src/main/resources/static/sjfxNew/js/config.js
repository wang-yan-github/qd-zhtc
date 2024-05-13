var url = 'http://jungoo.cn:98/wz/guangping/Z{z}/{y}/{x}.png';
    var attr = '<a href="http://www.jsdingchi.com/">江苏鼎驰电子科技有限公司</a>';
    var map;
    var marker;
	
	//广播图标
    var myIcon = G.icon({
        iconUrl: '../img/marker/btnjk.png',
        iconSize: [30, 36],
        iconAnchor: [30, 36],
        popupAnchor: [-15, -36]
    });
	
	var myIcon2 = G.icon({
        iconUrl: '../img/marker/btnjky.png',
        iconSize: [30, 36],
        iconAnchor: [30, 36],
        popupAnchor: [-15, -18]
    });
	//监控图标
    var mjIcon = G.icon({
        iconUrl: '../img/marker/btnmj.png',
        iconSize: [40, 40],
        iconAnchor: [40, 40],
        popupAnchor: [-20, -40]
    });

	var mjIcon2 = G.icon({
        iconUrl: '../img/marker/btnmj.png',
        iconSize: [40, 40],
        iconAnchor: [40, 40],
        popupAnchor: [-20, -40]
    });
	//wifi图标
    var jcIcon = G.icon({
        iconUrl: '../img/marker/btnjc.png',
        iconSize: [35, 71],
        iconAnchor: [35, 71],
        popupAnchor: [-17, -71]
    });	
	var jcIcon2 = G.icon({
        iconUrl: '../img/marker/btnjc.png',
        iconSize: [40, 40],
        iconAnchor: [40, 40],
        popupAnchor: [-20, -40]
    });
	//wc图标
    var bjIcon = G.icon({
        iconUrl: '../img/marker/btnbj.png',
        iconSize: [40, 40],
        iconAnchor: [40, 40],
        popupAnchor: [-20, -40]
    });
	   var bjIcon1 = G.icon({
        iconUrl: '../img/marker/btnbj.png',
        iconSize: [40, 40],
        iconAnchor: [40, 40],
        popupAnchor: [-20, -40]
    });
	var bjIcon2 = G.icon({
        iconUrl: '../img/marker/btnbaoj.gif',
        iconSize: [40, 40],
        iconAnchor: [40, 40],
        popupAnchor: [-20, -40]
    });



var moveIcon =  G.icon({	// 自定义图标
	iconUrl: '../img/marker/movemark.png',
	iconSize: [20, 20],	// 图标大小
	iconAnchor: [10, 10]	// 图标锚点，最好设置为图标的中点
});
var beginIcon =  G.icon({	// 自定义图标
	iconUrl: '../img/marker/beginmark.png',
	iconSize: [30, 30],	// 图标大小
	iconAnchor: [15, 15]	// 图标锚点，最好设置为图标的中点
});
var closeIcon =  G.icon({	// 自定义图标
	iconUrl: '../img/marker/endmark.png',
	iconSize: [30, 30],	// 图标大小
	iconAnchor: [15, 15]	// 图标锚点，最好设置为图标的中点
});
var throwIcon =  G.icon({	// 自定义图标
	iconUrl: '../img/marker/throwmark1.png',
	iconSize: [16, 16],	// 图标大小
	iconAnchor: [8, 8]	// 图标锚点，最好设置为图标的中点
});

var myIconCar =  G.icon({	// 自定义图标
	iconUrl: new G.Icon.Default()._detectIconPath() + 'marker-icon.png',
	iconSize: [15, 32],	// 图标大小
	iconAnchor: [7, 16]	// 图标锚点，最好设置为图标的中点
});

var myIconLudeng =  G.icon({	// 自定义图标
	iconUrl: '../img/marker/btnld.png',
        iconSize: [40, 40],
        iconAnchor: [40, 40],
        popupAnchor: [-20, -40]
});
	//标注点    
    var popupContents=[];
    var markers=[];
    mapBoxInit(23.0924663398,111.3112281241,18);
    //mapClick();  
	
    //clearMarker();


    //初始化地图
    function mapBoxInit(lat,lng,zoom){
	map = G.map('mapDiv',{
		zoomControl: false
	}).setView([lat, lng], zoom);
	//Mapbox图层
	G.tileLayer(url, {
		maxZoom: 21,
		minZoom: 17,
		attribution: attr,
		id: 'mapbox.streets'
	}).addTo(map);
	$(".goomap-control-attribution").html('');
		// 创建边界范围
	// var bounds = G.latLngBounds([22.6514, 114.1246], [22.41706, 113.70232]);
	// // 设置地图显示范围
	// map.setMaxBounds(bounds);
	G.control.distanceMeasure({
		position: 'bottomleft'
	}).addTo(map);
	G.control.areaMeasure({
		position: 'bottomleft'
	}).addTo(map);
    // 比例尺控件
    var scaleControl = G.control.scale({position: 'bottomleft'});
    // 缩放控件

	var zoomControl = G.control.zoom({position: 'bottomright'});
	var fullScreenControl = G.control.fullscreen({position: 'bottomleft'});
	map.addControl(fullScreenControl);
    map.addControl(scaleControl);
    map.addControl(zoomControl);
    }
    //侦听地图事件
    function mapClick() {
        //添加click时间
        map.on('click', function (e) {
            //清空图标
            if(marker!=null)
            {
                map.removeLayer(marker);
            }
            markerClick(e);
        });
    }
    //点击坐标获取地图标注点
    function markerClick(em) {
        //设置弹出信息窗1
        var popupContent='';
        marker=  G.marker([em.latlng.lat,em.latlng.lng], {icon: myIcon}).addTo(map).on('click',function (e) {
            if(popupContent=='')
            {
                popupContent =G.popup({"maxWidth":"500"}).setContent("<p>你点击坐标点： " + e.latlng.lat+","+e.latlng.lng+"</p>");
                marker.bindPopup(popupContent).openPopup();
            }
        });
    }

    //创建广播标注点
    function creatMarker(pointJson,icon) {
        $.each(pointJson, function (n, value) {
			popupContents[n]='';
			if(value.type==20){var marker= G.marker([value.lat,value.lng], {icon: value.icon}).addTo(map)}
			else if(value.type==2){
			  var marker= G.marker([value.lat,value.lng], {icon: value.icon}).addTo(map).on('click',function (e) {
			   	
			   //markers[n].setIcon(myIcon1);
                //设置弹出信息窗2
                //popupContent =G.popup({"maxWidth":"500"}).setLatLng(e.latlng).setContent("你点击坐标点： " + e.latlng.lat+","+e.latlng.lng).openOn(map);//设置弹窗信息窗自定义3
                 //alert(e.containerPoint.x);
                  var xset = e.containerPoint.x  + "px";
                  var yset =e.containerPoint.y-60 + "px";
                  layui.use('layer', function(){ 
                        var layer = layui.layer; 
			            layer.open({
                              type: 2,
                              title:false,
                              skin: 'shadows',  //定义一个类名 可修改弹框透明等样式
                              closeBtn: 0, //不显示关闭按钮
                              shade: 0.5,
                              shadeClose: true,
                              area: ['602px', '402px'],
                              //offset: yset, //右下角弹出
                              //time: 2000, //2秒后自动关闭
                              anim: 2,
                              content: ['/video.html', 'no'], //iframe的url，no代表不显示滚动条
                              });
				         });
			   
			   map.flyTo([e.latlng.lat, e.latlng.lng]);


            });
				
			}
			else{
           var marker= G.marker([value.lat,value.lng], {icon: value.icon}).addTo(map).on('click',function (e) {
                //$("#tstDiv").hide();
                if(popupContents[n]==''||popupContents[n]==null)
                {
					var html="";
					if(value.type==1){
						html="<div class='f-cb'><div class='cont'><p class='p1'>名称：</p>0001号门禁<br /><p class='p1'>状态：</p>关闭</div><div class='tc'><button class='btns'>开启</button></div></div>"
					}else if(value.type==3){
						html="<div class='f-cb'><p class='title'>A报警设备</p><div class='btnbox tl'><button class='btns'><i class='iconfont icon-yuyintonghua2'></i>语音通话</button><button class='btns'><i class='iconfont icon-shipintonghua'></i>视频通话</button></div></div>"
					}else if(value.type==5){
						html="<div class='f-cb'><p class='titles'>朱晓略<i>编号：BC565445</i></p><div class='conts'><div class='items'><i class='iconfont icon-dianhua1'></i>15865256878</div><div class='items'><i class='iconfont icon-weizhi11'></i>1号区域，报警区附近</div></div><div class='btnbox tl'><button class='btnsfa'><i class='iconfont icon-yuyintonghua'></i>语音通话</button><button class='btnsfa'><i class='iconfont icon-jichutubiao-'></i>视频通话</button></div></div>"
					}else if(value.type==6){
						html="<div class='f-cb'><div class='titles colorlan'>未处理<div class='normalbtnbox xqbtn'><button class='sbtns smallbtn' onclick='showdetail()'>查看详情</button></div></div><div class='cont'><p class='p1'>事件编号：</p>BCJY18458568002<br /><p class='p1'>事件类型：</p>火灾<br /><p class='p1'>报警设备：</p>15825468567<br /><p class='p1'>报警时间：</p>2020-09-22 12:25:45<br /><p class='p1'>事件等级：</p>II级（特大）<br /><p class='p1'>事件地点：</p>1号区域附件</div></div>"
					}
					else
						{html=""}
                    var popupContent =G.popup({"maxWidth":"600"}).setContent(html);
                    marker.bindPopup(popupContent).openPopup();					
                    popupContents[n]=popupContent;
					//initPlugin(); 
                }
			   	
			   //markers[n].setIcon(myIcon1);
                //设置弹出信息窗2
                //popupContent =G.popup({"maxWidth":"500"}).setLatLng(e.latlng).setContent("你点击坐标点： " + e.latlng.lat+","+e.latlng.lng).openOn(map);//设置弹窗信息窗自定义3
                 //alert(e.containerPoint.x);
//                  var xset = e.containerPoint.x  + "px";
//                  var yset =e.containerPoint.y-60 + "px";
//			      var html="";
//                  html+="<div class=\"action-title\">AAA摄像头<span class=\"close\">X</span></div>";
//			      html+="<div class=\"bk1\"></div>";
//			      html+="<div class=\"bk2\"></div>";
//			      $("#openwin").css({"left": xset, "top": yset, "z-index": '1000'});			   
//                  $("#openwin").html(html);
//                  $("#openwin").show();
			   
			   map.flyTo([e.latlng.lat, e.latlng.lng]);
//	           setTimeout(function() {
//		          map.panBy([0, -300]);
//	             }, 300);
			   
			   
			   
			   
            });}
			
//			marker.on('mouseover',function(){markers[n].setIcon(myIcon1);});
//			marker.on('mouseout',function(){markers[n].setIcon(myIcon);});
            markers.push(marker);
        });
    }	

    //清空所有标点
    function  clearMarker() {
        var myGroup=G.layerGroup(markers);
        map.addLayer(myGroup);
        myGroup.clearLayers();
    }

// 移除覆盖物
function removeOverlay(object) {
	map.removeLayer(object);	// 移除点
}
function showdetail(){
	$("#xqBox").addClass("show");
}
//随机生成位置
function randomPostion() {
	var bounds = map.getBounds();
	var sw = bounds.getSouthWest();
	var ne = bounds.getNorthEast();
	var lngSpan = Math.abs(sw.lng - ne.lng);
	var latSpan = Math.abs(ne.lat - sw.lat);
	var latlng = G.latLng(ne.lat - latSpan * (Math.random() * 0.7), sw.lng + lngSpan * (Math.random() * 0.7));
	return latlng;
}

//移动点位位置
function moveMarker() {
	var latlng=randomPostion();
	if(marker!=null)
	{
		//移动点位位置
		marker.setLatLng(latlng);
		map.panTo(latlng);
	}
    else
	{
		marker=G.marker(latlng,{icon:myIcon }).addTo(map);
		marker.bindTooltip("我是刘合", {permanent: true});
	}
}
// setInterval(function() {
// 	//moveMarker();
// }, 1000);
//绘制线路

var gjtc = {layers:[]};

// 轨迹形式1
// 初始绘制轨迹线，以一个标注从起点开始往终点移动
var popupContentq=[];
//snake(watchPoint);
function snake(watchPoint) {
	var londonBrusselFrankfurtAmsterdamLondon=[];
	var speeed=[];
	$.each(watchPoint, function (n, value) {
		londonBrusselFrankfurtAmsterdamLondon.push([value.pointY,value.pointX]);
		speeed.push(value.speed);
	});
	if(gjtc.group){
		gjtc.group.clearLayers();
		gjtc.layers = []
	}
	var latlngs = [];
	var len = londonBrusselFrankfurtAmsterdamLondon.length;
	for (var i = 0; i < len; i++) {
		latlngs.push(new G.latLng(londonBrusselFrankfurtAmsterdamLondon[i][0], londonBrusselFrankfurtAmsterdamLondon[i][1]));
	}
	$.each(latlngs, function (n, value) {
		if(n>0&&n<latlngs.length-1)
		{
			popupContentq[n]='';
			//批量显示栏点击触发
			var marker= G.marker(value,{icon:throwIcon }).addTo(map);//途经图标
			marker.on('click',function (e) {
				if (popupContentq[n] == '' || popupContentq[n] == null) {
					var popupContent = G.popup({"maxWidth": "500"}).setContent("<p>当前时间</p>");
					marker.bindPopup(popupContent).openPopup();
					popupContentq[n] = popupContent;

				}
			});
		}
	});
    var startIcon=G.marker(latlngs[0],{icon:beginIcon });//起始图标
	var endIcon=G.marker(latlngs[len - 1],{icon:closeIcon });//结束图标
	var polyline=G.polyline(latlngs, {color: 'red',"snakingSpeed":2000})
	var route = G.featureGroup([
		startIcon,
		polyline,
		endIcon
	]).addTo(map);

	polyline.snakeIn();  // 触发动画
	gjtc.marker2 = G.Marker.movingMarker(londonBrusselFrankfurtAmsterdamLondon,
		speeed, {rotate: true,loop: false,icon:moveIcon});//移动图标
	gjtc.marker2.on('end', function() {
		gjtc.marker2.stop();
		map.removeLayer(gjtc.marker2);
	});
	gjtc.marker2.on('animating', function (e) {
		// 判断标注位置是否在可视范围内
		if (!map.getBounds().contains(e.latLng)) {
			map.panTo(e.latLng);
		}
	})
	startIcon.on('click', function() {
		gjtc.layers.push(gjtc.marker2);
		gjtc.marker2.addTo(map);
		map.fitBounds(G.latLngBounds(latlngs));
		gjtc.marker2.start();
	});
	endIcon.on('click', function() {
		gjtc.marker2.pause();
	});
	gjtc.layers.push(route);
	gjtc.group = G.layerGroup(gjtc.layers).addTo(map);
}


//热力图
function heat(heatPoint) {
	var addressPoints =[];
	$.each(heatPoint, function (n, value) {
		addressPoints.push({"lat":value.pointY,"lng":value.pointX,"count":value.count});
	});
	var cfg = {
		// 当scaleRadius为true时，需要小半径
		// 如果scaleRadius为false，它将是以像素为单位
		radius: 0.0005,
		maxOpacity: .8,	// 最大不透明度
		// 基于地图级别的缩放半径
		scaleRadius: true,
		latField: 'lat',	// 纬度字段名
		lngField: 'lng',	// 经度字段名
		valueField: 'count'	// 数据值字段名
	};
	var testData = {
		data: addressPoints,
		max: 1000	// 最大值
	};
	var heatmapLayer = new HeatmapOverlay(cfg).addTo(map);
	heatmapLayer.setData(testData);
}

function closePop(){
    map.closePopup();
}