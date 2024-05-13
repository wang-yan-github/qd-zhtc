<template>
  <div>
    <el-card class="mgb20" shadow="hover">
      <div class="top-panel">
        <div class="left-panel">
          <el-button-group>
            <el-button icon="el-icon-s-grid" @click="goTo()"
              >基础数据</el-button
            >
            <el-button type="primary" icon="el-icon-s-marketing"
              >订单增长</el-button
            >
            <el-button icon="el-icon-s-order" @click="goTo2()"
              >订单状态占比</el-button
            >
          </el-button-group>
        </div>
        <div class="right-panel _mtp10">
          <el-form label-width="80px" inline size="small">
            <el-form-item label="时间">
              <el-date-picker
                size="small"
                v-model="form.date1"
                type="daterange"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                class="datepicker"
              >
              </el-date-picker>
            </el-form-item>
            <el-form-item label="选择层级">
              <el-select
                clearable
                v-model="form.selvalue"
                filterable
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
              <span class="mar5 color999"></span>
              <el-select
                clearable
                v-model="form.selvalue"
                filterable
                placeholder="所有街道"
                class="w100"
              >
                <el-option
                  v-for="item in form.czroptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
              <span class="mar5 color999"></span>
              <el-select
                clearable
                v-model="form.selvalue"
                filterable
                placeholder="所有路内"
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
            <el-form-item>
              <el-button size="small" type="primary" icon="el-icon-search"
                >查询</el-button
              >
              <!-- <el-button size="small" type="text" icon="el-icon-arrow-up">合并</el-button> -->
            </el-form-item>
            <el-form-item>
              <el-button size="small" type="success" icon="el-icon-upload2"
                >导出</el-button
              >
              <!-- <el-button size="small" type="text" icon="el-icon-arrow-up">合并</el-button> -->
            </el-form-item>
          </el-form>
        </div>
        <div class="clearfix"></div>
      </div>
    </el-card>
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
            <el-table-column pro="ID" label="ID" width="55" align="center">
              <template #default="scope">
                {{ scope.row.id }}
              </template>
            </el-table-column>
            <el-table-column
              prop="name"
              label="操作人"
              width="120"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="purviews"
              label="角色"
              width="120"
              align="center"
            >
            </el-table-column>
            <el-table-column prop="rizhiconent" label="日志内容">
            </el-table-column>

            <el-table-column
              prop="addtimes"
              label="操作时间"
              width="160"
              align="center"
            >
            </el-table-column>
            <el-table-column
              prop="ip"
              label="IP地址"
              width="160"
              align="center"
            >
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
</template>

<script>
import { ref, reactive } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import * as echarts from "echarts";
import { journalData } from "../../../api/index";

export default {
  name: "orderTongjiA",
  setup() {
    const query = reactive({
      address: "",
      name: "",
      pageIndex: 1,
      pageSize: 8,
    });
    const tableData = ref([]);
    const pageTotal = ref(0);
    let form = reactive({
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
      journalData(query).then((res) => {
        tableData.value = res.list;
        pageTotal.value = res.pageTotal || 50;
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
      handleSearch,
      handlePageChange,
    };
  },
  methods: {
    goTo() {
      //直接跳转
      this.$router.push("/orderstatic");

      //带参数跳转
      // this.$router.push({ path: "/testDemo", query: { setid: 123456 } });
      // this.$router.push({ name: "testDemo", params: { setid: 111222 } });
    },
    goTo2() {
      //直接跳转
      this.$router.push("/orderstaticthree");

      //带参数跳转
      // this.$router.push({ path: "/testDemo", query: { setid: 123456 } });
      // this.$router.push({ name: "testDemo", params: { setid: 111222 } });
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

