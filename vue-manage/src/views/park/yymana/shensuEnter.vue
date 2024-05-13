<template>
  <div>
    <div class="handle-title mgb20">
      <i class="el-icon-s-opportunity"></i>订单查询
    </div>
    <div class="container">
      <div class="handle-box">
        <el-form inline size="small">
          <el-form-item label="车牌号">
            <el-input
              size="small"
              v-model="query.name"
              placeholder="名称"
              class="handle-input mr10"
            ></el-input>
          </el-form-item>
          <el-form-item label="创建时间">
            <el-date-picker
              v-model="query.date1"
              placeholder=""
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
            ></el-date-picker>
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
      <el-table
        :data="tableData2"
        border
        class="table"
        ref="multipleTable"
        header-cell-class-name="table-header"
        @selection-change="handleSelectionChange"
      >
        <el-table-column pro="ID" label="ID" width="55" align="center">
          <template #default="scope">
            {{ scope.row.id }}
          </template>
        </el-table-column>
        <el-table-column prop="orderNo" label="订单号"></el-table-column>
        <el-table-column label="停车区域" width="100" align="center">
          <template #default="scope">{{ scope.row.money }}</template>
        </el-table-column>

        <el-table-column
          prop="addtimes"
          label="驶入时间"
          align="center"
          width="180"
        ></el-table-column>
        <el-table-column
          prop="logintimes"
          label="驶离时间"
          width="100"
          align="center"
        ></el-table-column>
        <el-table-column prop="roles" label="停车时长"></el-table-column>
        <el-table-column prop="roles" label="车牌号"></el-table-column>
        <el-table-column prop="roles" label="泊位编号"></el-table-column>
        <el-table-column prop="roles" label="订单来源"></el-table-column>
        <el-table-column prop="roles" label="停车费用"></el-table-column>
        <el-table-column prop="roles" label="状态"></el-table-column>
        <el-table-column label="是否会员" align="center" width="100">
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
        <el-table-column label="操作" width="120" align="center">
          <template #default="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleEdit(scope.$index, scope.row, false)"
              >申诉录入
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
    <div class="handle-title mgb20 mt20">
      <i class="el-icon-s-opportunity"></i>录入投诉信息
    </div>
    <div class="container">
      <el-form label-width="70px">
        <el-form-item label="订单号">
          <el-input placeholder=""></el-input>
        </el-form-item>
        <el-form-item label="内容">
          <el-input placeholder="" type="textarea" :rows="5"></el-input>
        </el-form-item>
        <el-form-item label="联系方式">
          <el-input placeholder=""></el-input>
        </el-form-item>
      </el-form>
    </div>
    <!-- 编辑弹出框 -->
    <!-- <el-dialog title="申诉录入" v-model="editVisible" width="30%">
      <el-form label-width="70px">
        <el-form-item label="订单号">
          <el-input v-model="form.name"></el-input>
        </el-form-item>
        <el-form-item label="内容">
          <el-input v-model="form.name" type="textarea" :rows="5"></el-input>
        </el-form-item>
        <el-form-item label="联系方式">
          <el-input v-model="form.name"></el-input>
        </el-form-item>
      </el-form>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="editVisible = false">取 消</el-button>
          <el-button type="primary" @click="saveEdit">确 定</el-button>
        </span>
      </template>
    </el-dialog> -->
  </div>
</template>

<script>
import { ref, reactive } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { fetchData } from "../../../api/index";

export default {
  name: "shensuenterA",
  setup() {
    const query = reactive({
      address: "",
      name: "",
      date1: "",
      pageIndex: 1,
      pageSize: 10,
    });
    const tableData = ref([]);
    const pageTotal = ref(0);
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
    const dialogT = "新增";

    // 表格编辑时弹窗和保存
    const editVisible = ref(false);
    let form = reactive({
      name: "",
      address: "",
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
