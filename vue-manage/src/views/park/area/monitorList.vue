<template>
  <div>
    <div class="container">
      <div class="handle-box">
        <div class="left-panel">
          <el-button
            type="primary"
            size="small"
            icon="el-icon-plus"
            @click="handleEdit(0, null, true)"
            >添加</el-button
          >
          <el-button
            type="danger"
            size="small"
            icon="el-icon-delete"
            @click="handleDeleteAll()"
            >批量删除</el-button
          >
          <el-button
            type="success"
            size="small"
            icon="el-icon-s-operation"
            @click="bindRoad()"
            >绑定路内</el-button
          >
        </div>
        <div class="right-panel">
          <el-form inline size="small">
            <el-input
              size="small"
              v-model="query.name"
              placeholder="请输入巡检姓名/手机号码"
              class="handle-input mr10"
            ></el-input>
            <el-select
              v-model="form.selvalue"
              filterable
              size="small"
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
            <span class="dispinline ml5"></span>
            <el-select
              v-model="form.selvalue"
              filterable
              size="small"
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
            <span class="dispinline ml5"></span>

            <el-button
              size="small"
              type="primary"
              icon="el-icon-search"
              @click="handleSearch"
              >查询</el-button
            >
          </el-form>
        </div>
        <div class="clear"></div>
      </div>
      <el-table
        :data="tableData"
        border
        class="table"
        ref="multipleTable"
        header-cell-class-name="table-header"
        @selection-change="handleSelectionChange"
      >
        <el-table-column
          type="selection"
          width="55"
          align="center"
        ></el-table-column>
        <el-table-column pro="ID" label="ID" width="65" align="center" sortable>
          <template #default="scope">
            {{ scope.row.id }}
          </template>
        </el-table-column>
        <el-table-column
          prop="name"
          label="姓名"
          width="100"
          align="center"
        ></el-table-column>
        <el-table-column label="登录账号" width="100" align="center">
          <template #default="scope">{{ scope.row.money }}</template>
        </el-table-column>
        <el-table-column
          prop="addtimes"
          label="上次登录时间"
          align="center"
          width="180"
        ></el-table-column>
        <el-table-column
          prop="logintimes"
          label="登录次数"
          width="100"
          align="center"
        ></el-table-column>
        <el-table-column prop="roles" label="角色"></el-table-column>
        <el-table-column label="状态" align="center" width="100">
          <template #default="scope">
            <el-tag
              size="small"
              :type="
                scope.row.state === '启用'
                  ? 'success'
                  : scope.row.state === '禁用'
                  ? 'danger'
                  : ''
              "
              >{{ scope.row.state }}</el-tag
            >
          </template>
        </el-table-column>
        <el-table-column label="操作" width="320" align="center">
          <template #default="scope">

            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="rEdit(scope.$index, scope.row)"
              >编辑
            </el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-circle-close"
              @click="handleEdit(scope.$index, scope.row, false)"
              >停用
            </el-button>

            <el-dropdown class="ml10 red" @command="handleCommand">
              <span class="el-dropdown-link">
                更多操作<i class="el-icon-arrow-down el-icon--right"></i>
              </span>
              <template v-slot:dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item icon="el-icon-delete" command="a"
                    >删除</el-dropdown-item
                  >
                  <el-dropdown-item icon="el-icon-upload2" command="c"
                    >改变图片上传模式</el-dropdown-item
                  >
                </el-dropdown-menu>
              </template>
            </el-dropdown>
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

    <!-- 编辑弹出框 -->
    <el-dialog :title="dialogT" v-model="editVisible" width="35%">
      <el-form label-width="70px">
        <el-form-item label="设备编号">
          <el-input v-model="form.name" type="textarea" :rows="8"></el-input>
          <span class="color999">设备编号(每个一行)</span>
        </el-form-item>
        <el-form-item label="设备类型">
          <el-select
              v-model="form.selvalue"
              filterable
              size="small"
              placeholder="所有区域"
              class="w"
            >
              <el-option
                v-for="item in form.czroptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
        </el-form-item>
        <el-form-item label="选择区域">
          <el-select
              v-model="form.selvalue"
              filterable
              size="small"
              placeholder="所有区域"
              class="w"
            >
              <el-option
                v-for="item in form.czroptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
          <span class="color999">操作提示：如没有您要的区域，请先添加区域！</span>
        </el-form-item>
        <el-form-item label="选择街道">
         <el-select
              v-model="form.selvalue"
              filterable
              size="small"
              placeholder="所有区域"
              class="w"
            >
              <el-option
                v-for="item in form.czroptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
            <span class="color999">操作提示：如没有您要的街道，请先添加街道！</span>
        </el-form-item>

        <el-form-item label="指定路内">
          <el-select
              v-model="form.selvalue"
              filterable
              size="small"
              placeholder="所有区域"
              class="w"
            >
              <el-option
                v-for="item in form.czroptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
            <span class="color999">操作提示：如没有您要的路内，请先添加路内！</span>
        </el-form-item>

      </el-form>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="editVisible = false">取 消</el-button>
          <el-button type="primary" @click="saveEdit">确 定</el-button>
        </span>
      </template>
    </el-dialog>
    <el-dialog v-model="ppVisible">
      <img width="100%" :src="dialogImageUrl" alt="" />
    </el-dialog>
    <!-- 详情弹出框 -->
    <el-dialog title="绑定路内" v-model="viewVisible" width="40%">
         <el-form label-width="70px">
        <el-form-item label="设备类型">
          <el-select
              v-model="form.selvalue"
              filterable
              size="small"
              placeholder="所有区域"
              class="w"
            >
              <el-option
                v-for="item in form.czroptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
        </el-form-item>
        <el-form-item label="选择区域">
          <el-select
              v-model="form.selvalue"
              filterable
              size="small"
              placeholder="所有区域"
              class="w"
            >
              <el-option
                v-for="item in form.czroptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
          <span class="color999">操作提示：如没有您要的区域，请先添加区域！</span>
        </el-form-item>
        <el-form-item label="选择街道">
         <el-select
              v-model="form.selvalue"
              filterable
              size="small"
              placeholder="所有区域"
              class="w"
            >
              <el-option
                v-for="item in form.czroptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
            <span class="color999">操作提示：如没有您要的街道，请先添加街道！</span>
        </el-form-item>

        <el-form-item label="指定路内">
          <el-select
              v-model="form.selvalue"
              filterable
              size="small"
              placeholder="所有区域"
              class="w"
            >
              <el-option
                v-for="item in form.czroptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
            <span class="color999">操作提示：如没有您要的路内，请先添加路内！</span>
        </el-form-item>

      </el-form>
    </el-dialog>
  </div>
</template>

<script>
import { ref, reactive } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { fetchData } from "../../../api/index";
import { useRouter } from "vue-router";

export default {
  name: "monitorlist",
  components: {},
  setup() {
    const query = reactive({
      address: "",
      name: "",
      pageIndex: 1,
      pageSize: 10,
    });
    const tableData = ref([]);
    const pageTotal = ref(0);
    const router = useRouter();
    // 获取表格数据
    const getData = () => {
      fetchData(query).then((res) => {
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
    const dialogT = "编辑";

    // 表格编辑时弹窗和保存
    const editVisible = ref(false);
    const viewVisible = ref(false);
    const ppVisible = ref(false);
    let form = reactive({
      name: "",
      address: "",
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
      radio2: "男",
    });
    let idx = -1;
    const handleEdit = (index, row, type) => {
      if (type) {
        //dialogT='新增'
      } else {
        //dialogT='编辑';
        idx = index;
        Object.keys(form).forEach((item) => {
          form[item] = row[item];
        });
      }

      editVisible.value = true;
    };

    const rEdit = (index, row) => {
      router.push("/equipmentedit");
      //router.push({ path: "/equipmentedit", query: { setid: 123456 } });
    };

    const handleView = (index, row) => {
      idx = index;
      Object.keys(form).forEach((item) => {
        form[item] = row[item];
      });
      viewVisible.value = true;
    };

    const saveEdit = () => {
      editVisible.value = false;
      ElMessage.success(`修改第 ${idx + 1} 行成功`);
      Object.keys(form).forEach((item) => {
        tableData.value[idx][item] = form[item];
      });
    };

    return {
      query,
      tableData,
      pageTotal,
      editVisible,
      viewVisible,
      form,
      dialogT,
      handleSearch,
      handlePageChange,
      handleDelete,
      handleEdit,
      handleView,
      saveEdit,
      rEdit,
      multipleSelection: [],
      dialogImageUrl: "",
      ppVisible: false,
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
    handleCommand(command) {
      this.$message("click on item " + command);
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
        //ElMessage.success("删除成功");
        this.$refs.multipleTable.clearSelection();
      } else {
        ElMessage.warning(`请选择一条记录`);
      }
    },
    bindRoad() {
      var that = this;
      var val = this.selectedData;
      if (val) {
        val.forEach(function (item, index) {
          that.tableData.forEach(function (itemI, indexI) {
            if (item === itemI) {

              //that.tableData.splice(indexI, 1);
            }
          });
        });
        this.viewVisible=true;
        ElMessage.success("绑定成功");
        this.$refs.multipleTable.clearSelection();
      } else {
        ElMessage.warning(`请选择一条记录`);
      }
    },
  },
};
</script>
