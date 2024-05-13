<template>
  <div>
    <div class="container">
      <el-form label-width="90px">
        <el-form-item label="车牌号码">
          <el-input placeholder="" v-model="form.value1" class="handle-input"></el-input>
        </el-form-item>
        <el-form-item label="车牌样式">
          <el-radio-group v-model="form.radio1">
            <el-radio :label="1">蓝牌</el-radio>
            <el-radio :label="2">绿牌</el-radio>
            <el-radio :label="3">黄牌</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="名单类型">
          <el-radio-group v-model="form.radio2">
            <el-radio :label="1">普通名单</el-radio>
            <el-radio :label="2">黑名单</el-radio>
            <el-radio :label="3">白名单</el-radio>
            <el-radio :label="4">残疾人车辆</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="车牌性质">
          <el-select v-model="form.address" placeholder="请选择" class="handle-input">
                <el-option key="bbk" label="车牌性质" value="bbk"></el-option>
                <el-option key="xtc" label="军用车" value="xtc"></el-option>
                <el-option key="imoo" label="内部车" value="imoo"></el-option>
                <el-option key="imodo" label="其他" value="imodo"></el-option>
              </el-select>
        </el-form-item>
        <el-form-item >
          <el-button type="primary">提交</el-button>
        </el-form-item>
      </el-form>
    </div>
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
      value1:"",
      radio1:2,
      radio2:1,
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