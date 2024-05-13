<template>
  <div>
    <div class="container">
      <div class="handle-box">
        <div class="left-panel">
          <el-button size="small" plain type="danger">充值人数(人)：
            {{ tableCountData.recharge_pens }}
          </el-button>
          <el-button size="small" plain type="danger">充值笔数(笔)：
            {{ tableCountData.transactions_number }}
          </el-button>
          <el-button size="small" plain type="danger">充值金额(元)：
            {{ tableCountData.recharge_amount }}
          </el-button>
        </div>
        <div class="right-panel">
          <el-button-group>
            <el-button icon="el-icon-time" size="small" @click="butTime">按时间</el-button>
            <el-button icon="el-icon-user" size="small" @click="butPeople">按人员</el-button>
          </el-button-group>
          <span class="dispinline ml5"></span>
          <el-input
              @keyup.enter="handleSearch()"
              size="small"
              v-model="query.nameOrPhone"
              placeholder="请输入姓名或手机号"
              class="handle-input mr10"
          ></el-input>
          <span class="dispinline ml5"></span>
          <el-select
              v-model="query.receivePaperInvoice"
              filterable
              size="small"
              placeholder="全部"
              class="w100"
          >
            <el-option value="">全部</el-option>
            <el-option key="1" label="已发派" value="1"></el-option>
            <el-option key="0" label="未派发" value="0"></el-option>
          </el-select>
          <span class="dispinline ml5 font14 color666">创建时间：</span>
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
          <span class="dispinline ml5"></span>
          <el-button
              size="small"
              type="primary"
              icon="el-icon-search"
              @click="handleSearch"
          >查询
          </el-button
          >
        </div>
        <div class="clear"></div>
      </div>
      <el-table
          v-show="timeVisible"
          :data="tableData"
          border
          class="table"
          :max-height="tableH"
          ref="multipleTable"
          header-cell-class-name="table-header"
      >
        <el-table-column
            prop="recharge_time"
            label="日期"
            align="center"
        ></el-table-column>
        <el-table-column
            prop="recharge_pens"
            label="充值人数(人)"
            align="center"
        ></el-table-column>
        <el-table-column
            prop="transactions_number"
            label="充值笔数(笔)"
            align="center"
        ></el-table-column>
        <el-table-column
            prop="recharge_amount"
            label="充值金额(元)"
            align="center"
        ></el-table-column>
      </el-table>

      <el-table
          v-show="peopleVisible"
          :data="tableData"
          border
          class="table"
          :max-height="tableH"
          ref="multipleTable"
          header-cell-class-name="table-header"
      >
        <el-table-column
            prop="name"
            label="收费员姓名"
            align="center"
        ></el-table-column>
        <el-table-column
            prop="phone"
            label="帐号"
            align="center"
        ></el-table-column>
        <el-table-column
            prop="transactions_number"
            label="充值笔数(笔)"
            align="center"
        ></el-table-column>
        <el-table-column
            prop="recharge_amount"
            label="充值金额(元)"
            align="center"
        ></el-table-column>
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
import {reactive, ref} from "vue";
import {InspectionRechargeStatistics, patrolRechargeManagementCount} from "../../api/index";

export default {
  name: "xjcztj",
  data() {
    return {
      tableH:0,
    };
  },
  setup() {
    const query = reactive({
      areaId: "",
      nameOrPhone: "",
      receivePaperInvoice: "",
      group: "",
      pageIndex: 1,
      pageSize: 15,
    });
    const tableData = ref([]);
    const tableCountData = ref({});
    const pageTotal = ref(0);
    // 默认按时间表格数据
    const getData = () => {
      InspectionRechargeStatistics(query).then((res) => {
        tableData.value = res.data.list;
        pageTotal.value = res.data.total;
      });
    };
    getData();

    //获取表格表头数据
    const getPatrolRechargeManagementCount = () => {
      patrolRechargeManagementCount(query).then((res) => {
        tableCountData.value = res.data;
      });
    };
    getPatrolRechargeManagementCount();

    // 查询操作
    const handleSearch = () => {
      query.pageIndex = 1;
      getData();
      getPatrolRechargeManagementCount();
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

    //按时间和人员统计切换table
    const timeVisible = ref(true);
    const peopleVisible = ref(false);

    //按时间统计
    const butTime = () => {
      query.group = "time";
      getData();
      getPatrolRechargeManagementCount();
      peopleVisible.value = false;
      timeVisible.value = true;
    }

    //按人员统计
    const butPeople = () => {
      query.group = "people";
      getData();
      getPatrolRechargeManagementCount();
      timeVisible.value = false;
      peopleVisible.value = true;
    }

    let form = reactive({
      time: "",
      areas: [],
      inspectManages: [],
    });

    return {
      query,
      tableData,
      tableCountData,
      pageTotal,
      form,
      getQueryDate,
      butTime,
      butPeople,
      handleSearch,
      handlePageChange,
      timeVisible,
      peopleVisible,
    };
  },
  methods: {},
  created() {
    let h = document.documentElement.clientHeight || document.body.clientHeight;
    this.tableH = h - 308 + 'px';
  },
};
</script>
