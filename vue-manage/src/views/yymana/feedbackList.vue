<template>
  <div>
    <div class="container">
      <div class="handle-box">
        <div class="left-panel">
          <el-form inline size="small" class="lineH0">
              <el-form-item label="关键字" class="search-mb0">
                <el-input
                  @keyup.enter="handleSearch()"
                  size="small"
                  v-model="query.feedback_content"
                  placeholder="请输入内容关键字"
                  class="handle-input mr10 w170"
                ></el-input>
              </el-form-item>
              <el-form-item label="车牌号" class="search-mb0">
                 <el-input
                  @keyup.enter="handleSearch()"
                  size="small"
                  v-model="query.carno"
                  placeholder="请输入车牌号"
                  class="handle-input mr10 w170"
                ></el-input>
              </el-form-item>
              <el-form-item class="search-mb0">
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
                  @click="exportFeedBack"
                  v-permission="['park_feedbacklist_excel', 'road_feedbacklist_excel']"
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
        <!--<el-table-column
          type="selection"
          width="55"
          align="center"
        ></el-table-column>-->
        <el-table-column label="序号" width="55" align="center">
          <template #default="scope">
             {{scope.$index + 1}}
          </template>
        </el-table-column>
        <el-table-column
          prop="feedback_content"
          label="内容"
        ></el-table-column>
        <el-table-column label="用户账号"  prop="phone" width="120" align="center"></el-table-column>
        <el-table-column
          prop="carno"
          label="车牌号码"
          align="center"
          width="180"
        ></el-table-column>
        <el-table-column
          prop="feedback_time"
          label="反馈时间"
          width="180"
          align="center"
        ></el-table-column>
        <el-table-column label="状态" align="center" width="100">
          <template #default="scope">
            <el-tag size="small" type="warning" v-if="scope.row.feedback_status == 1">待回复</el-tag >
            <el-tag size="small" v-else>已回复</el-tag >
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120" align="center">
          <template #default="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-view"
              @click="handleView(scope.$index, scope.row)"
              v-permission="['road_feedbacklist_details', 'park_feedbacklist_details']"
              >	详情
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
    <!-- 详情弹出框 -->
    <el-dialog title="反馈详情" v-model="viewVisible" width="40%">
        <table class="desctable">
            <tr>
                <td class="tit" width="80">内容</td>
                <td colspan="3">{{detailData.feedback_content}}</td>
            </tr>
            <tr>
                <td class="tit" width="80">车牌号</td>
                <td>{{detailData.carno}}</td>
                <td class="tit" width="80">联系方式</td>
                <td>{{detailData.tel_phone}}</td>
            </tr>
            <tr v-if="detailData.fileList !== undefined && detailData.fileList.length >0">
                <td class="tit" width="80">附件</td>
                <td colspan="3">
                  <template v-for="(item, i) in detailData.fileList" :key="i">
                      <el-image
                        style="width: 100px; height: 100px"
                        hide-on-click-modal="true" preview-teleported="true"
                        :src="imgurl(item)"
                        class="ml5"
                        :preview-src-list="[imgurl(item)]"
                      >
                      </el-image>
                   </template>
                </td>
            </tr>
            <tr>
                <td class="tit" width="80">状态</td>
                <td colspan="3" v-if="detailData.feedback_status==1"><el-tag type="warning">待回复</el-tag></td>
                <td colspan="3" v-else><el-tag type="success">已回复</el-tag></td>
            </tr>
            <tr v-if="detailData.feedback_status!=1">
              <td class="tit" width="80">回复详情</td>
              <td colspan="3">
                  <template v-for="(item, i) in detailData.replyList" :key="i">
                        {{item.reply_content}}
                        <p>{{item.reply_time}}</p>
                  </template>
              </td>
            </tr>
        </table>
        <el-form label-width="80" class="mt20" label-position="top" :rules="formRules" ref="formId" :model="form" >
            <el-input v-model="form.id" v-if="idHide = false"></el-input>
            <el-form-item label="回复" v-if="liHide">
                <el-input type="textarea" :rows="8" v-model="form.reply_content"></el-input>
            </el-form-item>
        </el-form>
      <template #footer >
        <span class="dialog-footer"  v-if="liHide">
          <el-button @click="viewVisible = false">取 消</el-button>
          <el-button type="primary" @click="saveEdit('formId')">确 定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import {reactive, ref} from "vue";
import {ElMessage} from "element-plus";
import {operatefeedbackDetails, operatefeedbackListData, operatefeedbackUpdData,exportFeedBackData} from "../../api/operateAppeal";

export default {
  name: "userList",
  data() {
    return {
      tableH:0,
      // 表单验证
      formRules: {
        reply_content: [{ required: true, message: "必填项", trigger: "blur" }]
      },
    };
  },
  components: {},
  setup() {
    const query = reactive({
      car_no:'',
      feedback_content:'',
      pageIndex: 1,
      pageSize: 15,
    });
    const tableData = ref([]);
    const pageTotal = ref(0);
    // 获取表格数据
    const getData = () => {
      operatefeedbackListData(query).then((res) => {
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

    const viewVisible = ref(false);
    let detailData = ref({});
    let liHide = ref(true);
    const form = ref({ id: "", reply_content: "" });
    //详情数据
    const handleView = (index, row) => {
      operatefeedbackDetails(reactive({ id: row.id })).then((res) => {
        detailData.value =  res.data
        form.value.id = row.id;
        if(row.feedback_status == 2){
          liHide.value = false;
        }
      });
      viewVisible.value = true;
    };
    return {
      query,
      tableData,
      pageTotal,
      viewVisible,
      detailData,
      form,
      liHide,
      handleView,
      handleSearch,
      handlePageChange,
      getData,
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
    handleCommand(command) {
      this.$message("click on item " + command);
    },
     //图片回显
    imgurl: function (url) {
      if (url != "" && url != null) {
        return url;
      }
    },
    saveEdit(formName) {
      //保存方法
      let falg = 0;
      let msg ="";
      let that = this;
      this.$refs[formName].validate((valid) => {
        if (valid) {
          operatefeedbackUpdData(that.form).then((res) => {
              falg = res.code;
              msg = res.msg;
            }).then(() => {
              if (falg == 0) {
                ElMessage.success(msg);
                that.viewVisible = false;
                that.query.pageIndex = 1;
                that.getData();
              } else if (falg == -1) {
                ElMessage.error(msg);
              }
            });
        }
      });
    },
   exportFeedBack() {
      ElMessage.success("正在下载中·····");
      exportFeedBackData(this.query).then((res) => {
        const url = window.URL.createObjectURL(new Blob([res]))
        const link = document.createElement('a')
        link.href = url
        link.setAttribute('download', '反馈建议.xls')
        document.body.appendChild(link)
        link.click()
      });
    },
  },
};
</script>
