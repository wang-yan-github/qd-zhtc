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
              icon="el-icon-view"
              @click="handleView(scope.$index, scope.row)"
              >查看
            </el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleEdit(scope.$index, scope.row, false)"
              >编辑
            </el-button>
            <!-- <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              class="red"
              @click="handleDelete(scope.$index, scope.row)"
              >删除</el-button
            >
            <el-button
              size="mini"
              type="text"
              icon="el-icon-finished"
              @click="handleDelete(scope.$index, scope.row)"
              >领发票登记</el-button
            > -->
            <el-dropdown class="ml10 red" @command="handleCommand">
              <span class="el-dropdown-link">
                更多操作<i class="el-icon-arrow-down el-icon--right"></i>
              </span>
              <template v-slot:dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item icon="el-icon-delete" command="a"
                    >删除</el-dropdown-item
                  >
                  <el-dropdown-item icon="el-icon-finished" command="b"
                    >领发票登记</el-dropdown-item
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
        <el-form-item label="姓名">
          <el-input v-model="form.name"></el-input>
        </el-form-item>
        <el-form-item label="密码">
          <el-row :gutter="20">
            <el-col :span="18"
              ><el-input v-model="form.mima"></el-input
            ></el-col>
            <el-col :span="6">
              <el-button type="danger">重置密码</el-button>
            </el-col>
          </el-row>
        </el-form-item>
        <el-form-item label="性别">
          <!-- <el-radio-group v-model="radio2" size="medium">
            <el-radio-button label="男"></el-radio-button>
            <el-radio-button label="女"></el-radio-button>
          </el-radio-group> -->
          <el-radio-group v-model="form.radio2">
            <el-radio :label="1">男</el-radio>
            <el-radio :label="2">女</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="年龄">
          <el-input v-model="form.remima"></el-input>
        </el-form-item>
        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="form.remima"></el-input>
        </el-form-item>
        <el-form-item label="身份证号" prop="sfz">
          <el-input v-model="form.remima"></el-input>
        </el-form-item>
        <el-form-item label="巡检照片" prop="tupian">
          <el-upload
            action="https://jsonplaceholder.typicode.com/posts/"
            list-type="picture-card"
            :on-preview="handlePictureCardPreview"
            :on-remove="handleRemove"
          >
            <i class="el-icon-plus"></i>
          </el-upload>
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
    <el-dialog title="" v-model="viewVisible" width="40%">
      <el-descriptions
        class="margin-top handle-box"
        title="基本信息"
        :column="3"
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
            <i class="el-icon-help"></i>
            性别
          </template>
          女
        </el-descriptions-item>
        <el-descriptions-item>
          <template v-slot:label>
            <i class="el-icon-time"></i>
            年龄
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
            身份证号
          </template>
          320323198107070620
        </el-descriptions-item>
        <el-descriptions-item>
          <template v-slot:label>
            <i class="el-icon-picture-outline"></i>
            照片
          </template>
          无
        </el-descriptions-item>
      </el-descriptions>
      <el-descriptions
        class="margin-top"
        title="值班信息"
        :column="1"
        :size="size"
        border
      >
        <el-descriptions-item>
          <template v-slot:label>
            <i class="el-icon-tickets"></i>
            负责停车场
          </template>
          <el-tag size="small"
            >川垣三路川垣三路南段西侧B（08:00:00 - 20:00:00）</el-tag
          >
        </el-descriptions-item>
        <el-descriptions-item>
          <template v-slot:label>
            <i class="el-icon-office-building"></i>
            负责总泊位数
          </template>
          25
        </el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script>
import { ref, reactive } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { fetchData } from "../../../api/index";

export default {
  name: "tollmanlist",
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
  },
};
</script>
