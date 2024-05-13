//地图地址
var url = 'http://jungoo.cn:98/wz/guangping/Z{z}/{y}/{x}.png';
var attr = ' 地图 数据 © <a href="http://www.jsdingchi.com/">江苏鼎驰电子科技有限公司</a>';
var map;
var marker;
var myIcon = G.icon({
	iconUrl: '/libs/img/marker-icon-2x.png',
	iconSize: [25, 41],
	iconAnchor: [12, 40],
	popupAnchor: [0, -40]
});
var myIconCar =  G.icon({	// 自定义图标
	iconUrl: new G.Icon.Default()._detectIconPath() + 'marker-icon.png',
	iconSize: [15, 32],	// 图标大小
	iconAnchor: [7, 16]	// 图标锚点，最好设置为图标的中点
});
var popupContents=[];
var markers=[];

mapBoxInit(23.0924663398,111.3112281241,16);
//mapBaiduInit(34.19289940978567,117.34929711688316,13);

//var pointJson=[{"lat":"23.090627913992105","lng":"111.30909919738771"},{"lat":"23.09068712979375","lng":"111.31371259689332"}]
//creatMarker(pointJson);
//clearMarker();



//初始化mapbox地图
function mapBoxInit(lat,lng,zoom){
	map = G.map('mapDiv',{
		zoomControl: false
	}).setView([lat, lng], zoom);
	//Mapbox图层
	G.tileLayer(url, {
		maxZoom: 21,
		minZoom: 14,
		attribution: attr,
		id: 'mapbox.streets'
	}).addTo(map);
	$(".goomap-control-attribution").html('');
	// 创建边界范围
	// var bounds = G.latlngBounds([22.6514, 114.1246], [22.41706, 113.70232]);
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

//初始化百度地图
function mapBaiduInit(lat,lng,zoom){
	var options = {
		maxZoom:18,
		minZoom:3,
		center: [lat, lng],
		zoom: zoom,
		crs: G.CRS.Baidu,
		layers: [new G.tileLayer.baidu({layer: 'vec'})]
	};
	map = G.map("mapDiv", options);
	$(".leaflet-control-attribution").html('');
}
//侦听地图事件
function mapClick(latId,lngId) {
	//添加click时间
	map.on('click', function (e) {
		//清空图标
		if(marker!=null)
		{
			map.removeLayer(marker);
		}
		markerClick(e,latId,lngId);
	});
}
//点击坐标获取地图标注点
function markerClick(em,latId,lngId) {
	// 赋值坐标点
	$('#'+latId).val(em.latlng.lat);
	$('#'+lngId).val(em.latlng.lng);

	//移动图标点到指定位置
	// map.flyTo([em.latlng.lat, em.latlng.lng]);
	// setTimeout(function() {
	// 	map.panBy([400, -200]);
	// }, 500);
	marker=  G.marker([em.latlng.lat,em.latlng.lng],{icon:myIcon }).addTo(map);
	var popupContent =G.popup({"maxWidth":"500"}).setContent("<p>坐标点： " + em.latlng.lat+","+em.latlng.lng+"</p>");
	marker.bindPopup(popupContent).openPopup();


}
//常见初始坐标点
function markerInit(latId,lngId) {
	// 赋值坐标点
	var lat= $('#'+latId).val();
	var lng= $('#'+lngId).val();
	marker=  G.marker([lat,lng],{icon:myIcon }).addTo(map);
	var popupContent =G.popup({"maxWidth":"500"}).setContent("<p>坐标点： " + lat+","+lng+"</p>");
	marker.bindPopup(popupContent).openPopup();
}
//创建标注点
function creatMarker(pointJson) {
	$.each(pointJson, function (n, value) {
		popupContents[n]='';
		//批量显示栏点击触发
		// var marker= G.marker([value.lat,value.lng],{icon:myIcon }).addTo(map).on('click',function (e) {
		// 	if(popupContents[n]==''|| popupContents[n]==null)
		// 	{
		// 		var popupContent =G.popup({"maxWidth":"500"}).setContent("<p>你点击坐标点： " + e.latlng.lat+","+e.latlng.lng+"</p>");
		// 		marker.bindPopup(popupContent).openPopup();
		// 		popupContents[n]=popupContent;
		// 	}
		// 	//设置弹出信息窗
		// 	//popupContent =G.popup({"maxWidth":"500"}).setlatlng(e.latlng).setContent("你点击坐标点： " + e.latlng.lat+","+e.latlng.lng).openOn(map);//设置弹窗信息窗自定义3
		// 	// alert(e.containerPoint.x);
		// 	//  var xset = e.containerPoint.x  + "px";
		// 	//  var yset =e.containerPoint.y-60 + "px";
		// 	//  $("#tstDiv").css({"position":"absolute","left": xset, "top": yset, "z-index": '1000'});
		// 	//  $("#tstDiv").html('我是小李自定义！');
		// 	//  $("#tstDiv").show();
		// });
		//批量显示标注点及文字框
		var marker= G.marker([value.lat,value.lng],{icon:myIcon }).addTo(map);
		marker = G.marker([value.lat,value.lng], {
			icon: G.divIcon({
				html: '<p>你点击坐标点： " + value.lat+","+value.lng+"</p>',
				iconSize: ['auto', 'auto'],
				iconAnchor: [25, 40],
				popupAnchor: [-10, -40],
				className: 'text'
			})
		}).addTo(map);
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
var json=[{"pointX":"111.30324125289918","pointY":"23.096840496269785","id":3000},
	{"pointX":"111.30331099033357","pointY":"23.096771414328956","id":2000},
	{"pointX":"111.30344510078432","pointY":"23.09665792249192","id":6000},
	{"pointX":"111.30365431308748","pointY":"23.096534561690763","id":8000},
	{"pointX":"111.30392253398897","pointY":"23.096361856378827","id":1000},
	{"pointX":"111.30408346652986","pointY":"23.09626810197366","id":1000},
	{"pointX":"111.30436778068544","pointY":"23.096125003018596","id":2000},
	{"pointX":"111.30494177341463","pointY":"23.097062545124693","id":5000}];
var json1=[];
//移动点位位置
function moveMarker() {
	$.each(json, function (n, value) {
        var isEsit=false;
        var jdsad;
		$.each(json1, function (n1, value1) {
              if(value.id==value1.id)
			  {
				  var isEsit=true;
				  jdsad=value1;
				  return;
			  }
		});
		if(isEsit)
		{
			jdsad.setlatlng(latlng);
		}
		else
		{
			var jsion;
			jsion.id=jdsad.id;
			jsion.marker=G.marker(latlng,{icon:myIcon }).addTo(map);
			jsion.marker.bindTooltip("我是刘合", {permanent: true});
			json1.push(jsion);
		}
	});

	var latlng=randomPostion();
	if(marker!=null)
	{
		//移动点位位置
		json[0].marker.setlatlng(latlng);
		//map.panTo(latlng);
	}
    else
	{


			json[i].id=id;
			json[i].marker=G.marker(latlng,{icon:myIcon }).addTo(map);
			json[i].marker.bindTooltip("我是刘合", {permanent: true});

	}
}
// setInterval(function() {
// 	//moveMarker();
// }, 1000);
//绘制线路
var gjtc = {layers:[]};
var watchPoint=[{"pointX":"111.30324125289918","pointY":"23.096840496269785","speed":3000},
	{"pointX":"111.30331099033357","pointY":"23.096771414328956","speed":2000},
	{"pointX":"111.30344510078432","pointY":"23.09665792249192","speed":6000},
	{"pointX":"111.30365431308748","pointY":"23.096534561690763","speed":8000},
	{"pointX":"111.30392253398897","pointY":"23.096361856378827","speed":1000},
	{"pointX":"111.30408346652986","pointY":"23.09626810197366","speed":1000},
	{"pointX":"111.30436778068544","pointY":"23.096125003018596","speed":2000},
	{"pointX":"111.30494177341463","pointY":"23.097062545124693","speed":5000}];
// 轨迹形式1
// 初始绘制轨迹线，以一个标注从起点开始往终点移动
var popupContentq=[];
snake(watchPoint);
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
			var marker= G.marker(value,{icon:myIcon }).addTo(map);
			marker.on('click',function (e) {
				if (popupContentq[n] == '' || popupContentq[n] == null) {
					var popupContent = G.popup({"maxWidth": "500"}).setContent("<p>当前时间</p>");
					marker.bindPopup(popupContent).openPopup();
					popupContentq[n] = popupContent;

				}
			});
		}
	});
    var startIcon=G.marker(latlngs[0],{icon:myIcon });
	var endIcon=G.marker(latlngs[len - 1],{icon:myIcon });
	var polyline=G.polyline(latlngs, {color: 'red',"snakingSpeed":2000})
	var route = G.featureGroup([
		startIcon,
		polyline,
		endIcon
	]).addTo(map);

	polyline.snakeIn();  // 触发动画
	gjtc.marker2 = G.Marker.movingMarker(londonBrusselFrankfurtAmsterdamLondon,
		speeed, {rotate: true,loop: false,icon:myIconCar});
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
