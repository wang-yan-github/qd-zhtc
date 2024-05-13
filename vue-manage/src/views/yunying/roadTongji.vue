<template>
  <div>
    <div class="container">
      <el-row>
        <el-col :span="24">
          <div class="top-panel">
            <el-form inline size="small">
              <el-form-item label="时间">
                <el-date-picker
                  size="small"
                  v-model="form.time"
                  type="daterange"
                  range-separator="至"
                  start-placeholder="开始日期"
                  end-placeholder="结束日期"
                  class="datepicker"
                  @change="getQueryDate"
                >
                </el-date-picker>
              </el-form-item>
              <el-form-item label="选择层级">
                <el-select
                  clearable
                  v-model="query.areaId"
                  filterable
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
                  placeholder="所有街道"
                  class="w100"
                  @change="getRoad"
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
                  v-model="query.roadId"
                  filterable
                  placeholder="所有路内"
                  class="w100"
                >
                  <el-option
                    v-for="item in form.roads"
                    :key="item.id"
                    :label="item.road_name"
                    :value="item.id"
                  ></el-option>
                </el-select>
              </el-form-item>
              <el-form-item>
                <el-button
                  size="small"
                  type="primary"
                  icon="el-icon-search"
                  @click="handleSearch"
                  >查询</el-button
                >
                <!-- <el-button size="small" type="text" icon="el-icon-arrow-up">合并</el-button> -->
              </el-form-item>
              <el-form-item>
                <el-button
                  size="small"
                  type="success"
                  icon="el-icon-upload2"
                  @click="exportExcel"
                  >导出</el-button
                >
                <!-- <el-button size="small" type="text" icon="el-icon-arrow-up">合并</el-button> -->
              </el-form-item>
            </el-form>
          </div>
        </el-col>
      </el-row>

      <el-table
        :data="tableData"
        border
        class="table"
        :max-height="tableH"
        ref="multipleTable"
        header-cell-class-name="table-header"
      >
        <el-table-column type="index" label="序号" width="55" align="center">
          <template #default="scope">
            {{ scope.$index + 1 }}
          </template>
        </el-table-column>
        <el-table-column prop="roadName" label="路内" align="center"> </el-table-column>
        <el-table-column
          v-show="userType == '0'"
          prop="area"
          label="区域"
          align="center"
          width="300"
        >
        </el-table-column>

        <el-table-column
          prop="timeLong"
          label="停车总时长"
          align="center"
          width="200"
        >
        </el-table-column>
        <el-table-column
          prop="utilizationRate"
          label="利用率(%)"
          align="center"
          width="200"
        >
        </el-table-column>
        <el-table-column
          prop="turnoverRate"
          label="周转率(%)"
          align="center"
          width="200"
        >
        </el-table-column>
        <el-table-column
          prop="turnoverAverRate"
          label="周转均比"
          align="center"
          width="200"
        >
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
  </div>
</template>

<script>
import { ref, reactive } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import {
  roadTjData,
  queryAreaData,
  queryStreetData,
  queryRoadData,
  roadTjExport,
} from "../../api/index";

export default {
  name: "roadTongji",
  data() {
    return {
      tableH:0,
    };
  },
  setup() {
    const query = reactive({
      time: "",
      startTime: "",
      endTime: "",
      areaId: "",
      streetId: "",
      roadId: "",
      pageIndex: 1,
      pageSize: 18,
    });
    const tableData = ref([]);
    const pageTotal = ref(0);
    const userType = ref(0);
    const model = ref(0);
    let form = reactive({
      time: "",
      areaId: "",
      streetId: "",
      roadId: "",
      areas: [],
      streets: [],
      roads: [],
    });
    const getStartTime = () => {
      var now = new Date(); //当前日期
      var nowMonth = now.getMonth(); //当前月
      var nowYear = now.getFullYear(); //当前年
      //本月的开始时间
      var monthStartDate = new Date(nowYear, nowMonth, 1);
      var monthEndDate = new Date(nowYear, nowMonth + 1, 0);
      form.time = [monthStartDate, monthEndDate];
      var date = monthStartDate.getDate();
      var month = monthStartDate.getMonth() + 1;
      if (month < 10) {
        month = "0" + month;
      }
      if (date < 10) {
        date = "0" + date;
      }
      query.startTime =
        monthStartDate.getFullYear().toString() +
        "-" +
        month.toString() +
        "-" +
        date.toString();
    };
    getStartTime();
    const getEndTime = () => {
      var now = new Date(); //当前日期
      var nowMonth = now.getMonth(); //当前月
      var nowYear = now.getFullYear(); //当前年
      //本月的结束时间
      var monthEndDate = new Date(nowYear, nowMonth + 1, 0);
      var date = monthEndDate.getDate();
      var month = monthEndDate.getMonth() + 1;
      if (month < 10) {
        month = "0" + month;
      }
      if (date < 10) {
        date = "0" + date;
      }
      query.endTime =
        monthEndDate.getFullYear().toString() +
        "-" +
        month.toString() +
        "-" +
        date.toString();
    };
    getEndTime();
    // 获取表格数据
    const getData = () => {
      if (query.startTime == "" || query.endTime == "") {
        ElMessage.error("请选择日期");
        return false;
      }
      roadTjData(query).then((res) => {
        tableData.value = res.data.list;
        pageTotal.value = res.data.page.total;
        userType.value = res.data.user_type;
        model.value = res.data.model;
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
        form.roads = [];
        query.streetId = "";
        query.roadId = "";
      } else {
        queryStreet.areaId = query.areaId;
        query.roadId = "";
        query.streetId = "";
        form.roads = [];
        queryStreetData(queryStreet).then((res) => {
          form.streets = res.data;
        });
      }
    };

    //获取路内下拉框数据
    const queryRoad = reactive({
      streetId: query.streetId,
    });
    const getRoad = () => {
      if (query.streetId == "") {
        form.roads = [];
        query.roadId = "";
      } else {
        form.roads = [];
        query.roadId = "";
        queryRoad.streetId = query.streetId;
        queryRoadData(queryRoad).then((res) => {
          form.roads = res.data;
        });
      }
    };

    // 查询操作
    const handleSearch = () => {
      query.pageIndex = 1;
      getData();
    };

    //导出excel
    const exportExcel = () => {
      // query.pageSize = 500;
      // 提示是否导出当页数据
      ElMessageBox.confirm("是否导出当页数据？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          roadTjExport(query).then((res) => {
            query.pageSize = 18;
            const url = window.URL.createObjectURL(new Blob([res]));
            const link = document.createElement("a");
            link.href = url;
            link.setAttribute("download", "导出报表.xls");
            document.body.appendChild(link);
            link.click();
          });
        })
        .catch(() => {
          ElMessage.info("已取消导出");
        });
    };
    // 分页导航
    const handlePageChange = (val) => {
      query.pageIndex = val;
      getData();
    };
    //日期控件change方法
    const getQueryDate = () => {
      if (null == form.time || [] == form.time || "" == form.time) {
        query.startTime = "";
        query.endTime = "";
      } else {
        query.startTime = dateFormat(form.time[0]);
        query.endTime = dateFormat(form.time[1]);
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

    return {
      query,
      tableData,
      pageTotal,
      form,
      queryArea,
      queryStreet,
      queryRoad,
      userType,
      model,
      getArea,
      getStreet,
      getQueryDate,
      getRoad,
      handleSearch,
      handlePageChange,
      exportExcel,
    };
  },
  methods: {},
  created() {
    let h = document.documentElement.clientHeight || document.body.clientHeight;
    this.tableH = h - 308 + 'px';
  },
};
</script>

