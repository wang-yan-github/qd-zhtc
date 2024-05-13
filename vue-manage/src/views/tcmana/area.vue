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
        </div>
        <div class="right-panel">
          <el-input
            size="small"
            v-model="query.area_name"
            placeholder="名称"
            class="handle-input mr10"
          ></el-input>
          <el-button
            size="small"
            type="primary"
            icon="el-icon-search"
            @click="handleSearch"
            >查询</el-button
          >
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
        <el-table-column type="index" label="序号" width="55" align="center">

        </el-table-column>
        <el-table-column
          prop="area_name"
          label="区域名称"
          width="100"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="area_level"
          label="区域等级"
          width="100"
          align="center"
        ></el-table-column>
        <!-- <el-table-column label="头像(查看大图)" align="center">
                    <template #default="scope">
                        <el-image class="table-td-thumb" :src="scope.row.thumb" :preview-src-list="[scope.row.thumb]">
                        </el-image>
                    </template>
                </el-table-column> -->
        <el-table-column
          prop="city"
          label="所属城市"
          align="center"
          width="180"
        ></el-table-column>
        <el-table-column
          prop="street_num"
          label="街道数量"
          width="100"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="create_time"
          label="添加时间"
          width="100"
          align="center"
        ></el-table-column>
        <el-table-column label="状态" align="center" width="100">
          <template #default="scope">
            <el-tag
              size="small"
              :type="
                scope.row.status === '0'
                  ? 'success'
                  : scope.row.status === '1'
                  ? 'danger'
                  : ''
              "
              v-if="scope.row.status==0">启用</el-tag
            >
            <el-tag
              size="small"
              :type="
                scope.row.status === '0'
                  ? 'success'
                  : scope.row.status === '1'
                  ? 'danger'
                  : ''
              "
              v-else>禁用</el-tag
            >
            <!-- <el-switch
              :value="
                scope.row.state === '成功'
                  ? true
                  : scope.row.state === '失败'
                  ? false
                  : ''
              "
              active-color="#13ce66"
              inactive-color="#ff4949"
            >
            </el-switch> -->
          </template>
        </el-table-column>
        <el-table-column label="操作" width="220" align="center">
          <template #default="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleEdit(scope.$index, scope.row, false)"
              >编辑
            </el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-circle-close"
              @click="handleEdit(scope.$index, scope.row, false)"
              >停用
            </el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              class="red"
              @click="handleDelete(scope.$index, scope.row)"
              >删除</el-button
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

    <!-- 编辑弹出框 -->
    <el-dialog  :title="dialogT" v-model="editVisible" width="30%">
          <el-form label-width="70px">
            <el-form-item label="区域名称">
              <el-input v-model="form.name"></el-input>
            </el-form-item>
            <el-form-item label="区域等级">
              <el-select v-model="form.region" placeholder="无" class="w">
                <el-option key="bbk" label="运维" value="bbk"></el-option>
                <el-option key="xtc" label="封闭式车场" value="xtc"></el-option>
                <el-option key="imoo" label="客服" value="imoo"></el-option>
              </el-select>
            </el-form-item>
          </el-form>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="editVisible = false">取 消</el-button>
          <el-button type="primary" @click="saveEdit">确 定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ref, reactive } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { areaData } from "../../api/index";

export default {
  name: "arealist",
  setup() {
    const query = reactive({
      area_name: "",
      pageIndex: 1,
      pageSize: 10,
    });
    const tableData = ref([]);
    const pageTotal = ref(0);
    // 获取表格数据
    const getData = () => {
      areaData(query).then((res) => {
        console.log(res.data);
        tableData.value = res.data.list;
        pageTotal.value = res.data.pages;
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
    const dialogT = "新增";

    // 表格编辑时弹窗和保存
    const editVisible = ref(false);
    let form = reactive({
      name: "",
      area_name: "",
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
      form,
      dialogT,
      handleSearch,
      handlePageChange,
      handleDelete,
      handleEdit,
      saveEdit,
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
};
</script>
