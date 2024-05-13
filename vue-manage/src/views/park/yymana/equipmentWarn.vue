<template>
  <div>
    <div class="container">
      <div class="handle-box">
        <div class="left-panel">
          <el-form inline label-width="80">
            <el-form-item label="">
              <el-input
                size="small"
                v-model="query.name"
                class="handle-input"
                placeholder="输入查询内容"
              ></el-input>
            </el-form-item>
            <el-form-item label="">
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
            </el-form-item>
            <el-form-item label="">
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
            </el-form-item>
            <el-form-item label="">
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
            </el-form-item>
            <el-form-item label="">
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
        <el-table-column pro="ID" label="ID" width="55" align="center">
          <template #default="scope">
            {{ scope.row.id }}
          </template>
        </el-table-column>
        <el-table-column
          prop="name"
          label="车牌号"
          width="130"
        ></el-table-column>
        <el-table-column
          prop="name"
          label="申诉人手机"
          width="130"
        ></el-table-column>
        <el-table-column label="报警图片" align="center" width="180">
          <template #default="scope">
            <el-image
              class="table-td-thumb"
              hide-on-click-modal="true" preview-teleported="true"
              :src="scope.row.thumb"
              :preview-src-list="[scope.row.thumb]"
            >
            </el-image>
          </template>
        </el-table-column>
        <el-table-column label="驾驶证" align="center" width="180">
          <template #default="scope">
            <el-image
              class="table-td-thumb"
              hide-on-click-modal="true" preview-teleported="true"
              :src="scope.row.thumb"
              :preview-src-list="[scope.row.thumb]"
            >
            </el-image>
          </template>
        </el-table-column>
        <el-table-column prop="name" label="备注"></el-table-column>

        <el-table-column
          label="申诉时间"
          prop="addtimes"
          width="180"
          align="center"
        >
        </el-table-column>

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
        <el-table-column label="审核人" prop="name" width="120" align="center">
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
        <el-input v-model="form.name" type="textarea" :rows="6"></el-input>
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
import { ref, reactive } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { fetchData } from "../../../api/index";
import Ueditor from "../../../components/UE.vue";

export default {
  name: "equipmentwarnA",
  components: {
    Ueditor,
  },
  data() {
    return {
      tableH:0
    };
  },
  setup() {
    const query = reactive({
      address: "",
      name: "",
      checked: false,
      pageIndex: 1,
      pageSize: 15,
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
    });
    let idx = -1;
    const handleEdit = (index, row) => {
      idx = index;
      Object.keys(form).forEach((item) => {
        form[item] = row[item];
      });

      editVisible.value = true;
    };

    const handleView = (index, row) => {
      ElMessageBox.confirm(
        "将解除车牌现有绑定者，重新绑定到申诉用户，确定审核通过吗？",
        "提示",
        {
          type: "warning",
        }
      )
        .then(() => {
          ElMessage.success("解除成功");
          that.tableData.splice(indexI, 1);
        })
        .catch(() => {
          ElMessage.info("取消解除");
        });
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
      handleView,
      saveEdit,
      multipleSelection: [],
      dialogImageUrl: "",
      ppVisible: false,
    };
  },
  created() {
    let h = document.documentElement.clientHeight || document.body.clientHeight;
    this.tableH = h - 308 + 'px';
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
