<template>
  <div>
    <div class="container">
      <div class="handle-box">
        <div class="left-panel">
          <el-form inline size="small">
            <el-input
              size="small"
              v-model="query.str"
              @keyup.enter="handleSearch()"
              placeholder="支持手机号查询"
              class="handle-input mr10"
            ></el-input>
            <span class="dispinline ml5 font14 color666">充值时间：</span>
            <el-date-picker
                    size="small"
                    v-model="form.time"
                    type="daterange"
                    range-separator="至"
                    start-placeholder="开始日期"
                    end-placeholder="结束日期"
                    class="datepicker"
                    @change="getQueryDate"
                  >
                  </el-date-picker>
            <span class="dispinline ml5"></span>
            <el-button
              size="small"
              type="primary"
              icon="el-icon-search"
              @click="handleSearch"
              >查询</el-button
            >
           <span class="dispinline ml5"></span>
              <el-button size="small" type="success" icon="el-icon-upload2" @click="exportExcel"  v-permission="['road_businessRecord_excel', 'park_businessRecord_excel']"
                >导出</el-button
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
        <!-- <el-table-column pro="ID" label="ID" width="55" align="center">
          <template #default="scope"> {{ scope.row.id }} </template> </el-table-column>-->
        <el-table-column prop="nickName" label="微信昵称" align="center" ></el-table-column>

        <el-table-column prop="phone" label="账号" width="200" align="center" ></el-table-column>

        <el-table-column prop="recharge_amount" label="充值金额" align="center" width="180" ></el-table-column>

        <el-table-column prop="payment_type" label="类型" width="150" align="center" ></el-table-column>

        <el-table-column prop="recharge_time" label="充值时间" width="200" align="center" > </el-table-column>

        <el-table-column prop="paymentNo" label="交易号" width="200" align="center" ></el-table-column>

        <el-table-column label="操作" width="120" align="center">
          <template #default="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleEdit(scope.$index, scope.row)"
              v-permission="['road_businessRecord_xgje', 'park_businessRecord_xgje']"
              >	修改金额
            </el-button>

          </template>
        </el-table-column>
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

    <!-- 编辑弹出框 -->
    <el-dialog title="修改金额" v-model="editVisible" width="20%">
      <el-form>
          <el-form-item>
              <el-input placeholder="输入金额" v-model="form.recharge_amount" oninput="if(isNaN(value)) { value = null } if(value.indexOf('.')>0){value=value.slice(0,value.indexOf('.')+3)}" ></el-input>
          </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="editVisible = false">取 消</el-button>
          <el-button type="primary" @click="saveEdit">确 定</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 详情弹出框 -->
    <el-dialog title="联合支付的订单" v-model="viewVisible" width="46%">
      <el-table
        :data="tableData"
        border
        class="table"
        ref="multipleTable"
        header-cell-class-name="table-header"
        @selection-change="handleSelectionChange"
      >
        <el-table-column
          prop="name"
          label="订单号"

          align="center"
        ></el-table-column>
        <el-table-column
          prop="purviews"
          label="支付金额"
          width="120"
          align="center"
        >
        </el-table-column>
        <el-table-column prop="purviews" label="订单金额" width="120" > </el-table-column>

        <el-table-column
          prop="addtimes"
          label="驶入时间"
          width="160"
          align="center"
        >
        </el-table-column>
        <el-table-column prop="addtimes" label="驶离时间" width="160" align="center">
        </el-table-column>
        <el-table-column prop="czs" label="停车时长" width="100" align="center">

        </el-table-column>
      </el-table>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="viewVisible = false">取 消</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ref, reactive } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { chongzhijiluList,chongzhijlUP,exportChongzhijilu } from "../../api/index";

export default {
  name: "chongzhilist",
  components: {},
  data() {
    return {
      tableH:0,
    };
  },
  setup() {

    const query = reactive({
      startTime:'',
      endTime:'',
      pageNum: 1,
      pageSize: 15,
      str:'',

    });


    const tableData = ref([]);
    const pageTotal = ref(0);
    // 获取表格数据
    const getData = () => {
      chongzhijiluList(query).then((res) => {
        var data = res.data;
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
    //导出excel
    const exportExcel = () => {
      exportChongzhijilu(query).then((res) => {
        const url = window.URL.createObjectURL(new Blob([res]))
        const link = document.createElement('a')
        link.href = url
        link.setAttribute('download', '充值记录.xlsx')
        document.body.appendChild(link)
        link.click()
      })
    }
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
      id: "",
      recharge_amount: "",
    });
    let idx = -1;
    const handleEdit = (index, row, type) => {
      if (type) {
        //dialogT='新增'
      } else {
        //dialogT='编辑';
        idx = index;
        console.log(row.id);
        Object.keys(form).forEach((item) => {
          form.id = row.id;
          form.recharge_amount = row.recharge_amount;
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
      chongzhijlUP(form).then((res) => {
          editVisible.value = false;
          if(res.code==0)
            ElMessage.success(res.logMsg);
          else
            ElMessage.error(res.logMsg);
          getData();
      })
    };

    //日期控件change方法
    const getQueryDate = () => {
      console.log(form.time);
      if(form.time!=null && form.time!= undefined && form.time.length>0){
	      query.startTime = dataFormat(form.time[0]);
	      query.endTime = dataFormat(form.time[1]);
      }else{
	      query.startTime = "";
	      query.endTime = "";
      }

    }
    //日期格式化 yyyy-MM-dd
    const dataFormat = (time) => {

      return `${time.getFullYear()}-${time.getMonth() + 1 >= 10 ? (time.getMonth() + 1) : '0' + (time.getMonth() + 1)}-${time.getDate() >= 10 ? time.getDate() : '0' + time.getDate()}`;
    }
    //日期格式化 yyyy-MM-dd HH:mm:ss
    const dateFormat = (time) => {
      return `${time.getFullYear()}-${time.getMonth() + 1 >= 10 ? (time.getMonth() + 1) : '0' + (time.getMonth() + 1)}-${time.getDate() >= 10 ? time.getDate() : '0' + time.getDate()} ${time.getHours() >= 10 ? time.getHours() : '0' + time.getHours()} : ${time.getMinutes()>=10?time.getMinutes():'0'+time.getMinutes()} : ${time.getSeconds() >= 10 ? time.getSeconds() : '0' + time.getSeconds()}`;
    }
    return {
      query,
      getQueryDate,
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
      exportExcel,
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
  },
  created() {
    let h = document.documentElement.clientHeight || document.body.clientHeight;
    this.tableH = h - 308 + 'px';
  },
};
</script>
