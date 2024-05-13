<template>
  <div>
    <el-row>
      <el-col :span="24">
        <div class="container">
          <div class="handle-box">
            <div class="left-panel">
              <el-form inline label-width="80" size="small">
                <el-form-item label="泊位号">
                  <el-input
                    @keyup.enter="handleSearch()"
                    size="small"
                    v-model="query.berth_code"
                    placeholder="泊位号"
                    class="w100"
                  ></el-input>
                </el-form-item>
                <el-form-item label="市区">
                  <el-select
                    clearable
                    v-model="query.area_id"
                    filterable
                    size="small"
                    placeholder="所有区域"
                    class="w100"
                    @change="getStreet"
                  >
                    <el-option
                      v-for="item in formqjl.areas"
                      :key="item.id"
                      :label="item.area_name"
                      :value="item.id"
                    ></el-option>
                  </el-select>
                </el-form-item>
                <el-form-item label="街道">
                  <el-select
                    clearable
                    v-model="query.street_id"
                    filterable
                    size="small"
                    placeholder="所有街道"
                    class="w100"
                    @change="getRoad"
                  >
                    <el-option
                      v-for="item in formqjl.streets"
                      :key="item.id"
                      :label="item.street_name"
                      :value="item.id"
                    ></el-option>
                  </el-select>
                </el-form-item>
                <el-form-item label="路内">
                  <el-select
                    clearable
                    v-model="query.road_id"
                    filterable
                    size="small"
                    placeholder="所有路内"
                    class="w100"
                  >
                    <el-option
                      v-for="item in formqjl.roads"
                      :key="item.id"
                      :label="item.road_name"
                      :value="item.id"
                    ></el-option>
                  </el-select>
                </el-form-item>
                <!-- <el-form-item label="状态">
                                    <el-select
                                            clearable
                                            v-model="query.sbstatu"
                                            filterable
                                            size="small"
                                            placeholder="所有状态"
                                            class="w100"
                                    >
                                        <el-option
                                                v-for="item in form.czroptions"
                                                :key="item.value"
                                                :label="item.label"
                                                :value="item.value"
                                        ></el-option>
                                    </el-select>
                                </el-form-item> -->

                <el-form-item label="">
                  <el-button
                    size="small"
                    type="primary"
                    icon="el-icon-search"
                    @click="handleSearch"
                    >查询
                  </el-button>
                </el-form-item>
                <el-form-item label="">
                  <el-button
                    size="small"
                    type="warning"
                    icon="el-icon-refresh"
                    @click="qctj"
                    >清空
                  </el-button>
                </el-form-item>
              </el-form>
            </div>
            <div class="right-panel">
              <ul class="tips-icon">
                <li>
                  <span>设备状态：</span>
                  <el-tooltip
                    class="item"
                    effect="dark"
                    content="设备空闲"
                    placement="top"
                  >
                    <i class="status-i kx-status">空闲</i>
                  </el-tooltip>
                  <el-tooltip
                    class="item"
                    effect="dark"
                    content="设备使用中"
                    placement="top"
                  >
                    <i class="status-i sy-status">使用中</i>
                  </el-tooltip>
                  <el-tooltip
                    class="item"
                    effect="dark"
                    content="设备异常"
                    placement="top"
                  >
                    <i class="status-i yc-status">异常</i>
                  </el-tooltip>
                </li>
                <li>
                  <span>设备/车牌识别异常：</span
                  ><i class="fa fa-bookmark abnormal"></i>
                </li>
                <li>
                  <span>设备正常：</span><i class="fa fa-bookmark normal"></i>
                </li>
              </ul>
            </div>

            <div class="clear"></div>
          </div>

          <ul class="card-bgimg">
            <li>
              <h4>{{ census.normal }}</h4>
              <h6>泊位总数</h6>
            </li>
            <li>
              <h4>{{ census.parked }}</h4>
              <h6>在停车辆数</h6>
            </li>
            <li>
              <h4>{{ census.zxamount }}</h4>
              <h6>在线泊位数</h6>
            </li>
            <li>
              <h4>{{ census.free }}</h4>
              <h6>空余泊位数</h6>
            </li>
            <li>
              <h4>{{ census.dxamount }}</h4>
              <h6>断线泊位数</h6>
            </li>
            <li>
              <h4>{{ census.utilizeRatio }}</h4>
              <h6>泊位饱和度(%)</h6>
            </li>
          </ul>
          <el-row :gutter="20" class="card-row">
            <el-col
              v-for="item in deviceList"
              :span="4"
              :key="item"
              @click="getRoadParkingOrderInfo(item, $event)"
            >
              <div class="p-card">
                <div class="p-card-con">
                  <!-- 会员：member  非会员：nonmember  欠费：Arrears 包月：monthly-->
                  <p v-if="item.ishy == 1" class="p-card-meb member">会员</p>
                  <p v-if="item.ishy == 0" class="p-card-meb nonmember">
                    非会员
                  </p>

                  <!-- 欠费金额 -->
                  <p v-if="item.amount != null" class="arrears">
                    历史欠费：{{ item.amount }}元
                  </p>

                  <!-- 状态 正常：normal  异常：abnormal  人工：artificial -->
                  <i
                    v-if="item.sbstatu == '1' || item.sbstatu == '2'"
                    class="fa fa-bookmark p-card-mark normal"
                    aria-hidden="true"
                  ></i>
                  <i
                    v-if="item.sbstatu == '3' || item.type == '2'"
                    class="fa fa-bookmark p-card-mark abnormal"
                    aria-hidden="true"
                  ></i>
                  <div class="car-num">
                    <span class="car-num-a"></span>
                    <span class="car-num-b"></span>
                    <span class="car-num-c"></span>
                    <span class="car-num-d"></span>
                    <p v-if="item.order_no == null && item.car_no == null"></p>
                    <p v-if="item.order_no != null && item.car_no != null">
                      {{ item.car_no }}
                    </p>
                    <p v-if="item.order_no != null && item.car_no == null">
                      未知车牌
                    </p>
                  </div>
                  <!--使用中-->
                  <h4 class="park-num" v-if="item.sbstatu == '2'">
                    {{ item.berth_code }}
                  </h4>
                  <!--空闲-->
                  <h4 class="park-num park-num-kx" v-if="item.sbstatu == '1'">
                    {{ item.berth_code }}
                  </h4>
                  <!--异常-->
                  <h4 class="park-num park-num-yc" v-if="item.sbstatu == '3'">
                    {{ item.berth_code }}
                  </h4>

                  <div v-if="item.isby == '1'" class="month-tip"></div>
                </div>
              </div>
            </el-col>
          </el-row>
          <div class="pagination">
            <el-pagination
              background
              layout="total, prev, pager, next"
              :current-page="query.pageNum"
              :page-size="query.pageSize"
              :total="pageTotal"
              @current-change="handlePageChange"
            ></el-pagination>
          </div>
        </div>
      </el-col>
    </el-row>
    <!-- 详情弹出框 -->
    <el-dialog
      :title="queryInfo.car_no_title + ' 驶入信息'"
      v-model="dialogVisible"
      width="990px"
    >
      <el-descriptions class="margin-top" title="" :column="2" border>
        <el-descriptions-item>
          <template v-slot:label>
            <i class="el-icon-tickets"></i> 订单号
          </template>
          {{ rpodInfo.orderNo }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template v-slot:label>
            <i class="el-icon-collection-tag"></i> 订单状态
          </template>
          {{ rpodInfo.statusStr }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template v-slot:label>
            <i class="el-icon-time"></i> 停留时间
          </template>
          {{ rpodInfo.resitime }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template v-slot:label> <i class="el-icon-money"></i> 费用 </template>
          {{ rpodInfo.sumAmount }}元
        </el-descriptions-item>
        <el-descriptions-item>
          <template v-slot:label>
            <i class="el-icon-location-information"></i> 停车地点
          </template>
          {{ rpodInfo.roadName }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template v-slot:label>
            <i class="el-icon-place"></i> 泊位号
          </template>
          {{ rpodInfo.berth }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template v-slot:label>
            <i class="el-icon-c-scale-to-original"></i> 车牌号码
          </template>
          <div v-show="isShowInfo">
            <span>{{ rpodInfo.carNo }}</span>
            <el-button
              size="small"
              class="ml5"
              type="primary"
              icon="el-icon-edit"
              @click="carUpdDate"
              >修改车牌号码</el-button
            >
          </div>
          <div v-show="isShow">
            <el-input
              v-model="rpodInfo.carNo"
              class="w100"
              size="small"
            ></el-input>
            <el-select
              v-model="rpodInfo.car_type"
              filterable
              size="small"
              placeholder="车牌分类"
              class="w100 ml5"
            >
              <el-option
                v-for="item in carType"
                :key="item.dc_value"
                :label="item.label"
                :value="item.dc_value"
              ></el-option>
            </el-select>
            <el-button
              size="small"
              class="ml5"
              type="primary"
              icon="el-icon-check"
              @click="changeisShowInfo(1)"
              >确定
            </el-button>
            <el-button
              size="small"
              icon="el-icon-close"
              @click="changeisShowInfo(2)"
              >取消</el-button
            >
          </div>
        </el-descriptions-item>
        <el-descriptions-item>
          <template v-slot:label>
            <i class="el-icon-postcard"></i> 车牌属性
          </template>
          {{ rpodInfo.rosterTypeStr }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template v-slot:label>
            <i class="el-icon-mobile-phone"></i> 车主手机号
          </template>
          {{ rpodInfo.phone }}
        </el-descriptions-item>

        <el-descriptions-item>
          <template v-slot:label>
            <i class="el-icon-user"></i> 巡检员
          </template>
          {{ rpodInfo.inspectName }}
        </el-descriptions-item>

        <el-descriptions-item>
          <template v-slot:label>
            <i class="el-icon-time"></i> 创建时间
          </template>
          {{ rpodInfo.createTime }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template v-slot:label>
            <i class="el-icon-bank-card"></i> 订单来源
          </template>
          {{ rpodInfo.sourceStr }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template v-slot:label>
            <i class="el-icon-time"></i> 入场时间
          </template>
          {{ rpodInfo.driveinTime }}
        </el-descriptions-item>

        <el-descriptions-item>
          <template v-slot:label>
            <i class="el-icon-picture-outline"></i> 车辆进场照片
          </template>
          <template v-for="(item, i) in rpodInfo.images" :key="i">
            <el-image
              style="width: 60px; height: 60px; margin-right: 10px"
              hide-on-click-modal="true" preview-teleported="true"
              :src="imgurl(item)"
              class="ml5"
              :preview-src-list="[imgurl(item)]"
            >
            </el-image>
          </template>
          <!-- <el-image v-for="item in rpodInfo.images"
                              style="width: 100px; height: 100px;margin-right: 10px;"
                              :src="imgurl(item)"
                              :preview-src-list="[imgurl(rpodInfo.images)]"
                    >
                    </el-image> -->
        </el-descriptions-item>

        <el-descriptions-item
          v-if="rpodInfo.arrearage != null && rpodInfo.arrearage > 0"
        >
          <template v-slot:label>
            <i class="el-icon-wallet"></i> 历史欠费
          </template>
          {{ rpodInfo.arrearage }}元
          <span class="linktext" @click="handleArrearages">（查看列表）</span>
        </el-descriptions-item>
      </el-descriptions>
      <div style="margin-top: 20px">
        <el-collapse-transition>
          <div v-show="showMoneyH" class="history-box">
            <el-divider content-position="left"
              ><h4 class="history-tit">
                <i class="el-icon-menu"></i>历史欠费
              </h4></el-divider
            >
            <el-table :data="arrears" style="width: 100%">
              <el-table-column prop="orderNo" label="订单号" width="180">
              </el-table-column>
              <el-table-column prop="roadName" label="欠费路内">
              </el-table-column>
              <el-table-column prop="driveinTime" width="180" label="驶入时间">
              </el-table-column>
              <el-table-column prop="driveoutTime" width="180" label="驶出时间">
              </el-table-column>
              <el-table-column prop="unpaidAmount" width="100" label="欠费金额">
              </el-table-column>
            </el-table>
            <div class="pagination">
              <el-pagination
                background
                layout="total, prev, pager, next"
                :current-page="infoPage.pageIndex"
                :page-size="infoPage.pageSize"
                :total="infoPage.total"
                @current-change="handleInfoPageChange"
              ></el-pagination>
            </div>
          </div>
        </el-collapse-transition>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import Schart from "vue-schart";
// 全局引入
import { reactive, ref } from "vue";
import { ElMessage } from "element-plus";
import {
  deviceInfoList,
  selectRoadDataList,
  getRoadsectmonitorStatisticsList,
  queryAreaData,
  queryRoadData,
  queryStreetData,
  roadParkingOrderInfo,
  dictData,
} from "../../api/index";
import {
  roadParkingOrderCarData,
  getArrearsList,
} from "../../api/roadParkingOrder.js";
import File_URL from "../../file_url";
export default {
  name: "realparking",
  components: {
    Schart,
  },
  data() {
    return {
      //dialogVisible: false
      // isShow: false,
      // isShowInfo: true,
    };
  },
  setup() {
    const dialogVisible = ref(false);
    const query = reactive({
      berth_code: "",
      str2: "",
      variance: "",
      variance2: "",
      variance3: "",
      area_id: "",
      street_id: "",
      road_id: "",
      pageNum: 1,
      pageSize: 18,
    });

    const infoPage = reactive({
      pageIndex: 1,
      pageSize: 10,
      total: 0,
      carnoId: "",
    });

    const name = localStorage.getItem("ms_username");
    const role = name === "admin" ? "超级管理员" : "普通用户";
    let form = reactive({
      czroptions: [
        {
          value: "1",
          label: "在线",
        },
        {
          value: "2",
          label: "断线",
        },
      ],
    });
    let formqjl = reactive({
      time: "",
      areaId: "",
      streetId: "",
      roadId: "",
      areas: [],
      streets: [],
      roads: [],
    });
    const census = reactive({
      inuse: "",
      free: "",
      parked: "",
      zxamount: "",
      dxamount: "",
      utilizeRatio: "",
    });
    const deviceList = ref([]);
    const rpodInfo = ref({});
    let isShow = ref(false);
    let isShowInfo = ref(true);

    // 历史费用
    const showMoneyH = ref(false);

    const pageTotal = ref(0);
    const queryInfo = reactive({
      berth_code: "",
      pageType: "",
      str2: "",
      car_no_title: "",
    });

    const carType = ref([]);
    const defaultCarType = ref("");
    //字典列表
    dictData(reactive({ dict_type: "car_type" })).then((res) => {
      carType.value = res.data;
      defaultCarType.value = res.data[0].value;
    });

    const arrears = ref([]);
    const handleArrearages = () => {
      showMoneyH.value = !showMoneyH.value;
      getArrearages();
    };
    const getArrearages = () => {
      getArrearsList(infoPage).then((res) => {
        arrears.value = res.data.list;
        infoPage.total = res.data.total;
      });
    };
    //获取区域下拉框数据
    const queryArea = reactive({});
    const getArea = () => {
      queryAreaData(queryArea).then((res) => {
        formqjl.areas = res.data;
      });
    };
    getArea();
    //获取街道下拉框数据
    let queryStreet = reactive({
      areaId: query.area_id,
    });
    const getStreet = () => {
      queryStreet.areaId = query.area_id;
      query.street_id = "";
      query.road_id = "";
      queryStreetData(queryStreet).then((res) => {
        formqjl.streets = res.data;
      });
    };
    //获取路内下拉框数据
    const queryRoad = reactive({
      streetId: query.street_id,
    });
    const getRoad = () => {
      queryRoad.streetId = query.street_id;
      query.road_id = "";
      queryRoadData(queryRoad).then((res) => {
        formqjl.roads = res.data;
      });
    };

    //泊位使用率
    function getPercent(num, total) {
      /// <summary>
      /// 求百分比
      /// </summary>
      /// <param name="num">当前数</param>
      /// <param name="total">总数</param>
      num = parseFloat(num);
      total = parseFloat(total);
      if (isNaN(num) || isNaN(total)) {
        return "-";
      }
      return total <= 0
        ? "0%"
        : Math.round((num / total) * 10000) / 100.0 + "%";
    }

    //在线泊位、断线泊位数
    const getDeviceStatistics = () => {
      getRoadsectmonitorStatisticsList(query).then((res) => {
        census.normal = res.data.normal; //总泊位数
        census.parked = res.data.abnormal; //在停车位
        census.free = res.data.inuse; //空闲车位
        census.zxamount = res.data.zxamount; //在线数
        census.dxamount = res.data.dxamount; //异常设备
        census.utilizeRatio = getPercent(census.parked, census.normal);
      });
    };
    //获取表格数据
    const getDeviceInfo = () => {
      selectRoadDataList(query).then((res) => {
        deviceList.value = res.data.list;
        pageTotal.value = res.data.total;
      });
    };

    getDeviceInfo();
    getDeviceStatistics();

    // 清楚条件
    const qctj = () => {
      (query.berth_code = ""),
        (query.str2 = ""),
        (query.variance = ""),
        (query.variance2 = ""),
        (query.variance3 = ""),
        (query.area_id = ""),
        (query.street_id = ""),
        (query.road_id = ""),
        (query.pageNum = 1);
      getDeviceInfo();
      getDeviceStatistics();
    };
    // 查询操作
    const handleSearch = () => {
      query.pageNum = 1;
      getDeviceInfo();
      getDeviceStatistics();
    };
    // 分页导航
    const handlePageChange = (val) => {
      query.pageNum = val;
      getDeviceInfo();
    };
    const handleInfoPageChange = (val) => {
      infoPage.pageIndex = val;
      getArrearages();
    };

    //详情
    const getRoadParkingOrderInfo = (data, event) => {
      isShow.value = false;
      isShowInfo.value = true;
      showMoneyH.value = false;
      infoPage.pageIndex = 1;
      infoPage.total = 0;
      if (data.rpId != null && data.rpId != undefined) {
        queryInfo.berth_code = data.order_no;
        queryInfo.str2 = data.rpId;
        queryInfo.pageType = data.type;
        roadParkingOrderInfo(queryInfo).then((res) => {
          var qiData = res.data;
          qiData.arrearage = data.amount;
          if (
            qiData.carNo == null ||
            qiData.carNo == undefined ||
            qiData.carNo == ""
          ) {
            qiData.carNo = "";
          }
          rpodInfo.value = qiData;
          queryInfo.car_no_title = rpodInfo.value.carNo;

          if (
            rpodInfo.value.car_type == null ||
            rpodInfo.value.car_type == "" ||
            rpodInfo.value.car_type == undefined
          ) {
            rpodInfo.value.car_type = defaultCarType.value;
          } else {
            rpodInfo.value.car_type = rpodInfo.value.car_type + "";
          }
          infoPage.carnoId = qiData.carnoId;
        });
        dialogVisible.value = true;
        showMoneyH.value = false;
      } else {
        queryInfo.berth_code = "";
        queryInfo.str2 = "";
        queryInfo.pageType = "";
        dialogVisible.value = false;
        showMoneyH.value = true;
      }
    };

    return {
      name,
      query,
      form,
      carType,
      dialogVisible,
      pageTotal,
      arrears,
      qctj,
      handleSearch,
      handlePageChange,
      getArrearages,
      handleArrearages,
      deviceList,
      census,
      formqjl,
      queryArea,
      queryStreet,
      queryRoad,
      getArea,
      getStreet,
      getDeviceInfo,
      getRoad,
      getRoadParkingOrderInfo,
      handleInfoPageChange,
      rpodInfo,
      queryInfo,
      infoPage,
      isShow,
      isShowInfo,
      showMoneyH,
      role,
      url: "https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg",
      srcList: [
        "https://fuss10.elemecdn.com/8/27/f01c15bb73e1ef3793e64e6b7bbccjpeg.jpeg",
        "https://fuss10.elemecdn.com/8/27/f01c15bb73e1ef3793e64e6b7bbccjpeg.jpeg",
      ],
    };
  },
  methods: {
    init() {},
    //图片回显
    imgurl: function (url) {
      console.log(url);
      if (url != "" && url != null) {
        return File_URL.file_hx_img_url + url;
      }
    },
    //修改车牌(车牌id,新车牌号，车牌类型(下拉选项))
    carUpdDate() {
      this.isShow = !this.isShow; //取反
      this.isShowInfo = !this.isShowInfo; //取反
    },
    // 修改车牌确定取消
    changeisShowInfo(val) {
      console.log(val);
      let _this = this;
      this.isShow = !this.isShow; //取反
      this.isShowInfo = !this.isShowInfo; //取反
      if (val == 1) {
        roadParkingOrderCarData(
          reactive({
            id: _this.rpodInfo.id,
            car_id: _this.rpodInfo.carnoId,
            car_no: _this.rpodInfo.carNo,
            car_type: _this.rpodInfo.car_type,
          })
        ).then((res) => {
          console.log(res);
          if (res.code == 0) {
            ElMessage.success(res.msg);
            _this.dialogVisible = false;
            _this.getDeviceInfo();
          } else {
            ElMessage.error(res.msg);
          }
        });
      }
    },
  },
  mounted() {
    this.init();
  },
};
</script>

<style scoped>
.handle-box {
  border-bottom: 2px solid #e4e5e6;
}

.card-row .el-col {
  margin-bottom: 1.25rem;
}

.p-card {
  padding: 8px;
  border-radius: 4px;
  border: 1px solid #f6f8fc;
  background-color: #fff;
  overflow: hidden;
  color: #303133;
  -webkit-transition: 0.3s;
  transition: 0.3s;
  box-shadow: 0 0px 4px 0 rgba(0, 0, 0, 0.1);
  cursor: pointer;
}

.p-card:hover {
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.p-card .p-card-con {
  padding-top: 3.5rem;
  position: relative;
  border: 1px solid #eff0f2;
}

.p-card-mark {
  padding: 1px;
  position: absolute;
  top: -2px;
  right: 0.75rem;
  font-size: 16px;
}

.normal {
  color: #67c23a;
}

.abnormal {
  color: #f56c6c;
}

.artificial {
  color: #a2a8f7;
}

.p-card-meb {
  position: absolute;
  left: 0;
  top: 0.3rem;
  padding: 6px 0.6rem;
  font-size: 14px;
  line-height: 1rem;
  color: #fff;
  text-align: center;
  border-top-right-radius: 1rem;
  border-bottom-right-radius: 1rem;
}

.member {
  background-color: #f2c423;
}

.nonmember {
  background-color: #d2b860;
}

.arrears {
  position: absolute;
  top: 2.2rem;
  text-align: center;
  font-size: 14px;
  width: 100%;
  line-height: 1.4;
  color: #f56c6c;
  font-weight: bold;
  letter-spacing: 1px;
}

.monthly {
  background-color: #c3aae1;
}

.car-num {
  position: relative;
  margin: 0 auto;
  width: 8.375rem;
  padding: 0.0625rem;
  text-align: center;
  border: 2px solid #c9d3eb;
  border-radius: 4px;
}

.car-num p {
  height: 2.5rem;
  line-height: 2.3rem;
  border-radius: 0.125rem;
  background-color: #8091bf;
  color: #fff;
  font-weight: bold;
  letter-spacing: 0.0625rem;
}

.car-num span {
  display: inline-block;
  position: absolute;
  background-color: #fff;
}

.car-num-a,
.car-num-b {
  width: 0.25rem;
  height: 0.25rem;
  border-radius: 50%;
  top: -1px;
}

.car-num-a {
  left: 2.25rem;
}

.car-num-b {
  right: 2.25rem;
}

.car-num-c,
.car-num-d {
  width: 0.75rem;
  height: 0.1875rem;
  border-radius: 0.125rem;
  bottom: 0.25rem;
}

.car-num-c {
  left: 2rem;
}

.car-num-d {
  right: 2rem;
}

.park-num {
  margin: 0.8rem 0;
  width: 100%;
  text-align: center;
  font-size: 1.5rem;
  color: #656d82;
}
.park-num.park-num-kx {
  color: #67c23a;
}
.park-num.park-num-yc {
  color: #f56c6c;
}
.tips-icon {
  line-height: 2.4;
  overflow: hidden;
}

.tips-icon li {
  float: right;
  margin-right: 20px;
  font-size: 14px;
}

.tips-icon-status {
  line-height: 2.4;
  overflow: hidden;
}

.tips-icon-status li {
  padding: 6px 0.875rem;
  font-size: 14px;
  line-height: 0.7rem;
  color: #fff;
  text-align: center;
  border-top-right-radius: 1rem;
  border-bottom-right-radius: 1rem;
  display: inline-block;
  margin-right: 16px;
}

.card-bgimg {
  width: 100%;
  padding: 10px 0;
  display: flex;
  margin-bottom: 1.25rem;
  background: linear-gradient(125deg, #f4f6fd 35%, #f3f6ff 65%);
  overflow: hidden;
  border-radius: 4px;
}

.card-bgimg li {
  flex: 1;
  text-align: center;
  color: #656d82;
  border-right: 1px solid #fff;
}

.card-bgimg li:last-child {
  border-right: none;
}

.card-bgimg li h4 {
  font-size: 24px;
}

.card-bgimg li h6 {
  font-size: 14px;
  font-weight: normal;
}
.linktext {
  cursor: pointer;
  font-size: 14px;
  font-weight: 500;
  color: #409eff;
}
.history-box {
  margin-top: 30px !important;
  margin-bottom: 20px !important;
}
.history-tit i {
  margin-right: 6px;
}
.mt-20 {
  margin-bottom: 20px;
}
.status-i {
  font-style: normal;
  margin-right: 8px;
  font-weight: bold;
}
.status-i:hover {
  text-decoration: underline;
}
.status-i.kx-status {
  color: #67c23a;
}
.status-i.sy-status {
  color: #656d82;
}
.status-i.yc-status {
  color: #f56c6c;
}
</style>
