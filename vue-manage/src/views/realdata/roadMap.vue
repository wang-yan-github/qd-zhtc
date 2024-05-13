<template>
  <div style="height: 100%">
    <div id="mapDiv" style="height: 100%">
      <div id="has-map" class="has-map" />
    </div>

    <!-- <div class="baidumap" id:"allmap"></div> -->
  </div>
</template>

<script>
import { ref, reactive, onBeforeUnmount } from "vue";
import imgUrl from "../../assets/img/marker.png";
// import { creatMarker } from "../../plugins/loadinitTmap";
import { RoadGPCountList } from "../../api";

export default {
  name: "roadmap",
  setup() {
    let form = reactive({
      name: "",
      address: "",
      czroptions: [
        {
          value: "1",
          label: "平台管理员",
        },
        {
          value: "2",
          label: "管理员",
        },
        {
          value: "3",
          label: "车场",
        },
      ],
      selvalue: "",
      radio2: "男",
    });
    //const markerUrl = imgUrl;
    const pointJson = ref([]);
    // var pointJson = [{
    // 		"lat": "34.25639",
    // 		"lng": "117.1996",
    // 		"icon": imgUrl,
    // 		"name": "东坡休闲广场",
    // 		"type": "1",
    // 		"totalparking": "58",
    // 		"freeparking": "23",
    // 		"id": "000001"
    // 	}];
    const getRoadGPCount = () => {
      RoadGPCountList(reactive({})).then((res) => {
        pointJson.value = res.data;
        clearMaps();
        initMaps();
      });
    };
    getRoadGPCount();
    var map = null;
    const clearMaps = () => {
      if (map != undefined || map != null) {
        // map.eachLayer(function (layer) {
        //   layer.off();
        //   layer.remove();
        // });
        // marker = null;

        map = null;
      }
      //this.map.destory();
    };
    const initMaps = () => {
      //console.log(1111);
      //map.destroy();
      if (document.getElementById("has-map")) {
        var point = [34.262858, 117.280228];
        //console.log(point);
        const center = new window.TMap.LatLng(point[0], point[1]);
        // 初始化地图
        map = new window.TMap.Map(document.getElementById("has-map"), {
          rotation: 0, // 设置地图旋转角度
          pitch: 0, // 设置俯仰角度（0~45）
          zoom: 14, // 设置地图缩放级别
          center: center, // 设置地图中心点坐标
        });
        map.setCenter(new window.TMap.LatLng(point[0], point[1]));
        const markergeometries = [];
        const labelgeometries = [];
        const sContent = [];
        //console.log("pointjson值为:"+JSON.stringify(pointJson.value));
        pointJson.value.forEach((item, i) => {
          //var labelname=item.name+"【总泊位:"+item.totalparking+"】【空泊位:"+item.freeparking+"】";
          var labelname = item.name;
          sContent.push(
            "<p style='margin:0px 0px 0px 0px;line-height:20px;font-size:14px;'>总泊位：<span style='color:#fe0808'>" +
              item.totalparking +
              "</span> <br>空泊位：<span style='color:#fe0808'>" +
              item.freeparking +
              "</span>"
          );
          markergeometries.push({
            styleId: "big",
            position: new window.TMap.LatLng(item.lat, item.lng),
            properties: { title: "marker1" },
            contents: sContent[i],
          });
          labelgeometries.push({
            id: item.id,
            styleId: "label",
            position: new window.TMap.LatLng(item.lat, item.lng),
            content: labelname,
            properties: { title: "label" },
          });
        });

        const maketLyer = new window.TMap.MultiMarker({
          map: map,
          styles: {
            big: new window.TMap.MarkerStyle({
              width: 50,
              height: 64,
              anchor: { x: 26, y: 16 },
              src: imgUrl,
              color: "#333",
              size: 22,
              direction: "bottom",
              strokeColor: "#fff",
              offset: { x: 0, y: 10 },
              strokeWidth: 2,
            }),
          },
          geometries: markergeometries,
        });

        // 简单文本标记
        const label = new window.TMap.MultiLabel({
          id: "001",
          map: map,
          enableCollision: true, //是否开启图层内部的文本标注碰撞
          styles: {
            label: new window.TMap.LabelStyle({
              color: "#ff0000", // 颜色属性
              size: 14, // 文字大小属性
              offset: { x: 0, y: 58 }, // 文字偏移属性单位为像素
              angle: 0, // 文字旋转属性
              alignment: "center", // 文字水平对齐属性
              verticalAlignment: "center", // 文字垂直对齐属性
              // borderWidth:'1',
              // borderRadius:'5',
              // borderColor:'#000000',
              // backgroundColor:'#ffffff'
            }),
          },
          geometries: labelgeometries,
        });

        // 创建信息窗口
        const info = new window.TMap.InfoWindow({
          map,
          position: map.getCenter(),
          offset: { x: 0, y: -32 },
        });
        info.close();
        //console.log(sContent);

        maketLyer.on("click", (event) => {
          //console.log(event)
          info.open();
          info.setPosition(event.geometry.position);
          // var lat = event.latLng.getLat().toFixed(6);
          // var lng = event.latLng.getLng().toFixed(6);
          info.setContent(event.geometry.contents);
        });

        //点击地图事件
        // map.on("click", (evt) => {
        //   //console.log(evt)
        //   info.open();
        //   info.setPosition(evt.latLng);
        //   var lat = evt.latLng.getLat().toFixed(6);
        //   var lng = evt.latLng.getLng().toFixed(6);
        //   info.setContent("当前坐标：" + lat + "," + lng);

        // });
      }
    };
    onBeforeUnmount(() => {
      console.log("****************");
      map.destroy();
    });

    // var timer = setTimeout(function () {
    //   clearMaps();
    //   initMaps();
    // }, 300);

    return {
      form,
      pointJson,
      map,
      getRoadGPCount,
      initMaps,
      //timer,
      earthquakeList: "",
    };
  },
  beforeDestroy() {
    //clearInterval(this.timer);
    // this.timer = null;
    // this.map = null;
  },
  // methods: {
  // 	initMap() {

  // 		const timer = setTimeout(() => {
  // 			const mapcenter = [34.25639, 117.1996];
  // 			creatMarker(mapcenter, this.pointJson);
  // 		}, 300)
  // 	},
  // 	baiduMap() {
  // 		var map = new BMap.Map("allmap"); // 创建地图实例
  // 		map.enableScrollWheelZoom(true); //开启鼠标滚轮缩放
  // 		map.addControl(new BMap.NavigationControl());
  // 		map.addControl(new BMap.ScaleControl());
  // 		map.addControl(new BMap.OverviewMapControl());
  // 		map.addControl(new BMap.MapTypeControl());
  // 		// map.setMapStyle({ style: 'midnight' }) //地图风格

  // 		map.clearOverlays();
  // 		//将数据遍历
  // 		//自定义信息窗口内容
  // 		var sContent =
  // 				'<div style="width:300px;"><p style="font-size:16px;font-weight:bold;margin-top: 10px;color:#D07852">' +
  // 				this.position_NKYYGS.Name +
  // 				'</p><p style="font-size:13px;margin: 5px 0;">地址：' +
  // 				this.position_NKYYGS.address +
  // 				'</p><p style="font-size:13px;margin: 5px 0;">电话：' +
  // 				this.position_NKYYGS.phoneNumber +
  // 				'</p><p style="font-size:13px;margin: 5px 0;">坐标：' +
  // 				this.position_NKYYGS.bdLNG +
  // 				"," +
  // 				this.position_NKYYGS.bdLAT;
  // 		("</div>");
  // 		var point = new BMap.Point(
  // 				this.position_NKYYGS.bdLNG,
  // 				this.position_NKYYGS.bdLAT
  // 		); // 创建点坐标
  // 		var marker = new BMap.Marker(point);
  // 		map.addOverlay(marker);
  // 		map.centerAndZoom(point, 18); // 初始化地图，设置中心点坐标和地图级别
  // 		// //点击图标时候调用信息窗口
  // 		var infoWindow = new BMap.InfoWindow(sContent);
  // 		marker.addEventListener("click", function () {
  // 			this.openInfoWindow(infoWindow);
  // 		});
  // 	},

  // },
  // mounted() {
  // 	this.initMap();
  // },
};
</script>

<style scoped>
.el-row {
  margin-bottom: 20px;
}

.h500 {
  height: 800px !important;
}
.has-map {
  width: 100%;
  height: 100%;
}
.baidumap {
  width: 600px;
  height: 400px;
}
</style>
