<template>
  <div>
    <div class="container">
      <div class="handle-box">
        <div class="left-panel">
          <el-form inline label-width="80" size="small" class="lineH0">
            <el-form-item label="闸机编码" class="search-mb0">
              <el-input
                size="small"
                @keyup.enter="handleSearch()"
                v-model="query.driveout_gate"
                placeholder="请输入闸机编码"
                class="handle-input mr10"
              ></el-input>
            </el-form-item>
            <el-form-item label="操作时间" class="search-mb0">
              <el-date-picker
                v-model="timeData.time"
                placeholder=""
                type="daterange"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                @change="getQueryDate"
              ></el-date-picker>
            </el-form-item>
            <el-form-item label="停车场" class="search-mb0">
              <el-select
                clearable
                v-model="query.areaId"
                filterable
                size="small"
                placeholder="所有区域"
                class="w100"
                @change="getStreet"
              >
                <el-option
                  v-for="item in form.areas"
                  :key="item.id"
                  :label="item.area_name"
                  :value="item.id"
                ></el-option>
              </el-select>
              <span class="mar5 color999"></span>
              <el-select
                clearable
                v-model="query.streetId"
                filterable
                size="small"
                placeholder="所有街道"
                class="w100"
                @change="getPark"
              >
                <el-option
                  v-for="item in form.streets"
                  :key="item.id"
                  :label="item.street_name"
                  :value="item.id"
                ></el-option>
              </el-select>
              <span class="mar5 color999"></span>
              <el-select
                clearable
                v-model="query.parkId"
                filterable
                size="small"
                placeholder="所有停车场"
                class="w100"
              >
                <el-option
                  v-for="item in form.parks"
                  :key="item.id"
                  :label="item.park_name"
                  :value="item.id"
                ></el-option>
              </el-select>
              <span class="mar5 color999"></span>
            </el-form-item>
            <el-form-item label="" class="search-mb0">
              <el-button
                size="small"
                type="primary"
                icon="el-icon-search"
                @click="handleSearch"
                >查询</el-button
              >
              <el-button
                size="small"
                type="success"
                icon="el-icon-upload2"
                @click="exportOpengate" v-permission="'park_opengate_excel'"
                >导出</el-button
              >
            </el-form-item>
          </el-form>
        </div>
        <div class="clear"></div>
      </div>
      <el-table
        :data="tableData"
        border
        class="table"
        :max-height="tableH"
        ref="multipleTable"
        header-cell-class-name="table-header"
        @selection-change="handleSelectionChange"
      >
        <el-table-column label="序号" width="55" align="center">
          <template #default="scope">
            {{ scope.$index + 1 }}
          </template>
        </el-table-column>
        <el-table-column label="类型" prop="park_type" align="center">
          <template #default="scope">
            <el-tag size="small" v-if="scope.row.park_type == '1'">进场</el-tag>
            <el-tag size="small" v-if="scope.row.park_type == '2'">出场</el-tag>
          </template>
        </el-table-column>
        <el-table-column
          label="闸机编码"
          prop="driveout_gate"
          align="center"
        ></el-table-column>
        <el-table-column
          label="停车场"
          prop="parkName"
          align="center"
        ></el-table-column>
        <el-table-column
          label="操作内容"
          width="500"
          prop="content"
          align="center"
        ></el-table-column>
        <el-table-column
          label="订单号"
          prop="park_order_id"
          align="center"
        ></el-table-column>
        <el-table-column label="操作" width="100" align="center">
          <template #default="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-view"
              @click="closeBtn(scope.$index, scope.row)"
              v-permission="'park_opengate_details'"
              >详情
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination">
        <el-pagination
          background
          layout="total, prev, pager, next"
          :current-page="query.pageIndex"
          :page-size="query.pageSize"
          :total="pageTotal"
          @current-change="handlePageChange"
        ></el-pagination>
      </div>
    </div>
    <!-- 详情 -->
    <el-dialog title="查看详情" v-model="editVisible" width="40%">
      <el-tabs type="card" v-model="activeName">
        <el-tab-pane label="基本信息" name="first">
          <div class="mt20"></div>
          <table class="desctable mgb20 w">
            <tr>
              <td class="tit" width="60">订单号</td>
              <td>{{ parkDetails.order_no }}</td>
              <td class="tit" width="80">订单状态</td>
              <td>
                <el-tag type="success">{{ parkDetails.statusName }}</el-tag>
              </td>
            </tr>
            <tr>
              <td class="tit" width="60">停留时间</td>
              <td>{{ parkDetails.resitime }}</td>
              <td class="tit" width="80">费用</td>
              <td>
                <el-tag type="danger">{{
                  parkDetails.sum_amount ? parkDetails.sum_amount : "0"
                }}</el-tag>
              </td>
            </tr>
            <tr>
              <td class="tit" width="60">停车地点</td>
              <td colspan="3">{{ parkDetails.road_name }}</td>
            </tr>
            <tr v-if="parkDetails.status == 1 || parkDetails.status == 2">
              <td class="tit" width="60">车牌号码</td>
              <td colspan="3">
                <div v-show="isShowInfo">
                  <span>{{ parkDetails.car_no }}</span>
                </div>
                <div v-show="isShow">
                  <el-input
                    v-model="parkDetails.car_no"
                    class="w100"
                    size="small"
                  ></el-input>
                  <el-select
                    v-model="parkDetails.car_type"
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
              </td>
            </tr>
            <tr>
              <td class="tit" width="60">车牌类型</td>
              <td>{{ parkDetails.carType }}</td>
              <td class="tit" width="80">车主手机号</td>
              <td>{{ parkDetails.phone }}</td>
            </tr>
            <tr>
              <td class="tit" width="60">订单来源</td>
              <td v-if="parkDetails.source == 1">视频采集</td>
              <td v-else>人工</td>
              <td class="tit" width="80">创建时间</td>
              <td>{{ parkDetails.create_time }}</td>
            </tr>
            <tr>
              <td class="tit" width="60">进场时间</td>
              <td>{{ parkDetails.drivein_time }}</td>
              <td class="tit" width="80">离场时间</td>
              <td>{{ parkDetails.driveout_time }}</td>
            </tr>
          </table>
        </el-tab-pane>
        <el-tab-pane label="进出场信息" name="second">
          <div class="mt20"></div>
          <table class="desctable mgb20 w">
            <tr
              v-if="
                parkDetails.scList != undefined && parkDetails.scList.length > 0
              "
            >
              <td class="tit" width="60">进场照片</td>
              <td colspan="3">
                <template v-for="(item, i) in parkDetails.scList" :key="i">
                  <el-image
                    style="width: 100px; height: 100px"
                    :src="imgurl(item.file_url)"
                    hide-on-click-modal="true" preview-teleported="true"
                    class="ml5"
                    :preview-src-list="[imgurl(item.file_url)]"
                  >
                  </el-image>
                </template>
              </td>
            </tr>
            <tr
              v-if="
                parkDetails.slList != undefined && parkDetails.slList.length > 0
              "
            >
              <td class="tit" width="60">离场照片</td>
              <td colspan="3">
                <template v-for="(item, i) in parkDetails.slList" :key="i">
                  <el-image
                    style="width: 100px; height: 100px"
                    :src="imgurl(item.file_url)"
                    hide-on-click-modal="true" preview-teleported="true"
                    class="ml5"
                    :preview-src-list="[imgurl(item.file_url)]"
                  >
                  </el-image>
                </template>
              </td>
            </tr>
          </table>
        </el-tab-pane>
      </el-tabs>
    </el-dialog>
  </div>
</template>

<script>
import {reactive, ref} from "vue";
import {ElMessage} from "element-plus";
import Ueditor from "../../../components/UE.vue";
import {dictData, isModel, orderOpengateList} from "../../../api/index.js";
import {opengateList,  exportOpengateData} from "../../../api/park/park";
import {
  parkOrderDetailsData
} from "../../../api/roadParkingOrder";
import {
  jointPayment,
  queryAreaData,
  queryPaymentOrder,
  queryParkData,
  queryStreetData,
} from "../../../api/index";
import File_URL from "../../../file_url";

export default {
  name: "parkingOrder",
  components: {
    Ueditor,
  },
  data() {
    return {
      liHide: false,
      isShow: false,
      isShowInfo: true,
      stopTime: false,
      tableH: 0,
    };
  },
  setup() {
    const query = reactive({
      areaId: "",
      streetId: "",
      parkId: "",
      phone: "",
      orderNo: "",
      carNo: "",
      startTime: "",
      endTime: "",
      driveout_gate: "",
      checked: false,
      pageIndex: 1,
      pageSize: 15,
    });
    const tableData = ref([]);
    const pageTotal = ref(0);
    const pageTotal1 = ref(0);
    const todoList = ref([]);
    const specialVisible = ref(false);
    // 获取表格数据
    const getData = () => {
      opengateList(query).then((res) => {
        tableData.value = res.data.list;
        pageTotal.value = res.data.total;
      });
    };

    getData();
    //获取区域下拉框数据
    const queryArea = reactive({});
    const getArea = () => {
      queryAreaData(queryArea).then((res) => {
        form.areas = res.data;
      });
    };
    getArea();
    //获取街道下拉框数据
    let queryStreet = reactive({
      areaId: query.areaId,
    });
    const getStreet = () => {
      if (query.areaId == "") {
        form.streets = [];
        form.parks = [];
        query.streetId = "";
        query.parkId = "";
      } else {
        query.streetId = "";
        query.parkId = "";
        form.parks = [];
        queryStreet.areaId = query.areaId;
        queryStreetData(queryStreet).then((res) => {
          form.streets = res.data;
        });
      }
    };
    //获取停车场下拉框数据
    const getPark = () => {
      if (query.streetId == "") {
        form.parks = [];
        query.parkId = "";
      } else {
        form.parks = [];
        query.parkId = "";
        queryParkData(reactive({ streetId: query.streetId })).then((res) => {
          form.parks = res.data;
        });
      }
    };
    let timeData = ref({
      time: "",
    });
    //日期控件change方法
    const getQueryDate = () => {
      if (
        null == timeData.value.time ||
        [] == timeData.value.time ||
        "" == timeData.value.time
      ) {
        query.startTime = "";
        query.endTime = "";
      } else {
        query.startTime = dateFormat(timeData.value.time[0]);
        query.endTime = dateFormat(timeData.value.time[1]);
      }
    };

    //日期格式化
    const dateFormat = (time) => {
      return `${time.getFullYear()}-${
        time.getMonth() + 1 >= 10
          ? time.getMonth() + 1
          : "0" + (time.getMonth() + 1)
      }-${time.getDate() >= 10 ? time.getDate() : "0" + time.getDate()}`;
    };

    // 查询操作
    const handleSearch = () => {
      query.pageIndex = 1;
      getData();
    };

    // 分页导航
    const handlePageChange = (val) => {
      query.pageIndex = val;
      getData();
    };

    // 详情
    const editVisible = ref(false);
    const parkDetails = ref({});
    let form = reactive({
      areas: [],
      streets: [],
      parkss: [],
      id: "",
      car_id: "",
      order_no: "",
      car_type: "",
      road_name: "",
      berth: "",
      car_no: "",
      drivein_time: "",
      driveout_time: "",
      resitime: "",
      sum_amount: "",
      state_name: "",
      checked: false,
      startTime: "",
      endTime: "",
      source_type: "",
      company_name: "",
      create_time: "",
      time: "",
    });

    const imgurl = (url) => {
      if (url != "" && url != null) {
        return url;
      }
    };
    const carType = ref([]);
    //字典列表
    dictData(reactive({ dict_type: "car_type" })).then((res) => {
      carType.value = res.data;
    });
    //详情获取
    const handleEdit = (index, row) => {
      parkOrderDetailsData(reactive({ id: row.park_id })).then((res) => {
        parkDetails.value = res.data;
      });
      editVisible.value = true;
    };

    return {
      query,
      carType,
      tableData,
      pageTotal,
      pageTotal1,
      editVisible,
      form,
      imgurl,
      todoList,
      specialVisible,
      parkDetails,
      timeData,
      getData,
      getQueryDate,
      handleEdit,
      handleSearch,
      handlePageChange,
      getArea,
      getStreet,
      getPark,
      multipleSelection: [],
      dialogImageUrl: "",
      ppVisible: false,
      isActive: false,
      activeName: "first",
    };
  },
  methods: {
    //上传图片操作
    handleRemove(file, fileList) {},
    //图片预览
    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url;
      this.ppVisible = true;
    },
    handleSelectionChange(data) {
      this.selectedData = data;
    },
    //图片回显
    closeBtn(index, row) {
      var that = this;
      that.stopTime = false;
      (that.liHide = false),
        (that.isShow = false),
        (that.isShowInfo = true),
        (that.timeData.time1 = ""),
        (that.surevb = false);
      that.handleEdit(index, row);
    },
    exportOpengate() {
      exportOpengateData(this.query).then((res) => {
        const url = window.URL.createObjectURL(new Blob([res]))
        const link = document.createElement('a')
        link.href = url
        link.setAttribute('download', '遥控日志.xls')
        document.body.appendChild(link)
        link.click()
      });
    },
  },
  created() {
    let h = document.documentElement.clientHeight || document.body.clientHeight;
    this.tableH = h - 308 + "px";
  },
};
</script>
