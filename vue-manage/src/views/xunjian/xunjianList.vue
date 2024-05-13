<template>
  <div>
    <div class="container">
      <div class="handle-box">
        <div class="left-panel">
          <el-button
            type="primary"
            size="small"
            icon="el-icon-plus"
            @click="addxunjian(0, null, true, null)"
            v-permission="['road_onsitemanaxj_add','park_onsitemana_add']"
            >添加</el-button
          >
        </div>
        <div class="right-panel">
          <el-form inline size="small">
            <el-input
              size="small"
              v-model="query.name"
              @keyup.enter="handleSearch()"
              placeholder="请输入姓名/手机号码"
              class="handle-input mr10"
            ></el-input>
            <el-select
              v-model="query.area_id"
              filterable
              size="small"
              placeholder="所有区域"
              class="w100"
              @change="getStreetList()"
            >
              <el-option value="">全部</el-option>
              <el-option
                v-for="(item, i) in result.area_list"
                :key="i"
                :label="item.area_name"
                :value="item.id"
              ></el-option>
            </el-select>
            <span class="dispinline ml5"></span>
            <el-select
              v-model="query.street_id"
              filterable
              size="small"
              placeholder="所有街道"
              class="w100"
              @change="getRoadList()"
            >
              <el-option value="">全部</el-option>
              <el-option
                v-for="(item, i) in result.street_list"
                :key="i"
                :label="item.street_name"
                :value="item.id"
              ></el-option>
            </el-select>
            <span class="dispinline ml5"></span>
            <el-select
              v-model="query.road_id"
              filterable
              size="small"
              placeholder="所有路内"
              class="w100"
            >
              <el-option value="">全部</el-option>
              <el-option
                v-for="(item, i) in result.road_list"
                :key="i"
                :label="item.road_name"
                :value="item.id"
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
        :max-height="tableH"
        header-cell-class-name="table-header"
        @selection-change="handleSelectionChange"
      >
        <!--<el-table-column
          type="selection"
          width="55"
          align="center"
        ></el-table-column>-->
        <el-table-column type="index" label="序号" width="55" align="center">
          <template #default="scope">
            {{scope.$index + 1 }}
          </template>
        </el-table-column>
        <el-table-column
          prop="job_no"
          label="工号"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="login_name"
          label="登陆账号"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="name"
          label="姓名"
          width="100"
          align="center"
        ></el-table-column>
        <el-table-column label="性别" align="center" width="100" prop="gender">
          <template #default="scope">
            <p v-if="scope.row.gender == '0'">男</p>
            <p v-if="scope.row.gender == '1'">女</p>
          </template>
        </el-table-column>
        <el-table-column
          prop="age"
          label="年龄"
          width="100"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="idCard"
          label="身份证号码"
          align="center"
          width="200"
        ></el-table-column>
        <el-table-column
          prop="phone"
          label="电话"
          align="center"
          width="120"
        ></el-table-column>
        <el-table-column
          prop="invoice_balance"
          label="发票余额"
          align="center"
          width="120"
        >
          <template #default="scope">
            <p v-if="scope.row.invoice_balance == null">0.00</p>
            <p v-else>{{ scope.row.invoice_balance }}</p>
          </template>
        </el-table-column>
        <el-table-column
          prop="berth_num"
          label="负责泊位总数"
          width="140"
          align="center"
        >
          <template #default="scope">
            <p v-if="scope.row.berth_num == null">0</p>
            <p v-else>{{ scope.row.berth_num }}</p>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="220" align="center">
          <template #default="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-view"
              @click="handleView(scope.$index, scope.row)"
              v-permission="['road_onsitemanaxj_details','park_onsitemana_details']"
              >查看
            </el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              v-permission="['road_onsitemanaxj_edit','park_onsitemana_edit']"
              @click="addxunjian(scope.$index, scope.row, false)"
              >编辑
            </el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              class="red"
              v-permission="['road_onsitemanaxj_delete','park_onsitemana_delete']"
              @click="handleCommand(scope.row, false)"
              >删除
            </el-button>
            <!-- <el-dropdown class="ml10 red" @command="handleCommand">
              <span class="el-dropdown-link">
                更多操作<i class="el-icon-arrow-down el-icon--right"></i>
              </span>
              <template v-slot:dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item
                    icon="el-icon-delete"
                    :command="composeValue('a', scope.row)"
                    >删除</el-dropdown-item
                  >
                  <el-dropdown-item
                    icon="el-icon-finished"
                    :command="composeValue('b', scope.row)"
                    >领发票登记</el-dropdown-item
                  >
                </el-dropdown-menu>
              </template>
            </el-dropdown> -->
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
    <el-dialog :title="dialogT" v-model="editVisible" width="35%" top="5vh">
      <el-form label-width="80px" :rules="formRules" ref="formId" :model="form">
        <el-form-item label="工号" prop="job_no">
          <el-input v-model="form.job_no" placeholder="请输入工号"></el-input>
        </el-form-item>
        <el-form-item
          label="登陆账号"
          prop="login_name"
          :label-width="formLabelWidth"
        >
          <el-input
            v-model="form.login_name"
            placeholder="请输入账号"
          ></el-input>
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="form.name" placeholder="请输入姓名"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="logpwd">
          <el-input
            type="password"
            autocomplete="new-password"
            v-model="form.logpwd"
            placeholder="请输入密码"
          ></el-input>
          <!-- <el-row :gutter="20">
            <el-col :span="18" ></el-col>
           <el-col :span="6">
              <el-button type="danger" @click="resetBtn">重置密码</el-button>
            </el-col>
          </el-row> -->
        </el-form-item>

        <el-form-item label="性别">
          <el-radio-group v-model="form.gender">
            <el-radio :label="0">男</el-radio>
            <el-radio :label="1">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="年龄" prop="age">
          <el-input
            v-model="form.age"
            placeholder="请输入年龄"
            type="number"
          ></el-input>
          <!--<el-input-number v-model="form.age" placeholder="请输入年龄" controls-position="right"  :min="18" :max="110"></el-input-number>-->
        </el-form-item>
        <el-form-item
          label="联系电话"
          prop="phone"
          :label-width="formLabelWidth"
        >
          <el-input v-model="form.phone" placeholder="请输入电话"></el-input>
        </el-form-item>
        <el-form-item label="身份证号" prop="idCard">
          <el-input
            v-model="form.idCard"
            placeholder="请输入身份证号"
          ></el-input>
        </el-form-item>

        <el-form-item label="上传照片(最多一张)">
          <el-upload
            :action="fileurl"
            list-type="picture-card"
            limit="1"
            :on-preview="handlePictureCardPreview"
            :on-remove="handleRemove"
            :on-success="handleSuccess"
            :file-list="fileList2"
          >
            <i class="el-icon-plus"></i>
          </el-upload>
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
            工号
          </template>
          {{ xjData.job_no }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template v-slot:label>
            <i class="el-icon-user"></i>
            登陆账号
          </template>
          {{ xjData.login_name }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template v-slot:label>
            <i class="el-icon-user"></i>
            姓名
          </template>
          {{ xjData.name }}
        </el-descriptions-item>

        <el-descriptions-item>
          <template v-slot:label>
            <i class="el-icon-help"></i>
            性别
          </template>
          <p v-if="xjData.gender == 0">男</p>
          <p v-if="xjData.gender == 1">女</p>
        </el-descriptions-item>
        <el-descriptions-item>
          <template v-slot:label>
            <i class="el-icon-time"></i>
            年龄
          </template>
          {{ xjData.age }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template v-slot:label>
            <i class="el-icon-mobile-phone"></i>
            联系电话
          </template>
          {{ xjData.phone }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template v-slot:label>
            <i class="el-icon-bank-card"></i>
            身份证号
          </template>
          {{ xjData.idCard }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template v-slot:label>
            <i class="el-icon-bank-card"></i>
            发票余额
          </template>
          {{ xjData.invoice_balance }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template v-slot:label>
            <i class="el-icon-picture-outline"></i>
            照片
          </template>
          <el-image
            class="table-td-thumb"
            :src="imgurl(xjData.pic_url)"
            hide-on-click-modal="true" preview-teleported="true"
            :preview-src-list="[imgurl(xjData.pic_url)]"
          >
          </el-image>
        </el-descriptions-item>
      </el-descriptions>
      <el-descriptions
        class="margin-top"
        title="值班信息"
        :column="1"
        :size="size"
        border
      >
        <el-descriptions-item v-if="ldData.length != 0">
          <template v-slot:label>
            <i class="el-icon-tickets"></i>
            负责停车场
          </template>
          <el-tag
            size="small"
            class="mar5 mb5"
            v-for="(item, index) in ldData"
            :key="index"
            >{{ item.road_name }}</el-tag
          >
        </el-descriptions-item>
        <el-descriptions-item v-if="ldData.length == 0">
          <template v-slot:label>
            <i class="el-icon-tickets"></i>
            负责停车场
          </template>
          <el-tag size="small">无</el-tag>
        </el-descriptions-item>
        <el-descriptions-item>
          <template v-slot:label>
            <i class="el-icon-office-building"></i>
            负责总泊位数
          </template>
          {{ xjData.berth_num }}
        </el-descriptions-item>
      </el-descriptions>
    </el-dialog>
    <!-- 图片预览 -->
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
</style>
<script>
import { reactive, ref } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { delImgFile } from "../../api/sysResourceIndex.js";
import {
  queryAreaData,
  queryRoadData,
  queryStreetData,
} from "../../api/index.js";
import File_URL from "../../file_url";
import {
  inspectManageDeleteData,
  inspectManageListData,
  inspectManageSaveData,
  inspectManageUpdData,
} from "../../api/inspectManage.js";

export default {
  name: "inspectManage",
  components: {},
  data() {
    //手机号码
    var checkPhone = (rule, value, callback) => {
      if (!value) {
        return callback(new Error("必填项"));
      } else {
        const reg = /^1[3|4|5|6|7|8|9][0-9]\d{8}$/;
        if (reg.test(value)) {
          callback();
        } else {
          return callback(new Error("请输入正确的手机号"));
        }
      }
    };
    var checkLoginName = (rule, value, callback) => {
      if (!value) {
        return callback(new Error("必填项"));
      } else {
        const reg = /^[A-Za-z0-9]+$/;
        if (reg.test(value)) {
          callback();
        } else {
          return callback(new Error("登陆账号只能是字母或数字"));
        }
      }
    };

    //年龄验证
    var checkage = (rule, value, callback) => {
      const reg = /^[0-9]+$/;
      console.log(value);
      if (value != null) {
        if (reg.test(value)) {
          callback();
        } else {
          return callback(new Error("请输入正确的年龄格式"));
        }
      } else {
        callback();
      }
    };
    //身份证号长度验证
    var checkIdCard = (rule, value, callback) => {
      let check = /^\d{15}|(\d{17}(\d|x|X))$/.test(value);
      console.log(value);
      if (value != null) {
        if (check) {
          if (value.length == 15) {
            callback();
          } else if (value.length == 18) {
            callback();
          } else {
            return callback(new Error("请输入正确的身份证号"));
          }
        } else {
          return callback(new Error("请输入正确的身份证号"));
        }
      } else {
        callback();
      }
    };
    return {
      tableH: 0,
      // 表单验证
      fileurl: File_URL.file_img_url,
      formRules: {
        job_no: [{ required: true, message: "必填项", trigger: "blur" }],
        name: [{ required: true, message: "必填项", trigger: "blur" }],
        login_name: [
          { validator: checkLoginName, required: true, trigger: "blur" },
        ],
        logpwd: [{ required: true, message: "必填项", trigger: "blur" }],
        qrpwd: [{ required: true, message: "必填项", trigger: "blur" }],
        phone: [{ validator: checkPhone, required: true, trigger: "blur" }],
        age: [{ validator: checkage, trigger: "blur" }],
        idCard: [{ validator: checkIdCard, trigger: "blur" }],
      },
    };
  },
  setup() {
    const query = reactive({
      area_id: "",
      street_id: "",
      road_id: "",
      name: "",
      pageIndex: 1,
      pageSize: 15,
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
      inspectManageListData(query).then((res) => {
        tableData.value = res.data.list;
        pageTotal.value = res.data.total;
      });
    };
    getData();

    // 获取区域
    const getAreaList = () => {
      queryAreaData(query).then((res) => {
        // console.log(res);
        result.area_list = res.data;
        console.log(res.data);
      });
    };
    getAreaList();
    // 获取街道
    let queryStreet = reactive({
      areaId: query.area_id,
    });
    const getStreetList = (type) => {
      queryStreet.areaId = query.area_id;
      result.road_list = [];
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

    let dialogT = ref("编辑");
    // 表格编辑时弹窗和保存
    const editVisible = ref(false);
    const viewVisible = ref(false);
    const ppVisible = ref(false);
    const form = ref({});
    const xjData = ref({ berth_num: 0, pic_url: "" });
    const ldData = ref({});
    const fileIds = ref([]);
    const fileList2 = ref([]);
    const dialogVisible = ref(false);
    //新增、修改弹框
    const handleEdit = (index, row, type) => {
      form.value = "";
      fileIds.value = []; //图片上传ids 初始化
      fileList2.value = []; //图片上传列表 初始化
      if (type) {
        //默认设置选中男
        form.value = { gender: 0, personType: 0 };
        dialogT.value = "新增";
        editVisible.value = true;
      } else {
        dialogT.value = "编辑";
        //获取数据信息
        inspectManageUpdData(reactive({ id: row.id })).then((res) => {
          //如果后台取出是字符需要转换成数字
          let data = res.data.manages;
          data.gender = parseInt(data.gender);
          data.personType = parseInt(data.personType);
          if (data.picture_id != "" && data.picture_id != null) {
            fileIds.value = data.picture_id.split(",");
          } else {
            fileIds.value = [];
          }
          if (data.fileList != "" && data.fileList != null) {
            fileList2.value = data.fileList;
          } else {
            fileList2.value = [];
          }
          form.value = data;
          editVisible.value = true;
        });
      }
    };
    //详情
    const handleView = (index, row) => {
      inspectManageUpdData(reactive({ id: row.id })).then((res) => {
        xjData.value = res.data.manages;
        const dx = res.data.manages.fileList.length;
        if (dx > 0) {
          xjData.value.pic_url = xjData.value.fileList[0].file_url;
        }
        xjData.value.berth_num = row.berth_num;
        ldData.value = res.data.list;
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
      fileIds,
      fileList2,
      xjData,
      ldData,
      dialogT,
      getStreetList,
      getRoadList,
      result,
      dialogVisible,
      handleSearch,
      handlePageChange,
      handleEdit,
      handleView,
      getData,
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
    handleSuccess(response, file, fileList) {
      this.fileIds.push(response.id + "");
      file.id = response.id;
    },
    //删除图片
    handleRemove(file, fileList) {
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
        return url;
      }
    },
    handleCommand(row) {
      let that = this;
      let falg = 1;
      ElMessageBox.confirm("确定要删除吗？", "提示", {
        type: "warning",
      })
        .then(() => {
          inspectManageDeleteData(reactive({ id: row.id }))
            .then((res) => {
              falg = res;
            })
            .then(() => {
              if (falg == 1) {
                ElMessage.success("操作成功");
                that.editVisible = false;
                that.query.pageIndex = 1;
                that.getData();
              } else {
                ElMessage.error("操作失败");
              }
            });
        })
        .catch(() => {
          ElMessage.info("取消删除");
        });
    },
    saveEdit(formName) {
      //保存方法
      let falg = 0;
      let that = this;
      let msg = "";
      this.$refs[formName].validate((valid) => {
        if (valid) {
          that.form.fileList = [];
          if (that.fileIds.length > 0) {
            that.form.picture_id = that.fileIds.join(",");
          } else {
            that.form.picture_id = "";
          }
          inspectManageSaveData(that.form)
            .then((res) => {
              falg = res.code;
              msg = res.msg;
            })
            .then(() => {
              if (falg == 0) {
                ElMessage.success(msg);
                that.editVisible = false;
                that.form.fileList = []; //图片文件数组
                that.fileIds = []; //图片上传ids
                that.query.pageIndex = 1;
                that.getData();
              } else if (falg == -1) {
                ElMessage.error(msg);
              }
            });
        }
      });
    },
    addxunjian: function (index, row, type) {
      var that = this;
      that.$nextTick(() => {
        this.$refs["formId"].clearValidate();
      });
      that.handleEdit(index, row, type);
    },
    resetBtn: function () {
      var that = this;
      that.form.logpwd = "";
    },
  },
};
</script>
