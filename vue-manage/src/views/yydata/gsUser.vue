<template>
  <div>
    <div class="container">
      <div class="handle-box">
        <div class="left-panel">
          <el-button
            type="primary"
            size="small"
            icon="el-icon-plus"
            @click="handleEdit(null, null, true)"
            v-permission="['road_companyUsers_add', 'park_companyUsers_add']"
            >添加
          </el-button>
        </div>
        <div class="right-panel">
          <el-form inline label-width="80" size="small" class="lineH0">
            <el-form-item label="企业名称" class="search-mb0">
              <el-input
                @keyup.enter="handleSearch()"
                size="small"
                v-model="query.bean.company_name"
                placeholder="企业名称"
                class="w200"
              ></el-input>
            </el-form-item>

            <!-- <el-form-item label="车牌号">
                          <el-input size="small" v-model="query.name" class="w100" ></el-input>
                        </el-form-item>-->

            <!--            <el-form-item label="注册时间">-->
            <!--              <el-date-picker v-model="form.name" -->
            <!--                type="daterange" range-separator="至"-->
            <!--                start-placeholder="开始日期" end-placeholder="结束日期" >-->
            <!--              </el-date-picker>-->
            <!--            </el-form-item>-->

            <el-form-item label="" class="search-mb0">
              <el-button
                size="small"
                type="primary"
                icon="el-icon-search"
                @click="handleSearch"
                >查询
              </el-button>
              <el-button size="small" type="success" icon="el-icon-upload2" @click="exportGSUser"
              v-permission="['road_companyUsers_excel', 'park_companyUsers_excel']"
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
        :max-height="tableH"
        ref="multipleTable"
        header-cell-class-name="table-header"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="index" label="序号" width="55" align="center">
        </el-table-column>

        <el-table-column prop="company_name" label="企业名称" width="300" align="center">
        </el-table-column>

        <el-table-column prop="address" label="企业地址" align="center"> </el-table-column>

        <el-table-column
          prop="liaison"
          label="联系人"
          width="200"
          align="center"
        >
        </el-table-column>

        <el-table-column
          prop="phone"
          label="联系方式"
          width="200"
          align="center"
        >
        </el-table-column>

        <el-table-column label="操作项" align="center" width="220">
          <template #default="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleEdit(scope.row.id, scope.row, false)"
              v-permission="['road_companyUsers_edit', 'park_companyUsers_edit']"
              >编辑
            </el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-check"
              @click="handleDetail(scope.$index, scope.row)"
              v-permission="['road_companyUsers_clbd', 'park_companyUsers_clbd']"
              >车辆绑定
            </el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              @click="deleteCompany(scope.row.id)"
              class="red"
              v-permission="['park_companyUsers_delete', 'road_companyUsers_delete']"
              >删除
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

    <el-dialog
      title="车辆绑定"
      v-model="viewVisible"
      width="50%"
      :close-on-press-escape="false"
      :close-on-click-modal="false"
      @close="closedialog"
    >
      <el-dialog
        width="36%"
        top="28vh"
        title="车牌人信息"
        v-model="viewVisibleInner"
        :close-on-click-modal="false"
        :close-on-press-escape="false"
        :show-close="false"
        :destroy-on-close="true"
        append-to-body
      >
        <el-form :inline="true" label-position="top" class="innerDg">
          <el-form-item label="姓名">
            <el-input
              ref="focusName"
              v-model="carName"
              @keyup.enter.native="focusPhoneInput"
              placeholder="请输入姓名"
            ></el-input>
          </el-form-item>
          <el-form-item label="手机号">
            <el-input
              ref="focusPhone"
              v-model="carPhone"
              @keyup.enter.native="carInfoSure"
              placeholder="请输入手机号"
            ></el-input>
          </el-form-item>
          <el-form-item label="&nbsp;">
            <el-button
              type="primary"
              @click="carInfoSure()"
              :disabled="disabledBtn"
              >确 定</el-button
            >
            <el-button @click="innerClose()">取 消</el-button>
          </el-form-item>
        </el-form>
      </el-dialog>
      <el-tabs type="card" v-model="activeName">
        <el-tab-pane label="车牌绑定" name="first">
          <div class="left-panel">
            <el-form
              inline
              label-width="80"
              size="small"
              @submit.native.prevent
            >
              <el-form-item label="车牌号">
                <el-input
                  size="small"
                  placeholder="车牌号"
                  v-model="carno"
                  class="w200"
                ></el-input>
              </el-form-item>
              <el-form-item label="" class="search-mb0">
                <el-checkbox v-model="isYellow">黄牌</el-checkbox>
              </el-form-item>
              <el-form-item label="">
                <el-button
                  size="small"
                  type="primary"
                  icon="el-icon-search"
                  @click="getCarnoInfo"
                  >查询
                </el-button>
              </el-form-item>
            </el-form>
          </div>
          <table class="desctable mgb20 w">
            <tr>
              <td class="tit" width="60">车牌号</td>
              <td v-text="carnoInfo.car_no"></td>
              <td class="tit" width="80">车牌类型</td>
              <td v-text="carnoInfo.carTypeName"></td>
            </tr>
            <tr>
              <td class="tit" width="60">名单类型</td>
              <td v-text="carnoInfo.rosterTypeName"></td>
              <td class="tit" width="80">所属车主</td>
              <td v-text="carnoInfo.memberName"></td>
            </tr>
            <tr>
              <td class="tit" width="60">姓名</td>
              <td>
                <el-input
                  ref="focusName"
                  v-model="carName"
                  @keyup.enter.native="focusPhoneInput"
                  placeholder="请输入姓名"
                ></el-input>
              </td>
              <td class="tit" width="80">手机号</td>
              <td>
                <el-input
                  ref="focusPhone"
                  v-model="carPhone"
                  @keyup.enter.native="carInfoSure"
                  placeholder="请输入手机号"
                ></el-input>
              </td>
            </tr>
          </table>
          <div align="right">
            <el-button
              size="small"
              type="success"
              @click="bindCarno(carnoInfo.id)"
              >绑定
            </el-button>
          </div>
        </el-tab-pane>
        <el-tab-pane label="车牌批量绑定" name="second">
          <el-form @submit.native.prevent>
            <el-form-item label="蓝牌">
              <el-tag
                :key="tag"
                v-for="tag in blueTags"
                closable
                :disable-transitions="false"
                @close="handleClose(tag)"
              >
                {{ tag }}
              </el-tag>
              <el-input
                class="input-new-tag"
                v-if="inputVisible"
                v-model="inputValue"
                ref="saveTagInput"
                placeholder="车牌号"
                size="small"
                @keyup.enter.native="handleInputConfirm"
              >
              </el-input>
              <el-button
                v-else
                class="button-new-tag"
                size="small"
                @click="showInput"
                >+ 车牌号</el-button
              >
            </el-form-item>
            <el-form-item label="黄牌">
              <el-tag
                :key="tag"
                v-for="tag in yellowTags"
                type="warning"
                closable
                :disable-transitions="false"
                @close="handleCloseY(tag)"
              >
                {{ tag }}
              </el-tag>
              <el-input
                class="input-new-tag"
                v-if="inputVisibleY"
                v-model="inputValueY"
                placeholder="车牌号"
                ref="saveTagInputY"
                size="small"
                @keyup.enter.native="handleInputConfirmY"
              >
              </el-input>
              <el-button
                v-else
                class="button-new-tag"
                size="small"
                @click="showInputY"
                >+ 车牌号</el-button
              >
            </el-form-item>
            <el-form-item label="绿牌">
              <el-tag
                :key="tag"
                type="success"
                v-for="tag in greenTags"
                closable
                :disable-transitions="false"
                @close="handleCloseG(tag)"
              >
                {{ tag }}
              </el-tag>
              <el-input
                class="input-new-tag"
                v-if="inputVisibleG"
                v-model="inputValueG"
                ref="saveTagInputG"
                placeholder="车牌号"
                size="small"
                @keyup.enter.native="handleInputConfirmG"
              >
              </el-input>
              <el-button
                v-else
                class="button-new-tag"
                size="small"
                @click="showInputG"
                >+ 车牌号</el-button
              >
            </el-form-item>
          </el-form>
          <div align="right">
            <el-button size="small" type="info" @click="exportExcel()"
              >模板下载</el-button
            >
            <input type="file" id="inputFile" @change="exportImport()" hidden />
            <el-button size="small" ref="inputFile" @click="inputFileClick()"
              >Excel导入</el-button
            >
            <el-button size="small" type="success" @click="banchBindCarno()"
              >绑定</el-button
            >
          </div>
        </el-tab-pane>
      </el-tabs>

      <div>
        <div class="handle-title mgb20 mt20 small-title-box">
          已绑定车牌信息
          <el-button size="small" type="success" icon="el-icon-upload2" plain @click="exportCompanyCar"
            >导出
          </el-button>
        </div>
        <!-- <div class="mt20"></div> -->
        <el-table
          :data="tableDataCars"
          border
          class="table"
          ref="multipleTable"
          header-cell-class-name="table-header"
        >
          <el-table-column
            prop="car_no"
            label="车牌号"
            width="130"
            align="center"
          ></el-table-column>
          <el-table-column
            prop="carTypeName"
            label="车牌类型"
            width="130"
            align="center"
          ></el-table-column>
          <el-table-column
            prop="rosterTypeName"
            label="名单类型"
            align="center"
          >
          </el-table-column>
          <el-table-column prop="memberName" label="所属车主" align="center">
          </el-table-column>
          <el-table-column prop="companyName" label="公司名称" align="center">
          </el-table-column>
          <el-table-column prop="phone" label="手机号" align="center">
          </el-table-column>
          <el-table-column label="操作项" prop="name" align="center">
            <template #default="scope">
              <el-button
                size="mini"
                type="text"
                icon="el-icon-close"
                @click="unbindCarno(scope.row.id)"
                >解绑
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        <div class="pagination">
          <el-pagination
            background
            layout="total, prev, pager, next"
            :current-page="comCarsParams.pageIndex"
            :page-size="comCarsParams.pageSize"
            :total="pageTotalCars"
            @current-change="handlePageChangeCars"
          ></el-pagination>
        </div>
      </div>

      <!--<el-tabs type="card" v-model="activeName">
                <el-tab-pane label="已绑定车牌信息" name="first">

                </el-tab-pane>
            </el-tabs>-->

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="viewVisible = false">关 闭</el-button>
        </span>
      </template>
    </el-dialog>
    <!-- 编辑弹出框 -->
    <el-dialog :title="dialogT" v-model="editVisible" width="30%">
      <el-form
        label-width="100px"
        :rules="formRules"
        ref="formId"
        :model="companyForm"
      >
        <el-form-item label="企业名称" prop="company_name">
          <el-input
            placeholder="企业名称"
            v-model="companyForm.company_name"
          ></el-input>
        </el-form-item>
        <el-form-item label="企业地址" prop="address">
          <el-input
            placeholder="企业地址"
            v-model="companyForm.address"
          ></el-input>
        </el-form-item>
        <el-form-item label="联系人" prop="liaison">
          <el-input
            placeholder="联系人"
            v-model="companyForm.liaison"
          ></el-input>
        </el-form-item>
        <el-form-item label="联系方式" prop="phone">
          <el-input
            placeholder="联系方式"
            v-model="companyForm.phone"
          ></el-input>
        </el-form-item>
      </el-form>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="editVisible = false">取 消</el-button>
          <el-button type="primary" @click="saveEdit()">确 定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ref, reactive, nextTick } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import {
  getList,
  getCompanyCars,
  getDetail,
  saveCompany,
  updateCompany,
  unbindCar,
  delCompany,
  getCarnoDetail,
  bindCar,
  batchBindCars,
  exportGSUserData,
  exportCompanyCars,
} from "../../api/companyManage.js";
import { delMonitorEquipment } from "../../api/monitorEquipment";
import axios from "axios";
import File_URL from "../../file_url";

export default {
  data() {
    return {
      // blueTags:[],
      // yellowTags:[],
      // greenTags:[],
      tableH: 0,
    };
  },
  name: "companyUsers",
  setup() {
    const closedialog = () => {
      blueTagsValue.value = [];
      yellowTagsValue.value = [];
      greenTagsValue.value = [];
    };
    const query = reactive({
      pageNum: 1,
      pageSize: 15,
      bean: {
        company_name: "",
      },
    });
    const tableData = ref([]);
    const pageTotal = ref(0);
    getCompanyCars;
    // 获取表格数据
    const getData = () => {
      getList(query).then((res) => {
        let data = res.data;
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

    let tagsType = ref([]);
    let blueTags = ref([]);
    let blueTagsValue = ref([]);
    let yellowTags = ref([]);
    let yellowTagsValue = ref([]);
    let greenTags = ref([]);
    let inputFile = ref(null);
    let greenTagsValue = ref([]);
    let inputVisible = ref(false);
    let inputValue = ref("");
    let inputVisibleY = ref(false);
    let inputValueY = ref("");
    let inputVisibleG = ref(false);
    let inputValueG = ref("");
    let saveTagInputY = ref(null);
    let saveTagInput = ref(null);
    let saveTagInputG = ref(null);

    let dialogT = ref("新增");
    let isYellow = ref(false);

    // 表格编辑时弹窗和保存
    const editVisible = ref(false);
    const viewVisible = ref(false);
    const viewVisibleInner = ref(false);
    const formId = ref(null);
    let companyForm = ref({});
    let companyId = ref("");
    const handleEdit = (index, row, type) => {
      if (type) {
        companyForm.value = {};
        dialogT.value = "新增";
      } else {
        dialogT.value = "编辑";
        companyId.value = row.id;
        companyForm.value.id = row.id;
        // Object.keys(form).forEach((item) => {
        //   form[item] = row[item];
        // });
        const detailQuery = reactive({
          id: row.id,
        });
        getDetail(detailQuery).then((res) => {
          companyForm.value = res.data;
        });
      }
      editVisible.value = true;
      formId.value.clearValidate();
    };

    const saveEdit = () => {
      formId.value.validate((valid) => {
        if (valid) {
          if (companyId.value != null && companyId.value != "") {
            updateCompany(companyForm.value).then((res) => {
              if (res.code == 0) {
                ElMessage.success(`修改成功`);
                editVisible.value = false;
                getData();
              } else {
                ElMessage.error(res.msg);
              }
            });
          } else {
            saveCompany(companyForm.value).then((res) => {
              if (res.code == 0) {
                ElMessage.success(`新增成功`);
                editVisible.value = false;
                getData();
              } else {
                ElMessage.error(res.msg);
              }
            });
          }
        } else {
          ElMessage.error("请输入必填项！");
          return false;
        }
      });
    };
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
      checked: false,
      startTime: "",
      endTime: "",
    });
    const formRules = {
      company_name: [
        { required: true, message: "请输入企业名称", trigger: "blur" },
      ],
      address: [{ required: true, message: "请输入企业地址", trigger: "blur" }],
      liaison: [{ required: true, message: "请输入联系人", trigger: "blur" }],
      phone: [{ required: true, message: "请输入联系方式", trigger: "blur" }],
    };
    let idx = -1;
    let currCompanyId = ref("");
    const handleDetail = (index, row) => {
      idx = index;
      blueTags.value = [];
      inputVisible.value = false;
      inputVisibleY.value = false;
      inputVisibleG.value = false;
      yellowTags.value = [];
      greenTags.value = [];
      currCompanyId.value = row.id;
      carno.value = "";
      carnoInfo.value = {};
      getComCars();
      viewVisible.value = true;
    };
    let carnoInfo = ref({});
    let carno = ref("");
    // 获取车牌信息
    const getCarnoInfo = () => {
      let carType = "1";
      if (carno.value.length == 8) {
        carType = "2";
      } else if (isYellow.value) {
        carType = "3";
      }

      let carnoParams = reactive({
        carno: carno,
        carType: carType,
      });
      getCarnoDetail(carnoParams).then((res) => {
        if (res.code == 0) {
          carnoInfo.value = res.data;
        } else {
          ElMessage({
            showClose: true,
            message: res.msg,
            type: "error",
          });
        }
      });
    };
    // 绑定车牌
    const bindCarno = (index) => {
      if (index != null && index != "") {
        let bindInfoParams = reactive({
          carno_id: index,
          company_id: currCompanyId.value,
        });
        bindCar(bindInfoParams).then((res) => {
          if (res.code == 0) {
            ElMessage({
              showClose: true,
              message: "绑定成功！",
              type: "success",
            });
            getComCars();
          } else {
            ElMessage({
              showClose: true,
              message: res.msg,
              type: "error",
            });
          }
        });
      } else {
        ElMessage({
          showClose: true,
          message: "请先查询需要绑定的车牌信息！",
          type: "error",
        });
      }
    };

    const banchBindCarno = (index) => {
      for (var i = 0; i < blueTagsValue.value.length; i++) {
        var value = blueTagsValue.value[i];
        if (value.indexOf(",")) {
          var values = value.split(",");
          var len = values.length;
          blueTagsValue.value[i] = values[len - 1];
        }
      }
      for (var i = 0; i < yellowTagsValue.value.length; i++) {
        var value = yellowTagsValue.value[i];
        if (value.indexOf(",")) {
          var values = value.split(",");
          var len = values.length;
          yellowTagsValue.value[i] = values[len - 1];
        }
      }
      for (var i = 0; i < greenTagsValue.value.length; i++) {
        var value = greenTagsValue.value[i];
        if (value.indexOf(",")) {
          var values = value.split(",");
          var len = values.length;
          greenTagsValue.value[i] = values[len - 1];
        }
      }
      let bindInfoParams = reactive({
        blueCars: blueTagsValue.value,
        yellowCars: yellowTagsValue.value,
        greenCars: greenTagsValue.value,
        companyId: currCompanyId,
      });
      batchBindCars(bindInfoParams).then((res) => {
        if (res.code == 0) {
          ElMessage({
            showClose: true,
            message: "绑定成功！",
            type: "success",
          });
          getComCars();
        } else {
          ElMessage({
            showClose: true,
            message: res.msg,
            type: "error",
          });
        }
      });
    };

    // 获取车牌信息
    const unbindCarno = (index) => {
      ElMessageBox.confirm("确定要解绑该车辆吗？", "提示", {
        type: "warning",
      })
        .then(() => {
          let unbindInfoParams = reactive({
            carno_id: index,
            company_id: currCompanyId.value,
          });
          unbindCar(unbindInfoParams).then((res) => {
            if (res.code == 0) {
              ElMessage({
                showClose: true,
                message: "解除绑定成功",
                type: "success",
              });
              getComCars();
            } else {
              ElMessage({
                showClose: true,
                message: res.msg,
                type: "error",
              });
            }
          });
        })
        .catch(() => {});
    };
    //子页面列表
    const tableDataCars = ref([]);
    const pageTotalCars = ref(0);
    let comCarsParams = reactive({
      companyId: "",
      pageIndex: 1,
      pageSize: 5,
    });
    // 获取车牌信息
    const getComCars = () => {
      comCarsParams.companyId = currCompanyId.value;
      getCompanyCars(comCarsParams).then((res) => {
        let data = res.data;
        tableDataCars.value = data.list;
        pageTotalCars.value = data.total;
      });
    };
    // 分页导航
    const handlePageChangeCars = (val) => {
      comCarsParams.pageIndex = val;
      getComCars();
    };

    const deleteCompany = (id) => {
      ElMessageBox.confirm("确定要删除公司和车牌绑定信息吗？", "提示", {
        type: "warning",
      })
        .then(() => {
          const ddeleteQuery = reactive({
            id: id,
          });
          delCompany(ddeleteQuery).then((res) => {
            if (res.code == 0) {
              ElMessage.success(`删除成功`);
              getData();
            } else {
              ElMessage.error(res.msg);
            }
          });
        })
        .catch(() => {});
    };
    const handleClose = (tag) => {
      blueTags.value.splice(blueTags.value.indexOf(tag), 1);
      if (tagsType.value == "blueTags") {
        blueTagsValue.value.splice(blueTagsValue.value.indexOf(tag), 1);
      } else if (tagsType.value == "yellowTags") {
        yellowTagsValue.value.splice(yellowTagsValue.value.indexOf(tag), 1);
      } else if (tagsType.value == "greenTags") {
        greenTagsValue.value.splice(greenTagsValue.value.indexOf(tag), 1);
      }
    };

    const showInput = () => {
      inputVisible.value = true;
      nextTick((_) => {
        saveTagInput.value.input.focus();
      });
    };

    // 添加姓名手机号--start
    const carName = ref("");
    const carPhone = ref("");
    let focusName = ref(null);
    let focusPhone = ref(null);
    let tags = ref("");
    const focusPhoneInput = () => {
      nextTick((_) => {
        focusPhone.value.input.focus();
      });
    };
    let disabledBtn = ref(false);
    const carInfoSure = () => {
      disabledBtn.value = true;
      console.log(
        "车牌人信息验证：" + JSON.stringify(carName) + JSON.stringify(carPhone)
      );
      if (carName.value && carPhone.value) {
        viewVisibleInner.value = false;
        if (tagsType.value == "blueTags") {
          blueTagsValue.value.push(
            blueTags.value + "-" + carName.value + "-" + carPhone.value
          );
        } else if (tagsType.value == "yellowTags") {
          yellowTagsValue.value.push(
            yellowTags.value + "-" + carName.value + "-" + carPhone.value
          );
        } else if (tagsType.value == "greenTags") {
          greenTagsValue.value.push(
            greenTags.value + "-" + carName.value + "-" + carPhone.value
          );
        }
        ElMessage({
          showClose: true,
          message: "车牌信息维护成功！",
          type: "success",
        });
      } else {
        ElMessage({
          showClose: true,
          message: "姓名和手机号不能为空！",
          type: "error",
        });
      }
    };
    const innerClose = () => {
      viewVisibleInner.value = false;
      tags.value.pop();
      // ElMessage({
      //     showClose: true,
      //     message: '取消添加车牌信息！',
      //     type: 'error',
      // })
    };
    // 添加姓名手机号--end

    const formRulesInner = {
      carN: [{ required: true, message: "请输入姓名", trigger: "blur" }],
      carP: [{ required: true, message: "请输入手机号", trigger: "blur" }],
    };

    const handleInputConfirm = () => {
      tagsType.value = "blueTags";
      //let inputValue = inputValue.value;
      if (inputValue.value) {
        blueTags.value.push(inputValue.value);
        // console.info(inputValue)
      } else {
        ElMessage({
          showClose: true,
          message: "请输入内容",
          type: "error",
        });
        return;
      }
      inputVisible.value = true;
      inputValue.value = "";
      viewVisibleInner.value = true; //填写姓名手机弹窗
      disabledBtn.value = false;
      carName.value = ""; //清空姓名手机号
      carPhone.value = ""; //清空手机号
      tags = blueTags;
      setTimeout(() => {
        nextTick((_) => {
          focusName.value.input.focus();
        });
      }, 0);
    };
    const handleInputConfirmBlur = () => {
      //let inputValue = inputValue.value;
      if (inputValue.value != "") {
        blueTags.value.push(inputValue.value);
        inputVisible.value = true;
        inputValue.value = "";
        carName.value = ""; //清空姓名手机号
        carPhone.value = ""; //清空手机号
        viewVisibleInner.value = true; //填写姓名手机弹窗
        disabledBtn.value = false;
        tags = blueTags;
        setTimeout(() => {
          nextTick((_) => {
            focusName.value.input.focus();
          });
        }, 0);
      } else {
        return false;
      }
    };

    // yellow
    const handleCloseY = (tag) => {
      yellowTags.value.splice(yellowTags.value.indexOf(tag), 1);
    };

    const showInputY = () => {
      inputVisibleY.value = true;
      nextTick((_) => {
        saveTagInputY.value.input.focus();
      });
    };

    const handleInputConfirmY = () => {
      tagsType.value = "yellowTags";
      let inputValue = inputValueY.value;
      if (inputValue) {
        yellowTags.value.push(inputValue);
      } else {
        ElMessage({
          showClose: true,
          message: "请输入内容",
          type: "error",
        });
        return;
      }
      inputVisibleY.value = true;
      inputValueY.value = "";
      viewVisibleInner.value = true; //填写姓名手机弹窗
      disabledBtn.value = false;
      carName.value = ""; //清空姓名手机号
      carPhone.value = ""; //清空手机号
      tags = yellowTags;
      setTimeout(() => {
        nextTick((_) => {
          focusName.value.input.focus();
        });
      }, 0);
    };
    const handleInputConfirmYBlur = () => {
      let inputValue = inputValueY.value;
      if (inputValue != "") {
        yellowTags.value.push(inputValue);
        inputVisibleY.value = true;
        inputValueY.value = "";
        viewVisibleInner.value = true; //填写姓名手机弹窗
        disabledBtn.value = false;
        tags = yellowTags;
        setTimeout(() => {
          nextTick((_) => {
            focusName.value.input.focus();
          });
        }, 0);
      } else {
        return false;
      }
    };
    // G
    const handleCloseG = (tag) => {
      greenTags.value.splice(greenTags.value.indexOf(tag), 1);
    };

    const showInputG = () => {
      inputVisibleG.value = true;
      nextTick((_) => {
        saveTagInputG.value.input.focus();
      });
    };

    const handleInputConfirmG = () => {
      tagsType.value = "greenTags";
      let inputValue = inputValueG.value;
      if (inputValue) {
        greenTags.value.push(inputValue);
      } else {
        ElMessage({
          showClose: true,
          message: "请输入内容",
          type: "error",
        });
        return;
      }
      inputVisibleG.value = true;
      inputValueG.value = "";
      viewVisibleInner.value = true; //填写姓名手机弹窗
      disabledBtn.value = false;
      carName.value = ""; //清空姓名手机号
      carPhone.value = ""; //清空手机号
      tags = greenTags;
      setTimeout(() => {
        nextTick((_) => {
          focusName.value.input.focus();
        });
      }, 0);
    };
    const handleInputConfirmGBlur = () => {
      let inputValue = inputValueG.value;
      if (inputValue != "") {
        greenTags.value.push(inputValue);
        inputVisibleG.value = true;
        inputValueG.value = "";
        viewVisibleInner.value = true; //填写姓名手机弹窗
        disabledBtn.value = false;
        tags = greenTags;
        setTimeout(() => {
          nextTick((_) => {
            focusName.value.input.focus();
          });
        }, 0);
      } else {
        return false;
      }
    };

    //导出excel
    const exportExcel = () => {
      axios({
        url: File_URL.file_down + "excel/exportOperateCarno",
        method: "GET",
        headers: {
          "Content-Type": "multipart/form-data",
          Authorization: localStorage.getItem("token_value"),
        },
        responseType: "blob", // important
      }).then((response) => {
        const url = window.URL.createObjectURL(new Blob([response.data]));
        const link = document.createElement("a");
        link.href = url;
        link.setAttribute("download", "车牌批量导入模板.xlsx");
        document.body.appendChild(link);
        link.click();
      });
    };

    //模拟上传按钮点击
    const inputFileClick = () => {
      var e = document.createEvent("MouseEvents");
      e.initEvent("click", true, true);
      document.getElementById("inputFile").dispatchEvent(e);
    };

    //导入excel
    const exportImport = () => {
      var formData = new FormData();
      console.log(document.getElementById("inputFile").files[0]);
      formData.append("file", document.getElementById("inputFile").files[0]);
      formData.append("companyId", currCompanyId.value);
      axios({
        url: File_URL.file_down + "excel/importOperateCarno",
        method: "POST",
        data: formData,
        headers: {
          "Content-Type": "multipart/form-data",
          Authorization: localStorage.getItem("token_value"),
        },
      }).then((res) => {
        var data = res.data;
        if (data.code == 0) {
          ElMessage({
            showClose: true,
            message: data.msg,
            type: "success",
          });
          getComCars();
        } else {
          ElMessage({
            showClose: true,
            message: data.msg,
            type: "error",
          });
        }
      });
      //清空
      document.getElementById("inputFile").value = "";
    };

    return {
      closedialog,
      query,
      tableData,
      isYellow,
      pageTotal,
      tableDataCars,
      pageTotalCars,
      comCarsParams,
      deleteCompany,
      getComCars,
      handlePageChangeCars,
      carno,
      carnoInfo,
      companyForm,
      getCarnoInfo,
      editVisible,
      bindCarno,
      banchBindCarno,
      unbindCarno,
      viewVisible,
      viewVisibleInner,
      formRules,
      formId,
      form,
      dialogT,
      handleEdit,
      handleSearch,
      handlePageChange,
      handleDetail,
      saveEdit,
      multipleSelection: [],
      dialogImageUrl: "",
      ppVisible: false,
      isActive: false,
      activeName: "first",
      blueTags,
      yellowTags,
      greenTags,
      inputVisible,
      inputValue,
      inputVisibleY,
      inputValueY,
      inputVisibleG,
      inputValueG,
      saveTagInput,
      inputFile,
      saveTagInputY,
      saveTagInputG,
      handleClose,
      showInput,
      handleInputConfirm,
      handleCloseY,
      showInputY,
      handleInputConfirmY,
      handleCloseG,
      showInputG,
      handleInputConfirmG,
      carName,
      carPhone,
      focusPhoneInput,
      focusName,
      focusPhone,
      carInfoSure,
      formRulesInner,
      innerClose,
      tags,
      handleInputConfirmBlur,
      handleInputConfirmYBlur,
      handleInputConfirmGBlur,
      inputFileClick,
      exportExcel,
      exportImport,
      disabledBtn,
      currCompanyId,
    };
  },
  methods: {
    exportGSUser() {
      ElMessage.success("正在下载中·····");
      exportGSUserData(this.query.bean).then((res) => {
        const url = window.URL.createObjectURL(new Blob([res]));
        const link = document.createElement("a");
        link.href = url;
        link.setAttribute("download", "公司管理.xls");
        document.body.appendChild(link);
        link.click();
      });
    },
    exportCompanyCar() {
      var that = this;
      console.log(that.currCompanyId);
      exportCompanyCars({ company_id: that.currCompanyId }).then((res) => {
        const url = window.URL.createObjectURL(new Blob([res]));
        const link = document.createElement("a");
        link.href = url;
        link.setAttribute("download", "企业车牌.xlsx");
        document.body.appendChild(link);
        link.click();
      });
    },
  },
  created() {
    let h = document.documentElement.clientHeight || document.body.clientHeight;
    this.tableH = h - 308 + "px";
  },
};
</script>
<style scoped>
.el-tag--light {
  margin-right: 10px;
}
.button-new-tag {
  height: 32px;
  line-height: 30px;
  padding-top: 0;
  padding-bottom: 0;
}
.input-new-tag {
  width: 90px;
  vertical-align: middle;
}
.innerDg {
  padding-bottom: 40px;
}
.small-title-box {
  display: flex;
  justify-content: space-between;
}
</style>
