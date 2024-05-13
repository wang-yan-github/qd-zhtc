<template>
  <div>
      <el-row>
          <el-col>
              <el-button type="primary"  @click="saveEdit">提交</el-button>
          </el-col>
      </el-row>
    <el-row :gutter="20">
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>
            <div class="clearfix">
              <span>路内信息</span>
            </div>
          </template>
          <el-form label-width="70px" size="small">
            <el-form-item label="路内名称">
              <el-input v-model="form.road_name"></el-input>
            </el-form-item>
            <el-form-item label="路内等级">
              <el-select
                v-model="form.road_level"
                filterable
                size="small"
                placeholder="所有等级"
                class="w"
              >
                 <el-option v-for="(item,i) in result.road_levels" :key="i" :label="item.label" :value="item.dc_value"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="所属区域">
              <el-select
                v-model="form.area_id"
                filterable
                size="small"
                placeholder="所有区域"
                class="w"
              >
                 <el-option v-for="(item,i) in result.area_list" :key="i" :label="item.area_name" :value="item.id"></el-option>
              </el-select>
              <span class="color999"
                >此处为区域管理中区域数据，还没有您要的区域，请先添加区域！</span
              >
            </el-form-item>

            <el-form-item label="所属街道">
              <el-select
                v-model="form.street_id"
                filterable
                size="small"
                placeholder="所有街道"
                class="w"
              >
                 <el-option v-for="(item,i) in result.street_list" :key="i" :label="item.street_name" :value="item.id"></el-option>
              </el-select>
              <span class="color999"
                >此处为街道管理中街道数据，还没有您要的街道，请先添加街道！</span
              >
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover" class="h500">
          <template #header>
            <div class="clearfix">
              <span>收费方案</span>
            </div>
          </template>
          <el-form label-width="150px" label-position="left">
            <el-form-item label="选择收费方案(蓝牌)">
              <el-select
                v-model="form.blue_charge_id"
                filterable
                size="small"
                placeholder="所有区域"
                class="w"
              >
              <el-option v-for="(item,i) in result.chargeProgramme_list" :key="i" :label="item.programme_name" :value="item.id"></el-option>
                
              </el-select>
            </el-form-item>
            <el-form-item label="选择收费方案(绿牌)">
              <el-select
                v-model="form.green_charge_id"
                filterable
                size="small"
                placeholder="所有区域"
                class="w"
              >
                 <el-option v-for="(item,i) in result.chargeProgramme_list" :key="i" :label="item.programme_name" :value="item.id"></el-option>
              </el-select>
            </el-form-item>

            <el-form-item label="选择收费方案(黄牌)">
              <el-select
                v-model="form.yellow_charge_id"
                filterable
                size="small"
                placeholder="所有区域"
                class="w"
              >
                 <el-option v-for="(item,i) in result.chargeProgramme_list" :key="i" :label="item.programme_name" :value="item.id"></el-option>
              </el-select>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
      <!-- <el-col :span="8">
        <el-card shadow="hover">
          <template #header>
            <div class="clearfix">
              <span>路内位置</span>
            </div>
          </template>
          <el-form label-width="70px" size="small">
            <el-form-item label="路内地址">
              <el-input
                v-model="form.name"
                placeholder=""
                type="textarea"
                :rows="8"
              ></el-input>
            </el-form-item>

            <el-form-item label="坐标范围">
              <el-input v-model="form.name" placeholder=""></el-input>
              <span class="color999"
                >操作提示：http://lbs.qq.com/tool/getpoint/index.html，以腾讯位置服务坐标拾取为准。</span
              >
            </el-form-item>
          </el-form>
          <el-form size="small">
            <el-form-item>
              <el-input v-model="form.name"
                ><template v-slot:append
                  ><el-button>查询</el-button></template
                ></el-input
              >
            </el-form-item>
          </el-form>          
        </el-card>
      </el-col> -->
    </el-row>
    <el-row>
      <el-col :span="24">
        <el-card shadow="hover">
          <template #header>
            <div class="clearfix">
              <span>路内位置</span>
            </div>
          </template>
          <el-form label-width="70px" size="small">
            <el-form-item label="路内地址">
              <el-input
                v-model="form.address"
                placeholder=""
                type="textarea"
                :rows="8"
              ></el-input>
            </el-form-item>

            <el-form-item label="坐标范围">
              <el-input v-model="form.longitude" placeholder=""></el-input>
              <span class="color999"
                >操作提示：http://lbs.qq.com/tool/getpoint/index.html，以腾讯位置服务坐标拾取为准。</span
              >
            </el-form-item>
          </el-form>
          <el-form size="small">
            <el-form-item>
              <el-input v-model="form.name"
                ><template v-slot:append
                  ><el-button>查询</el-button></template
                ></el-input
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
import { getDevice,addRoad,queryAreaData,queryStreetData,chargeProgrammeData,dictData, editDevice } from "../../api/index";
import { useRouter } from "vue-router";
export default {
  name: "roadadd",
  setup() {
    let form = reactive({
      road_name:"",
      road_level:"",
      area_id:"",
      street_id:"",
      blue_charge_id:"",
      green_charge_id:"",
      yellow_charge_id:"",
      longitude:"",
      address:""
    });

    let result = reactive({
      area_list:[],
      street_list:[],
      road_levels:[],
      chargeProgramme_list:[]
    });

    const query = reactive({
      area_id:"",
      street_id:"",
    });
    const router = useRouter();


    // 获取收费方案
    const getChargeProgrammeData = () => {
      chargeProgrammeData(query).then((res) => {
        // console.log(res);
         result.chargeProgramme_list= res.data;
        console.log(res.data);
      });
    };
    getChargeProgrammeData();
   
    // 获取区域
    const getAreaList = () => {
      queryAreaData(query).then((res) => {
        // console.log(res);
         result.area_list= res.data;
        console.log(res.data);
      });
    };
    getAreaList();
    // 获取街道
    const getStreetList = () => {
      queryStreetData(query).then((res) => {
        // console.log(res);
         result.street_list= res.data;
        console.log(res.data);
      });
    };
    getStreetList();
    // 查询区域等级
    let dict = reactive({
      is_del:"0",
      dict_type:"roadLevel",
      
    });
    const getRoadLevel = () => {
      dictData(dict).then((res) => {
          console.log(res);
          result.road_levels=res.data;
        });
    };
    getRoadLevel();


    const saveEdit = () => {
      
      console.log(1111);
          
          addRoad(form).then((res) => {
            console.log(res);
            ElMessage.success("添加成功");
          }).then(()=>{
            router.push({path:"roadList"});
          });
        

      
      
    };
    


    return {
      form,
      result,
      query,
      saveEdit,
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
