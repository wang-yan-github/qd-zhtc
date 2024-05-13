<template>
  <div>
    <div class="container">
      <div class="top-panel f-cb">
        <div class="left-panel">
          <el-form-item class="mb0">
            <el-button-group>
              <el-button icon="el-icon-s-grid" @click="goTo()"
                >收入报表</el-button
              >
              <el-button icon="el-icon-s-marketing" @click="goTo3()"
                >充值增长</el-button
              >
              <el-button icon="el-icon-money" @click="goTo2()"
                >缴费方式统计</el-button
              >
              <el-button type="primary" icon="el-icon-s-custom"
                >免单统计</el-button
              >
            </el-button-group>
          </el-form-item>
        </div>
        <div class="right-panel">
          <el-form label-width="80px" inline size="small">
            <el-form-item label="时间">
                  <!-- <el-button-group>
                    <el-button type="primary" plain @click="day">日</el-button>
                    <el-button plain @click="month">月</el-button>
                    <el-button plain @click="year">年</el-button>
                  </el-button-group> -->
                  <span class="dispinline mlr5"></span>
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

            <el-form-item>
              <el-button size="small" type="primary" icon="el-icon-search" @click="handleSearch"
                >查询</el-button
              >
              <!-- <el-button size="small" type="text" icon="el-icon-arrow-up">合并</el-button> -->
            </el-form-item>
            <!-- <el-form-item class="mb0">
              <el-button size="small" type="success" icon="el-icon-upload"
                >导出Excel</el-button
              >
            </el-form-item> -->
          </el-form>
        </div>
        <div class="clear"></div>
      </div>
    <el-row :gutter="20">
      <el-col :span="24">
        <el-card shadow="hover">
          <el-table
            :data="tableData"
            border
            class="table"
            ref="multipleTable"
            header-cell-class-name="table-header"
            @selection-change="handleSelectionChange"
          >
            <el-table-column type="index" label="序号" width="55" align="center">
          
            </el-table-column>
            <el-table-column
              prop="freeType"
              label="免单类型"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="carNum"
              label="免单车牌数"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="freeAmount"
              label="免单金额"
              align="center"
            >
            </el-table-column>
            <el-table-column prop="freeNum" label="免单数量" align="center">
            </el-table-column>

            <el-table-column
              prop="freeProportion"
              label="占总订单数比例"
              align="center"
            >
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
  </div>
</template>

<script>
import { ref, reactive } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import * as echarts from "echarts";
import { getRoadParkingFree } from "../../api/index";

export default {
  name: "orderTongji",
  setup() {
    const query = reactive({
      startTime: "",
      endTime: "",
      address: "",
      name: "",
      pageIndex: 1,
      pageSize: 8,
    });
    const tableData = ref([]);
    const pageTotal = ref(0);
    let form = reactive({
      time:"",
      name: "",
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
      date1: "",
      date2: "",
    });

    // 获取表格数据
    const getData = () => {
      getRoadParkingFree(query).then((res) => {
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

    //日期控件change方法
    const getQueryDate = () => {
      if (null == form.time || [] == form.time || "" == form.time) {
        query.startTime = "";
        query.endTime = "";
      } else {
        query.startTime = dateFormat(form.time[0]);
        query.endTime = dateFormat(form.time[1]);
      }
    }
    //日期格式化
    const dateFormat = (time) => {
      return `${time.getFullYear()}-${time.getMonth() + 1 >= 10 ? (time.getMonth() + 1) : '0' + (time.getMonth() + 1)}-${time.getDate() >= 10 ? time.getDate() : '0' + time.getDate()}`
    }
    const options4 = {
      title: {
        text: "近八天订单增长分析",
        show: true,
        textStyle: {
            fontSize: 16,
            fontWeight: "normal",
            color: "#303133",
        },
      },
      tooltip: {
        trigger: "axis",
        axisPointer: {
          type: "cross",
          label: {
            backgroundColor: "#6a7985",
          },
        },
      },
      legend: {
        data: ["订单总数", "缴费订单数"],
      },
      grid: {
        left: "10px",
        right: "10px",
        bottom: "3%",
        containLabel: true,
      },
      xAxis: [
        {
          type: "category",
          boundaryGap: false,
          data: ["12-17", "12-18", "12-19", "12-20", "12-21", "12-22", "12-23"],
        },
      ],
      yAxis: [
        {
          type: "value",
        },
      ],
      series: [
        {
          name: "订单总数",
          type: "line",
          stack: "Total",
          areaStyle: {},
          emphasis: {
            focus: "series",
          },
          data: [120, 132, 101, 134, 90, 230, 210],
        },
        {
          name: "缴费订单数",
          type: "line",
          stack: "Total",
          areaStyle: {},
          emphasis: {
            focus: "series",
          },
          data: [220, 182, 191, 234, 290, 330, 310],
        },
      ],
    };
    return {
      query,
      tableData,
      form,
      pageTotal,
      options4,
      getQueryDate,
      handleSearch,
      handlePageChange,
    };
  },
  methods: {
    goTo() {
      //直接跳转
      this.$router.push("/cashstatic");

      //带参数跳转
      // this.$router.push({ path: "/testDemo", query: { setid: 123456 } });
      // this.$router.push({ name: "testDemo", params: { setid: 111222 } });
    },
    goTo2() {
      //直接跳转
      this.$router.push("/cashstaticthree");
    },
    goTo3() {
      //直接跳转
      this.$router.push("/cashstatictwo");
    },
  },
  mounted() {
    // this.init();
  },
};
</script>

