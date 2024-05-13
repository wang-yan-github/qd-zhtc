<template>
  <div>
    <div class="container">
      <div class="handle-box">
        <div class="left-panel">
          <el-button v-permission="['road_recharge_add', 'park_recharge_add']" type="primary" size="small"
            icon="el-icon-plus" @click="handleEdit(0, null, true)">添加</el-button>
          <el-button v-permission="['road_recharge_deleteAll', 'park_recharge_deleteAll']" type="danger" size="small"
            icon="el-icon-delete" @click="handleDeleteAll()">批量删除</el-button>
        </div>
        <div class="right-panel">
          <el-input @keyup.enter="handleSearch()" size="small" v-model="query.activity_name" placeholder="活动名称"
            class="handle-input mr10"></el-input>
          <el-button size="small" type="primary" icon="el-icon-search" @click="handleSearch">查询</el-button>
        </div>
        <div class="clear"></div>
      </div>
      <el-table :data="tableData" border class="table" ref="multipleTable" :max-height="tableH"
        header-cell-class-name="table-header" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center"></el-table-column>
        <el-table-column label="序号" width="55" align="center">
          <template #default="scope">
            {{ scope.$index + 1 }}
          </template>
        </el-table-column>
        <el-table-column prop="activity_name" label="活动名称" align="center"></el-table-column>
        <!-- <el-table-column label="价格" width="100" align="center">
                  <template #default="scope">{{ scope.row.money }}</template>
                </el-table-column> -->
        <el-table-column prop="start_time" label="开始时间" align="center" width="180"></el-table-column>
        <el-table-column prop="end_time" label="结束时间" align="center" width="180"></el-table-column>
        <el-table-column label="活动类型" align="center" width="100">
          <template #default="scope">
            <el-tag size="small" v-if="scope.row.type == '1'">普通充值 </el-tag>
            <el-tag size="small" v-if="scope.row.type == '2'">活动充值 </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="启用" align="center" width="100">
          <template #default="scope">
            <el-tag size="small" :type="scope.row.status === '0'
                ? 'success'
                : scope.row.status === '1'
                  ? 'danger'
                  : ''
              " v-if="scope.row.status == 0">启用
            </el-tag>
            <el-tag size="small" :type="scope.row.status === '0'
                ? 'success'
                : scope.row.status === '1'
                  ? 'danger'
                  : ''
              " v-else>禁用
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="sort" label="排序" align="center" width="170">
        </el-table-column>
        <el-table-column prop="create_time" label="创建时间" align="center" width="170">
        </el-table-column>
        <el-table-column label="操作" width="180" align="center">
          <template #default="scope">
            <el-button v-permission="['road_recharge_edit', 'park_recharge_edit']" size="mini" type="text"
              icon="el-icon-edit" @click="handleEdit(scope.$index, scope.row, false)">编辑
            </el-button>
            <el-button v-permission="['road_recharge_status', 'park_recharge_status']" size="mini" type="text"
              icon="el-icon-circle-close" class="red" @click="handlejinyong(scope.$index, scope.row)"
              v-if="scope.row.status == 0">
              禁用
            </el-button>
            <el-button v-permission="['road_recharge_status', 'park_recharge_status']" size="mini" type="text"
              icon="el-icon-circle-check" @click="handleqiyong(scope.$index, scope.row)" v-else>
              启用
            </el-button>
            <el-button v-permission="['road_recharge_delete', 'park_recharge_delete']" size="mini" type="text"
              icon="el-icon-delete" class="red" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination">
        <el-pagination background layout="total, prev, pager, next" :current-page="query.pageIndex"
          :page-size="query.pageSize" :total="pageTotal" @current-change="handlePageChange"></el-pagination>
      </div>
    </div>

    <!-- 编辑弹出框 -->
    <el-dialog :title="dialogT" v-model="editVisible" width="30%">
      <el-tabs type="card" v-model="activeName">
        <el-tab-pane label="活动信息" name="first">
          <div class="mt20"></div>
          <el-form label-width="80px" :rules="formRules" :model="form">
            <el-form-item prop="type" label="活动类型">
              <el-radio-group v-model="form.type" @change="changeHandler">
                <el-radio :label="1">普通充值</el-radio>
                <el-radio :label="2">活动充值</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="活动名称" prop="activity_name">
              <el-input v-model="form.activity_name" placeholder="请输入活动名称"></el-input>
            </el-form-item>
            <el-form-item label="开始时间" prop="start_time" v-show="form.showtime">
              <el-date-picker v-model="form.start_time" placeholder="请输入开始时间" style="width:100%;" @change="getStartDate"></el-date-picker>
            </el-form-item>
            <el-form-item label="结束时间" prop="end_time" v-show="form.showtime">
              <el-date-picker v-model="form.end_time" placeholder="请输入结束时间" style="width:100%;" @change="getEndDate"></el-date-picker>
            </el-form-item>
            <el-form-item label="排序" prop="sort">
              <el-input type="number" placeholder="请输入排序" v-model="form.sort"></el-input>
              <el-lable style="color: red">*按排序号，从小到大排列</el-lable>
            </el-form-item>
          </el-form>
        </el-tab-pane>
        <el-tab-pane label="赠送规则" name="second">
          <table class="w desctable">
            <colgroup>
              <col />
              <col />
              <col class="w100" />
            </colgroup>
            <thead>
              <th>充值金额</th>
              <th>赠送金额</th>
              <th>
                <el-button round size="mini" type="primary" icon="el-icon-plus" @click="addItem"></el-button>
              </th>
            </thead>
            <tbody>
              <tr v-for="(item, index) in formArr" :key="index">
                <td>
                  <el-input type="number" size="mini" v-model="item.recharge_amount"><template
                      v-slot:append>元</template></el-input>
                </td>
                <td>
                  <el-input type="number" size="mini" v-model="item.additional_amount"><template
                      v-slot:append>元</template></el-input>
                </td>
                <td align="center">
                  <el-button round size="mini" type="danger" icon="el-icon-delete" @click="delItem(index)"></el-button>
                </td>
              </tr>
            </tbody>
          </table>
        </el-tab-pane>
      </el-tabs>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="editVisible = false">取 消</el-button>
          <el-button type="primary" @click="saveEdit" :disabled="saveVisible">确 定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { reactive, ref } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import {
  addRechargeActivity,
  delAllRechargeActivity,
  delRechargeActivity,
  editRechargeActivity,
  getRechargeActivityById,
  rechargeActivityData,
} from "../../api/index";

export default {
  name: "userList",
  data() {
    return {
      tableH: 0,
    };
  },
  setup() {
    const query = reactive({
      address: "",
      name: "",
      pageIndex: 1,
      pageSize: 15,
    });
    const tableData = ref([]);
    const pageTotal = ref(0);
    // 获取表格数据
    const getData = () => {
      rechargeActivityData(query).then((res) => {
        tableData.value = res.data.list;
        pageTotal.value = res.data.total;
      });
    };
    getData();

    //日期控件change方法
    const getStartDate = () => {
      form.start_time = dateFormat(form.start_time);
    };
    const getEndDate = () => {
      form.end_time = dateFormat(form.end_time);
    };
    //日期格式化
    const dateFormat = (time) => {
      return `${time.getFullYear()}-${time.getMonth() + 1 >= 10
          ? time.getMonth() + 1
          : "0" + (time.getMonth() + 1)
        }-${time.getDate() >= 10 ? time.getDate() : "0" + time.getDate()}`;
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

    // 启用操作
    const handleqiyong = (index, row) => {
      // 二次确认
      ElMessageBox.confirm("确定要启用吗？", "提示", {
        type: "warning",
      })
        .then(() => {
          let qiyong = reactive({
            status: "0",
            id: row.id,
          });
          delRechargeActivity(qiyong)
            .then((res) => {
              ElMessage.success("启用成功");
            })
            .then(() => {
              getData();
            });
        })
        .catch(() => { });
    };

    // 禁用操作
    const handlejinyong = (index, row) => {
      // 二次确认
      ElMessageBox.confirm("确定要禁用吗？", "提示", {
        type: "warning",
      })
        .then(() => {
          let jinyong = reactive({
            status: "1",
            id: row.id,
          });
          delRechargeActivity(jinyong)
            .then((res) => {
              ElMessage.success("禁用成功");
            })
            .then(() => {
              getData();
            });
        })
        .catch(() => { });
    };

    // 删除操作
    const handleDelete = (index, row) => {
      // 二次确认删除
      ElMessageBox.confirm("确定要删除吗？", "提示", {
        type: "warning",
      })
        .then(() => {
          let del = reactive({
            is_del: "1",
            id: row.id,
          });
          delRechargeActivity(del)
            .then((res) => {
              ElMessage.success("删除成功");
            })
            .then(() => {
              getData();
            });
        })
        .catch(() => { });
    };
    const dialogT = "新增";

    // 表格编辑时弹窗和保存
    const editVisible = ref(false);
    const saveVisible = ref(true);
    let form = reactive({
      activity_name: "",
      start_time: "",
      end_time: "",
      sort: "",
      type: "",
      showtime: "",
      // rechargeActivityConfigs: [],
    });

    const formArr = ref([{ recharge_amount: "", additional_amount: "" }]);

    let idx = "";
    const handleEdit = (index, row, type) => {
      if (type) {
        //dialogT='新增'
        idx = "";
        form.activity_name = "";
        form.start_time = "";
        form.end_time = "";
        form.sort = "";
        form.type = 1;
        form.showtime = false;
        formArr.value = [{ recharge_amount: "", additional_amount: "" }];
      } else {
        //dialogT='编辑';
        idx = row.id;
        getRechargeActivityById(reactive({ id: row.id })).then((res) => {
          form.activity_name = res.data.activity_name;
          form.sort = res.data.sort;
          form.type = parseInt(res.data.type);
          if (form.type == 2) {
            form.showtime = true;
            form.start_time = res.data.start_time;
            form.end_time = res.data.end_time;
          } else {
            form.showtime = false;
            form.start_time = "";
            form.end_time = "";
          }
          formArr.value = res.data.rechargeActivityConfigs;
        });
      }

      editVisible.value = true;
      saveVisible.value = false;
    };

    const saveEdit = () => {
      saveVisible.value = true;
      setTimeout(() => {
        saveVisible.value = false   //点击一次时隔两秒后才能再次点击
      }, 2000)
      if (form.type == 1) {
        if (
          form.activity_name == null ||
          form.activity_name == "" ||
          form.sort == null ||
          form.sort == ""
        ) {
          ElMessage.error("参数不可为空！");
          saveVisible.value = false;
          return false;
        }
      } else {
        if (
          form.activity_name == null ||
          form.activity_name == "" ||
          form.start_time == null ||
          form.start_time == "" ||
          form.end_time == null ||
          form.end_time == "" ||
          form.sort == null ||
          form.sort == ""
        ) {
          ElMessage.error("参数不可为空！");
          saveVisible.value = false;
          return false;
        }
      }

      var isOk = true;
      formArr.value.forEach(function (item) {
        if (
          item.recharge_amount == null ||
          item.recharge_amount == "" ||
          item.additional_amount == null ||
          item.additional_amount == ""
        ) {
          isOk = false;
        }
      });
      if (!isOk) {
        ElMessage.error("赠送规则不符合规则！");
        saveVisible.value = false;
        return false;
      }
      if (form.type == 2 && form.start_time > form.end_time) {
        ElMessage.error("充值活动时间不符合规则！");
        saveVisible.value = false;
        return false;
      }
      editVisible.value = false;
      // let formArray=JSON.stringify(formArr.value);
      let rechargeActivity;
      if (form.type == 1) {
        rechargeActivity = {
          id: idx,
          activity_name: form.activity_name,
          sort: form.sort,
          type: form.type,
          rechargeActivityConfigs: formArr.value,
        };
      } else {
        rechargeActivity = {
          id: idx,
          activity_name: form.activity_name,
          start_time: form.start_time,
          end_time: form.end_time,
          sort: form.sort,
          type: form.type,
          rechargeActivityConfigs: formArr.value,
        };
      }

      // 新增
      if (!idx) {
        addRechargeActivity(
          reactive({ rechargeActivity: JSON.stringify(rechargeActivity) })
        )
          .then((res) => {
            if (res.code == 0) {
              ElMessage.success(res.data);
            } else {
              ElMessage.error(res.msg);
            }
            saveVisible.value = true;
          })
          .then(() => {
            query.pageIndex = 1;
            getData();
          });
      } else {
        // 编辑
        editRechargeActivity(
          reactive({ rechargeActivity: JSON.stringify(rechargeActivity) })
        )
          .then((res) => {
            if (res.code == 0) {
              ElMessage.success(res.data);
            } else {
              ElMessage.error(res.msg);
            }
            saveVisible.value = false;
          })
          .then(() => {
            query.pageIndex = 1;
            getData();
          });
        // let edit= reactive({
        //   id:idx,
        //   activity_name:form.activity_name,
        //   start_time:form.start_time,
        //   end_time:form.end_time,
        //   partake_times:form.partake_times,
        // });
        // editRechargeActivity(edit).then((res) => {
        //   console.log(res);
        //   ElMessage.success("修改成功");
        // }).then(()=>{
        //   getData();
        // });
      }
      // editVisible.value = false;
      // ElMessage.success(`修改第 ${idx + 1} 行成功`);
      // Object.keys(form).forEach((item) => {
      //   tableData.value[idx][item] = form[item];
      // });
    };

    const changeHandler = (value) => {
      if (value == 1) {
        form.showtime = false;
      }
      if (value == 2) {
        form.showtime = true;
      }
    };

    const formRules = {
      activity_name: [
        { required: true, message: "请输入活动名称", trigger: "blur" },
      ],
      start_time: [
        { required: true, message: "请选择开始时间", trigger: "blur" },
      ],
      end_time: [
        { required: true, message: "请选择结束时间", trigger: "blur" },
      ],
      sort: [
        { required: true, message: "请输入排序", trigger: ["blur", "change"] },
      ],
    };

    return {
      query,
      tableData,
      formArr,
      pageTotal,
      editVisible,
      form,
      dialogT,
      formRules,
      getData,
      getEndDate,
      getStartDate,
      handleSearch,
      handlePageChange,
      handleqiyong,
      handlejinyong,
      handleDelete,
      handleEdit,
      saveEdit,
      changeHandler,
      multipleSelection: [],
      value: true,
      activeName: "first",
      saveVisible,
      //addItem,
      //delItem,
    };
  },
  created() {
    let h = document.documentElement.clientHeight || document.body.clientHeight;
    this.tableH = h - 308 + "px";
  },
  methods: {
    handleSelectionChange(data) {
      this.selectedData = data;
    },
    handleDeleteAll() {
      ElMessageBox.confirm("确定要删除吗？", "提示", {
        type: "warning",
      })
        .then(() => {
          var that = this;
          var val = this.selectedData;
          var ids = "";
          if (val) {
            val.forEach(function (item, index) {
              ids = ids + item.id + ",";
            });
            delAllRechargeActivity({ ids: ids }).then((res) => {
              ElMessage.success("删除成功");
              that.getData();
            });
          } else {
            ElMessage.warning(`请选择一条记录`);
          }
        })
        .catch(() => { });
      // var that = this;
      // var val = this.selectedData;

      // if (val) {
      //   val.forEach(function (item, index) {
      //     that.tableData.forEach(function (itemI, indexI) {
      //       if (item === itemI) {
      //         // ElMessageBox.confirm("确定要删除吗？", "提示", {
      //         //   type: "warning",
      //         // })
      //         //   .then(() => {
      //         //     ElMessage.success("删除成功");
      //         //     that.tableData.splice(indexI, 1);
      //         //   })
      //         //   .catch(() => {
      //         //     ElMessage.info("取消删除");
      //         //   });
      //         that.tableData.splice(indexI, 1);
      //       }
      //     });
      //   });
      //   ElMessage.success("删除成功");
      //   this.$refs.multipleTable.clearSelection();
      // } else {
      //   ElMessage.warning(`请选择一条记录`);
      // }
    },
    addItem() {
      this.formArr.push({
        id: "",
        recharge_amount: "",
        additional_amount: "",
      });
      //console.log(this.formArr);
      // this.$nextTick(function () {
      //   this.formArr;
      // });
      //this.$forceUpdate
    },
    delItem(index) {
      if (this.formArr.length == 1) {
        ElMessage.warning(`至少保留一条赠送规则`);
      } else {
        this.formArr.splice(index, 1);
      }
      //this.$forceUpdate

      // this.$nextTick(function () {
      //   this.formArr;
      // });
    },
  },
};
</script>
