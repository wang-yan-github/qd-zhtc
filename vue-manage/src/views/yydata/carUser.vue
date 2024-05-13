<template>
  <div>
    <div class="container">
      <div class="handle-box">
        <div class="left-panel">
          <el-form inline label-width="80" size="small" class="lineH0">
            <el-form-item label="手机号" class="search-mb0">
              <el-input
                @keyup.enter="handleSearch()"
                size="small"
                v-model="query.phone"
                placeholder="手机号"
                class="w170"
              ></el-input>
            </el-form-item>
            <el-form-item label="车牌号" class="search-mb0">
              <el-input
                @keyup.enter="handleSearch()"
                size="small"
                v-model="query.car_no"
                placeholder="车牌号"
                class="w170"
              ></el-input>
            </el-form-item>
            <el-form-item label="注册时间" class="search-mb0">
              <el-date-picker
                v-model="form.time"
                type="daterange"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                @change="getQueryDate"
              >
              </el-date-picker>
            </el-form-item>

            <el-form-item label="" class="search-mb0">
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
        <el-table-column type="index" label="序号" width="55" align="center">
          <template #default="scope">
            {{ scope.$index + 1 }}
          </template>
        </el-table-column>
        <el-table-column
          prop="phone"
          label="手机号"
          width="130"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="member_portrait"
          label="微信头像"
          width="100"
          align="center"
        >
          <template #default="scope">
            <el-image
              :src="scope.row.member_portrait"
              hide-on-click-modal="true" preview-teleported="true"
              class="table-td-thumb"
              :preview-src-list="[scope.row.member_portrait]"
            >
            </el-image>
          </template>
        </el-table-column>
        <el-table-column align="center" prop="nick_name" label="微信昵称"></el-table-column>
        <el-table-column
          prop="balance"
          label="账户余额"
          width="100"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="register_time"
          label="注册时间"
          width="200"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="openid"
          align="center"
          label="公众号openid"
        ></el-table-column>
        <el-table-column
          prop="operateCarnos"
          label="车牌号"
          width="200"
          align="center"
        >
          <template #default="scope">
            <template
              v-for="(item, index) in scope.row.operateCarnos"
              :key="index"
            >
              <el-tag
                size="small"
                class="mar5 mb5"
                v-if="item.car_type == '1'"
                >{{ item.car_no }}</el-tag
              >
              <el-tag
                size="small"
                class="mar5 mb5"
                v-else-if="item.car_type == '2'"
                type="success"
                >{{ item.car_no }}</el-tag
              >
              <el-tag
                size="small"
                class="mar5 mb5"
                v-if="item.car_type == '3'"
                type="warning"
                >{{ item.car_no }}</el-tag
              >
            </template>
            <!-- <el-tag
              v-for="(item, index) in scope.row.operateCarnos"
              size="small"
              :key="index"
              class="mar5 mb5"
              >{{ item.car_no }}</el-tag
            > -->
          </template>
        </el-table-column>

        <el-table-column label="操作项" align="center" width="100">
          <template #default="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-view"
              @click="handleEdit(scope.$index, scope.row)"
              v-permission="['road_caruser_details', 'park_caruser_details']"
              >查看详情
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

    <el-dialog
      title="查看详情"
      v-model="editVisible"
      width="40%"
      @close="closeDialog"
    >
      <table class="desctable mgb20 w">
        <tr>
          <td class="tit" width="60">账号</td>
          <td>{{ result.car_user.phone }}</td>
          <td class="tit" width="80">昵称</td>
          <td>{{ result.car_user.nick_name }}</td>
        </tr>
        <tr>
          <td class="tit" width="60">账户余额</td>
          <td>{{ result.car_user.balance }}</td>
          <td class="tit" width="80">创建时间</td>
          <td>{{ result.car_user.create_time }}</td>
        </tr>
      </table>
      <el-tabs type="card" v-model="activeName">
        <el-tab-pane label="消费记录" name="first">
          <div class="mt20"></div>
          <el-table
            :data="tableData1"
            border
            class="table"
            ref="multipleTable"
            header-cell-class-name="table-header"
            @selection-change="handleSelectionChange"
          >
            <el-table-column prop="order_no" label="订单" min-width="180" align="center"></el-table-column>
            <el-table-column prop="create_time" label="时间" width="180" align="center"></el-table-column>
            <el-table-column label="账单类型" width="100" align="center"
              >停车缴费
            </el-table-column>
            <el-table-column label="支付方式" width="100" align="center">
              <template #default="scope">
                <el-tag
                  size="small"
                  type="danger"
                  v-if="scope.row.paymentOrder == null"
                  >未支付</el-tag
                >
                <el-tag
                  size="small"
                  type="danger"
                  v-else-if="scope.row.paymentOrder.payment_type == '1'"
                  >包月</el-tag
                >
                <el-tag
                  size="small"
                  type="danger"
                  v-else-if="scope.row.paymentOrder.payment_type == '2'"
                  >微信</el-tag
                >
                <el-tag
                  size="small"
                  type="danger"
                  v-else-if="scope.row.paymentOrder.payment_type == '3'"
                  >支付宝</el-tag
                >
                <el-tag
                  size="small"
                  type="danger"
                  v-else-if="scope.row.paymentOrder.payment_type == '4'"
                  >钱包</el-tag
                >
                <el-tag
                  size="small"
                  type="danger"
                  v-else-if="scope.row.paymentOrder.payment_type == '5'"
                  >现金</el-tag
                >
              </template>
            </el-table-column>
            <el-table-column label="消费" width="100" prop="name" align="center">
              <template #default="scope">
                <el-tag
                  size="small"
                  type="danger"
                  v-if="scope.row.paymentOrder == null"
                  >0</el-tag
                >
                <el-tag size="small" type="danger" v-else>{{
                  scope.row.paymentOrder.amount
                }}</el-tag>
              </template>
            </el-table-column>
          </el-table>
          <div class="pagination">
            <el-pagination
              background
              layout="total, prev, pager, next"
              :current-page="query2.pageIndex"
              :page-size="query2.pageSize"
              :total="pageTotal1"
              @current-change="handlePageChange1"
            ></el-pagination>
          </div>
        </el-tab-pane>
        <el-tab-pane label="充值记录" name="second">
          <div class="mt20"></div>
          <!-- <el-button type="success" size="small">充 值</el-button> -->
          <el-table
            :data="tableData2"
            border
            class="table mt20"
            ref="multipleTable"
            header-cell-class-name="table-header"
            @selection-change="handleSelectionChange"
          >
            <el-table-column prop="paymentNo" label="订单" align="center"></el-table-column>
            <el-table-column prop="name" label="类型" align="center">钱包充值</el-table-column>

            <el-table-column label="支付方式" align="center">
              <template #default="scope">
                <el-tag
                  size="small"
                  type="success"
                  v-if="scope.row.paymentType == '2'"
                  >微信</el-tag
                >
                <el-tag
                  size="small"
                  type="success"
                  v-else-if="scope.row.paymentType == '3'"
                  >支付宝</el-tag
                >
                <el-tag
                  size="small"
                  type="success"
                  v-else-if="scope.row.paymentType == '4'"
                  >钱包</el-tag
                >
                <el-tag
                  size="small"
                  type="success"
                  v-else-if="scope.row.paymentType == '5'"
                  >现金</el-tag
                >
              </template>
            </el-table-column>
            <el-table-column label="金额" prop="recharge_amount" align="center">
            </el-table-column>
          </el-table>
          <div class="pagination">
            <el-pagination
              background
              layout="total, prev, pager, next"
              :current-page="query3.pageIndex"
              :page-size="query3.pageSize"
              :total="pageTotal2"
              @current-change="handlePageChange2"
            ></el-pagination>
          </div>
        </el-tab-pane>
        <el-tab-pane label="车牌" name="third">
          <div class="left-panel">
            <el-form inline label-width="80" size="small" @submit.prevent>
              <el-form-item label="车牌号">
                <el-input
                  size="small"
                  v-model="querycar.carno"
                  class="w200"
                ></el-input>
              </el-form-item>
              <el-form-item label="" class="search-mb0">
                <el-checkbox v-model="isYellow">黄牌</el-checkbox>
              </el-form-item>
              <el-form-item label="">
                <el-button
                  size="small"
                  type="primary"
                  icon="el-icon-search"
                  @click="getCarnoInfo"
                  >查询</el-button
                >
              </el-form-item>
            </el-form>
          </div>
          <table class="desctable mgb20 w">
            <tr>
              <td class="tit" width="60">车牌号</td>
              <td v-text="carinfo.car_no"></td>
              <td class="tit" width="80">车牌类型</td>
              <td v-text="carinfo.carTypeName"></td>
            </tr>
            <tr>
              <td class="tit" width="60">名单类型</td>
              <td v-text="carinfo.rosterTypeName"></td>
              <td class="tit" width="80">所属车主</td>
              <td v-text="carinfo.memberName"></td>
            </tr>
          </table>
          <div align="right">
            <el-button
              size="small"
              type="success"
              icon="el-icon-circle-check"
              @click="bindCarno(carinfo.id)"
              >绑定
            </el-button>
          </div>
          <div class="mt20"></div>

          <el-table
            :data="tableData3"
            border
            class="table mt20"
            ref="multipleTable"
            header-cell-class-name="table-header"
            @selection-change="handleSelectionChange"
          >
            <el-table-column
              prop="bind_date"
              label="绑定时间"
              align="center"
            ></el-table-column>
            <el-table-column prop="car_no" label="车牌" align="center"></el-table-column>

            <el-table-column prop="car_type" label="样式" align="center">
              <template #default="scope">
                <el-tag size="small" v-if="scope.row.car_type == '1'"
                  >蓝牌</el-tag
                >
                <el-tag
                  size="small"
                  type="success"
                  v-else-if="scope.row.car_type == '2'"
                  >绿牌</el-tag
                >
                <el-tag
                  size="small"
                  type="warning"
                  v-else-if="scope.row.car_type == '3'"
                  >黄牌</el-tag
                >
              </template>
            </el-table-column>
            <el-table-column label="操作" align="center">
              <template #default="scope">
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-circle-close"
                  @click="bindRoad(scope.$index, scope.row)"
                  >解绑
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="editVisible = false">关 闭</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { reactive, ref } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import {
  memberManageList,
  operateCarnoData,
  queryChargeRecords,
  queryOrder,
} from "../../api/index";
import { getCarnoDetail } from "../../api/companyManage.js";
import { bindCarPerson, relieveBind } from "../../api/operateCar";
import Ueditor from "../../components/UE.vue";

export default {
  name: "caruser",
  components: {
    Ueditor,
  },
  data() {
    return {
      tableH:0,
    };
  },
  setup() {
    const query = reactive({
      phone: "",
      car_no: "",
      create_time: "",
      pageIndex: 1,
      pageSize: 15,
      start_time: "",
      end_time: "",
    });
    let result = reactive({
      car_user: {},
    });
    const tableData = ref([]);
    const tableData1 = ref([]);
    const tableData2 = ref([]);
    const tableData3 = ref([]);
    const pageTotal = ref(0);
    const pageTotal1 = ref(0);
    const pageTotal2 = ref(0);
    const isYellow = ref(false);
    const carform = ref({});
    const carinfo = ref({});
    const closeDialog = () => {
      //done();
      getData();
    };
    // 获取表格数据
    const getData = () => {
      memberManageList(query).then((res) => {
        tableData.value = res.data.list;
        pageTotal.value = res.data.total;
      });
    };
    getData();

    // 车牌绑定记录
    const memberId = ref("");
    const getAllOperateCarno = (member_id) => {
      let member = reactive({
        member_id: member_id,
      });
      operateCarnoData(member).then((res) => {
        console.log("sss");
        console.log(res.data);
        tableData3.value = res.data;
        console.log(tableData3);
      });
    };

    const query2 = reactive({
      pageIndex: 1,
      pageSize: 3,
    });
    //消费记录
    const getOrderByMemberId = (memberId) => {
      query2.memberId = memberId;
      queryOrder(query2).then((res) => {
        tableData1.value = res.data.list;
        pageTotal1.value = res.data.total;
      });
    };

    const query3 = reactive({
      pageIndex: 1,
      pageSize: 3,
    });
    //充值记录
    const getRechargeRecord = (memberId) => {
      query3.memberId = memberId;
      queryChargeRecords(query3).then((res) => {
        tableData2.value = res.data.list;
        pageTotal2.value = res.data.total;
      });
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

    const handlePageChange1 = (val) => {
      query2.pageIndex = val;
      getOrderByMemberId(memberId.value);
    };

    const handlePageChange2 = (val) => {
      query2.pageIndex = val;
      getRechargeRecord(memberId.value);
    };

    // 删除操作
    const handleDelete = (index) => {
      // 二次确认删除
      ElMessageBox.confirm("确定要删除吗？", "提示", {
        type: "warning",
      })
        .then(() => {
          ElMessage.success("删除成功");
          tableData.value.splice(index, 1);
        })
        .catch(() => {});
    };
    const dialogT = "新增";

    // 表格编辑时弹窗和保存
    const editVisible = ref(false);
    const viewVisible = ref(false);
    let form = reactive({
      name: "",
      address: "",
      time: "",
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
      checked: false,
      startTime: "",
      endTime: "",
    });
    let idx = -1;
    const handleEdit = (index, row) => {
      (querycar.value.carno = ""),
        (carinfo.value = {}),
        (memberId.value = row.id);
      idx = index;
      result.car_user = row;

      getAllOperateCarno(row.id);
      getOrderByMemberId(row.id);
      getRechargeRecord(row.id);

      editVisible.value = true;
    };

    const handleView = (index, row) => {
      ElMessageBox.confirm(
        "将解除车牌现有绑定者，重新绑定到申诉用户，确定审核通过吗？",
        "提示",
        {
          type: "warning",
        }
      )
        .then(() => {
          ElMessage.success("解除成功");
          that.tableData.splice(indexI, 1);
        })
        .catch(() => {
          ElMessage.info("取消解除");
        });
    };

    const saveEdit = () => {
      editVisible.value = false;
      ElMessage.success(`修改第 ${idx + 1} 行成功`);
      Object.keys(form).forEach((item) => {
        tableData.value[idx][item] = form[item];
      });
    };
    //日期控件change方法
    const getQueryDate = () => {
      if (null == form.time || [] == form.time || "" == form.time) {
        query.start_time = "";
        query.end_time = "";
      } else {
        query.start_time = dateFormat(form.time[0]);
        query.end_time = dateFormat(form.time[1]);
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
    const bindRoad = (index, row) => {
      ElMessageBox.confirm("确定解绑该车牌吗？", "提示", {
        type: "warning",
      }).then(() => {
        let edit_OperateCarno = reactive({
          id: row.id,
          member_id: -1,
        });
        relieveBind(reactive({ carnoId: row.id })).then((res) => {
          if (res.code === 0) {
            ElMessage.success("解绑成功");
            getAllOperateCarno(memberId.value);
          } else {
            ElMessage.success("解绑失败");
          }
        });
        // editOperateCarno(edit_OperateCarno).then((res) => {
        //   ElMessage.success("解绑成功");
        // }).then(()=>{
        //   getAllOperateCarno(row.id);
        // });
      });
    };
    const querycar = ref({
      carno: "",
    });
    // 获取车牌信息
    const getCarnoInfo = () => {
      let carType = "1";
      if (querycar.value.carno.length == 8) {
        carType = "2";
      } else if (isYellow.value) {
        carType = "3";
      }
      let carnoParams = reactive({
        carno: querycar.value.carno,
        carType: carType,
      });
      getCarnoDetail(carnoParams).then((res) => {
        if (res.code == 0) {
          carinfo.value = res.data;
        } else {
          carinfo.value = {};
          ElMessage({
            showClose: true,
            message: res.msg,
            type: "error",
          });
        }
      });
    };
    // 绑定车牌
    const bindCarno = (index) => {
      if (tableData3.value.length >= 5) {
        ElMessage.error("每名用户最多绑定5个车牌");
        return false;
      }
      if (index != null && index != "") {
        let bindInfoParams = reactive({
          carId: index,
          memberId: memberId.value,
        });
        bindCarPerson(bindInfoParams).then((res) => {
          if (res.code == 0) {
            ElMessage({
              showClose: true,
              message: "绑定成功！",
              type: "success",
            });
            getAllOperateCarno(memberId.value);
          } else {
            ElMessage({
              showClose: true,
              message: res.msg,
              type: "error",
            });
          }
        });
      } else {
        ElMessage({
          showClose: true,
          message: "请先查询需要绑定的车牌信息！",
          type: "error",
        });
      }
    };

    return {
      query,
      query2,
      query3,
      querycar,
      memberId,
      isYellow,
      bindRoad,
      getCarnoInfo,
      bindCarno,
      getQueryDate,
      closeDialog,
      result,
      carform,
      carinfo,
      tableData,
      tableData1,
      tableData2,
      tableData3,
      pageTotal,
      editVisible,
      viewVisible,
      pageTotal1,
      pageTotal2,
      form,
      dialogT,
      handleSearch,
      handlePageChange,
      handlePageChange1,
      handleDelete,
      handleEdit,
      handleView,
      saveEdit,
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
    handleDeleteAll() {
      var that = this;
      var val = this.selectedData;

      if (val) {
        val.forEach(function (item, index) {
          that.tableData.forEach(function (itemI, indexI) {
            if (item === itemI) {
              // ElMessageBox.confirm("确定要删除吗？", "提示", {
              //   type: "warning",
              // })
              //   .then(() => {
              //     ElMessage.success("删除成功");
              //     that.tableData.splice(indexI, 1);
              //   })
              //   .catch(() => {
              //     ElMessage.info("取消删除");
              //   });
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
