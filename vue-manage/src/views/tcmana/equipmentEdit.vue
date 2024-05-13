<template>
  <div>
    <el-row class="mgb20">
      <el-col>
        <el-button type="primary" @click="saveEdit">提交</el-button>
      </el-col>
    </el-row>
    
    <el-row :gutter="20" class="mgb20">
      <el-col :span="12">
        <el-card shadow="hover">
           <template #header>
            <div class="clearfix">
              <span>设备信息</span>
            </div>
          </template>
          <div class="handle-box">
            <el-form size="small">
              <el-form-item label="设备编号">
                <el-input
                  size="small"
                  v-model="result.device.device_code"
                  placeholder=""         
                ></el-input>
              </el-form-item>
              <el-form-item label="设备类型">
                <el-select
                  v-model="result.device.device_type"
                  filterable
                  size="small"
                  placeholder="所有区域"
                  class="w"
                >
                   <el-option v-for="(item,i) in result.device_types" :key="i" :label="item.label" :value="item.dc_value"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="选择区域">
                <el-select
                  v-model="result.device.area_id"
                  filterable
                  size="small"
                  placeholder="所有区域"
                  class="w"
                >
                  <el-option v-for="(item,i) in result.area_list" :key="i" :label="item.area_name" :value="item.id"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="指定街道">
                <el-select
                  v-model="result.device.street_id"
                  filterable
                  size="small"
                  placeholder="所有区域"
                  class="w"
                >
                 <el-option v-for="(item,i) in result.street_list" :key="i" :label="item.street_name" :value="item.id"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="指定路内">
                <el-select
                  v-model="result.device.road_id"
                  filterable
                  size="small"
                  placeholder="所有区域"
                  class="w"
                >
                  <el-option v-for="(item,i) in result.road_list" :key="i" :label="item.road_name" :value="item.id"></el-option>
                </el-select>
              </el-form-item>
            </el-form>
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>
            <div class="clearfix">
              <span>路内位置</span>
            </div>
          </template>
          <el-form label-width="70px" size="small">
            <el-form-item label="路内地址">
              <el-input
                v-model="result.device.address"
                placeholder=""
                type="textarea"
                :rows="8"
              ></el-input>
            </el-form-item>

            <el-form-item label="坐标范围">
              <el-input v-model="result.device.latitude" placeholder=""></el-input>
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
import { getDevice,addDevice,queryAreaData,queryStreetData,queryRoadData,dictData, editDevice } from "../../api/index";
import { useRouter } from "vue-router";



export default {
  name: "equipmentedit",
  components: {},
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
      radio2: "男",
    });
     let result = reactive({
      area_list:[],
      street_list:[],
      road_list:[],
      device_types:[],
      device:{}
    });
     const router = useRouter();
    
    const saveEdit = () => {
      
      console.log(1111);
          
          editDevice(result.device).then((res) => {
            console.log(res);
            ElMessage.success("修改成功");
          }).then(()=>{
            router.push({path:"equipmentList"});
          });
        

      
      
    };
    
    
    

    // 获取表格数据 
    let device_id = reactive({
       id:router.currentRoute.value.query.id
    });
    const getData = () => {

      getDevice(device_id).then((res) => {
        console.log("sss");
        console.log(res.data);
        result.device = res.data;
      });
    };
    getData();
   
    
    const query = reactive({
      device_code:"",
      area_id:"",
      street_id:"",
      pageIndex: 1,
      pageSize: 10,
    });



   
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
    // 获取路内
    const getRoadList = () => {
      queryRoadData(query).then((res) => {
        console.log(res.data);
        result.road_list= res.data;
      });
    };
    getRoadList();
    // 查询区域等级
    let dict = reactive({
      is_del:"0",
      dict_type:"deviceType",
      
    });
    const getDeviceType = () => {
      dictData(dict).then((res) => {
          console.log(res);
          result.device_types=res.data;
        });
    };
    getDeviceType();

    
    console.log();
    


    return { 
      form,
    result,
    saveEdit,
    query 
    };




  },
  methods: {
    initMap() {
      var that = this;
      var searchService,
        geocoder,
        map,
        markersArray = [];
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
    },
  },
  mounted() {
    // this.getData();
    //this.initMap();
  },
};
</script>
