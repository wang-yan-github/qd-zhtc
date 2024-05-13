<template>
  <div>
    <div class="container">
      <div class="handle-box">
        <div class="left-panel">
          <el-form inline label-width="80" size="small" class="lineH0">
            <el-form-item label="车牌号" class="search-mb0">
              <el-input
                @keyup.enter="handleSearch()"
                size="small"
                v-model="query.carNo"
                class="handle-input w170"
                placeholder="车牌号"
              ></el-input>
            </el-form-item>
            <el-form-item label="" class="search-mb0">
              <el-checkbox v-model="query.isYellowCard">黄牌</el-checkbox>
            </el-form-item>
            <el-form-item label="状态" class="search-mb0">
              <el-select
                clearable
                v-model="query.status"
                filterable
                size="small"
                class="w100"
              >
                <el-option key="-1" label="待审核 " value="-1"></el-option>
                <el-option key="1" label="通过 " value="1"></el-option>
                <el-option key="0" label="未通过 " value="0"></el-option>
              </el-select>
            </el-form-item>

            <el-form-item label="申诉人手机" class="search-mb0">
              <el-input
                @keyup.enter="handleSearch()"
                size="small"
                v-model="query.phone"
                class="handle-input w170"
                placeholder="手机号"
              ></el-input>
            </el-form-item>
            <el-form-item label="" class="search-mb0">
              <el-button
                size="small"
                type="primary"
                icon="el-icon-search"
                @click="handleSearch"
                >查询
              </el-button>
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
        <el-table-column type="index" label="序号" width="55" align="center">
        </el-table-column>
        <el-table-column
          prop="car_no"
          label="车牌号"
          width="110"
          align="center"
        >
          <template #default="scope">
            <el-tag
              size="small"
              v-if="scope.row.car_type == '1'"
              v-text="scope.row.car_no"
            ></el-tag>
            <el-tag
              size="small"
              type="success"
              v-else-if="scope.row.car_type == '2'"
              v-text="scope.row.car_no"
            ></el-tag>
            <el-tag
              size="small"
              type="warning"
              v-else-if="scope.row.car_type == '3'"
              v-text="scope.row.car_no"
            ></el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="phone"
          label="申诉人手机"
          align="center"
          width="130"
        >
          <template #default="scope">
            {{ scope.row.phone }}
          </template>
        </el-table-column>
        <el-table-column label="行驶证" align="center" width="130">
          <template #default="scope">
            <el-image
              class="table-td-thumb"
              :src="imgurl(scope.row.driveringlic_url)"
              hide-on-click-modal="true" preview-teleported="true"
              :preview-src-list="[imgurl(scope.row.driveringlic_url)]"
            >
            </el-image>
          </template>
        </el-table-column>
        <el-table-column label="驾驶证" align="center" width="130">
          <template #default="scope">
            <el-image
              class="table-td-thumb"
              :src="imgurl(scope.row.driverlic_url)"
              hide-on-click-modal="true" preview-teleported="true"
              :preview-src-list="[imgurl(scope.row.driverlic_url)]"
            >
            </el-image>
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注">
          <template #default="scope">
            {{ scope.row.remark }}
          </template>
        </el-table-column>

        <el-table-column
          label="申诉时间"
          prop="create_time"
          width="180"
          align="center"
        >
          <template #default="scope">
            {{ scope.row.create_time }}
          </template>
        </el-table-column>

        <el-table-column label="状态" align="center" width="100">
          <template #default="scope">
            <el-tag
              size="small"
              v-if="scope.row.status != 0 && scope.row.status != 1"
              >待审批</el-tag
            >
            <el-tag size="small" v-if="scope.row.status == 1" type="success"
              >通过</el-tag
            >
            <el-tag size="small" v-if="scope.row.status == 0" type="warning"
              >未通过</el-tag
            >
            <!--            <span v-if=""></span>-->
            <!--            <span v-if="scope.row.status == 1">通过</span>-->
            <!--            <span v-if="scope.row.status == 0">未通过</span>-->
          </template>
        </el-table-column>
        <el-table-column label="审核人" prop="name" width="120" align="center">
          <template #default="scope">
            {{ scope.row.login_name }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" align="center">
          <template #default="scope">
            <el-button
              v-if="scope.row.status != 0 && scope.row.status != 1"
              size="mini"
              type="text"
              icon="el-icon-circle-check"
              @click="handleView(scope.$index, scope.row)"
              v-permission="['road_carnumshensu_tg', 'park_carnumshensu_tg']"
              >通过
            </el-button>
            <el-button
              v-if="scope.row.status != 0 && scope.row.status != 1"
              size="mini"
              type="text"
              icon="el-icon-circle-close"
              @click="handleEdit(scope.$index, scope.row)"
              class="red"
              v-permission="['road_carnumshensu_bh', 'park_carnumshensu_bh']"
              >不通过
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

    <!-- 编辑弹出框 -->
    <el-dialog title="填写拒绝原因" v-model="editVisible" width="35%">
      <el-form label-width="70px" size="small">
        <el-input
          v-model="form.rejectReason"
          type="textarea"
          :rows="6"
        ></el-input>
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
  </div>
</template>

<script>
import { reactive, ref } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { complaintHandling, queryCarnoAppeal } from "../../api/index";
import Ueditor from "../../components/UE.vue";
import File_URL from "../../file_url";

export default {
  name: "noticeList",
  components: {
    Ueditor,
  },
  data() {
    return {
      tableH: 0,
      imgViewUrl: File_URL.file_hx_img_url, //图片回显路径
    };
  },
  setup() {
    const query = reactive({
      carNo: "",
      status: "",
      isYellowCard: false,
      phone: "",
      pageIndex: 1,
      pageSize: 15,
    });
    const tableData = ref([]);
    const pageTotal = ref(0);
    // 获取表格数据
    const getData = () => {
      queryCarnoAppeal(query).then((res) => {
        console.log(res.data);
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
      id: "",
      status: "",
      rejectReason: "",
    });
    let idx = -1;
    const handleEdit = (index, row) => {
      form.id = row.id;
      idx = index;
      Object.keys(form).forEach((item) => {
        form[item] = row[item];
      });

      editVisible.value = true;
    };

    const handleView = (index, row) => {
      form.id = row.id;
      ElMessageBox.confirm("确定审核通过吗？", "提示", {
        type: "warning",
      }).then(() => {
        form.status = 1;
        complaintHandling(form).then((res) => {
          ElMessage.success("审核通过成功");
          getData();
        });
      });
    };

    const saveEdit = () => {
      form.status = 0;
      complaintHandling(form).then((res) => {
        ElMessage.success("审核不通过成功");
        getData();
      });
      editVisible.value = false;
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
      handleView,
      saveEdit,
      multipleSelection: [],
      dialogImageUrl: "",
      ppVisible: false,
    };
  },
  created() {
    let h = document.documentElement.clientHeight || document.body.clientHeight;
    this.tableH = h - 308 + "px";
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
    //图片回显
    imgurl: function (url) {
      if (url != "" && url != null) {
        return this.imgViewUrl + url;
      }
    },
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
