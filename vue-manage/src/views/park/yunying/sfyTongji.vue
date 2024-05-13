<template>
  <div>
    <div class="container">
      <div class="top-panel">
        {{ form.date1 }}
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
          <el-form-item label="选择层级" v-show="userType == '0'">
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

      <el-table
        :data="tableData"
        border
        class="table"
        :max-height="tableH"
        ref="multipleTable"
        header-cell-class-name="table-header"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="index" label="序号" width="55" align="center">
        </el-table-column>
        <el-table-column
          prop="jobNo"
          label="巡检工号"
          align="center"
        ></el-table-column>
        <el-table-column prop="name" label="巡检姓名" align="center">
        </el-table-column>
        <el-table-column prop="roadCount" label="停车场数量" align="center">
        </el-table-column>
        <el-table-column prop="deviceCount" label="泊位数量" align="center">
        </el-table-column>
        <el-table-column prop="orderCount" label="创建订单数" align="center">
        </el-table-column>
        <el-table-column prop="cash" label="现金收入" align="center">
        </el-table-column>
          <el-table-column prop="qr" label="扫码收入" align="center">
        </el-table-column>

        <!-- <el-table-column prop="addtimes" label="报警处理率" align="center">
        </el-table-column> -->
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
  inspectTjData,
  queryAreaData,
  queryStreetData,
  inspectTjExport,
  queryParkData,
} from "../../../api/index";

export default {
  name: "xunjianTongji",
  data() {
    return {
      tableH:0,
    };
  },
  setup() {
    const query = reactive({
      startTime: "",
      endTime: "",
      areaId: "",
      streetId: "",
      roadId: "",
      pageIndex: 1,
      pageSize: 15,
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
      inspectTjData(query).then((res) => {
        tableData.value = res.data.list;
        pageTotal.value = res.data.page.total;
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
        queryStreet.areaId = query.areaId;
        query.parkId = "";
        query.streetId = "";
        form.parks = [];
        queryStreetData(queryStreet).then((res) => {
          form.streets = res.data;
        });
      }
    };

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

    //导出excel
    const exportExcel = () => {
      inspectTjExport(query).then((res) => {
        const url = window.URL.createObjectURL(new Blob([res]));
        const link = document.createElement("a");
        link.href = url;
        link.setAttribute("download", "导出收费员统计报表.xls");
        document.body.appendChild(link);
        link.click();
      });
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
      form,
      pageTotal,
      queryArea,
      queryStreet,
      userType,
      model,
      getArea,
      getStreet,
      getQueryDate,
      getPark,
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

