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
              <el-button icon="el-icon-s-marketing" @click="goTo2()"
                >充值增长</el-button
              >
              <el-button type="primary" icon="el-icon-money"
                >缴费方式统计</el-button
              >
              <el-button icon="el-icon-s-custom" @click="goTo3()"
                >免单统计</el-button
              >
            </el-button-group>
          </el-form-item>
        </div>
        <div class="right-panel">
          <el-form label-width="80px" inline size="small">
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
        <el-col :span="12">
          <el-card shadow="hover">
            <el-table
              :data="tableData"
              border
              class="table"
              ref="multipleTable"
              header-cell-class-name="table-header"
              @selection-change="handleSelectionChange"
            >
              <el-table-column
                type="index"
                label="序号"
                width="55"
                align="center"
              >
              </el-table-column>
              <el-table-column
                prop="paymentMethod"
                label="缴费方式"
                align="center"
              ></el-table-column>
              <el-table-column prop="num" label="数量" align="center">
              </el-table-column>
              <el-table-column prop="totalAmount" label="总金额" align="center">
              </el-table-column>

              <el-table-column prop="proportion" label="占比" align="center">
              </el-table-column>
            </el-table>
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card shadow="hover">
            <div id="main" class="schartmain"></div>
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
import { getRoadParkingPaymentMethod } from "../../api/index";

export default {
  name: "orderTongji",
  setup() {
    const query = reactive({
      startTime: "",
      endTime: "",
      pageIndex: 1,
      pageSize: 100000,
    });
    const tableData = ref([]);
    const pageTotal = ref(0);
    let form = reactive({
      time: "",
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
      getRoadParkingPaymentMethod(query).then((res) => {
        tableData.value = res.data.data;
        options4.series[0].data = res.data.view;
        var chartDom = document.getElementById("main");
        var myChart = echarts.init(chartDom);
        myChart.setOption(options4);
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
    };
    //日期格式化
    const dateFormat = (time) => {
      return `${time.getFullYear()}-${
        time.getMonth() + 1 >= 10
          ? time.getMonth() + 1
          : "0" + (time.getMonth() + 1)
      }-${time.getDate() >= 10 ? time.getDate() : "0" + time.getDate()}`;
    };

    const options4 = {
      title: {
        text: "订单状态占比分析",
        show: true,
        textStyle: {
            fontSize: 16,
            fontWeight: "normal",
            color: "#303133",
        },
      },
      tooltip: {
        trigger: "item",
      },
      legend: {
        //data: ["订单总数", "缴费订单数"],
        orient: "vertical",
        left: "left",
        show: false,
      },
      grid: {
        left: "10px",
        right: "10px",
        bottom: "3%",
        containLabel: true,
      },
      series: [
        {
          name: "订单总数",
          type: "pie",
          radius: "50%",
          data: [
            // { value: 1048, name: "已缴费" },
            // { value: 735, name: "待缴费" },
            // { value: 580, name: "未完成" },
            // { value: 484, name: "已完成" },
            // { value: 300, name: "在停中" },
          ],
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: "rgba(0, 0, 0, 0.5)",
            },
          },
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
      this.$router.push("/cashstatictwo");
    },
    goTo3() {
      //直接跳转
      this.$router.push("/cashstaticfour");
    },
    init() {
      var chartDom = document.getElementById("main");
      chartDom.removeAttribute("_echarts_instance_");
      var myChart = echarts.init(chartDom);
      myChart.setOption(this.options4);
    },
  },
  mounted() {
    this.init();
  },
};
</script>

