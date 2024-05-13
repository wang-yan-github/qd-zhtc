<template>
  <div>
    <div class="container">
      <div class="handle-box f-cb">
        <el-form size="small" inline class="lineH0">
          <el-form-item label="手机号" class="search-mb0">
            <el-input
              @keyup.enter="handleSearch()"
              v-model="query.phone"
              placeholder="手机号"
              class="handle-input mr10 w170"
            ></el-input>
          </el-form-item>
          <el-form-item label="开票状态" class="search-mb0">
            <el-select
              v-model="query.invoice_mode"
              filterable
              size="small"
              placeholder="全部"
              class="w100"
            >
              <el-option key="" label="所有" value=""></el-option>
              <el-option
                v-for="item in invoicesMode"
                :key="item.dc_value"
                :label="item.label"
                :value="item.dc_value"
              ></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="" class="search-mb0">
            <el-button
              size="small"
              type="primary"
              icon="el-icon-search"
              @click="handleSearch"
              >查询
            </el-button>
          </el-form-item>
        </el-form>
      </div>
      <el-table
        :data="tableData"
        border
        class="table"
        ref="multipleTable"
        :max-height="tableH"
        header-cell-class-name="table-header"
      >
        <el-table-column type="index" label="序号" width="55" align="center">
          <template #default="scope">
            {{ scope.$index + 1 }}
          </template>
        </el-table-column>
        <el-table-column
          prop="memberName"
          label="申请人"
          width="100"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="phone"
          label="手机号"
          width="150"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="invoice_type"
          label="发票类型"
          width="150"
          align="center"
        ></el-table-column>
        <el-table-column label="发票信息" align="center">
          <template #default="scope">
            {{
              scope.row.invoice_header == null || scope.row.invoice_header == ""
                ? "--"
                : scope.row.invoice_header
            }}<br />
            {{
              scope.row.duty_paragraph == null || scope.row.duty_paragraph == ""
                ? "--"
                : scope.row.duty_paragraph
            }}<br />
            {{
              scope.row.address == null || scope.row.address == ""
                ? "--"
                : scope.row.address
            }}<br />
            {{
              scope.row.bank_of_deposit == null ||
              scope.row.bank_of_deposit == ""
                ? "--"
                : scope.row.bank_of_deposit
            }}<br />
            {{
              scope.row.bank_account == null || scope.row.bank_account == ""
                ? "--"
                : scope.row.bank_account
            }}<br />
            {{
              scope.row.contact_info == null || scope.row.contact_info == ""
                ? "--"
                : scope.row.contact_info
            }}<br />
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          label="收票人"
          prop="receiver"
          width="100"
        ></el-table-column>
        <el-table-column
          align="center"
          label="收票人手机号"
          prop="receive_phone"
          width="150"
        ></el-table-column>
        <el-table-column
          align="center"
          label="收票人地址"
          prop="receive_address"
          width="100"
        ></el-table-column>
        <el-table-column
          prop="application_time"
          label="申请时间"
          align="center"
          :formatter="dateFormat"
          width="180"
        ></el-table-column>
        <el-table-column
          prop="invoice_mode"
          label="状态"
          align="center"
          width="100"
        >
        </el-table-column>
        <el-table-column label="操作" width="200" align="center">
          <template #default="scope">
            <el-button
              v-if="scope.row.invoice_mode == '已开票'"
              size="mini"
              type="text"
              icon="el-icon-finished"
              disabled
            >
              开票完成
            </el-button>
            <el-button
              v-else
              size="mini"
              type="text"
              icon="el-icon-finished"
              @click="approvalInvoicing(scope.$index, scope.row)"
              v-permission="['road_dkfplist_kpwc', 'park_dkfplist_kpwc']"
            >
              开票完成
            </el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-finished"
              @click="openDetails(scope.$index, scope.row)"
              v-permission="['road_dkfplist_kpxq', 'park_dkfplist_kpxq']"
            >
              开票详情
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

    <!--发票详情-->
    <el-dialog title="发票详情" v-model="detailsVisible" width="45%">
      <el-table
        :data="tableDetailsData"
        border
        class="table"
        ref="multipleTable"
        header-cell-class-name="table-header"
      >
        <el-table-column type="index" label="序号" width="55" align="center">
        </el-table-column>
        <el-table-column prop="order_no" label="订单号" align="center">
        </el-table-column>
        <el-table-column prop="drivein_time" label="进场时间" align="center">
        </el-table-column>
        <el-table-column prop="driveout_time" label="出场时间" align="center">
        </el-table-column>
        <el-table-column prop="paid_amount" label="已付金额" align="center">
        </el-table-column>
      </el-table>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="detailsVisible = false">关 闭</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { reactive, ref } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import {
  getDictsByType,
  getFapiaoList,
  getInvoiceDetails,
  invoicingApproval,
} from "../../api/fapiao.js";

export default {
  name: "dkfplist",
  data() {
    return {
      tableH: 0,
    };
  },
  setup() {
    const query = reactive({
      phone: "",
      invoice_mode: "",
      pageIndex: 1,
      pageSize: 10,
    });
    const tableData = ref([]);
    const tableDetailsData = ref([]);
    const pageTotal = ref(0);
    const invoicesMode = ref([]);
    // 获取表格数据
    const getDictsMode = () => {
      getDictsByType(reactive({ type: "invoice_mode" })).then((res) => {
        invoicesMode.value = res.data;
      });
    };
    getDictsMode();
    // 获取表格数据
    const getData = () => {
      getFapiaoList(query).then((res) => {
        let pageinfo = res.data;
        tableData.value = pageinfo.list;
        pageTotal.value = pageinfo.total;
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

    // 审核
    const approvalInvoicing = (index, row) => {
      // 二次确认删除
      ElMessageBox.confirm("确定开票完成", "提示", {
        type: "warning",
      })
        .then(() => {
          const approvalQuery = reactive({
            id: row.id,
          });
          invoicingApproval(approvalQuery).then((res) => {
            if (res.code == 0) {
              ElMessage.success("操作成功！");
              getData();
            } else {
              ElMessage.error(res.msg);
            }
          });
        })
        .catch(() => {});
    };
    const dialogT = "新增";

    const openDetails = (index, row) => {
      getInvoiceDetails({ invoicesId: row.id }).then((res) => {
        tableDetailsData.value = res.data;
        detailsVisible.value = true;
      });
    };

    // 发票详情弹出框
    const detailsVisible = ref(false);
    let form = reactive({});

    //日期格式化
    const dateFormat = (row, column) => {
      var t = new Date(row.application_time); //row 表示一行数据, updateTime 表示要格式化的字段名称
      var year = t.getFullYear(),
        month = t.getMonth() + 1,
        day = t.getDate(),
        hour = t.getHours(),
        min = t.getMinutes(),
        sec = t.getSeconds();
      var newTime =
        year +
        "-" +
        (month < 10 ? "0" + month : month) +
        "-" +
        (day < 10 ? "0" + day : day) +
        " " +
        (hour < 10 ? "0" + hour : hour) +
        ":" +
        (min < 10 ? "0" + min : min) +
        ":" +
        (sec < 10 ? "0" + sec : sec);
      return newTime;
    };

    return {
      query,
      tableData,
      tableDetailsData,
      pageTotal,
      dateFormat,
      detailsVisible,
      form,
      dialogT,
      invoicesMode,
      getDictsMode,
      handleSearch,
      handlePageChange,
      approvalInvoicing,
      openDetails,
      multipleSelection: [],
      value: true,
      activeName: "first",
    };
  },
  methods: {
    handleSelectionChange(data) {
      this.selectedData = data;
    },
    handleDeleteAll() {
      var that = this;
      var val = this.selectedData;

      if (val) {
        val.forEach(function (item, index) {
          that.tableData.forEach(function (itemI, indexI) {
            if (item === itemI) {
              that.tableData.splice(indexI, 1);
            }
          });
        });
        ElMessage.success("删除成功");
        this.$refs.multipleTable.clearSelection();
      } else {
        ElMessage.warning(`请选择一条记录`);
      }
    },
  },
  created() {
    let h = document.documentElement.clientHeight || document.body.clientHeight;
    this.tableH = h - 308 + "px";
  },
};
</script>
