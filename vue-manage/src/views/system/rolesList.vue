<template>
  <div>
    <div class="container">
      <div class="handle-box">
        <div class="left-panel">
          <el-button v-permission="['road_rolemanager_add','park_rolemanager_add']" type="primary" size="small" icon="el-icon-plus"
            @click="handleadd(0, null, true)">添加</el-button>
          <el-button v-permission="['road_rolemanager_deleteAll','park_rolemanager_deleteAll']" type="danger" size="small" icon="el-icon-delete"
            @click="handleDeleteAll()">批量删除</el-button>
        </div>
        <div class="right-panel">
          <el-input size="small" v-model="query.role_name" placeholder="角色名" class="handle-input mr10"
            @keyup.enter="handleSearch()"></el-input>

          <el-button size="small" type="primary" icon="el-icon-search" @click="handleSearch">查询</el-button>
        </div>
        <div class="clear"></div>
      </div>

      <el-table :data="tableData" border class="table" ref="multipleTable" header-cell-class-name="table-header"
        @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center"></el-table-column>
        <el-table-column type="index" label="序号" width="55" align="center"></el-table-column>
        <el-table-column prop="role_name" label="角色名称" min-width="120" align="center"></el-table-column>
        <!-- <el-table-column prop="munes" label="权限">
          <template #default="scope">
            <el-tag size="small" v-for="(item, index) in scope.row.menus" :key="index" class="mar5 mb5">{{ item.menu_name
            }}</el-tag>
          </template>
        </el-table-column> -->
        <el-table-column prop="remark" label="备注" min-width="120" align="center"></el-table-column>
        <el-table-column label="操作" width="220" align="center">
          <template #default="scope">
            <el-button v-permission="['park_rolemanager_edit','road_rolemanager_edit']" size="mini" type="text" icon="el-icon-edit"
              @click="handleEdit(scope.$index, scope.row, false)" v-if="scope.row.role_type == 'road'">编辑
            </el-button>
            <el-button v-permission="['park_rolemanager_delete','road_rolemanager_delete']" size="mini" type="text" icon="el-icon-delete" class="red"
              @click="handleDelete(scope.$index, scope.row)" v-if="scope.row.role_type == 'road'">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination">
        <el-pagination background layout="total, prev, pager, next" :current-page="query.pageIndex"
          :page-size="query.pageSize" :total="pageTotal" @current-change="handlePageChange"></el-pagination>
      </div>
    </div>

    <!-- 新增弹出框 -->
    <el-dialog :title="dialogT" v-model="addVisible" width="30%" top="2vh">
      <el-form label-width="100px" :rules="formRules" ref="addform" label-position="top" :model="form" @submit.prevent>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="角色名称" prop="role_name">
              <el-input v-model="form.role_name" placeholder="请输入角色名称"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="路内权限">
              <div class="treeborder">
                <el-tree ref="addRoadTree" :data="treedata" show-checkbox node-key="id" :check-strictly="checkStrictly">
                </el-tree>
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="停车场权限">
              <div class="treeborder">
                <el-tree ref="addParkTree" :data="parktreedata" show-checkbox node-key="id"
                  :check-strictly="checkStrictly">
                </el-tree>
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注">
              <el-input v-model="form.remark" placeholder="请输入备注内容" type="textarea" :rows="4"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <!-- <el-form-item label="角色类型">
              <el-radio v-model="form.role_type" label="road">路内</el-radio>
              <el-radio v-model="form.role_type" label="park">停车场</el-radio>
            </el-form-item> -->
      </el-form>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="addVisible = false">取 消</el-button>
          <el-button type="primary" @click="save">确 定</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 编辑弹出框 -->
    <el-dialog :title="dialogT" v-model="editVisible" width="30%" top="2vh">
      <el-form label-width="100px" :rules="formRules" ref="editform" :model="form" label-position="top" @submit.prevent>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="角色名称" prop="role_name">
              <el-input v-model="form.role_name" placeholder="请输入角色名称"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="路内权限">
              <div class="treeborder">
                <el-tree ref="editRoadTree" :data="treedata" show-checkbox node-key="id"
                  :default-checked-keys="form.roadMenuList" :check-strictly="checkStrictly">
                </el-tree>
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="停车场权限">
              <div class="treeborder">
                <el-tree ref="editParkTree" :data="parktreedata" show-checkbox node-key="id"
                  :default-checked-keys="form.parkMenuList" :check-strictly="checkStrictly">
                </el-tree>
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注">
              <el-input v-model="form.remark" placeholder="请输入备注内容" type="textarea" :rows="4"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <!-- <el-form-item label="角色类型">
              <el-radio v-model="form.role_type" label="road">路内</el-radio>
              <el-radio v-model="form.role_type" label="park">停车场</el-radio>
            </el-form-item> -->
      </el-form>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="editVisible = false">取 消</el-button>
          <el-button type="primary" @click="edit">确 定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ref, reactive, getCurrentInstance, nextTick } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import {
  queryRoleList,
  queryMenuTree,
  saveRole,
  queryeRole,
  editRole,
  delRole,
  delRoleAll,
} from "../../api/index";

export default {
  name: "rolesList",

  setup() {
    const { proxy } = getCurrentInstance();
    const query = reactive({
      role_name: "",
    });
    const tableData = ref([]);
    const pageTotal = ref(0);
    // 获取表格数据
    const getData = () => {
      queryRoleList(query).then((res) => {
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

    // 删除操作
    const handleDelete = (index, row) => {
      // 二次确认删除
      ElMessageBox.confirm("确定要删除吗？", "提示", {
        type: "warning",
      })
        .then(() => {
          delRole(reactive({ roleId: row.id })).then((res) => {
            if (res.code == 0) {
              ElMessage.success(res.msg);
              getData();
              const queryTree = () => {
                queryMenuTree(reactive({})).then((res) => {
                  treedata.value = res.data.roadTree;
                  parktreedata.value = res.data.parkTree;
                });
              };
            } else {
              ElMessage.error(res.msg);
            }
          });
        })
        .catch(() => { });
    };
    const treedata = ref([]);
    const parktreedata = ref([]);
    const selected = ref([]);
    // const queryMenu = reactive({
    // });
    const queryTree = () => {
      queryMenuTree(reactive({})).then((res) => {
        treedata.value = res.data.roadTree;
        parktreedata.value = res.data.parkTree;
      });
    };

    const addRoadTree = ref(null);
    const addParkTree = ref(null);
    const editRoadTree = ref(null);
    const editParkTree = ref(null);
    // const handleCheckChange = (data, checked, indeterminate) => {
    //   console.log(data, checked, indeterminate);
    //   console.log('***************************'+tree.value.getCheckedKeys())
    //   if(checked){
    //     selected.value.push(data.id);
    //   }else{
    //     let nodelength = selected.value.length;
    //     for(let i = 0; i < nodelength; i++){
    //       if(selected.value[i] === data.id && !indeterminate){
    //         selected.value.splice(i,1);
    //         break;
    //       }
    //     }
    //   }
    // }
    const radioChange = (val) => {
      addRoadTree.value.setCheckedNodes([]);
    };
    const dialogT = ref("新增");
    // 表格编辑时弹窗和保存
    const editVisible = ref(false);
    const addVisible = ref(false);
    let form = ref({});
    const checkStrictly = ref(false);
    //保存
    const addform = ref(null);
    const save = () => {
      addform.value.validate((v) => {
        if (v) {
          let nodes = addRoadTree.value
            .getCheckedKeys()
            .concat(addRoadTree.value.getHalfCheckedKeys())
            .concat(addParkTree.value.getCheckedKeys())
            .concat(addParkTree.value.getHalfCheckedKeys());

          form.value.menuIds = nodes.toString();
          if (null === form.value.menuIds || "" === form.value.menuIds) {
            ElMessage.error("请选择权限");
            return false;
          }
          saveRole(form.value).then((res) => {
            if (res.code === 0) {
              addVisible.value = false;
              getData();
            } else {
              ElMessage.error(res.msg);
            }
          });
        }
      });
    };
    const editform = ref(null);
    const edit = () => {
      editform.value.validate((v) => {
        if (v) {
          let nodes = editRoadTree.value
            .getCheckedKeys()
            .concat(editRoadTree.value.getHalfCheckedKeys())
            .concat(editParkTree.value.getCheckedKeys())
            .concat(editParkTree.value.getHalfCheckedKeys());
          form.value.menuIds = nodes.toString();
          form.value.roadMenuList = null;
          form.value.parkMenuList = null;
          if (null === form.value.menuIds || "" === form.value.menuIds) {
            ElMessage.error("请选择权限");
            return false;
          }
          form.value.menus = null;
          form.value.menuIdList = null;

          editRole(form.value).then((res) => {
            if (res.code === 0) {
              editVisible.value = false;
              getData();
            } else {
              ElMessage.error(res.msg);
            }
          });
        }
      });
    };
    queryTree();
    let idx = -1;
    const handleadd = (index, row, type) => {
      form.value = { role_type: "0" };
      dialogT.value = "新增";
      addVisible.value = true;
      checkStrictly.value = false;
      nextTick(() => {
        addform.value.clearValidate();
      });
    };

    const handleEdit = (index, row, type) => {
      dialogT.value = "编辑";
      editVisible.value = true;
      checkStrictly.value = true;
      //queryTree();

      nextTick(() => {
        queryeRole(reactive({ roleId: row.id })).then((res) => {
          editRoadTree.value.setCheckedNodes(res.data.roadMenuList);
          editParkTree.value.setCheckedNodes(res.data.parkMenuList);
          form.value = res.data;
          checkStrictly.value = false;
          editform.value.clearValidate();
        });
      });
    };

    const saveEdit = () => {
      editVisible.value = false;
      ElMessage.success(`修改第 ${idx + 1} 行成功`);
      Object.keys(form).forEach((item) => {
        tableData.value[idx][item] = form[item];
      });
    };

    const formRules = {
      role_name: [
        { required: true, message: "请输入角色名称", trigger: "blur" },
      ],
    };

    return {
      query,
      tableData,
      addRoadTree,
      addParkTree,
      editRoadTree,
      editParkTree,
      pageTotal,
      editVisible,
      addVisible,
      form,
      parktreedata,
      radioChange,
      checkStrictly,
      dialogT,
      addform,
      editform,
      formRules,
      //handleCheckChange,
      handleSearch,
      //handleDeleteAll,
      handlePageChange,
      handleDelete,
      handleEdit,
      handleadd,
      saveEdit,
      save,
      edit,
      queryMenuTree,
      getData,
      multipleSelection: [],
      value: true,
      activeName: "first",
      treedata,
      defaultProps: {
        children: "children",
        label: "label",
      },
    };
  },
  methods: {
    handleSelectionChange(data) {
      this.selectedData = data;
    },
    handleDeleteAll() {
      var that = this;
      var val = this.selectedData;
      ElMessageBox.confirm("确定要删除吗？", "提示", {
        type: "warning",
      })
        .then(() => {
          var ids = "";
          if (val) {
            val.forEach(function (item, index) {
              //alert(item.id);
              ids = ids + item.id + ",";
            });
            delRoleAll({ roleIds: ids }).then((res) => {
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
