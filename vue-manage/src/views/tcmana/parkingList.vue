<template>
  <div>
    <div class="container">
      <div class="handle-box" v-show="result.sysUser.user_type == 0">
        <div class="left-panel">
          <el-button
            type="primary"
            size="small"
            icon="el-icon-plus"
            @click="handleEdit(0, null, true)"
            v-permission="'park_parkinglist_add'"
            >添加
          </el-button>
          <el-button
            type="danger"
            size="small"
            icon="el-icon-delete"
            @click="handleDeleteAll()"
            v-permission="'park_parkinglist_deleteAll'"
            >批量删除
          </el-button>
        </div>
        <div class="right-panel">
          <el-form inline size="small">
            <el-input
              @keyup.enter="handleSearch()"
              size="small"
              v-model="query.park_name"
              placeholder="请输入停车场名称"
              class="handle-input mr10"
            ></el-input>
            <span class="dispinline ml5"></span>

            <el-button
              size="small"
              type="primary"
              icon="el-icon-search"
              @click="handleSearch"
              >查询
            </el-button>
            <el-button size="small" icon="el-icon-upload2" type="success"  @click="exportParking"
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
        header-cell-class-name="table-header"
        :max-height="tableH"
        @selection-change="handleSelectionChange"
      >
        <el-table-column
          type="selection"
          width="55"
          align="center"
        ></el-table-column>
        <el-table-column type="index" label="序号" width="55" align="center">
        </el-table-column>
        <el-table-column
          prop="park_code"
          label="停车场编号"
          width="100"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="traffic_park_code"
          label="市交编号"
          width="100"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="park_name"
          label="名称"
          align="center"
        ></el-table-column>
        <!-- <el-table-column
          prop="city"
          label="城市"
          width="100"
          align="center"
        ></el-table-column> -->
        <el-table-column
          prop="area_name"
          label="区域"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="street_name"
          label="街道"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="brand"
          label="厂商"
          align="center"
          width="50"
        ></el-table-column>

        <el-table-column label="状态" align="center" width="100">
          <template #default="scope">
            <el-tag
              size="small"
              :type="
                scope.row.status === '0'
                  ? 'success'
                  : scope.row.status === '1'
                  ? 'danger'
                  : ''
              "
              v-if="scope.row.status == 0"
              >启用
            </el-tag>
            <el-tag
              size="small"
              :type="
                scope.row.status === '0'
                  ? 'success'
                  : scope.row.status === '1'
                  ? 'danger'
                  : ''
              "
              v-else
              >禁用
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="park_num"
          label="总泊位"
          width="100"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="free_count"
          label="空闲泊位"
          width="100"
          align="center"
        ></el-table-column>
        <!--<el-table-column-->
          <!--prop="longitude,latitude"-->
          <!--label="坐标"-->
          <!--width="180"-->
          <!--align="center"-->
        <!--&gt;-->
          <!--<template #default="scope">-->
            <!--{{ scope.row.latitude }},{{ scope.row.longitude }}-->
          <!--</template>-->
        <!--</el-table-column>-->
        <el-table-column
          prop="create_time"
          label="时间"
          width="180"
          align="center"
        ></el-table-column>
        <el-table-column label="限流开关" align="center" width="100">
          <template #default="scope">
            <el-tag size="small" :type="
                scope.row.on_off === '0'
                  ? 'success'
                  : scope.row.on_off === '1'
                  ? 'danger'
                  : ''
              "
                    v-if="scope.row.on_off == 0">开启
            </el-tag>
            <el-tag size="small" :type="
                scope.row.on_off === '0'
                  ? 'success'
                  : scope.row.on_off === '1'
                  ? 'danger'
                  : ''
              "
                    v-else>关闭
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          label="操作"
          width="320"
          align="center"
          v-if="result.sysUser.user_type == '0'"
        >
          <template #default="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-view"
              @click="handleView(scope.$index, scope.row)"
              v-permission="'park_parkinglist_details'"
              >查看设备
            </el-button>

            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleEdit(scope.$index, scope.row, false)"
              v-permission="'park_parkinglist_edit'"
              >编辑
            </el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-circle-close"
              @click="handleStop(scope.$index, scope.row, '1')"
              v-if="scope.row.status == '0'"
              class="red"
              v-permission="'park_parkinglist_stop'"
              >停用
            </el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-circle-check"
              @click="handleStop(scope.$index, scope.row, '0')"
              v-if="scope.row.status == '1'"
              v-permission="'park_parkinglist_stop'"
              >启用
            </el-button>

            <el-dropdown class="ml10 red" @command="handleCommand">
              <span class="el-dropdown-link">
                更多操作<i class="el-icon-arrow-down el-icon--right"></i>
              </span>
              <template v-slot:dropdown>
                <el-dropdown-menu>
                  <!-- <el-dropdown-item icon="el-icon-view" command="a"  @click="handleView(scope.$index, scope.row)"
                  >查看设备</el-dropdown-item
                  > -->

                  <el-dropdown-item
                    icon="el-icon-delete"
                    command="a"
                    @click="handleDelete(scope.$index, scope.row)"
                    v-permission="'park_parkinglist_delete'"
                    >删除
                  </el-dropdown-item>
                  <el-dropdown-item v-if="scope.row.on_off == 0" icon="el-icon-turn-off" command="a"
                        @click="handleOnOff(scope.$index, scope.row)"
                          v-permission="'park_parkinglist_onoff'"
                  >限流关闭</el-dropdown-item>
                  <el-dropdown-item v-if="scope.row.on_off == 1" icon="el-icon-open" command="a"
                          @click="handleOnOff(scope.$index, scope.row)"
                          v-permission="'park_parkinglist_onoff'"
                  >限流开启</el-dropdown-item>
                  <el-dropdown-item
                    icon="el-icon-money"
                    command="b"
                    @click="handleRemark(scope.$index, scope.row)"
                    v-permission="'park_parkinglist_sfsm'"
                    >收费说明
                  </el-dropdown-item>

                  <el-dropdown-item icon="el-icon-refresh-right" command="e"   v-permission="'park_parkinglist_sbcq'"
                    >批量重启设备
                  </el-dropdown-item>
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
    <el-dialog
      :title="result.dialogT"
      v-model="editVisible"
      width="650px"
      top="2vh"
      destroy-on-close="true"
      :close-on-click-modal="false"
      @closed="cancleEdit"
    >
      <el-tabs type="card" v-model="activeName">
        <el-tab-pane label="停车场信息" name="first">
          <div class="mt10"></div>
          <el-form
            label-width="100px"
            size="small"
            :rules="formRules"
            :model="form"
          >
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="停车场编号" prop="park_code">
                  <el-input
                    v-model="form.park_code"
                    placeholder="输入停车场编号"
                  ></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="停车场名称" prop="park_name">
                  <el-input
                    v-model="form.park_name"
                    placeholder="输入停车场名称"
                  ></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="泊位总数" prop="park_num">
                  <!-- <el-radio-group v-model="form.radio2">
                    <el-radio :label="1">男</el-radio>
                    <el-radio :label="2">女</el-radio>
                  </el-radio-group> -->
                  <el-input
                    v-model="form.park_num"
                    type="number"
                    placeholder="泊位总数"
                  ></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="空闲泊位数量" prop="free_count">
                  <!-- <el-radio-group v-model="form.radio2">
                    <el-radio :label="1">男</el-radio>
                    <el-radio :label="2">女</el-radio>
                  </el-radio-group> -->
                  <el-input
                    v-model="form.free_count"
                    type="number"
                    placeholder="空闲泊位数量"
                  ></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="停车场等级" prop="park_grade">
                  <el-select
                    v-model="form.park_grade"
                    placeholder="停车场等级"
                    class="w"
                  >
                    <el-option
                      v-for="(item, i) in result.park_grades"
                      :key="i"
                      :label="item.label"
                      :value="item.dc_value"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="选择区域" prop="area_id">
                  <el-select
                    v-model="form.area_id"
                    @change="getStreetList('b')"
                    placeholder="所有区域"
                    class="w"
                  >
                    <el-option
                      v-for="(item, i) in result.area_list"
                      :key="i"
                      :label="item.area_name"
                      :value="item.id"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="指定街道" prop="street_id">
                  <el-select
                    v-model="form.street_id"
                    filterable
                    size="small"
                    placeholder="所有街道"
                    class="w"
                  >
                    <el-option
                      v-for="(item, i) in result.street_list"
                      :key="i"
                      :label="item.street_name"
                      :value="item.id"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item label="厂商" prop="brand">
                  <el-input
                    v-model="form.brand"
                    placeholder="厂商"
                  ></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item label="包月上限" prop="limit_monthly">
                  <!-- <el-radio-group v-model="form.radio2">
                    <el-radio :label="1">男</el-radio>
                    <el-radio :label="2">女</el-radio>
                  </el-radio-group> -->
                  <el-input
                    v-model="form.limit_monthly"
                    type="number"
                    placeholder="包月上限"
                  ></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item label="停车场地址" prop="address">
                  <el-input
                    v-model="form.address"
                    placeholder="停车场地址"
                  ></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item label="坐标范围">
                  <el-input
                    v-model="form.coordinate"
                    placeholder=""
                    disabled
                  ></el-input>
                  <span class="color999 lh20"
                    >提示：http://lbs.qq.com/tool/getpoint/index.html，以腾讯位置服务坐标拾取为准。</span
                  >
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item label="停车场查询">
                  <el-input v-model="form.searchkey"
                    ><template v-slot:append
                      ><el-button @click="searchMaps()"
                        >查询</el-button
                      ></template
                    ></el-input
                  >
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <div class="map">
                  <div id="has-map" class="has-map" />
                </div>
              </el-col>
            </el-row>
          </el-form>
        </el-tab-pane>
        <el-tab-pane label="收费方案" name="second">
          <div class="mt10"></div>
          <el-form
            label-width="150px"
            size="small"
            :rules="charge"
            :model="form"
          >
            <el-form-item label="选择收费方案(蓝牌)" prop="blue_charge_id">
              <el-select
                v-model="form.blue_charge_id"
                filterable
                size="small"
                placeholder="所有收费方案"
                class="w"
              >
                <el-option
                  v-for="(item, i) in result.chargeProgramme_list"
                  :key="i"
                  :label="item.programme_name"
                  :value="item.id"
                ></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="选择收费方案(绿牌)" prop="green_charge_id">
              <el-select
                v-model="form.green_charge_id"
                filterable
                size="small"
                placeholder="所有收费方案"
                class="w"
              >
                <el-option
                  v-for="(item, i) in result.chargeProgramme_list"
                  :key="i"
                  :label="item.programme_name"
                  :value="item.id"
                ></el-option>
              </el-select>
            </el-form-item>

            <el-form-item label="选择收费方案(黄牌)" prop="yellow_charge_id">
              <el-select
                v-model="form.yellow_charge_id"
                filterable
                size="small"
                placeholder="所有收费方案"
                class="w"
              >
                <el-option
                  v-for="(item, i) in result.chargeProgramme_list"
                  :key="i"
                  :label="item.programme_name"
                  :value="item.id"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="cancleEdit">取 消</el-button>
          <el-button type="primary" @click="saveEdit">确 定</el-button>
        </span>
      </template>
    </el-dialog>
    <!-- 停车场收费说明 -->
    <el-dialog title="停车场收费说明" v-model="ppVisible" width="560px">
      <el-form label-width="90px">
        <el-form-item label="停车场名称">
          <el-tag>{{ result.park.park_name }}</el-tag>
        </el-form-item>
        <el-form-item label="收费说明">
          <el-input
            v-model="result.park.charge_remark"
            type="textarea"
            rows="10"
          ></el-input>
        </el-form-item>
      </el-form>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="ppVisible = false">取 消</el-button>
          <el-button type="primary" @click="saveRemark">确 定</el-button>
        </span>
      </template>
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
            >川垣三路川垣三路南段西侧B（08:00:00 - 20:00:00）
          </el-tag>
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
import { ref, reactive, nextTick } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import {
  parkList,
  queryAreaData,
  queryStreetData,
  delParkAll,
  chargeProgrammeData,
  dictData,
  addPark,
  editPark,
  loginUser,
  exportParkingData,
} from "../../api/index";
import { useRouter } from "vue-router";

export default {
  name: "parkinglist",
  components: {},
  data() {
    return {
      tableH: 0,
      formRules: {
        park_code: [{ required: true, message: "必填项", trigger: "blur" }],
        park_name: [{ required: true, message: "必填项", trigger: "blur" }],
        park_num: [{ required: true, message: "必填项", trigger: "blur" }],
        park_grade: [{ required: true, message: "必填项", trigger: "blur" }],
        area_id: [{ required: true, message: "必填项", trigger: "blur" }],
        street_id: [{ required: true, message: "必填项", trigger: "blur" }],
        address: [{ required: true, message: "必填项", trigger: "blur" }],
        brand: [{ required: true, message: "必填项", trigger: "blur" }],
        // coordinate: [{ required: true, message: "必填项", trigger: "blur" }],
      },
      charge: {
        blue_charge_id: [
          { required: true, message: "必填项", trigger: "blur" },
        ],
        green_charge_id: [
          { required: true, message: "必填项", trigger: "blur" },
        ],
        yellow_charge_id: [
          { required: true, message: "必填项", trigger: "blur" },
        ],
      },
    };
  },
  setup() {
    const query = reactive({
      park_name: "",
      area_id: "",
      street_id: "",
      pageIndex: 1,
      pageSize: 15,
    });
    let form = reactive({
      park_code: "",
      traffic_park_code: "",
      park_name: "",
      park_num: "",
      park_grade: "",
      area_id: "",
      street_id: "",
      address: "",
      brand: "",
      coordinate: "",
      longitude: "",
      latitude: "",
      blue_charge_id: "",
      green_charge_id: "",
      yellow_charge_id: "",
      id: "",
      searchkey: "",
      limit_monthly: "",
      free_count: "",
    });
    // 结果集
    const result = reactive({
      area_list: [],
      park_grades: [],
      street_list: [],
      query_street_list: [],
      chargeProgramme_list: [],
      sysUser: {},
      park: {},
      dialogT: "",
    });

    const getLoginUser = () => {
      loginUser().then((res) => {
        // console.log(11);
        // console.log(res.data);
        result.sysUser = res.data;
      });
    };
    getLoginUser();

    // 区域
    const getAreaData = () => {
      queryAreaData(query).then((res) => {
        // console.log(res);
        result.area_list = res.data;
        // console.log(res.data);
      });
    };
    getAreaData();
    // 获取街道
    let queryStreet = reactive({
      areaId: query.area_id,
    });
    const getStreetList = (type) => {
      if (type == "a") {
        queryStreet.areaId = query.area_id;
        query.street_id = "";
      } else if (type == "b") {
        queryStreet.areaId = form.area_id;
        form.street_id = "";
      } else {
        queryStreet.areaId = form.area_id;
      }
      queryStreetData(queryStreet).then((res) => {
        // console.log(res);
        if (type == "a") {
          result.query_street_list = res.data;
        } else {
          result.street_list = res.data;
        }
        // console.log(res.data);
      });
    };

    // 获取收费方案
    const getChargeProgrammeData = () => {
      chargeProgrammeData(query).then((res) => {
        // console.log(res);
        result.chargeProgramme_list = res.data;
        // console.log(res.data);
      });
    };
    // 查询区域等级
    let dict = reactive({
      is_del: "0",
      dict_type: "parkGrade",
    });
    const getParkGrade = () => {
      dictData(dict).then((res) => {
        // console.log(res);
        result.park_grades = res.data;
      });
    };

    const tableData = ref([]);
    const pageTotal = ref(0);
    // 获取表格数据
    const getData = () => {
      parkList(query).then((res) => {
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

    // 停用操作
    const handleStop = (index, row, sta) => {
      // 二次确认停用
      ElMessageBox.confirm("确定当前操作吗？", "提示", {
        type: "warning",
      })
        .then(() => {
          let edit_park = reactive({
            status: sta,
            id: row.id,
          });
          editPark(edit_park)
            .then((res) => {
              // console.log(res);
              ElMessage.success("停用成功");
            })
            .then(() => {
              getData();
            });
        })
        .catch(() => {});
    };

    // 删除操作
    const handleDelete = (index, row) => {
      // 二次确认删除
      ElMessageBox.confirm("确定要删除吗？", "提示", {
        type: "warning",
      })
        .then(() => {
          let edit_park = reactive({
            is_del: "1",
            id: row.id,
          });
          editPark(edit_park)
            .then((res) => {
              // console.log(res);
              ElMessage.success("删除成功");
            })
            .then(() => {
              getData();
            });
        })
        .catch(() => {});
    };
    // 限流开启/关闭
    const handleOnOff = (index, row) => {
        var msg = "开启";
        var on_off = "0";
        if (row.on_off == "0") {
            msg = "关闭";
            on_off = "1";
        } else {
            msg = "开启";
            on_off = "0";
        }
      ElMessageBox.confirm("确定要"+msg+"吗？", "提示", {
        type: "warning",
      })
        .then(() => {
          let edit_park = reactive({
            on_off: on_off,
            id: row.id,
          });
          editPark(edit_park)
            .then((res) => {
              // console.log(res);
              ElMessage.success(msg+"成功");
            })
            .then(() => {
              getData();
            });
        })
        .catch(() => {});
    };
    const dialogT = "编辑";

    // 表格编辑时弹窗和保存
    const editVisible = ref(false);
    const viewVisible = ref(false);
    const ppVisible = ref(false);
    const router = useRouter();
    let idx = "";
    var timer = "";

    const handleEdit = (index, row, type) => {
      result.street_list = [];
      getAreaData();
      //initMap();
      getChargeProgrammeData();
      getParkGrade();
      if (type) {
        result.dialogT = "新增";
        idx = "";
        form.park_code = "";
        form.park_name = "";
        form.park_num = "";
        form.park_grade = "";
        form.area_id = "";
        form.street_id = "";
        form.address = "";
        form.brand = "";
        form.longitude = "";
        form.coordinate = "";
        form.latitude = "";
        form.id = "";
        form.blue_charge_id = "";
        form.green_charge_id = "";
        form.yellow_charge_id = "";
        form.free_count = "";
        form.traffic_park_code = "";
      } else {
        result.dialogT = "编辑";
        idx = row.id;
        Object.keys(form).forEach((item) => {
          form[item] = row[item];
        });
        form.coordinate = form.latitude + "," + form.longitude;
        getStreetList("c");
        // console.log(111);
        // console.log(form);
      }
      timer = setTimeout(function () {
        initMaps();
      }, 500);
      editVisible.value = true;
    };

    var map;
    const cancleEdit = () => {
      map.destroy();
      editVisible.value = false;
    };
    const initMaps = () => {
      if (document.getElementById("has-map")) {
        var point = [34.24277, 117.184547];
        if (form.coordinate != "" && form.coordinate != null) {
          point[0] = form.coordinate.split(",")[0];
          point[1] = form.coordinate.split(",")[1];
        }
        //console.log(point);
        const center = new window.TMap.LatLng(point[0], point[1]);
        // 初始化地图
        map = new window.TMap.Map(document.getElementById("has-map"), {
          rotation: 0, // 设置地图旋转角度
          pitch: 0, // 设置俯仰角度（0~45）
          zoom: 16, // 设置地图缩放级别
          center: center, // 设置地图中心点坐标
        });
        var geocoder = new window.TMap.service.Geocoder(); // 新建一个正逆地址解析类
        geocoder
          .getAddress({ location: center }) // 将给定的坐标位置转换为地址
          .then((result) => {
            form.searchkey = result.result.address;
            // 显示查询到的地址
          });
        var markers = new window.TMap.MultiMarker({
          map: map,
          geometries: [
            {
              id: "main",
              position: center, // 将得到的坐标位置用点标记标注在地图上
            },
          ],
        });
        // 创建信息窗口
        const info = new window.TMap.InfoWindow({
          map,
          position: map.getCenter(),
          offset: { x: 0, y: -52 },
        });
        info.close();

        //点击地图事件
        map.on("click", (evt) => {
          //console.log(evt)
          info.open();
          info.setPosition(evt.latLng);
          var lat = evt.latLng.getLat().toFixed(6);
          var lng = evt.latLng.getLng().toFixed(6);
          info.setContent("当前坐标：" + lat + "," + lng);
          form.coordinate = lat + "," + lng;
          markers.setGeometries([]); // 将给定的地址转换为坐标位置
          markers.updateGeometries([
            {
              id: "main",
              position: evt.latLng, // 将得到的坐标位置用点标记标注在地图上
            },
          ]);
          geocoder
            .getAddress({ location: evt.latLng }) // 将给定的坐标位置转换为地址
            .then((result) => {
              form.searchkey = result.result.address;
              // 显示查询到的地址
            });
          //map.setCenter(evt.latLng);
        });

        // map.destroy();   //销毁地图
        clearInterval(timer);
        // }, 500);
      }
    };
    const searchMaps = () => {
      if (form.searchkey == "" || form.searchkey == null) {
        ElMessage.warning("查询地址不能为空");
      } else {
        if (form.searchkey.indexOf("徐州") == -1) {
          form.searchkey = "徐州" + form.searchkey;
        }

        document.getElementById("has-map").innerHTML = "";
        var point = [34.24277, 117.184547];
        if (form.coordinate != "" && form.coordinate != null) {
          point[0] = form.coordinate.split(",")[0];
          point[1] = form.coordinate.split(",")[1];
        }
        //console.log(point);
        const center = new window.TMap.LatLng(point[0], point[1]);
        // 初始化地图
        map = new window.TMap.Map(document.getElementById("has-map"), {
          rotation: 0, // 设置地图旋转角度
          pitch: 0, // 设置俯仰角度（0~45）
          zoom: 16, // 设置地图缩放级别
          center: center, // 设置地图中心点坐标
        });

        var geocoder = new window.TMap.service.Geocoder(); // 新建一个正逆地址解析类
        var markers = new window.TMap.MultiMarker({
          map: map,
          geometries: [
            {
              id: "main",
              position: center, // 将得到的坐标位置用点标记标注在地图上
            },
          ],
        });
        // 创建信息窗口
        const info = new window.TMap.InfoWindow({
          map,
          position: map.getCenter(),
          offset: { x: 0, y: -52 },
        });
        info.close();
        markers.setGeometries([]); // 将给定的地址转换为坐标位置
        geocoder.getLocation({ address: form.searchkey }).then((result) => {
          markers.updateGeometries([
            {
              id: "main",
              position: result.result.location, // 将得到的坐标位置用点标记标注在地图上
            },
          ]);
          info.open();
          info.setPosition(result.result.location);
          var lat = result.result.location.getLat().toFixed(6);
          var lng = result.result.location.getLng().toFixed(6);
          info.setContent("当前坐标：" + lat + "," + lng);
          map.setCenter(result.result.location);
          form.coordinate = result.result.location.toString();
          // 显示坐标数值
          //console.log(form.coordinate);
        });

        //点击地图事件
        map.on("click", (evt) => {
          //console.log(evt)
          info.open();
          info.setPosition(evt.latLng);
          var lat = evt.latLng.getLat().toFixed(6);
          var lng = evt.latLng.getLng().toFixed(6);
          info.setContent("当前坐标：" + lat + "," + lng);
          form.coordinate = lat + "," + lng;
          markers.setGeometries([]); // 将给定的地址转换为坐标位置
          markers.updateGeometries([
            {
              id: "main",
              position: evt.latLng, // 将得到的坐标位置用点标记标注在地图上
            },
          ]);
          geocoder
            .getAddress({ location: evt.latLng }) // 将给定的坐标位置转换为地址
            .then((result) => {
              form.searchkey = result.result.address;
              // 显示查询到的地址
            });
          //map.setCenter(evt.latLng);
        });
      }

      //map.destroy();
    };

    // 收费说明
    const handleRemark = (index, row) => {
      result.park = row;

      ppVisible.value = true;
    };

    const handleView = (index, row) => {
      router.push({
        path: "equipmentListA",
        query: {
          park_id: row.id,
          area_id: row.area_id,
          street_id: row.street_id,
          routerAlive: false,
        },
      });
    };

    const saveEdit = () => {
      // 不可为空
      if (
        form.park_code == null ||
        form.park_code == "" ||
        form.park_name == null ||
        form.park_name == "" ||
        form.park_num == null ||
        form.park_num == "" ||
        form.park_grade == null ||
        form.park_grade == "" ||
        form.area_id == null ||
        form.area_id == "" ||
        form.street_id == null ||
        form.street_id == "" ||
        form.address == null ||
        form.address == "" ||
        form.brand == null ||
        form.brand == "" ||
        form.coordinate == null ||
        form.coordinate == "" ||
        form.blue_charge_id == null ||
        form.blue_charge_id == "" ||
        form.green_charge_id == null ||
        form.green_charge_id == "" ||
        form.yellow_charge_id == null ||
        form.yellow_charge_id == ""
      ) {
        ElMessage.error("参数不可为空！");
        return false;
      }

      form.longitude = form.coordinate.split(",")[1];
      form.latitude = form.coordinate.split(",")[0];

      if (!idx) {
        addPark(form)
          .then((res) => {
            // console.log(res.data);
            ElMessage.success(res.msg);
          })
          .then(() => {
            query.pageIndex = 1;
            getData();
          });
      } else {
        console.log("修改");
        form.id = idx;
        editPark(form)
          .then((res) => {
            // console.log(res);
            ElMessage.success(res.msg);
          })
          .then(() => {
            getData();
          });
      }

      editVisible.value = false;
    };

    const saveRemark = () => {
      ppVisible.value = false;

      let edit_park = reactive({
        id: result.park.id,
        charge_remark: result.park.charge_remark,
      });

      editPark(edit_park).then((res) => {
        // console.log(res);
        ElMessage.success("修改成功");
      });
    };

    return {
      query,
      timer,
      result,
      tableData,
      pageTotal,
      editVisible,
      viewVisible,
      form,
      map,
      cancleEdit,
      searchMaps,
      handleRemark,
      saveRemark,
      handleStop,
      handleSearch,
      getStreetList,
      queryStreet,
      handlePageChange,
      handleDelete,
        handleOnOff,
      handleEdit,
      handleView,
      activeName: "first",
      getData,
      saveEdit,
      multipleSelection: [],
      dialogImageUrl: "",
      ppVisible,
    };
  },
  created() {
    let h = document.documentElement.clientHeight || document.body.clientHeight;
    this.tableH = h - 308 + "px";
  },
  methods: {
    //上传图片操作

    handleRemove(file, fileList) {
      // console.log(file, fileList);
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
      //this.$message("click on item " + command);
    },
    handleDeleteAll() {
      ElMessageBox.confirm("确定要删除吗？", "提示", {
        type: "warning",
      })
        .then(() => {
          var that = this;
          var val = this.selectedData;
          // console.log(val);
          var ids = "";
          if (val) {
            val.forEach(function (item, index) {
              //alert(item.id);
              ids = ids + item.id + ",";
            });
            delParkAll({ parkIds: ids }).then((res) => {
              ElMessage.success("删除成功");
              that.getData();
            });
          } else {
            ElMessage.warning(`请选择一条记录`);
          }
        })
        .catch(() => {});
    },
    exportParking() {
      ElMessage.success("正在下载中·····");
      exportParkingData(this.query).then((res) => {
        const url = window.URL.createObjectURL(new Blob([res]));
        const link = document.createElement("a");
        link.href = url;
        link.setAttribute("download", "停车场管理.xls");
        document.body.appendChild(link);
        link.click();
      });
    },
  },
};
</script>
