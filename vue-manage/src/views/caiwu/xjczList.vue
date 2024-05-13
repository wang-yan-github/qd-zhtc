<template>
  <div>

    <div class="container">
      <div class="handle-box">
        <div class="left-panel">
          <el-row class="">
            <el-col :span="24">
              <el-button size="small" plain type="danger">充值人数(人)：
                {{ tableCountData.recharge_pens }}
              </el-button>
              <el-button size="small" plain type="danger">充值笔数(笔)：
                {{ tableCountData.transactions_number }}
              </el-button>
              <el-button size="small" plain type="danger">充值金额(元)：
                {{ tableCountData.recharge_amount }}
              </el-button>
            </el-col>
          </el-row>

        </div>
        <div class="right-panel">
          <el-input
              @keyup.enter="handleSearch()"
              size="small"
              v-model="query.nameOrPhone"
              placeholder="请输入姓名或手机号"
              class="handle-input mr10"
          ></el-input>
          <span class="dispinline ml5"></span>
          <el-select
              clearable
              v-model="query.receivePaperInvoice"
              filterable
              size="small"
              placeholder="全部"
              class="w100"
          >
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
          </el-button>
          <span class="dispinline ml5"></span>
          <el-button
              type="success"
              size="small"
              icon="el-icon-plus"
              @click="handleEdit(0, null, true)"
              v-permission="['road_xunjianczmana_cz', 'park_shoufeiczmana_cz']"
          >充值
          </el-button>
        </div>
        <div class="clear"></div>
      </div>
      <el-table
          :data="tableData"
          border
          class="table"
          ref="multipleTable"
          :max-height="tableH"
          header-cell-class-name="table-header"
          @selection-change="handleSelectionChange"
      >
        <el-table-column
            type="selection"
            width="55"
            align="center"
        ></el-table-column>
        <el-table-column type="index" label="序号" width="55" align="center"></el-table-column>
        <el-table-column
            prop="name"
            label="姓名"
            width="100"
            align="center"
        ></el-table-column>
        <el-table-column
            prop="phone"
            label="手机号码"
            align="center"
        ></el-table-column>
        <el-table-column
            prop="recharge_time"
            label="充值时间"
            align="center"
        ></el-table-column>
        <el-table-column
            prop="recharge_amount"
            label="充值金额(元)"
            align="center"
        ></el-table-column>
        <el-table-column label="是否已派放发票" align="center">
          <template #default="scope">
            {{ scope.row.receive_paper_invoice == 1 ? "已派发" : "未派发" }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="220" align="center">
          <template #default="scope">
            <el-button
                v-if="scope.row.receive_paper_invoice != 1"
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="handleInvoiceReceiving(scope.$index, scope.row, false)"
                v-permission="['road_xunjianczmana_lqfpdj', 'park_shoufeiczmana_lqfpdj']"
            >领取发票登记
            </el-button>
            <el-button
                v-if="scope.row.receive_paper_invoice != 1"
                size="mini"
                type="text"
                icon="el-icon-delete"
                class="red"
                @click="handleDelete(scope.$index, scope.row)"
                v-permission="['road_xunjianczmana_delete', 'park_shoufeiczmana_delete']"
            >删除
            </el-button
            >
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

    <!-- 充值验证弹出框 -->
    <el-dialog title="充值验证" v-model="editVisible" width="30%">
      <el-form @submit.native.prevent label-width="90px" size="small">
        <el-form-item label="充值密码">
          <el-input type="password" v-model="formPatrolRecharge.password"></el-input>
        </el-form-item>
<!--        <el-form-item label="短信验证码">-->
<!--          <el-input v-model="formPatrolRecharge.code">-->
<!--            <template v-slot:append>-->
<!--              <el-button type="primary" icon="el-icon-s-promotion">获取验证码</el-button>-->
<!--            </template>-->
<!--          </el-input>-->
<!--        </el-form-item>-->
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="editVisible = false">取 消</el-button>
          <el-button type="primary" @click="saveEdit">去充值</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 充值弹出框 -->
    <el-dialog title="充值" v-model="patrolRechargeVisible" width="30%">
      <el-form label-width="5px" size="small">
        <el-form-item label="">
          <el-select
              v-model="formPatrolRecharge.inspectId"
              placeholder="请选择巡检员"
              filterable
              size="small"
              class="w"
          >
            <el-option
                v-for="item in form.inspectManages"
                :key="item.id"
                :label="item.name"
                :value="item.id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="">
          <el-input type="number" placeholder="请输入充值金额" v-model="formPatrolRecharge.rechargeAmount"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="patrolRechargeVisible = false">取 消</el-button>
          <el-button type="primary" @click="savePatrolRecharge">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import {reactive, ref} from "vue";
import {ElMessage, ElMessageBox} from "element-plus";
import {
  deletePatrolRechargeManagement,
  inspectManageSelectList,
  invoiceReceiving,
  listPatrolRechargeManagement,
  patrolRecharge,
  patrolRechargeManagementCount,
  queryAreaData,
  rechargeVerification,
} from "../../api/index";

export default {
  name: "xjczmana",
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
      pageIndex: 1,
      pageSize: 15,
    });
    const tableData = ref([]);
    const tableCountData = ref({});
    const pageTotal = ref(0);
    // 获取表格数据
    const getData = () => {
      listPatrolRechargeManagement(query).then((res) => {
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
      })
    }
    getArea();

    //获取表格表头数据
    const getPatrolRechargeManagementCount = () => {
      patrolRechargeManagementCount(query).then((res) => {
        tableCountData.value = res.data;
      });
    };
    getPatrolRechargeManagementCount();

    //获取巡检员下拉框数据
    const getInspectManageSelectList = () => {
      inspectManageSelectList().then((res) => {
        form.inspectManages = res.data;
      })
    }
    getInspectManageSelectList();

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
      getPatrolRechargeManagementCount();
    };

    // 删除操作
    const handleDelete = (index, row) => {
      // 二次确认删除
      ElMessageBox.confirm("确定要删除吗？", "提示", {
        type: "warning",
      })
          .then(() => {
            deletePatrolRechargeManagement({"PatrolRechargeManagementId": row.id}).then(() => {
            })
            ElMessage.success("删除成功");
            getData();
            getPatrolRechargeManagementCount();
            tableData.value.splice(index, 1);
          })
          .catch(() => {
          });
    };
    // 领取发票等级
    const handleInvoiceReceiving = (index, row) => {
      // 二次确认
      ElMessageBox.confirm("是否领取发票登记？", "提示", {
        type: "warning",
      })
          .then(() => {
            invoiceReceiving({"PatrolRechargeManagementId": row.id}).then(() => {
              ElMessage.success("成功领取发票登记");
              getData();
              getPatrolRechargeManagementCount();
            })
          })
          .catch(() => {
          });
    };
    const dialogT = "充值";

    // 表格编辑时弹窗和保存
    const editVisible = ref(false);
    const patrolRechargeVisible = ref(false);
    let form = reactive({
      time: "",
      areas: [],
      inspectManages: [],
    });
    let formPatrolRecharge = reactive({
      password: "",
      code: "",
      inspectId: "",
      rechargeAmount: "",
    });
    let idx = -1;
    const handleEdit = (index, row, type) => {
      if (type) {
        // dialogT='新增'
      } else {
        //dialogT='编辑';
        idx = index;
        Object.keys(form).forEach((item) => {
          form[item] = row[item];
        });
      }

      editVisible.value = true;
    };

    const saveEdit = () => {
      if (formPatrolRecharge.password) {
        rechargeVerification(formPatrolRecharge).then((res) => {
          if (res.data == true) {
            formPatrolRecharge.password = '';
            formPatrolRecharge.code = '';
            editVisible.value = false;
            patrolRechargeVisible.value = true;
            getData();
            getPatrolRechargeManagementCount();
          } else {
            ElMessage.warning(`充值密码不正确`);
          }
        })
      } else {
        ElMessage.warning(`请输入充值密码`);
      }
    };
    const savePatrolRecharge = () => {
      if (formPatrolRecharge.inspectId && formPatrolRecharge.rechargeAmount) {
        patrolRecharge(formPatrolRecharge).then((res) => {
          formPatrolRecharge.inspectId = '';
          formPatrolRecharge.rechargeAmount = '';
          patrolRechargeVisible.value = false;
          getData();
          getPatrolRechargeManagementCount();
        })
      } else {
        ElMessage.warning(`请输入充值金额或巡检员`);
      }
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

    return {
      query,
      tableData,
      tableCountData,
      pageTotal,
      editVisible,
      patrolRechargeVisible,
      form,
      formPatrolRecharge,
      dialogT,
      getQueryDate,
      handleSearch,
      handlePageChange,
      handleDelete,
      handleInvoiceReceiving,
      handleEdit,
      saveEdit,
      savePatrolRecharge,
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
    this.tableH = h - 308 + 'px';
  },
};
</script>
