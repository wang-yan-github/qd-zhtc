<template>
  <div>
    <div class="container">
      <div class="handle-box">
        <div class="left-panel">
          <el-button
            type="danger"
            size="small"
            icon="el-icon-delete"
            @click="handleDeleteAll()"
            >批量删除</el-button
          >
        </div>
        <div class="right-panel">
          <el-form inline size="small">
            <el-input
              size="small"
              v-model="query.name"
              placeholder="请输入巡检姓名/工号"
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
            <el-select
              v-model="form.selvalue"
              filterable
              size="small"
              placeholder="所有路内"
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
              placeholder="全部状态"
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
            <span class="color666 font14 mlr5">添加时间：</span>
            <el-date-picker
              size="small"
              v-model="form.data1"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              class="datepicker"
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
        <el-table-column pro="ID" label="ID" width="55" align="center">
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
        <!-- <el-table-column label="头像(查看大图)" align="center">
                    <template #default="scope">
                        <el-image class="table-td-thumb" :src="scope.row.thumb" :preview-src-list="[scope.row.thumb]">
                        </el-image>
                    </template>
                </el-table-column> -->
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
        <el-table-column label="操作" width="320" align="center">
          <template #default="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleEdit(scope.$index, scope.row, false)"
              >反馈
            </el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-view"
              @click="handleView(scope.$index, scope.row)"
              >查看
            </el-button>

            <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              class="red"
              @click="handleDelete(scope.$index, scope.row)"
              >删除</el-button
            >
            <!-- <el-button
              size="mini"
              type="text"
              icon="el-icon-finished"
              @click="handleDelete(scope.$index, scope.row)"
              >领发票登记</el-button
            > -->
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
    <el-dialog title="上报反馈" v-model="editVisible" width="35%">
      <el-form label-width="70px">
        <el-form-item label="反馈内容" prop="phone">
          <el-input v-model="form.remima" type="textarea" :rows="8"></el-input>
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
    <el-dialog title="上报详情" v-model="viewVisible" width="40%">
      <el-descriptions
        class="margin-top handle-box"
        title=""
        :column="2"
        :size="size"
        border
      >
        <el-descriptions-item>
          <template v-slot:label>
            <i class="el-icon-user"></i>
            姓名
          </template>
          赵冬梅
        </el-descriptions-item>
        <el-descriptions-item>
          <template v-slot:label>
            <i class="el-icon-time"></i>
            时间
          </template>
          2020-12-04 09:09
        </el-descriptions-item>
        <el-descriptions-item>
          <template v-slot:label>
            <i class="el-icon-time"></i>
            巡检工号
          </template>
          40
        </el-descriptions-item>
        <el-descriptions-item>
          <template v-slot:label>
            <i class="el-icon-mobile-phone"></i>
            联系电话
          </template>
          15352925945
        </el-descriptions-item>
        <el-descriptions-item>
          <template v-slot:label>
            <i class="el-icon-bank-card"></i>
            内容
          </template>
          停车位有洗车铺的这个，白天晚上都有
        </el-descriptions-item>
        <el-descriptions-item>
          <template v-slot:label>
            <i class="el-icon-picture-outline"></i>
            照片
          </template>
          <el-image
            style="width: 100px; height: 100px"
            :src="url"
            hide-on-click-modal="true" preview-teleported="true"
            :preview-src-list="srcList"
          >
          </el-image>
        </el-descriptions-item>
        <el-descriptions-item>
          <template v-slot:label>
            <i class="el-icon-picture-outline"></i>
            反馈内容
          </template>
          无
        </el-descriptions-item>
      </el-descriptions>
      <!-- <table class="w desctable">
        <tr>
          <td class="tit" width="120">姓名</td>
          <td width="200">赵冬梅</td>
          <td class="tit" width="120">时间</td>
          <td>2020-12-04 09:09</td>
        </tr>
      </table> -->
    </el-dialog>
  </div>
</template>

<script>
import { ref, reactive } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { fetchData } from "../../../api/index";

export default {
  name: "tollmanfeed",
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
      data1: "",
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
      ppVisible: false,
      url: "https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg",
      srcList: [
        "https://fuss10.elemecdn.com/8/27/f01c15bb73e1ef3793e64e6b7bbccjpeg.jpeg",
      ],
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
};
</script>
