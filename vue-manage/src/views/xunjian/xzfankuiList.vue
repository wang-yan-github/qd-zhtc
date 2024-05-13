<template>
  <div>
    <div class="container">
      <div class="handle-box">
        <div class="right-panel">
          <el-form inline size="small">
            <el-input
              size="small"
              v-model="query.keyword"
              @keyup.enter="handleSearch()"
              placeholder="请输入姓名/工号"
              class="handle-input mr10"
            ></el-input>
<!--            <el-select-->
<!--              v-model="query.area_id"-->
<!--              filterable-->
<!--              size="small"-->
<!--              placeholder="所有区域"-->
<!--              class="w100"-->
<!--              @change="getStreetList()"-->
<!--            >-->
<!--             <el-option value="">全部</el-option>-->
<!--              <el-option-->
<!--                v-for="(item, i) in result.area_list"-->
<!--                :key="i"-->
<!--                :label="item.area_name"-->
<!--                :value="item.id"-->
<!--              ></el-option>-->
<!--            </el-select>-->
<!--            <span class="dispinline ml5"></span>-->
<!--            <el-select-->
<!--              v-model="query.street_id"-->
<!--              filterable-->
<!--              size="small"-->
<!--              placeholder="所有街道"-->
<!--              class="w100"-->
<!--              @change="getRoadList()"-->
<!--            >-->
<!--             <el-option value="">全部</el-option>-->
<!--              <el-option-->
<!--                v-for="(item, i) in result.street_list"-->
<!--                :key="i"-->
<!--                :label="item.street_name"-->
<!--                :value="item.id"-->
<!--              ></el-option>-->
<!--            </el-select>-->
<!--            <span class="dispinline ml5"></span>-->
<!--            <el-select-->
<!--              v-model="query.road_id"-->
<!--              filterable-->
<!--              size="small"-->
<!--              placeholder="所有路内"-->
<!--              class="w100"-->
<!--            >-->
<!--             <el-option value="">全部</el-option>-->
<!--              <el-option-->
<!--                v-for="(item, i) in result.road_list"-->
<!--                :key="i"-->
<!--                :label="item.road_name"-->
<!--                :value="item.id"-->
<!--              ></el-option>-->
<!--            </el-select>-->
            <span class="dispinline ml5"></span>
            <!-- <span class="color666 font14 mlr5">时间：</span>
            <el-date-picker
              size="small"
              v-model="form.start"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              class="datepicker"
            >
            </el-date-picker> -->
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
        <!--<el-table-column
          type="selection"
          width="55"
          align="center"
        ></el-table-column>-->
        <el-table-column pro="ID" label="ID" width="55" align="center">
          <template #default="scope">
            {{(query.pageIndex - 1) * query.pageSize + scope.$index + 1}}
          </template>
        </el-table-column>
        <el-table-column
          prop="content"
          label="内容"
        ></el-table-column>
        <el-table-column
          prop="name"
          label="姓名"
          width="180"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="create_time"
          label="时间"
          width="200"
          align="center"
        ></el-table-column>
        <el-table-column label="状态" align="center" width="100">
          <template #default="scope">
            <el-tab v-if="scope.row.fkstate == 1">已反馈</el-tab>
            <el-tab v-else>未反馈</el-tab>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="220" align="center">
          <template #default="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleEdit(scope.$index, scope.row, false)"
              v-if="scope.row.fkstate == 0"
              v-permission="'road_onsitefeedbackxj_fk'"
              >反馈
            </el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-view"
              @click="handleView(scope.$index, scope.row)"
              v-permission="'road_onsitefeedbackxj_details'"
              >查看
            </el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              class="red"
              @click="handleDelete(scope.$index, scope.row)"
              v-permission="'road_onsitefeedbackxj_delete'"
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
    <el-dialog title="上报反馈" v-model="editVisible" width="35%">
      <el-form label-width="80px" :rules="formRules" ref="formId" :model="form">
        <!-- <el-input v-model="form.id" v-show="false"></el-input> -->
        <el-form-item label="反馈内容" prop="feedback">
          <el-input
            v-model="form.feedback"
            type="textarea"
            :rows="8"
            placeholder="请输入反馈内容"
          ></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="editVisible = false">取 消</el-button>
          <el-button type="primary" @click="saveEdit('formId')"
            >确 定</el-button
          >
        </span>
      </template>
    </el-dialog>
    <el-dialog v-model="ppVisible">
      <img width="100%" :src="dialogImageUrl" alt="" />
    </el-dialog>
    <!-- 详情弹出框 -->
    <el-dialog title="反馈详情" v-model="viewVisible" width="40%">
        <table class="desctable mgb20 w">
          <tr>
            <td class="tit" width="60">姓名</td>
            <td>{{ detailData.name }}</td>
            <td class="tit" width="80">时间</td>
            <td>
              <el-tag type="success">{{ detailData.create_time }}</el-tag>
            </td>
          </tr>
          <tr>
            <td class="tit" width="60">工号</td>
            <td>{{ detailData.job_no }}</td>
            <td class="tit" width="80">联系电话</td>
            <td>{{ detailData.phone?detailData.phone:'无' }}</td>
          </tr>
          <tr>
            <td class="tit" width="60">反馈时间</td>
            <td>{{ detailData.feedback_time }}</td>
            <td class="tit" width="60">反馈类型</td>
            <td>{{ detailData.feedback_type }}</td>
          </tr>
           <tr>
            <td class="tit" width="60">内容</td>
            <td colspan="3">{{ detailData.content }}</td>
          </tr>
          <tr v-if="detailData.fkstate==1">
            <td class="tit" width="60">反馈内容</td>
            <td colspan="3">{{ detailData.feedback }}</td>
          </tr>
          <tr>
            <td class="tit" width="60">照片</td>
            <td colspan="3">
              <template v-for="(item, i) in detailData.fileList" :key="i">
                <el-image
                  style="width: 100px; height: 100px"
                  hide-on-click-modal="true" preview-teleported="true"
                  :src="imgurl(item.file_url)"
                  class="ml5"
                  :preview-src-list="[imgurl(item.file_url)]"
                >
                </el-image>
              </template>
            </td>
          </tr>
        </table>
    </el-dialog>
  </div>
</template>

<script>
import { ref, reactive } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import {
  queryAreaData,
  queryStreetData,
  queryRoadData,
} from "../../api/index.js";
import {
  inspectFeedbackListData,
  inspectFeedbackDetailsData,
  inspectFeedbackData,
  inspectFeedbackDeleteData,
} from "../../api/inspectManage.js";

export default {
  name: "inspectFeedback",
  components: {},
  data() {
    return {
      // 表单验证
      formRules: {
        feedback: [{ required: true, message: "必填项", trigger: "blur" }],
      },
    };
  },
  setup() {
    const query = reactive({
      name: "",
      start: "",
      road_id: "",
      area_id: "",
      street_id: "",
      pageIndex: 1,
      pageSize: 10,
    });
    const result = reactive({
      area_list: [],
      street_list: [],
      road_list: [],
    });
    const tableData = ref([]);
    const pageTotal = ref(0);
    // 获取表格数据
    const getData = () => {
      inspectFeedbackListData(query).then((res) => {
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

    // 获取区域
    const getAreaList = () => {
      queryAreaData(query).then((res) => {
        result.area_list = res.data;
      });
    };
    getAreaList();
    // 获取街道
    let queryStreet = reactive({
      areaId: query.area_id,
    });
    const getStreetList = (type) => {
      queryStreet.areaId = query.area_id;
       result.road_list=[];
      query.road_id = "";
      query.street_id = "";
      queryStreetData(queryStreet).then((res) => {
        result.street_list = res.data;
      });
    };
    //获取路内下拉框数据
    const queryRoad = reactive({
      streetId: query.street_id,
    });
    const getRoadList = () => {
      queryRoad.streetId = query.street_id;
       query.road_id = "";
      queryRoadData(queryRoad).then((res) => {
        result.road_list = res.data;
      });
    };
    // 删除操作
    const handleDelete = (index, row) => {
      let falg = 0;
      // 二次确认删除
      ElMessageBox.confirm("确定要删除吗？", "提示", { type: "warning" })
        .then(() => {
          inspectFeedbackDeleteData(reactive({ id: row.id }))
            .then((res) => {
              falg = res.code;
            })
            .then(() => {
              if (falg == 0) {
                ElMessage.success("操作成功！");
                editVisible.value = false;
                query.pageIndex = 1;
                getData();
              } else {
                ElMessage.success("操作失败！");
              }
            });
        })
        .catch(() => {});
    };
    // 表格编辑时弹窗和保存
    const editVisible = ref(false);
    const viewVisible = ref(false);
    const ppVisible = ref(false);
    const form = ref({ id: "", feedback: "" });
    const detailData = ref({ pic_id: "" });
    const handleEdit = (index, row, type) => {
      form.value = { id: row.id };
      editVisible.value = true;
    };
    //详情数据
    const handleView = (index, row) => {
      inspectFeedbackDetailsData(reactive({ id: row.id })).then((res) => {
        detailData.value = res.data;
        viewVisible.value = true;
      });
    };
    return {
      query,
      tableData,
      pageTotal,
      editVisible,
      viewVisible,
      form,
      detailData,
      result,
      getStreetList,
      getRoadList,
      getData,
      handleSearch,
      handlePageChange,
      handleDelete,
      handleEdit,
      handleView,
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
    saveEdit(formName) {
      //保存方法
      let falg = 0;
      let that = this;
      this.$refs[formName].validate((valid) => {
        if (valid) {
          inspectFeedbackData(that.form)
            .then((res) => {
              falg = res.code;
            })
            .then(() => {
              if (falg == 0) {
                ElMessage.success("操作成功");
                that.editVisible = false;
                that.query.pageIndex = 1;
                that.getData();
              } else if (falg == -1) {
                ElMessage.error("操作失败!");
              }
            });
        }
      });
    },
    //图片回显
    imgurl: function (url) {
      if (url != "" && url != null) {
            return url;
        }
    },
  },
};
</script>
