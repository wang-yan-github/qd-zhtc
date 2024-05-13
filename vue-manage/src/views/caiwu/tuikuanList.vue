<template>
  <div>
    <div class="container">
      <div class="handle-box">
        <div class="left-panel">
          <el-form inline size="small" class="lineH0">
            <el-form-item label="车牌号" class="search-mb0">
              <el-input
                size="small"
                @keyup.enter="handleSearch()"
                v-model="query.carNo"
                placeholder="车牌号"
                class="handle-input mr10 w170"
              ></el-input>
            </el-form-item>
            <el-form-item label="订单号" class="search-mb0">
              <el-input
                size="small"
                @keyup.enter="handleSearch()"
                v-model="query.orderNo"
                placeholder="订单号"
                class="handle-input mr10 w170"
              ></el-input>
            </el-form-item>
            <el-form-item label="手机号" class="search-mb0">
              <el-input
                size="small"
                @keyup.enter="handleSearch()"
                v-model="query.phone"
                placeholder="手机号"
                class="handle-input mr10 w170"
              ></el-input>
            </el-form-item>

            <el-form-item label="" class="search-mb0">
              <el-button
                size="small"
                type="primary"
                icon="el-icon-search"
                @click="handleSearch"
                >查询</el-button
              >
               <el-button
                size="small"
                type="success"
                icon="el-icon-upload2"
                @click="exportTuiKuan"
                v-permission="['park_tklist_excel', 'road_tklist_excel']"
                >导出</el-button
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
        ref="multipleTable"
        :max-height="tableH"
        header-cell-class-name="table-header"
        @selection-change="handleSelectionChange"
      >
        <!-- <el-table-column
          type="selection"
          width="55"
          align="center"
        ></el-table-column> -->

        <el-table-column type="index" label="序号" width="55" align="center"> </el-table-column>

        <el-table-column prop="payment_serialno" label="流水号" align="center" ></el-table-column>

        <el-table-column prop="orderNo" label="订单号" align="center" ></el-table-column>

<!--        <el-table-column prop="carNo" label="车牌号" align="center" width="180" ></el-table-column>-->

        <el-table-column
            prop="carNo"
            label="车牌号"
            width="200"
            align="center"
        >
          <template #default="scope">
            <el-tag
                size="small"
                v-if="scope.row.car_type == '1'"
                v-text = "scope.row.carNo"
            ></el-tag
            >
            <el-tag
                size="small"
                type="success"
                v-else-if="scope.row.car_type == '2'"
                v-text = "scope.row.carNo"
            ></el-tag
            >
            <el-tag
                size="small"
                type="warning"
                v-else-if="scope.row.car_type == '3'"
                v-text = "scope.row.carNo"
            ></el-tag
            >
          </template>
        </el-table-column>

        <el-table-column prop="phone" label="用户手机号" width="150" align="center" ></el-table-column>

        <el-table-column prop="refund_amount" label="金额" width="200" align="center" > </el-table-column>

        <el-table-column prop="tkTypeName" label="退款渠道" width="200" align="center" ></el-table-column>

        <el-table-column prop="refund_time" label="退款时间" width="200" align="center" ></el-table-column>


      </el-table>
      <div class="pagination">
        <el-pagination
          background
          layout="total, prev, pager, next"
          :current-page="query.pageNum"
          :page-size="query.pageSize"
          :total="pageTotal"
          @current-change="handlePageChange"
        ></el-pagination>
      </div>
    </div>

  </div>
</template>

<script>
import { ref, reactive } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { tkDataList,exportRefundListData } from "../../api/index";

export default {
  name: "tklist",
  components: {},
  data() {
    return {
      tableH:0,
    };
  },
  setup() {
    const query = reactive({
      str: "",
      str2: "",
      str3: "",
      pageNum: 1,
      pageSize: 15,
    });
    const tableData = ref([]);
    const pageTotal = ref(0);
    // 获取表格数据
    const getData = () => {
      tkDataList(query).then((res) => {
        let data = res.data
        console.log(data)
        tableData.value = data.list;
        pageTotal.value = data.total;
      });
    };
    getData();

    // 查询操作
    const handleSearch = () => {
      query.pageNum = 1;
      getData();
    };
    // 分页导航
    const handlePageChange = (val) => {
      query.pageNum = val;
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
        ElMessage.success("删除成功");
        this.$refs.multipleTable.clearSelection();
      } else {
        ElMessage.warning(`请选择一条记录`);
      }
    },
    exportTuiKuan() {
       ElMessage.success("正在下载中·····");
      exportRefundListData(this.query).then((res) => {
        const url = window.URL.createObjectURL(new Blob([res]))
        const link = document.createElement('a')
        link.href = url
        link.setAttribute('download', '退款记录.xls')
        document.body.appendChild(link)
        link.click()
      });
    },
  },
  created() {
    let h = document.documentElement.clientHeight || document.body.clientHeight;
    this.tableH = h - 308 + 'px';
  },
};
</script>
