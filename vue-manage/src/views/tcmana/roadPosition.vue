<template>
  <div>
    <el-row :gutter="20">     
        <el-col :span="24">
        <el-card shadow="hover">       
          <el-form label-width="70px" size="small" inline>
            <el-form-item label="泊位号">
              <el-input
                v-model="form.name"
                placeholder=""
                class="w100"
              ></el-input>
            </el-form-item>
            <el-form-item label="坐标">
              <el-input
                v-model="form.name"
                placeholder=""
                class="w100"
              ></el-input>
              <span class="ml10"></span>
              <el-input
                v-model="form.name"
                placeholder=""
                class="w100"
              ></el-input>
            </el-form-item>

            <el-form-item label="">
              <el-button type="primary" icon="el-icon-circle-plus-outline">添加车位点</el-button>
            </el-form-item>
            <el-form-item label="">
              
              <span class="color999"
                > 拖动标记可重新设定位置</span
              >
            </el-form-item>
          </el-form>
          <div id="mapDiv"></div>     
        </el-card>
      </el-col>
    </el-row>

  </div>
</template>

<script>
import { ref, reactive } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { useRouter } from "vue-router";
export default {
  name: "roadedit",
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

    return {
      form,
      map: null,
      latlng:[{lat:'',lng:''}],
      alllng:"",
    };
  },
  methods: {
      // 地图初始化函数，本例取名为init，开发者可根据实际情况定义
    initMap() {
        var that=this;
      var searchService,geocoder,map,markersArray = [];
      //console.log(window); // 通过window获取
      var center = new window.TMap.LatLng(39.98412, 116.307484);
      //初始化地图
      var map = new window.TMap.Map("mapDiv", {
        zoom: 18, //设置地图缩放级别
        center: center, //设置地图中心点坐标
        baseMap: {
          // 设置卫星地图
          //type: "satellite",
        },
      });
      //console.log(map);
    //   var latlngBounds = new window.TMap.LatLngBounds();

    //    window.TMap.event.addListener(map, "click", function(event) {
    //         //console.log(event);
    //         that.latlng.lng = event.latLng.getLng(); // 经度
    //         that.latlng.lat = event.latLng.getLat(); // 纬度
    //         that.alllng=event.latLng.getLng()+","+event.latLng.getLat();

    //         if (markersArray) {
    //           for (let i in markersArray) {
    //             markersArray[i].setMap(null);
    //           }
    //         }
    //         var marker = new window.TMap.Marker({
    //           map: map,
    //           position: event.latLng
    //         });
    //         markersArray.push(marker);
    //       });
    },
    
  },
  mounted() {
      //this.initMap();
  },
};
</script>

<style scoped>
.el-row {
  margin-bottom: 20px;
}
.h500{height:360px;}
</style>
