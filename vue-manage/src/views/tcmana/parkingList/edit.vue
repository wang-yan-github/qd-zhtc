<template>
    <el-dialog
      :title="dialogT"
      v-model="editVisible"
      width="650px"
      top="2vh"
      destroy-on-close="true"
      :close-on-click-modal="false"
      @closed="close"
    >
      <el-tabs type="card" v-model="activeName">
        <el-tab-pane label="停车场信息" name="first">
          <div class="mt10"></div>
          <el-form
            label-width="100px"
            size="small"
            :rules="formRules"
            :model="form"
          >
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="停车场编号" prop="park_code">
                  <el-input
                    v-model="form.park_code"
                    placeholder="输入停车场编号"
                  ></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="停车场名称" prop="park_name">
                  <el-input
                    v-model="form.park_name"
                    placeholder="输入停车场名称"
                  ></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="泊位总数" prop="park_num">
                  <!-- <el-radio-group v-model="form.radio2">
                    <el-radio :label="1">男</el-radio>
                    <el-radio :label="2">女</el-radio>
                  </el-radio-group> -->
                  <el-input
                    v-model="form.park_num"
                    type="number"
                    placeholder="泊位总数"
                  ></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="空闲泊位数量" prop="free_count">
                  <!-- <el-radio-group v-model="form.radio2">
                    <el-radio :label="1">男</el-radio>
                    <el-radio :label="2">女</el-radio>
                  </el-radio-group> -->
                  <el-input
                    v-model="form.free_count"
                    type="number"
                    placeholder="空闲泊位数量"
                  ></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="停车场等级" prop="park_grade">
                  <el-select
                    v-model="form.park_grade"
                    placeholder="停车场等级"
                    class="w"
                  >
                    <el-option
                      v-for="(item, i) in park_grades"
                      :key="i"
                      :label="item.label"
                      :value="item.dc_value"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="选择区域" prop="area_id">
                  <el-select
                    v-model="form.area_id"
                    @change="getStreetList('b')"
                    placeholder="所有区域"
                    class="w"
                  >
                    <el-option
                      v-for="(item, i) in area_list"
                      :key="i"
                      :label="item.area_name"
                      :value="item.id"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="指定街道" prop="street_id">
                  <el-select
                    v-model="form.street_id"
                    filterable
                    size="small"
                    placeholder="所有街道"
                    class="w"
                  >
                    <el-option
                      v-for="(item, i) in street_list"
                      :key="i"
                      :label="item.street_name"
                      :value="item.id"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item label="厂商" prop="brand">
                  <el-input
                    v-model="form.brand"
                    placeholder="厂商"
                  ></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item label="包月上限" prop="limit_monthly">
                  <!-- <el-radio-group v-model="form.radio2">
                    <el-radio :label="1">男</el-radio>
                    <el-radio :label="2">女</el-radio>
                  </el-radio-group> -->
                  <el-input
                    v-model="form.limit_monthly"
                    type="number"
                    placeholder="包月上限"
                  ></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item label="停车场地址" prop="address">
                  <el-input
                    v-model="form.address"
                    placeholder="停车场地址"
                  ></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item label="坐标范围">
                  <el-input
                    v-model="form.coordinate"
                    placeholder=""
                    disabled
                  ></el-input>
                  <span class="color999 lh20"
                    >提示：http://lbs.qq.com/tool/getpoint/index.html，以腾讯位置服务坐标拾取为准。</span
                  >
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item label="停车场查询">
                  <el-input v-model="form.searchkey" @keyup.enter.native="searchMaps()"
                    ><template v-slot:append
                      ><el-button @click="searchMaps()"
                        >查询</el-button
                      ></template
                    ></el-input
                  >
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <div class="map">
                  <div id="has-map" class="has-map" />
                </div>
              </el-col>
            </el-row>
          </el-form>
        </el-tab-pane>
        <el-tab-pane label="收费方案" name="second">
          <div class="mt10"></div>
          <el-form
            label-width="150px"
            size="small"
            :rules="charge"
            :model="form"
          >
            <el-form-item label="选择收费方案(蓝牌)" prop="blue_charge_id">
              <el-select
                v-model="form.blue_charge_id"
                filterable
                size="small"
                placeholder="所有收费方案"
                class="w"
              >
                <el-option
                  v-for="(item, i) in chargeProgramme_list"
                  :key="i"
                  :label="item.programme_name"
                  :value="item.id"
                ></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="选择收费方案(绿牌)" prop="green_charge_id">
              <el-select
                v-model="form.green_charge_id"
                filterable
                size="small"
                placeholder="所有收费方案"
                class="w"
              >
                <el-option
                  v-for="(item, i) in chargeProgramme_list"
                  :key="i"
                  :label="item.programme_name"
                  :value="item.id"
                ></el-option>
              </el-select>
            </el-form-item>

            <el-form-item label="选择收费方案(黄牌)" prop="yellow_charge_id">
              <el-select
                v-model="form.yellow_charge_id"
                filterable
                size="small"
                placeholder="所有收费方案"
                class="w"
              >
                <el-option
                  v-for="(item, i) in chargeProgramme_list"
                  :key="i"
                  :label="item.programme_name"
                  :value="item.id"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="close">取 消</el-button>
          <el-button type="primary" @click="saveEdit">确 定</el-button>
        </span>
      </template>
    </el-dialog>
</template>
<script>
    import {
        queryAreaData,
        queryStreetData,
        chargeProgrammeData,
        dictData,
        addPark,
        editPark
    } from "@/api/index";
    export default{
        data(){
            return{
                dialogT:'编辑',
                editVisible:false,
                activeName:'first',
                form:{
                    park_code: "",
                    traffic_park_code: "",
                    park_name: "",
                    park_num: "",
                    park_grade: "",
                    area_id: "",
                    street_id: "",
                    address: "",
                    brand: "",
                    coordinate: "",
                    longitude: "",
                    latitude: "",
                    blue_charge_id: "",
                    green_charge_id: "",
                    yellow_charge_id: "",
                    id: "",
                    searchkey: "",
                    limit_monthly: "",
                    free_count: ""
                },
                formRules: {
                    park_code: [{ required: true, message: "必填项", trigger: "blur" }],
                    park_name: [{ required: true, message: "必填项", trigger: "blur" }],
                    park_num: [{ required: true, message: "必填项", trigger: "blur" }],
                    park_grade: [{ required: true, message: "必填项", trigger: "blur" }],
                    area_id: [{ required: true, message: "必填项", trigger: "blur" }],
                    street_id: [{ required: true, message: "必填项", trigger: "blur" }],
                    address: [{ required: true, message: "必填项", trigger: "blur" }],
                    brand: [{ required: true, message: "必填项", trigger: "blur" }],
                    // coordinate: [{ required: true, message: "必填项", trigger: "blur" }],
                },
                area_list:[],
                chargeProgramme_list:[],
                park_grades:[],
                street_list:[],
                map:'',
                mapWindow:'',
            }
        },
        mounted(){
            // 区域
            queryAreaData().then((res) => {
                this.area_list = res.data;
            });
             // 获取收费方案
            chargeProgrammeData().then((res) => {
                this.chargeProgramme_list = res.data;
            });
            // 查询区域等级
            dictData({is_del: "0",dict_type: "parkGrade"}).then((res) => {
                this.park_grades = res.data;
            });
        },
        methods:{
            open(){
                this.editVisible = true;
                this.$nextTick(()=>{
                    this.initMap();
                })
            },
            close(){
                this.map.destroy();
                this.mapWindow = '';
                this.editVisible = false;
            },
            saveEdit(){
                // 不可为空
                if (
                    this.form.park_code == null ||
                    this.form.park_code == "" ||
                    this.form.park_name == null ||
                    this.form.park_name == "" ||
                    this.form.park_num == null ||
                    this.form.park_num == "" ||
                    this.form.park_grade == null ||
                    this.form.park_grade == "" ||
                    this.form.area_id == null ||
                    this.form.area_id == "" ||
                    this.form.street_id == null ||
                    this.form.street_id == "" ||
                    this.form.address == null ||
                    this.form.address == "" ||
                    this.form.brand == null ||
                    this.form.brand == "" ||
                    this.form.coordinate == null ||
                    this.form.coordinate == "" ||
                    this.form.blue_charge_id == null ||
                    this.form.blue_charge_id == "" ||
                    this.form.green_charge_id == null ||
                    this.form.green_charge_id == "" ||
                    this.form.yellow_charge_id == null ||
                    this.form.yellow_charge_id == ""
                ) {
                    this.$message.error("参数不可为空！");
                    return false;
                }

                this.form.longitude = this.form.coordinate.split(",")[1];
                this.form.latitude = this.form.coordinate.split(",")[0];

                if (!idx) {
                    addPark(this.form)
                    .then((res) => {
                        // console.log(res.data);
                         this.$message.success(res.msg);
                    })
                    .then(() => {
                        query.pageIndex = 1;
                        getData();
                    });
                } else {
                    this.form.id = idx;
                    editPark(this.form)
                    .then((res) => {
                        // console.log(res);
                         this.$message.success(res.msg);
                    })
                    .then(() => {
                        getData();
                    });
                }

                editVisible.value = false;
            },
            initMap(){
                if (document.getElementById("has-map")) {
                    var point = [34.24277, 117.184547];
                    if (this.form.coordinate != "" && this.form.coordinate != null) {
                        point[0] = this.form.coordinate.split(",")[0];
                        point[1] = this.form.coordinate.split(",")[1];
                    }
                    const center = new window.TMap.LatLng(point[0], point[1]);
                    // 初始化地图
                    this.map = new window.TMap.Map(document.getElementById("has-map"), {
                        rotation: 0, // 设置地图旋转角度
                        pitch: 0, // 设置俯仰角度（0~45）
                        zoom: 16, // 设置地图缩放级别
                        center: center, // 设置地图中心点坐标
                    });
                    var geocoder = new window.TMap.service.Geocoder(); // 新建一个正逆地址解析类
                    geocoder.getAddress({ location: center }).then((result) => {// 将给定的坐标位置转换为地址
                        this.form.searchkey = result.result.address;
                    });
                    var markers = new window.TMap.MultiMarker({
                        map: this.map,
                        geometries: [
                            {
                                id: "main",
                                position: center, // 将得到的坐标位置用点标记标注在地图上
                            },
                        ],
                    });
                    // 创建信息窗口
                    this.mapWindow = new window.TMap.InfoWindow({
                        map:this.map,
                        position: this.map.getCenter(),
                        offset: { x: 0, y: -52 },
                    });
                    this.mapWindow.close();
                    //点击地图事件
                    this.map.on("click", (evt) => {
                        this.mapWindow.open();
                        this.mapWindow.setPosition(evt.latLng);
                        var lat = evt.latLng.getLat().toFixed(6);
                        var lng = evt.latLng.getLng().toFixed(6);
                        this.mapWindow.setContent("当前坐标：" + lat + "," + lng);
                        this.form.coordinate = lat + "," + lng;
                        markers.setGeometries([]); // 将给定的地址转换为坐标位置
                        markers.updateGeometries([
                            {
                            id: "main",
                            position: evt.latLng, // 将得到的坐标位置用点标记标注在地图上
                            },
                        ]);
                        geocoder.getAddress({ location: evt.latLng }) // 将给定的坐标位置转换为地址
                        .then((result) => {
                            this.form.searchkey = result.result.address;// 显示查询到的地址
                        });
                    });
                };
            },
            searchMaps(){
                if (!this.form.searchkey) {
                    this.$message.warning("查询地址不能为空");
                } else {
                    if (this.form.searchkey.indexOf("徐州") == -1) {
                        this.form.searchkey = "徐州" + this.form.searchkey;
                    }
                    var geocoder = new window.TMap.service.Geocoder(); // 新建一个正逆地址解析类
                    geocoder.getLocation({ address: this.form.searchkey }).then((result) => {
                        var markers = new window.TMap.MultiMarker({
                            map: this.map,
                            geometries: [
                                {
                                    id: "main",
                                    position: result.result.location, // 将得到的坐标位置用点标记标注在地图上
                                },
                            ],
                        });
                        markers.updateGeometries([
                            {
                                id: "main",
                                position: result.result.location, // 将得到的坐标位置用点标记标注在地图上
                            },
                        ]);
                        this.mapWindow.open();
                        this.mapWindow.setPosition(result.result.location);
                        var lat = result.result.location.getLat().toFixed(6);
                        var lng = result.result.location.getLng().toFixed(6);
                        this.mapWindow.setContent("当前坐标：" + lat + "," + lng);
                        this.map.setCenter(result.result.location);
                        this.form.coordinate = result.result.location.toString();
                    });
                }
            },
            getStreet(type){
                 // 获取街道
                let queryStreet = reactive({
                    areaId: query.area_id,
                });
                if (type == "a") {
                    queryStreet.areaId = query.area_id;
                    query.street_id = "";
                } else if (type == "b") {
                    queryStreet.areaId = form.area_id;
                    form.street_id = "";
                } else {
                    queryStreet.areaId = form.area_id;
                }
                queryStreetData(queryStreet).then((res) => {
                    // console.log(res);
                    if (type == "a") {
                    result.query_street_list = res.data;
                    } else {
                    result.street_list = res.data;
                    }
                    // console.log(res.data);
                });
            }
        }
    }
</script>
