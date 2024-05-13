<template>
  <div>
    <!-- <div class="crumbs">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item>
          <i class="el-icon-lx-cascades"></i> 用户管理
        </el-breadcrumb-item>
      </el-breadcrumb>
    </div> -->
    <div class="container">
      <div class="handle-box">
        <div class="left-panel">
          <el-button v-permission="['road_user_add','park_user_add']" type="primary" size="small" icon="el-icon-plus"
            @click="handleadd(0, null, true)">添加</el-button>
          <el-button v-permission="['road_user_deleteAll','park_user_deleteAll']" type="danger" size="small" icon="el-icon-delete"
            @click="handleDeleteAll()">批量删除</el-button>
        </div>
        <div class="right-panel">
          <el-input size="small" v-model="query.user_name" placeholder="姓名" class="handle-input mr10"
            @keyup.enter="handleSearch()"></el-input>
          <el-input size="small" v-model="query.login_name" placeholder="登陆账号" class="handle-input mr10"
            @keyup.enter="handleSearch()"></el-input>
          <el-button size="small" type="primary" icon="el-icon-search" @click="handleSearch">查询</el-button>
        </div>
        <div class="clear"></div>
      </div>
      <!-- <el-row>
                <el-col :span="24">
                    <div class="top-panel">
                        <el-form ref="form" :model="form" label-width="80px" inline size="small">
                            <el-form-item label="姓名">
                               <el-input v-model="form.name" size="small"></el-input>
                            </el-form-item>
                            <el-form-item label="登陆账号">
                               <el-input v-model="form.name" size="small"></el-input>
                            </el-form-item>
                            <el-form-item label="登陆账号">
                               <el-button size="small" type="primary" icon="el-icon-search">查询</el-button>
                               <el-button size="small" type="text" icon="el-icon-arrow-up">合并</el-button>
                            </el-form-item>
                        </el-form>
                    </div>
                </el-col>
            </el-row> -->
      <el-table :data="tableData" border class="table" ref="multipleTable" :max-height="tableH"
        header-cell-class-name="table-header" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center"></el-table-column>
        <el-table-column type="index" label="序号" width="55" align="center">
        </el-table-column>
        <el-table-column prop="user_name" label="姓名" min-width="120" align="center"></el-table-column>
        <el-table-column prop="login_name" label="登陆账号" min-width="100" align="center"></el-table-column>
        <el-table-column prop="phone" label="手机号" width="120" align="center"></el-table-column>
        <el-table-column prop="sex" label="性别" width="100" align="center">
          <template #default="scope">
            <el-tag size="small" type="success" v-if="scope.row.sex == '0'">男</el-tag>
            <el-tag size="small" v-else>女</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="scope">
            <el-tag size="small" type="success" v-if="scope.row.status == '1'">启用</el-tag>
            <el-tag size="small" type="danger" v-else>禁用</el-tag>
          </template>
        </el-table-column>
        <!-- <el-table-column label="头像(查看大图)" align="center">
                    <template #default="scope">
                        <el-image class="table-td-thumb" :src="scope.row.thumb" :preview-src-list="[scope.row.thumb]">
                        </el-image>
                    </template>
                </el-table-column> -->

        <el-table-column prop="roles" label="角色" align="center">
          <template #default="scope">
            <el-tag size="small" v-for="(item, index) in scope.row.sysRoles" :key="index" class="mar5">{{ item.role_name
            }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="user_type" label="人员类型" width="120" align="center">
          <template #default="scope">
            <el-tag size="small" type="success" v-if="scope.row.user_type == '0'">运营人员</el-tag>
            <el-tag size="small" type="warning" v-else>停车场管理员</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="220" align="center">
          <template #default="scope">
            <el-button v-permission="['road_user_edit','park_user_edit']" size="mini" type="text" icon="el-icon-edit"
              @click="handleEdit(scope.$index, scope.row, false)">编辑
            </el-button>
            <el-button v-permission="['road_user_status','park_user_status']" size="mini" type="text" icon="el-icon-circle-check"
              @click="handleSta(scope.row, '1')" v-if="scope.row.status == '0'">启用
            </el-button>
            <el-button v-permission="['road_user_status','park_user_status']" size="mini" type="text" icon="el-icon-circle-close"
              @click="handleSta(scope.row, '0')" v-if="scope.row.status == '1'" class="red">禁用
            </el-button>
            <!-- <el-button
              size="mini"
              type="text"
              icon="el-icon-close"
              @click="handleEdit(scope.$index, scope.row, false)"
              >停用
            </el-button> -->
            <el-button v-permission="['road_user_delete','park_user_delete']" size="mini" type="text" icon="el-icon-delete" class="red"
              @click="handleDelete(scope.$index, scope.row)">删除</el-button>
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
        <el-tab-pane label="账户信息" name="first">
          <div class="mt20"></div>
          <el-form label-width="70px" :rules="formRules" ref="editform" :model="form">
            <el-form-item label="姓名" prop="user_name">
              <el-input v-model="form.user_name" placeholder="请输入姓名"></el-input>
            </el-form-item>
            <el-form-item label="登录账号" prop="login_name">
              <el-input v-model="form.login_name" placeholder="请输入账号"></el-input>
            </el-form-item>
            <el-form-item label="性别" prop="sex">
              <el-select v-model="form.sex" placeholder="请选择" style="width:100%;">
                <el-option key="0" label="男" value="0"></el-option>
                <el-option key="1" label="女" value="1"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="停车场" prop="park_id" v-show="form.user_type == '1'">
              <el-select v-model="form.park_id" placeholder="请选择" style="width:100%;">
                <el-option v-for="item in parks" :key="item.id" :label="item.park_name" :value="item.id"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="角色" class="w" prop="roleId" v-show="form.user_type == '0'">
              <el-select v-model="form.roleId" placeholder="请选择" style="width:100%;">
                <el-option v-for="item in roles" :key="item.id" :label="item.role_name" :value="item.id"></el-option>
              </el-select>
            </el-form-item>
          </el-form>
        </el-tab-pane>
        <el-tab-pane label="巡检充值信息" name="second" v-if="form.user_type == '0'">
          <div class="mt20"></div>
          <el-form label-width="100px" :rules="formRules" ref="xjeditform" :model="form">
            <el-form-item label="手机号" prop="phone">
              <el-input v-model="form.phone" placeholder="请输入手机号"></el-input>
            </el-form-item>

            <el-form-item label="充值密码" prop="recharge_pwd">
              <el-input type="password" placeholder="请输入充值密码" autocomplete="new-password"
                v-model="form.recharge_pwd"></el-input>
            </el-form-item>
            <el-form-item label="确认充值密码">
              <el-input type="password" placeholder="请再次输入充值密码" autocomplete="new-password"
                v-model="form.remima"></el-input>
            </el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="editVisible = false">取 消</el-button>
          <el-button type="primary" @click="saveEdit">确 定</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 新增弹出框 -->
    <el-dialog :title="dialogT" v-model="addVisible" width="30%">
      <el-tabs type="card" v-model="activeName">
        <el-tab-pane label="账户信息" name="first">
          <div class="mt20"></div>
          <el-form label-width="70px" :rules="formRules" ref="addform" :model="form">
            <el-form-item label="姓名" prop="user_name">
              <el-input v-model="form.user_name" placeholder="请输入姓名"></el-input>
            </el-form-item>
            <el-form-item label="登录账号" prop="login_name">
              <el-input v-model="form.login_name" placeholder="请输入账号"></el-input>
            </el-form-item>
            <el-form-item label="用户类型">
              <el-radio v-model="form.user_type" label="0">运营中心</el-radio>
              <el-radio v-model="form.user_type" label="1">岗亭</el-radio>
            </el-form-item>
            <el-form-item label="停车场" prop="park_id" v-show="form.user_type == '1'">
              <el-select v-model="form.park_id" placeholder="请选择">
                <el-option v-for="item in parks" :key="item.id" :label="item.park_name" :value="item.id"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="性别" prop="sex">
              <el-select v-model="form.sex" placeholder="请选择">
                <el-option key="0" label="男" value="0"></el-option>
                <el-option key="1" label="女" value="1"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="密码" prop="password">
              <el-input type="password" placeholder="请输入密码" v-model="form.password"></el-input>
            </el-form-item>
            <el-form-item label="确认密码" prop="ensure_password">
              <el-input type="password" placeholder="请再次输入密码" v-model="form.ensure_password"></el-input>
            </el-form-item>
            <el-form-item label="角色" class="w" v-if="form.user_type == '0'" prop="roleId" v-show="form.user_type == '0'">
              <el-select v-model="form.roleId" placeholder="请选择">
                <el-option v-for="item in roles" :key="item.id" :label="item.role_name" :value="item.id"></el-option>
              </el-select>
            </el-form-item>
          </el-form>
        </el-tab-pane>
        <el-tab-pane label="巡检充值信息" name="second" v-if="form.user_type == '0'">
          <div class="mt20"></div>
          <el-form label-width="100px" :rules="formRules" ref="xjaddform">
            <el-form-item label="手机号" prop="phone">
              <el-input v-model="form.phone" placeholder="请输入手机号"></el-input>
            </el-form-item>

            <el-form-item label="充值密码" prop="recharge_pwd">
              <el-input type="password" placeholder="请输入充值密码" autocomplete="new-password"
                v-model="form.recharge_pwd"></el-input>
            </el-form-item>
            <el-form-item label="确认充值密码" prop="remima">
              <el-input type="password" placeholder="请再次输入充值密码" autocomplete="new-password"
                v-model="form.remima"></el-input>
            </el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="addVisible = false">取 消</el-button>
          <el-button type="primary" @click="save">确 定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ref, reactive, nextTick } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import {
  queryuser,
  adduser,
  edituser,
  queryRoleAll,
  getuser,
  deluser,
  deluserAll,
  gongsiyonghuList,
  validateLoginName,
  queryParkData,
  handleStatus,
} from "../../api/index";
import { validatePhone } from "../../utils/validate";

export default {
  name: "userList",
  data() {
    return {
      tableH: 0,
    };
  },
  setup() {
    const dialogT = ref("");
    const query = reactive({
      user_name: "",
      login_nameme: "",
      pageIndex: 1,
      pageSize: 15,
    });
    const tableData = ref([]);
    const pageTotal = ref(0);
    // 获取表格数据
    const getData = () => {
      queryuser(query).then((res) => {
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

    const parks = ref([]);
    const getParks = () => {
      queryParkData(reactive({})).then((res) => {
        parks.value = res.data;
      });
    };

    const roles = ref([]);
    const listRole = () => {
      queryRoleAll({ role_type: "road" }).then((res) => {
        roles.value = res.data;
      });
    };
    // 表格编辑时弹窗和保存
    const editVisible = ref(false);
    const addVisible = ref(false);
    let form = ref({});
    let idx = -1;
    const handleEdit = (index, row, type) => {
      dialogT.value = "编辑";
      activeName.value = "first";
      editVisible.value = true;

      getParks();
      listRole();
      getuser(reactive({ userId: row.id })).then((res) => {
        form.value = res.data;
        if (
          null != res.data.recharge_pwd &&
          "" != res.data.recharge_pwd &&
          undefined != res.data.recharge_pwd
        ) {
          form.value.remima = res.data.recharge_pwd;
        } else {
          form.value.remima = "";
          form.value.recharge_pwd = "";
        }
      });
      nextTick(() => {
        editform.value.clearValidate();
      });
    };

    const handleadd = (index, row, type) => {
      dialogT.value = "新增";
      activeName.value = "first";
      listRole();
      getParks();
      form.value = {
        recharge_pwd: "",
        remima: "",
      };
      form.value.user_type = "0";
      addVisible.value = true;
      nextTick(() => {
        addform.value.clearValidate();
        xjaddform.value.clearValidate();
      });
    };
    const addform = ref(null);
    const editform = ref(null);
    const xjaddform = ref(null);
    const xjeditform = ref(null);
    const activeName = ref("first");
    const save = () => {
      addform.value.validate((v) => {
        if (v) {
          var value = form.value.phone;
          var user_type = form.value.user_type;
          if (user_type == 0) {
            var mobile = /^(((13[0-9]{1})|(14[0-9]{1})|(19[0-9]{1})|(16[0-9]{1})|(15[0-9]{1})|(18[0-9]{1})|(17[0-9]{1}))+\d{8})$/;
            if (value != undefined && value.length > 0) {
              if (value.length != 11 || !mobile.test(value)) {
                ElMessage.error("请输入正确的手机号");
                return false;
              }
            }
          }
          if (form.value.password != form.value.ensure_password) {
            ElMessage.error("密码与确认密码不一致");
            return false;
          }
          if (form.value.recharge_pwd != form.value.remima) {
            ElMessage.error("充值密码与确认充值密码不一致");
            return false;
          }
          form.value.sysRole = null;
          adduser(form.value).then((res) => {
            if (res.code == "0") {
              ElMessage.success(res.msg);
              addVisible.value = false;
              getData();
            } else {
              ElMessage.error(res.msg);
            }
          });
        }
      });
    };
    const saveEdit = () => {
      editform.value.validate((v) => {
        if (v) {
          var value = form.value.phone;
          var user_type = form.value.user_type;
          if (user_type == 0) {
            var mobile = /^(((13[0-9]{1})|(14[0-9]{1})|(19[0-9]{1})|(16[0-9]{1})|(15[0-9]{1})|(18[0-9]{1})|(17[0-9]{1}))+\d{8})$/;
            if (value != undefined && value.length > 0) {
              if (value.length != 11 || !mobile.test(value)) {
                ElMessage.error("请输入正确的手机号");
                return false;
              }
            }
          }
          if (form.value.recharge_pwd != form.value.remima) {
            ElMessage.error("充值密码与确认充值密码不一致");
            return false;
          }
          form.value.sysRole = null;
          edituser(form.value).then((res) => {
            if (res.code == "0") {
              ElMessage.success(res.msg);
              editVisible.value = false;
              getData();
            }
          });
        }
      });
    };

    const handleDelete = (index, row) => {
      // 二次确认删除
      ElMessageBox.confirm("确定要删除吗？", "提示", {
        type: "warning",
      })
        .then(() => {
          deluser(reactive({ userId: row.id })).then((res) => {
            if (res.code == -1) {
              ElMessage.error(res.msg);
            } else {
              ElMessage.success(res.msg);
            }

            getData();
          });
        })
        .catch(() => { });
    };

    const handleSta = (row, type) => {
      let params = { id: row.id, status: type };
      handleStatus(params).then((res) => {
        if (res.code == 0) {
          ElMessage.success(res.msg);
          getData();
        } else {
          ElMessage.error(res.msg);
        }
      });
    };

    //验证

    const validateName = (rule, value, callback) => {
      validateLoginName(
        reactive({ loginName: value, userId: form.value.id })
      ).then((res) => {
        if (!res.data) {
          return callback(new Error("登录名重复"));
        } else {
          callback();
        }
      });
    };

    const formRules = {
      user_name: [{ required: true, message: "请输入用户名", trigger: "blur" }],
      // login_name: [
      //   { required: true, message: "请输入登录名", trigger: "blur" },
      // ],
      login_name: [{ validator: validateName, trigger: ["blur", "change"] }],
      password: [{ required: true, message: "请输入密码", trigger: "blur" }],
      // ensure_password: [
      //   { required: true, message: "请输入设备账号", trigger: "blur" },
      // ],
      phone: [{ validator: validatePhone, trigger: ["blur", "change"] }],
      sex: [{ required: true, message: "请选择性别", trigger: "blur" }],
      roleId: [{ required: true, message: "请选择角色", trigger: "blur" }],

      // recharge_pwd: [
      //   { required: true, message: "请输入rtsp地址", trigger: "blur" },
      // ]
    };

    return {
      query,
      tableData,
      pageTotal,
      editVisible,
      addVisible,
      form,
      getParks,
      parks,
      addform,
      xjaddform,
      editform,
      xjeditform,
      dialogT,
      roles,
      formRules,
      save,
      listRole,
      handleSta,
      handleSearch,
      handlePageChange,
      handleDelete,
      handleEdit,
      saveEdit,
      handleadd,
      getData,
      multipleSelection: [],
      value: true,
      activeName,
    };
  },
  created() {
    let h = document.documentElement.clientHeight || document.body.clientHeight;
    this.tableH = h - 308 + 'px';
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
          console.log(val);
          var ids = "";
          if (val) {
            val.forEach(function (item, index) {
              //alert(item.id);
              ids = ids + item.id + ",";
            });
            deluserAll({ userIds: ids }).then((res) => {
              ElMessage.success("删除成功");
              that.getData();
            });
          } else {
            ElMessage.warning(`请选择一条记录`);
          }
        })
        .catch(() => { });
    },
  },
};
</script>
