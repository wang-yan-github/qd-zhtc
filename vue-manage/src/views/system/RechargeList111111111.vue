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
        </div>
        <div class="right-panel">
          <el-input
            size="small"
            v-model="query.name"
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
        <el-table-column pro="ID" label="ID" width="55" align="center">
          <template #default="scope">
            {{ scope.row.id }}
          </template>
        </el-table-column>
        <el-table-column prop="name" label="活动名称"></el-table-column>
        <el-table-column label="价格" width="100" align="center">
          <template #default="scope">{{ scope.row.money }}</template>
        </el-table-column>
        <el-table-column
          prop="times"
          label="开始时间"
          align="center"
          width="180"
        ></el-table-column>
        <el-table-column
          prop="times"
          label="结束时间"
          align="center"
          width="180"
        ></el-table-column>
        <el-table-column
          prop="roles"
          label="用户可参与次数"
          align="center"
          width="160"
        ></el-table-column>
        <el-table-column label="启用" align="center" width="100">
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
        <el-table-column
          prop="addtimes"
          label="创建时间"
          align="center"
          width="170"
        >
        </el-table-column>
        <el-table-column label="操作" width="180" align="center">
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
    <el-dialog :title="dialogT" v-model="editVisible" width="30%">
      <el-tabs type="card" v-model="activeName">
        <el-tab-pane label="活动信息" name="first">
          <div class="mt20"></div>
          <el-form label-width="80px">
            <el-form-item label="活动名称">
              <el-input v-model="form.name"></el-input>
            </el-form-item>
            <el-form-item label="开始时间">
              <el-input v-model="form.zhanghao"></el-input>
            </el-form-item>
            <el-form-item label="结束时间">
              <el-input v-model="form.mima"></el-input>
            </el-form-item>
            <el-form-item label="参与次数">
              <el-input v-model="form.remima"></el-input>
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
                <el-button
                  round
                  size="mini"
                  type="primary"
                  icon="el-icon-plus"
                  @click="addItem"
                ></el-button>
              </th>
            </thead>
            <tbody>
              <tr v-for="(item, index) in formArr" :key="index">
                <td>
                  <el-input size="mini"
                    ><template v-slot:append>元</template></el-input
                  >
                </td>
                <td>
                  <el-input size="mini"
                    ><template v-slot:append>元</template></el-input
                  >
                </td>
                <td align="center">
                  <el-button
                    round
                    size="mini"
                    type="danger"
                    icon="el-icon-delete"
                    @click="delItem(index)"
                  ></el-button>
                </td>
              </tr>
            </tbody>
          </table>
        </el-tab-pane>
      </el-tabs>

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
import { fetchData } from "../../api/index";

export default {
  name: "userList",
  setup() {
    const query = reactive({
      address: "",
      name: "",
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


    const formArr=[{ value1: "",value2: "" }];
    // const addItem = () => {
    //   formArr.push({
    //     value: "",
    //   });
    // };
    // const delItem = (index) => {
    //   if(formArr.length==1){
    //     ElMessage.warning(`至少保留一条赠送规则`);
    //   }else{
    //      formArr.splice(index, 1);
    //   }
    // };


    return {
      query,
      tableData,
      formArr,
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
      //addItem,
      //delItem,
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
    addItem() {
      this.formArr.push({
        value1: "",
        value2: "",
      });
      //console.log(this.formArr);
      // this.$nextTick(function () {
      //   this.formArr;
      // });
      //this.$forceUpdate
    },
    delItem(index) {      
      if(this.formArr.length==1){
        ElMessage.warning(`至少保留一条赠送规则`);
      }else{
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