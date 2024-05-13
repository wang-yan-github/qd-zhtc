<template>
  <div>
    <div class="container">
      <div class="handle-box">
        <div class="left-panel">
          <el-button v-permission="['park_posapp_add', 'road_posapp_add']" type="primary" size="small" icon="el-icon-plus"
            @click="handleEdit(0, null, true, null)">添加
          </el-button>
        </div>
        <div class="right-panel">
          <el-input @keyup.enter="handleSearch()" size="small" v-model="query.version" placeholder="请输入版本号"
            class="handle-input mr10"></el-input>
          <el-button size="small" type="primary" icon="el-icon-search" @click="handleSearch">查询
          </el-button>
        </div>
        <div class="clear"></div>
      </div>
      <el-table :data="tableData" border class="table" ref="multipleTable" :max-height="tableH"
        header-cell-class-name="table-header" @selection-change="handleSelectionChange">
        <el-table-column type="index" label="序号" width="55" align="center"></el-table-column>
        <el-table-column prop="title" label="标题" align="center"></el-table-column>
        <el-table-column prop="version" label="版本号" align="center"></el-table-column>
        <el-table-column prop="upgrade_content" label="升级内容" align="center"></el-table-column>
        <el-table-column prop="apk_url" label="文件名" align="center"></el-table-column>
        <el-table-column label="创建时间" prop="create_time" width="180" align="center">
        </el-table-column>
        <el-table-column prop="update_time" label="更新时间" align="center" width="180"></el-table-column>
        <el-table-column prop="release_time" label="发布时间" align="center"></el-table-column>
        <el-table-column label="是否启用" align="center" width="100">
          <template #default="scope">
            <el-tag size="small" :type="scope.row.is_use === '1'
                ? 'success'
                : scope.row.is_use === '0'
                  ? 'danger'
                  : ''
              " v-if="scope.row.is_use == '0'">未启用
            </el-tag>
            <el-tag size="small" :type="scope.row.is_use === '1'
                ? 'success'
                : scope.row.is_use === '0'
                  ? 'danger'
                  : ''
              " v-else>已启用
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="220" align="center">
          <template #default="scope">
            <el-button v-permission="['road_posapp_status', 'park_posapp_status']" size="mini" type="text"
              icon="el-icon-circle-check" @click="handlePush(scope.$index, scope.row, scope.row.is_use)"
              v-if="scope.row.is_use == '0'">启用
            </el-button>
            <el-button v-permission="['road_posapp_status', 'park_posapp_status']" size="mini" type="text"
              icon="el-icon-circle-close" class="red" @click="handlePush(scope.$index, scope.row, scope.row.is_use)"
              v-else>禁用
            </el-button>
            <!--            <el-button-->
            <!--                size="mini"-->
            <!--                type="text"-->
            <!--                icon="el-icon-download"-->
            <!--                @click="handleEdit(scope.$index, scope.row, false)"-->
            <!--            >下载-->
            <!--            </el-button>-->

            <el-button v-permission="['road_posapp_delete', 'park_posapp_delete']" size="mini" type="text"
              icon="el-icon-delete" class="red" @click="handleDelete(scope.$index, scope.row)">删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination">
        <el-pagination background layout="total, prev, pager, next" :current-page="query.pageIndex"
          :page-size="query.pageSize" :total="pageTotal" @current-change="handlePageChange"></el-pagination>
      </div>
    </div>

    <!-- 新增编辑弹出框 -->
    <el-dialog :title="dialogT" v-model="editVisible" width="35%">
      <el-form label-width="70px" size="small" :rules="formRules" ref="formId" :model="form">
        <el-form-item label="标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入标题"></el-input>
        </el-form-item>
        <el-form-item label="版本号" prop="title">
          <el-input v-model="form.version" placeholder="请输入版本号"></el-input>
        </el-form-item>

        <el-form-item label="文件上传">
          <!-- limit 上传文件数量 -->
          <el-upload class="upload-demo" ref="uploadFileApk" action="https://jsonplaceholder.typicode.com/posts/"
            :on-preview="handlePreview" :on-remove="handleRemove" :on-change="onProgress" v-model="form.file_apk"
            :limit="1" :auto-upload="false">
            <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
            <!--            <el-button style="margin-left: 10px;" size="small" type="success" @click="submitUpload">上传到服务器</el-button>-->
            <!--            <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过500kb</div>-->
          </el-upload>
        </el-form-item>
        <el-form-item label="升级内容" prop="upgrade_content">
          <el-input type="textarea" :rows="4" placeholder="请输入升级内容" v-model="form.upgrade_content"></el-input>
        </el-form-item>
      </el-form>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="editVisible = false">取 消</el-button>
          <el-button type="primary" @click="save">确 定</el-button>
        </span>
      </template>
    </el-dialog>

    <el-dialog v-model="dialogVisible">
      <div class="customWidth">
        <img class="imgWidth" :src="dialogImageUrl" alt="" />
      </div>
    </el-dialog>
  </div>
</template>
<style>
.customWidth {
  padding: 20px;
}

.imgWidth {
  vertical-align: middle;
  display: inline-block;
  width: 100%;
}

.el-upload--text {
  height: auto;
  border: none;
  text-align: left;
}

.upload-demo {
  padding: 10px;
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
}
</style>
<script>
import { reactive, ref } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import Ueditor from "../../components/UE.vue";
import {
  delImgFile,
  delSysPosappManager,
  detailsSysPosappManager,
  sysPosappManagerEnableDisable,
  sysPosappManagerList,
} from "../../api/sysPosappManager.js";

import File_URL from '../../file_url';
import axios from 'axios';

export default {
  name: "sysPosappManagerList",
  components: {
    Ueditor,
  },
  data() {
    return {
      tableH: 0,
      imgViewUrl: File_URL.file_hx_img_url,//图片上传路径
      uploadUrl: File_URL.file_upload,//上传路径
      // 表单验证
      formRules: {
        version: [{ required: true, message: "必填项", trigger: "blur" }],
      },
    };
  },
  setup() {
    const query = reactive({
      title: "",
      version: "",
      category: "4",//公告咨询
      pageIndex: 1,
      pageSize: 15,
    });
    const uploadFileApk = ref([]);
    const tableData = ref([]);
    const pageTotal = ref(0);
    // 表格编辑时弹窗和保存
    const editVisible = ref(false);
    const dialogVisible = ref(false);
    const form = ref({});
    const fileIds = ref([]);
    const fileList2 = ref([]);
    let dialogT = ref("编辑");
    let idx = -1;

    // 获取表格数据
    const getData = () => {
      sysPosappManagerList(query).then((res) => {
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
    const handleDelete = (index, row) => {
      // 二次确认删除
      ElMessageBox.confirm("确定要删除吗？", "提示", {
        type: "warning",
      })
        .then(() => {
          let edit_resource = reactive({
            is_del: "1",
            id: row.id
          });
          delSysPosappManager(edit_resource).then((res) => {
            console.log(res);
            ElMessage.success("删除成功");
          }).then(() => {
            getData();
          });
        })
        .catch(() => {
        });
    };

    // 推送状态 禁用启用
    const handlePush = (index, row, is_use) => {
      let msg = "确定要禁用吗？";
      if (is_use == "0") {
        msg = "确定要启用吗？";
        is_use = "1";
      } else {
        is_use = "0";
      }
      // 二次确认禁用
      ElMessageBox.confirm(msg, "提示", {
        type: "warning",
      })
        .then(() => {
          let edit_resource = reactive({
            is_use: is_use,
            id: row.id
          });
          sysPosappManagerEnableDisable(edit_resource).then((res) => {
            console.log(res);
            ElMessage.success("操作成功");
          }).then(() => {
            getData();
          });
        })
        .catch(() => {
        });
    };

    const handleEdit = (index, row, type) => {
      editVisible.value = true;
      if (document.getElementsByClassName("el-upload-list__item-name").length != 0) {
        uploadFileApk.value.clearFiles();
      }
      form.value = {};
      fileIds.value = [];//图片上传ids 初始化;
      fileList2.value = [];//图片上传列表 初始化
      if (type) {
        dialogT.value = "新增";
      } else {
        dialogT.value = "编辑";
        idx = index;
        //获取数据信息
        detailsSysPosappManager(reactive({ id: row.id })).then((res) => {
          form.value = res.data;
          if (res.data.picture_id != "" && res.data.picture_id != null) {
            fileIds.value = res.data.picture_id.split(",");
          } else {
            fileIds.value = [];
          }
          if (res.data.fileList != "" && res.data.fileList != null) {
            fileList2.value = res.data.fileList;
          } else {
            fileList2.value = [];
          }
        });
      }
    };

    return {
      query,
      tableData,
      uploadFileApk,
      pageTotal,
      editVisible,
      form,
      fileIds,
      fileList2,
      dialogT,
      getData,
      handleSearch,
      handlePageChange,
      handleDelete,
      handlePush,
      handleEdit,
      multipleSelection: [],
      dialogImageUrl: "",
      dialogVisible,
    };
  },
  created() {
    let h = document.documentElement.clientHeight || document.body.clientHeight;
    this.tableH = h - 308 + 'px';
  },
  methods: {
    //上传图片操作
    handleSuccess(response, file, fileList) {
      this.fileIds.push(response.id + "");
      file.id = response.id;
      console.log(response, file, fileList);
    },
    handlePreview(file) {
      console.log(file);
    },
    //删除图片
    handleRemove(file, fileList) {
      console.log(file, fileList);
      let index = this.fileIds.indexOf(file.id + "");
      if (index == -1) {
        index = this.fileIds.indexOf(file.id);
      }
      let flag = 0;
      if (index != -1) {
        delImgFile(reactive({ id: file.id }))
          .then((res) => {
            flag = res.success;
          })
          .then(() => {
            if (flag == 0) {
              ElMessage.success("操作成功");
              //移除删除id
              this.fileIds.splice(index, 1);
            } else if (flag == -1) {
              ElMessage.error("操作失败!");
            }
          });

      }
    },
    //图片预览
    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url;
      this.dialogVisible = true;
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
      ElMessageBox.confirm("确定要删除吗？", "提示", {
        type: "warning",
      }).then(() => {
        var that = this;
        var val = this.selectedData;
        console.log(val);
        var ids = "";
        if (val) {
          val.forEach(function (item, index) {
            ids = ids + item.id + ",";
          });
          delSysPosappManager({ ids: ids }).then((res) => {
            ElMessage.success("删除成功");
            that.getData();
          })
        } else {
          ElMessage.warning(`请选择一条记录`);
        }
      }).catch(() => {
      });

    },
    onProgress(event, file, fileList) {
      this.form.file_apk = file;
      console.info(file);
    },
    save() {
      // 保存方法
      var that = this;
      var title = that.form.title;
      var version = that.form.version;
      var upgrade_content = that.form.upgrade_content;
      var file_apk = that.form.file_apk;
      if (title == undefined || title == null || title == '') {
        ElMessage.error("请输入标题");
        return;
      } else if (version == undefined || version == null || version == '') {
        ElMessage.error("请输入版本号");
        return;
      } else if (file_apk == undefined || file_apk == null || file_apk == '') {
        ElMessage.error("请上传文件");
        return;
      } else if (upgrade_content == undefined || upgrade_content == null || upgrade_content == '') {
        ElMessage.error("请输入升级内容");
        return;
      } else {
        let param = new FormData();
        param.append('fileApk', file_apk[0].raw);
        param.append('title', title);
        param.append('version', version);
        param.append('upgrade_content', upgrade_content);
        let config = {
          headers: { 'Content-Type': 'multipart/form-data', 'Authorization': localStorage.getItem("token_value") }
        }
        // 添加请求头
        axios.post(that.uploadUrl + '/sysPosappManager/save', param, config)
          .then(res => {
            if (res.data.code === 0) {
              self.ImgUrl = res.data.data
            }
            that.editVisible = false;
            that.form = {};
            that.getData();
          });
      }
    },


  },
};
</script>
