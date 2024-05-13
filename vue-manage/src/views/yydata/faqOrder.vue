<template>
  <div>
    <div class="container">
      <div class="handle-box">
        <div class="left-panel">
          <el-form inline label-width="80" size="small" class="lineH0">
            <!-- <el-form-item label="车牌号" class="search-mb0">
              <el-input
                @keyup.enter="handleSearch()"
                size="small"
                v-model="query.car_no"
                class="w100"
                placeholder="车牌号"
              ></el-input>
            </el-form-item>
            <el-form-item label="" class="search-mb0">
              <el-checkbox v-model="form.checked">黄牌</el-checkbox>
            </el-form-item> -->
            <el-form-item label="订单号" class="search-mb0">
              <el-input
                @keyup.enter="handleSearch()"
                size="small"
                placeholder="订单号"
                v-model="query.order_no"
              ></el-input>
            </el-form-item>
            <el-form-item label="手机号" class="search-mb0">
              <el-input
                @keyup.enter="handleSearch()"
                size="small"
                v-model="query.phone"
                placeholder="手机号"
                class="w100"
              ></el-input>
            </el-form-item>
            <el-form-item label="泊位号" class="search-mb0">
              <el-input
                @keyup.enter="handleSearch()"
                size="small"
                v-model="query.berth"
                placeholder="泊位号"
                class="w170"
              ></el-input>
            </el-form-item>
            <el-form-item label="时间" class="search-mb0">
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
                @click="exportAbnormalParking"
                >导出</el-button
              >
            </el-form-item>
            <!-- <el-form-item label=""
              ><el-button
                size="small"
                type="text"
                icon="el-icon-arrow-up"
                @click="isActive = !isActive"
                >展开</el-button
              ></el-form-item
            > -->
            <div v-show="isActive">
              <el-form-item label="" class="search-mb0">
                <el-select
                  v-model="form.selvalue"
                  filterable
                  size="small"
                  placeholder="所有区域"
                  class="w100"
                >
                  <el-option
                    v-for="item in form.czroptions"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  ></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="" class="search-mb0">
                <el-select
                  v-model="form.selvalue"
                  filterable
                  size="small"
                  placeholder="所有区域"
                  class="w100"
                >
                  <el-option
                    v-for="item in form.czroptions"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  ></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="" class="search-mb0">
                <el-select
                  v-model="form.selvalue"
                  filterable
                  size="small"
                  placeholder="所有区域"
                  class="w100"
                >
                  <el-option
                    v-for="item in form.czroptions"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  ></el-option>
                </el-select>
              </el-form-item>
            </div>
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
        <!--<el-table-column
          type="selection"
          width="55"
          align="center"
        ></el-table-column>-->
        <el-table-column label="序号" width="55" align="center">
          <template #default="scope">
            {{ scope.$index + 1 }}
          </template>
        </el-table-column>
        <el-table-column
          prop="order_no"
          label="订单号"
          width="200"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="road_name"
          label="路内"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="berth"
          label="泊位编号"
          align="center"
          width="200"
        ></el-table-column>
        <!-- <el-table-column prop="car_no" label="车牌号码" align="center" width="100"></el-table-column> -->
        <el-table-column
          prop="car_no"
          label="车牌号"
          width="100"
          align="center"
        >
          <template #default="scope">
            <el-tag
              size="small"
              v-if="scope.row.car_type == '蓝牌'"
              v-text="scope.row.car_no"
            ></el-tag>
            <el-tag
              size="small"
              type="success"
              v-else-if="scope.row.car_type == '绿牌'"
              v-text="scope.row.car_no"
            ></el-tag>
            <el-tag
              size="small"
              type="warning"
              v-else-if="scope.row.car_type == '黄牌'"
              v-text="scope.row.car_no"
            ></el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="drivein_time"
          label="停入时间"
          width="180"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="driveout_time"
          label="离场时间"
          width="180"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="resi_time"
          width="120"
          label="停留时间"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="sum_amount"
          label="停车费用"
          width="80"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="state_name"
          label="状态"
          width="80"
          align="center"
        >
          <template #default="scope">
            <el-tag
              size="small"
              v-if="scope.row.status == '1'"
              v-text="scope.row.state_name"
            ></el-tag>
            <el-tag
              size="small"
              type="success"
              v-else-if="scope.row.status == '2'"
              v-text="scope.row.state_name"
            ></el-tag>
            <el-tag
              size="small"
              type="warning"
              v-else-if="scope.row.status == '3'"
              v-text="scope.row.state_name"
            ></el-tag>
            <el-tag
              size="small"
              type="danger"
              v-else-if="scope.row.status == '4'"
              v-text="scope.row.state_name"
            ></el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="car_type" v-if="liHide"> </el-table-column>
        <el-table-column prop="create_time" v-if="liHide"></el-table-column>
        <el-table-column prop="source_type" v-if="liHide"></el-table-column>
        <el-table-column prop="car_id" v-if="liHide"></el-table-column>
        <el-table-column label="操作" width="100" align="center">
          <template #default="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-view"
              @click="closeBtn(scope.$index, scope.row)"
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
              <td>{{ form.order_no }}</td>
              <td class="tit" width="80">订单状态</td>
              <td>
                <el-tag type="success">{{ form.state_name }}</el-tag>
              </td>
            </tr>
            <tr>
              <td class="tit" width="60">停留时间</td>
              <td>{{ form.resi_time }}</td>
              <td class="tit" width="80">费用</td>
              <td>
                <el-tag type="danger">{{ form.sum_amount }}</el-tag>
              </td>
            </tr>
            <tr>
              <td class="tit" width="60">停车地点</td>
              <td>{{ form.road_name }}</td>
              <td class="tit" width="80">泊位号</td>
              <td>{{ form.berth }}</td>
            </tr>
            <tr>
              <td class="tit" width="60">车牌号码</td>
              <td colspan="3">
                <div v-show="isShowInfo">
                  <span>{{ form.car_no }}</span>
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
                    v-model="form.car_no"
                    class="w100"
                    size="small"
                  ></el-input>
                  <el-select
                    v-model="form.car_type"
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
              <td>{{ form.car_type }}</td>
              <td class="tit" width="80">车主手机号</td>
              <td>{{ form.company_name }}</td>
            </tr>
            <tr>
              <td class="tit" width="60">订单来源</td>
              <td>{{ form.source_type }}</td>
              <td class="tit" width="80">创建时间</td>
              <td>{{ form.create_time }}</td>
            </tr>
          </table>
        </el-tab-pane>
        <el-tab-pane label="进出场信息" name="second">
          <div class="mt20"></div>
          <table class="desctable mgb20 w">
            <tr>
              <td class="tit" width="60">进场时间</td>
              <td>{{ form.drivein_time }}</td>
              <td class="tit" width="80">离场时间</td>
              <td>{{ form.driveout_time }}</td>
            </tr>
            <tr v-if="scList.length > 0">
              <td class="tit" width="60">进场照片</td>
              <td colspan="3">
                <template v-for="(item, i) in scList" :key="i">
                  <el-image
                    style="width: 100px; height: 100px"
                    hide-on-click-modal="true" preview-teleported="true"
                    :src="imgurl(item.file_url)"
                    class="ml5"
                    :preview-src-list="[imgurl(item.file_url)]"
                  >
                  </el-image>
                </template>
              </td>
            </tr>
            <tr v-if="slList.length > 0">
              <td class="tit" width="60">离场照片</td>
              <td colspan="3">
                <template v-for="(item, i) in slList" :key="i">
                  <el-image
                    style="width: 100px; height: 100px"
                    hide-on-click-modal="true" preview-teleported="true"
                    :src="imgurl(item.file_url)"
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
      <el-form v-show="stopTime">
        <el-form-item label="结束计时时间">
          <el-date-picker
            v-model="timeData.time1"
            type="datetime"
            @change="getQueryDate1"
            placeholder="选择日期时间"
          >
          </el-date-picker>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="editVisible = false">取 消</el-button>
          <el-button type="primary" @click="endTimeMethod" v-show="surevb"
            >确 定</el-button
          >
          <!-- <el-button type="danger" @click="endTimeTK"  v-if="(form.driveout_time==null||form.driveout_time==undefined||form.driveout_time=='') &&form.status==1" v-show="!surevb">结束计时</el-button> -->
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { reactive, ref } from "vue";
import { ElMessage } from "element-plus";
import Ueditor from "../../components/UE.vue";
import { dictData } from "../../api/index.js";
import {
  abnormalParkingCarData,
  abnormalParkingClosureData,
  abnormalParkingDetailsData,
  abnormalParkingListData,
  exportAbnormalParkingData,
} from "../../api/abnormalParking.js";

export default {
  name: "abnormalParking",
  components: {
    Ueditor,
  },
  data() {
    return {
      liHide: false,
      isShow: false,
      isShowInfo: true,
      stopTime: false,
      surevb: false,
      tableH: 0,
    };
  },
  setup() {
    const query = reactive({
      phone: "",
      order_no: "",
      berth: "",
      car_no: "",
      create_time: "",
      checked: 0,
      pageIndex: 1,
      pageSize: 15,
    });
    const tableData = ref([]);
    const pageTotal = ref(0);
    // 获取表格数据
    const getData = () => {
      abnormalParkingListData(query).then((res) => {
        console.log(res.data);
        tableData.value = res.data.list;
        pageTotal.value = res.data.total;
      });
    };

    getData();

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
    const scList = ref({});
    const slList = ref({});
    let form = reactive({
      id: "",
      car_id: "",
      resi_time: "",
      order_no: "",
      road_name: "",
      berth: "",
      car_no: "",
      car_type: "",
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
    });
    let timeData = ref({
      time: "",
      time1: "",
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

    //日期控件change方法
    const getQueryDate1 = () => {
      if (
        null == timeData.value.time1 ||
        [] == timeData.value.time1 ||
        "" == timeData.value.time1
      ) {
        query.driveout_time = "";
      } else {
        form.driveout_time = dateFormat(timeData.value.time1);
      }
    };
    //日期格式化
    const dateFormat = (time) => {
      return `${time.getFullYear()}-${
        time.getMonth() + 1 >= 10
          ? time.getMonth() + 1
          : "0" + (time.getMonth() + 1)
      }-${time.getDate() >= 10 ? time.getDate() : "0" + time.getDate()} ${
        time.getHours() >= 10 ? time.getHours() : "0" + time.getHours()
      }:${
        time.getMinutes() >= 10 ? time.getMinutes() : "0" + time.getMinutes()
      }:${
        time.getSeconds() >= 10 ? time.getSeconds() : "0" + time.getSeconds()
      }`;
    };
    let idx = -1;
    //详情获取
    const handleEdit = (index, row) => {
      idx = index;
      Object.keys(form).forEach((item) => {
        form[item] = row[item];
      });
      editVisible.value = true;
      abnormalParkingDetailsData(reactive({ id: row.id })).then((res) => {
        scList.value = res.data.scList;
        slList.value = res.data.slList;
      });
    };

    const carType = ref([]);
    //字典列表
    dictData(reactive({ dict_type: "car_type" })).then((res) => {
      carType.value = res.data;
    });

    return {
      query,
      tableData,
      pageTotal,
      editVisible,
      form,
      scList,
      carType,
      slList,
      timeData,
      getData,
      getQueryDate1,
      handleEdit,
      handleSearch,
      handlePageChange,
      getQueryDate,
      multipleSelection: [],
      dialogImageUrl: "",
      ppVisible: false,
      isActive: false,
      activeName: "first",
    };
  },
  methods: {
    //上传图片操作
    handleRemove(file, fileList) {
      console.log(file, fileList);
    },
    //图片预览
    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url;
      this.ppVisible = true;
    },
    handleSelectionChange(data) {
      this.selectedData = data;
    },
    //结束计时,应该弹框,数据先写死
    endTimeTK() {
      this.stopTime = !this.stopTime;
      this.surevb = true;
    },
    endTimeMethod() {
      let _this = this;
      let query = reactive({
        id: _this.form.id,
        driveout_time: _this.form.driveout_time,
        drivein_time: _this.form.drivein_time,
      });
      abnormalParkingClosureData(query).then((res) => {
        if (res.code == 0) {
          ElMessage.success(res.msg);
          _this.editVisible = false;
          _this.getData();
        } else {
          ElMessage.success(res.msg);
        }
      });
    },
    //修改车牌(车牌id,新车牌号，车牌类型(下拉选项))
    carUpdDate() {
      this.isShow = !this.isShow; //取反
      this.isShowInfo = !this.isShowInfo; //取反
    },
    // 修改车牌确定取消
    changeisShowInfo(val) {
      this.isShow = !this.isShow; //取反
      this.isShowInfo = !this.isShowInfo; //取反
      if (val == 1) {
        let _this = this;
        abnormalParkingCarData(
          reactive({
            id: _this.form.id,
            car_id: _this.form.car_id,
            car_no: _this.form.car_no,
            car_type: _this.form.car_type,
          })
        ).then((res) => {
          if (res.code == 0) {
            _this.editVisible = false;
            ElMessage.success(res.msg);
            _this.getData();
          } else {
            ElMessage.error(res.msg);
          }
        });
      }
    },
    //图片回显
    imgurl: function (url) {
      if (url != "" && url != null) {
        return url;
      }
    },
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
    exportAbnormalParking() {
      ElMessage.success("正在下载中·····");
      exportAbnormalParkingData(this.query).then((res) => {
        const url = window.URL.createObjectURL(new Blob([res]));
        const link = document.createElement("a");
        link.href = url;
        link.setAttribute("download", "问题订单.xls");
        document.body.appendChild(link);
        link.click();
      });
    },
  },
  created() {
    let h = document.documentElement.clientHeight || document.body.clientHeight;
    this.tableH = h - 308 + "px";
  },
};
</script>
