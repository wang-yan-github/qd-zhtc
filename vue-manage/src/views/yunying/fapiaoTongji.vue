<template>
  <div>
    <div class="container">
      <el-row>
        <el-col :span="24">
          <div class="top-panel">
            <div class="left-panel">
              <el-form label-width="80px" inline size="small">
                <el-form-item>
                  <el-input
                    @keyup.enter="handleSearch()"
                    v-model="query.phone"
                    placeholder="用户手机号码"
                  ></el-input>
                </el-form-item>
                <!-- <el-form-item>
                      <el-select
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
                  </el-form-item> -->
                <el-form-item>
                  <el-select
                    clearable
                    v-model="query.invoice_type"
                    filterable
                    placeholder="全部"
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
                </el-form-item>
              </el-form>
            </div>
            <div class="right-panel">
              <el-button size="small" plain type="danger"
                >个人(元)：{{ form.dianzi }}</el-button
              >
              <el-button size="small" plain type="danger"
                >企业(元)：{{ form.shousi }}</el-button
              >
            </div>
            <div class="clearfix"></div>
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
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="index" label="序号" width="55" align="center">
        </el-table-column>
        <el-table-column
          prop="userName"
          label="微信昵称"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="phone"
          label="手机号"
          width="200"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="balance"
          label="开票金额余额(元)"
          width="200"
          align="center"
        >
        </el-table-column>
        <el-table-column prop="type" label="类型" align="center" width="200">
        </el-table-column>
        <el-table-column prop="source" label="来源" align="center" width="200">
        </el-table-column>

        <el-table-column
          prop="time"
          label="申请/发放时间"
          width="200"
          align="center"
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
  getRoadParkingInvoicing,
  getInvoiceAmount,
  queryAreaData,
} from "../../api/index";

export default {
  name: "roadTongji",
  data() {
    return {
      tableH: 0,
    };
  },
  setup() {
    const query = reactive({
      phone: "",
      invoice_type: "",
      startTime: "",
      endTime: "",
      pageIndex: 1,
      pageSize: 18,
    });
    const tableData = ref([]);
    const pageTotal = ref(0);
    let form = reactive({
      time: "",
      invoice_type: "",
      czroptions: [
        {
          value: "0",
          label: "个人",
        },
        {
          value: "1",
          label: "企业",
        },
      ],
      selvalue: "",
      date1: "",
      date2: "",
      dianzi: "",
      shousi: "",
    });

    //获取区域下拉框数据
    const queryArea = reactive({});
    const getArea = () => {
      queryAreaData(queryArea).then((res) => {
        form.areas = res.data;
      });
    };
    getArea();

    // 获取表格数据
    const getData = () => {
      getRoadParkingInvoicing(query).then((res) => {
        tableData.value = res.data.list;
        pageTotal.value = res.data.total;
      });
    };
    getData();

    const getInvoice = () => {
      query.invoice_type = "0";
      getInvoiceAmount(query).then((res) => {
        if (res.data != null) {
          form.dianzi = res.data;
        } else {
          form.dianzi = "0.0";
        }
      });
      query.invoice_type = "1";
      getInvoiceAmount(query).then((res) => {
        if (res.data != null) {
          form.shousi = res.data;
        } else {
          form.shousi = "0.0";
        }
      });
    };

    getInvoice();

    query.invoice_type = "";

    // 查询操作
    const handleSearch = () => {
      query.pageIndex = 1;
      var type = query.invoice_type;
      getData();
      getInvoice();
      query.invoice_type = type;
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
      form,
      pageTotal,
      handleSearch,
      handlePageChange,
      getQueryDate,
    };
  },
  methods: {},
  created() {
    let h = document.documentElement.clientHeight || document.body.clientHeight;
    this.tableH = h - 308 + "px";
  },
};
</script>

