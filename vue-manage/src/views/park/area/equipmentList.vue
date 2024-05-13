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
            v-permission="'park_equipmentlistA_add'"
            >添加
          </el-button>
          <el-button
            type="danger"
            size="small"
            icon="el-icon-delete"
            @click="handleDeleteAll()"
            v-permission="'park_equipmentlistA_deleteAll'"
            >批量删除
          </el-button>

          <el-button
            type="primary"
            size="small"
            icon="el-icon-view"
            plain
            @click="handleVideoAll()"
            >查看视频
          </el-button>
          <!-- <el-button
            type="success"
            size="small"
            icon="el-icon-s-operation"
            @click="bindRoad()"
            >绑定路内</el-button
          > -->
        </div>
        <div class="right-panel">
          <el-form inline size="small">
            <el-input
              @keyup.enter="handleSearch()"
              size="small"
              clearable
              v-model="query.device_code"
              placeholder="请输入设备编号"
              class="handle-input mr10"
            ></el-input>
            <el-select
              v-model="query.status"
              filterable
              size="small"
              clearable
              placeholder="设备在线状态"
              class="w100"
            >
              <el-option value="1" label="空闲"></el-option>
              <el-option value="2" label="使用中"></el-option>
              <el-option value="3" label="异常"></el-option>
            </el-select>
            <span class="dispinline ml5"></span>
            <el-select
              v-model="query.area_id"
              filterable
              size="small"
              clearable
              placeholder="所有区域"
              class="w100"
              @change="getStreetList('a')"
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
              clearable
              placeholder="所有街道"
              class="w100"
              @change="getParkList('a')"
            >
              <el-option value="">全部</el-option>
              <el-option
                v-for="(item, i) in result.query_street_list"
                :key="i"
                :label="item.street_name"
                :value="item.id"
              ></el-option>
            </el-select>
            <span class="dispinline ml5"></span>
            <el-select
              v-model="query.park_id"
              filterable
              size="small"
              clearable
              placeholder="所有停车场"
              class="w100"
            >
              <el-option value="">全部</el-option>
              <el-option
                v-for="(item, i) in result.query_park_list"
                :key="i"
                :label="item.park_name"
                :value="item.id"
              ></el-option>
            </el-select>
            <span class="dispinline ml5"></span>

            <el-button
              size="small"
              type="primary"
              icon="el-icon-search"
              @click="handleSearch"
              >查询
            </el-button>
            <el-button size="small" icon="el-icon-upload2" type="success" @click="exportEquipment"
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
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center"></el-table-column>
        <el-table-column type="index" label="序号" width="55" align="center">
        </el-table-column>
        <el-table-column prop="device_type" label="设备类型" width="100" align="center">
          <template #default="scope">
            <p v-if="scope.row.device_type == 1">闸机</p>
          </template>
        </el-table-column>
        <el-table-column prop="passageway" label="通道口" width="100" align="center">
          <template #default="scope">
            <p v-if="scope.row.passageway == 0">出口闸机</p>
            <p v-if="scope.row.passageway == 1">入口闸机</p>
          </template>
        </el-table-column>
        <el-table-column
          prop="serialNo"
          label="设备序列号"
          width="160"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="channel_id"
          label="通道号"
          width="100"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="channel_name"
          label="通道名称"
          width="160"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="device_code"
          label="设备编号"
          width="160"
          align="center"
        ></el-table-column>
        <!--<el-table-column-->
          <!--prop="berth_code"-->
          <!--label="泊位编号"-->
          <!--width="160"-->
          <!--align="center"-->
        <!--&gt;</el-table-column>-->
        <el-table-column
          prop="street_name"
          label="街道"
          min-width="200"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="park_name"
          label="停车场"
          min-width="200"
          align="center"
        ></el-table-column>
        <!--<el-table-column-->
          <!--prop="longitude,latitude"-->
          <!--label="坐标"-->
          <!--width="180"-->
          <!--align="center"-->
        <!--&gt;-->
          <!--<template #default="scope">-->
            <!--{{ scope.row.latitude }}, {{ scope.row.longitude }}-->
          <!--</template>-->
        <!--</el-table-column>-->
        <el-table-column prop="status" label="设备在线状态" width="130" align="center">
          <template #default="scope">
            <p v-if="scope.row.status == '1'" style="color: #67c23a">空闲</p>
            <p v-if="scope.row.status == '2'" style="color: #656d82">使用中</p>
            <p v-if="scope.row.status == '3'" style="color: #f56c6c">异常</p>
          </template>
        </el-table-column>
        <el-table-column label="状态" align="center" width="100">
          <template #default="scope">
            <el-tag
              size="small"
              :type="
                scope.row.is_use === '1'
                  ? 'success'
                  : scope.row.is_use === '0'
                  ? 'danger'
                  : ''
              "
              v-if="scope.row.is_use == 1"
              >启用
            </el-tag>
            <el-tag
              size="small"
              :type="
                scope.row.is_use === '1'
                  ? 'success'
                  : scope.row.is_use === '0'
                  ? 'danger'
                  : ''
              "
              v-else
              >禁用
            </el-tag>
          </template>
        </el-table-column>
        <!--<el-table-column-->
          <!--prop="agrver"-->
          <!--label="软件版本号"-->
          <!--width="100"-->
          <!--align="center"-->
        <!--&gt;</el-table-column>-->
        <el-table-column
          prop="create_time"
          label="添加时间"
          width="170"
          align="center"
        ></el-table-column>

        <el-table-column
          label="操作"
          width="260"
          fixed="right"
          align="center"
          v-if="result.sysUser.user_type == '0'"
        >
          <template #default="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-video-camera"
              @click="handleVideo(scope.$index, scope.row)"
              >查看视频
            </el-button>

            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleEdit(scope.$index, scope.row, false)"
              v-permission="'park_equipmentlistA_edit'"
              >编辑
            </el-button>
            <!-- <el-button
              size="mini"
              type="text"
              icon="el-icon-bell"
              v-permission="'park_equipmentlistA_open'"
              @click="onOpenGate(scope.$index, scope.row)"
              >人工开闸
            </el-button> -->
            <!-- <el-button
              size="mini"
              type="text"
              icon="el-icon-view"
              @click="handleView(scope.$index, scope.row)"
              v-if="scope.row.passageway == '0'"
              v-permission="'park_equipmentlistA_qrcode'"
              >支付二维码
            </el-button> -->

            <!-- <el-button
              size="mini"
              type="text"
              icon="el-icon-circle-check"
              @click="handleStop(scope.$index, scope.row, '1')"
              v-if="scope.row.is_use == '0'"
              v-permission="'park_equipmentlistA_status'"
              >启用
            </el-button> -->
            <!-- <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              class="red"
              @click="handleDelete(scope.$index, scope.row)"
              v-permission="'park_equipmentlistA_delete'"
              >删除
            </el-button> -->
            <!--
            <el-button
              size="mini"
              type="text"
              icon="el-icon-star-off"
              v-permission="'park_equipmentlistA_topic'"
              @click="onAddTopic(scope.$index, scope.row)"
              >订阅主题
            </el-button>

            <el-button
              size="mini"
              type="text"
              icon="el-icon-circle-close"
              @click="handleStop(scope.$index, scope.row, '0')"
              class="red"
              v-if="scope.row.is_use == '1'"
              v-permission="'park_equipmentlistA_status'"
              >停用
            </el-button> -->
            <el-dropdown class="ml10 red" @command="handleCommand">
              <span class="el-dropdown-link">
                更多操作<i class="el-icon-arrow-down el-icon--right"></i>
              </span>
              <template v-slot:dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item
                    command="a"
                    icon="el-icon-bell"
                    v-permission="'park_equipmentlistA_open'"
                    @click="onOpenGate(scope.$index, scope.row)"
                    >人工开闸
                  </el-dropdown-item>
                  <el-dropdown-item
                  icon="el-icon-view"
                    command="a"
                    @click="handleView(scope.$index, scope.row)"
                    v-if="scope.row.passageway == '0'"
                    v-permission="'park_equipmentlistA_qrcode'"
                    >支付二维码
                  </el-dropdown-item>
                  <el-dropdown-item
                  icon="el-icon-circle-check"
                  command="a"
                  @click="handleStop(scope.$index, scope.row, '1')"
                  v-if="scope.row.is_use == '0'"
                  v-permission="'park_equipmentlistA_status'"
                    >启用
                  </el-dropdown-item>

                  <el-dropdown-item
                  icon="el-icon-circle-close"
                  command="c"
                  v-if="scope.row.is_use == '1'"
                  v-permission="'park_equipmentlistA_status'"
                  @click="handleStop(scope.$index, scope.row, '0')"
                    >停用
                  </el-dropdown-item>
                  <el-dropdown-item
                    icon="el-icon-star-off"
                    v-permission="'park_equipmentlistA_topic'"
                    command="b"
                    @click="onAddTopic(scope.$index, scope.row)"
                    >订阅主题
                  </el-dropdown-item>
                  <el-dropdown-item
                  icon="el-icon-delete"
                  command="a"
                  @click="handleDelete(scope.$index, scope.row)"
                  v-permission="'park_equipmentlistA_delete'"
                    >删除
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
            <!-- <el-dropdown class="ml10 red" @command="handleCommand">
              <span class="el-dropdown-link">
                更多操作<i class="el-icon-arrow-down el-icon--right"></i>
              </span>
              <template v-slot:dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item icon="el-icon-delete" command="a"
                   @click="handleDelete(scope.$index, scope.row)" >删除</el-dropdown-item
                  >
                  <el-dropdown-item icon="el-icon-upload2" command="c"
                    >改变图片上传模式</el-dropdown-item
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
    <el-dialog
      :title="result.dialogT"
      v-model="editVisible"
      width="800px"
      top="2vh"
      destroy-on-close="true"
      :close-on-click-modal="false"
      @closed="cancleEdit"
    >
      <el-form label-width="100px" size="small" :rules="formRules" :model="form">
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="设备编号" v-if="result.addOrEdit" prop="device_code">
              <el-input v-model="form.device_code" type="textarea" :rows="5"></el-input>
              <p class="color999 lh20">操作提示：各个设备编号请用英文逗号“,”间隔</p>
            </el-form-item>
            <el-form-item label="设备编号" prop="device_code" v-else>
              <el-input v-model="form.device_code"></el-input>
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <el-form-item label="通道号" prop="channel_id">
              <el-input v-model="form.channel_id"></el-input>
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <el-form-item label="通道名称" prop="channel_name">
              <el-input v-model="form.channel_name"></el-input>
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <el-form-item label="设备序列号" prop="serialNo">
              <el-input v-model="form.serialNo"></el-input>
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <el-form-item label="设备类型" prop="device_type">
              <el-select
                v-model="form.device_type"
                filterable
                size="small"
                placeholder="所有设备类型"
                class="w"
              >
                <el-option
                  v-for="(item, i) in result.device_types"
                  :key="i"
                  :label="item.label"
                  :value="item.dc_value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="通道口" prop="passageway">
              <el-select
                v-model="form.passageway"
                filterable
                size="small"
                placeholder="所有设备类型"
                class="w"
              >
                <el-option
                  v-for="(item, i) in result.gate_types"
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
                filterable
                size="small"
                placeholder="所有区域"
                class="w"
                @change="getStreetList('b')"
              >
                <el-option
                  v-for="(item, i) in result.area_list"
                  :key="i"
                  :label="item.area_name"
                  :value="item.id"
                ></el-option>
              </el-select>
              <!-- <span class="color999">操作提示：如没有您要的区域，请先添加区域！</span> -->
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="选择街道" prop="street_id">
              <el-select
                v-model="form.street_id"
                filterable
                size="small"
                placeholder="所有街道"
                class="w"
                @change="getParkList('b')"
              >
                <el-option
                  v-for="(item, i) in result.street_list"
                  :key="i"
                  :label="item.street_name"
                  :value="item.id"
                ></el-option>
              </el-select>
              <!-- <span class="color999">操作提示：如没有您要的街道，请先添加街道！</span> -->
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="指定停车场" prop="park_id">
              <el-select
                v-model="form.park_id"
                filterable
                size="small"
                placeholder="所有停车场"
                class="w"
              >
                <el-option
                  v-for="(item, i) in result.park_list"
                  :key="i"
                  :label="item.park_name"
                  :value="item.id"
                ></el-option>
              </el-select>
              <!-- <span class="color999">操作提示：如没有您要的路内，请先添加路内！</span> -->
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="坐标范围">
              <el-input v-model="form.coordinate" placeholder="" disabled></el-input>
              <span class="color999"
                >操作提示：http://lbs.qq.com/tool/getpoint/index.html，以腾讯位置服务坐标拾取为准。</span
              >
            </el-form-item>
            <!-- <el-form-item>
              <el-input v-model="form.name"
                ><template v-slot:append
                  ><el-button>查询</el-button></template
                ></el-input
              >
            </el-form-item> -->
          </el-col>
          <el-col :span="24">
            <el-form-item label="路内查询">
              <el-input v-model="form.searchkey">
                <template v-slot:append>
                  <el-button @click="searchMaps()">查询 </el-button>
                </template>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <div class="map">
              <div id="has-map" class="has-map" />
            </div>
          </el-col>
        </el-row>
      </el-form>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="cancleEdit">取 消</el-button>
          <el-button type="primary" @click="saveEdit">确 定</el-button>
        </span>
      </template>
    </el-dialog>
    <el-dialog v-model="ppVisible">
      <img width="100%" :src="dialogImageUrl" alt="" />
    </el-dialog>
    <!-- 详情弹出框 -->
    <el-dialog title="支付二维码" v-model="viewVisible" width="400px">
      <div style="padding: 0 60px">
        <img style="width: 100%" :src="'data:image/png;base64,' + zfCode.qrCode" alt="" />
      </div>
      <template #footer>
        <span class="dialog-footer">
          <!-- <el-button @click="viewVisible = false">关 闭</el-button>
          <el-button type="primary" @click="print">下 载</el-button> -->
        </span>
      </template>
    </el-dialog>

    <!-- 视频弹出框 -->
    <el-dialog
      ref="qyvideoOne"
      title="监控视频"
      v-model="videoVisible"
      width="800px"
      destroy-on-close

    >
      <div style="padding: 0 0px">
        <videoPlayOne :rtspData="rtspData" />
      </div>
    </el-dialog>

    <!-- 视频弹出框---多个 -->
    <el-dialog
      ref="qyvideoAll"
      title="监控视频"
      v-model="videoVisibleAll"
      width="840px"
      fullscreen
      modal-class="self-modal"
      destroy-on-close
      :before-close="handleCloseAll"
    >
      <div style="padding: 0 0px">
        <videoPlay :rtsplist="rtsplist" />
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { ref, reactive } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import {
  queryAreaData,
  queryStreetData,
  queryParkData,
  dictData,
  loginUser,
} from "../../../api/index";
import {
  parkDeviceList,
  addParkDevice,
  getQrCode,
  delParkDeviceAll,
  editParkDevice,
  exportEquipmentData,
  playVideo,
  openGate,
  addTopic,
} from "../../../api/park/park";
import { useRouter } from "vue-router";

import videoPlay from "./videoPlay/index.vue";
import videoPlayOne from "./videoPlay/indexOne.vue";

export default {
  name: "equipmentlistA",
  components: { videoPlay, videoPlayOne },
  data() {
    return {
      formRules: {
        device_code: [{ required: true, message: "必填项", trigger: "blur" }],
        channel_id: [{ required: true, message: "必填项", trigger: "blur" }],
        channel_name: [{ required: true, message: "必填项", trigger: "blur" }],
        serialNo: [{ required: true, message: "必填项", trigger: "blur" }],
        device_type: [{ required: true, message: "必填项", trigger: "blur" }],
        passageway: [{ required: true, message: "必填项", trigger: "blur" }],
        area_id: [{ required: true, message: "必填项", trigger: "blur" }],
        street_id: [{ required: true, message: "必填项", trigger: "blur" }],
        park_id: [{ required: true, message: "必填项", trigger: "blur" }],
        // coordinate: [{ required: true, message: "必填项", trigger: "blur" }],
      },
    };
  },
  setup() {
    const router = useRouter();

    const query = reactive({
      device_code: "",

      area_id: "",
      street_id: "",
      park_id: "",
      pageIndex: 1,
      pageSize: 10,
    });

    let form = reactive({
      device_code: "",
      channel_id: "",
      channel_name: "",
      device_type: "",
      area_id: "",
      street_id: "",
      status: "",
      park_id: "",
      coordinate: "",
      passageway: "",
      longitude: "",
      latitude: "",
      id: "",
      searchkey: "",
      serialNo: "",
    });

    let result = reactive({
      area_list: [],
      street_list: [],
      query_street_list: [],
      park_list: [],
      query_park_list: [],
      device_types: [],
      gate_types: [],
      sysUser: {},
      addOrEdit: true,
      dialogT: "",
      qrCode: "",
    });

    const getLoginUser = () => {
      loginUser().then((res) => {
        console.log(11);
        console.log(res.data);
        result.sysUser = res.data;
      });
    };

    getLoginUser();
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
      result.park_list = [];
      result.query_park_list = [];
      if (type == "a") {
        queryStreet.areaId = query.area_id;
        query.park_id = "";
        query.street_id = "";
      } else if (type == "b") {
        queryStreet.areaId = form.area_id;
        form.park_id = "";
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

        console.log(res.data);
      });
    };

    //获取停车场下拉框数据
    const queryPark = reactive({
      streetId: query.street_id,
    });
    const getParkList = (type) => {
      if (type == "a") {
        queryPark.streetId = query.street_id;
        query.park_id = "";
      } else if (type == "b") {
        queryPark.streetId = form.street_id;
        form.park_id = "";
      } else {
        queryPark.streetId = form.street_id;
      }

      queryParkData(queryPark).then((res) => {
        console.log(res.data);
        if (type == "a") {
          result.query_park_list = res.data;
        } else {
          result.park_list = res.data;
        }
      });
    };

    // 查询设备类型
    let dict = reactive({
      is_del: "0",
      dict_type: "parkDeviceType",
    });
    const getDeviceType = () => {
      dictData(dict).then((res) => {
        console.log(res);
        result.device_types = res.data;
      });
    };

    // 查询通道类型
    let gate = reactive({
      is_del: "0",
      dict_type: "gateType",
    });
    const getGateType = () => {
      dictData(gate).then((res) => {
        console.log(res);
        result.gate_types = res.data;
      });
    };

    if (router.currentRoute.value.query.park_id != null) {
      // console.log(router.currentRoute.value.query.area_id);
      // console.log(router.currentRoute.value.query.street_id);
      // console.log(router.currentRoute.value.query.park_id);
      query.area_id = parseInt(router.currentRoute.value.query.area_id);
      getStreetList("a");
      query.street_id = parseInt(router.currentRoute.value.query.street_id);
      getParkList("a");
      query.park_id = parseInt(router.currentRoute.value.query.park_id);
    }

    const tableData = ref([]);
    const pageTotal = ref(0);
    // const router = useRouter();

    // 获取表格数据
    const getData = () => {
      console.log(query);
      parkDeviceList(query).then((res) => {
        // console.log(res.data);
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
      ElMessageBox.confirm("请确定当前操作？", "提示", {
        type: "warning",
      })
        .then(() => {
          let edit_device = reactive({
            is_use: sta,
            id: row.id,
          });
          editParkDevice(edit_device)
            .then((res) => {
              console.log(res);
              ElMessage.success("操作成功");
            })
            .then(() => {
              getData();
            });
        })
        .catch(() => {});
    };

    let zfCode = reactive({
      qrCode: "",
    });
    //支付二维码功能
    const handleView = (index, row) => {
      let device_qrcode = reactive({
        gateCode: row.channel_id,
        parkCode: row.park_id,
      });
      getQrCode(device_qrcode).then((res) => {
        zfCode.qrCode = res.data;
      });
      viewVisible.value = true;
    };

    //控制视频弹出框
    const videoVisible = ref(false);
    const videoVisibleAll = ref(false);

    //视频查看
    const rtspData = reactive({});
    const handleVideo = (index, row) => {
      // debugger
      // let obj = {};
      // obj.rtsp = "https://zhtc.aldwxa.top/live?port=1935&app=live&stream=" + row.device_code;
      // obj.player = null;
      // rtspData.value = obj
      // debugger
      rtspData.value = {
        rtsp: "https://zhtc.aldwxa.top/live?port=1935&app=live&stream=" + row.device_code,
        sn: row.serialNo,
        channelName: row.channel_name,
        player: null,
      };
      videoVisible.value = true;
    };
    //视频查看
    const handleVideoAll = (index, row) => {
      // let device_qrcode = reactive({
      //   gateCode: row.channel_id,
      //   parkCode: row.park_id,
      // });
      // getQrCode(device_qrcode).then((res) => {
      //   zfCode.qrCode = res.data;
      // });
      videoVisibleAll.value = true;
    };
   //关闭单个弹窗销毁视频
    const qyvideoOne = ref(null);
    const handleClose = () => {
      videoVisible.value = false;
      // console.log(videoPlayOneRef.value.serial);
      qyvideoOne.value.destroy();
    };

    const qyvideoAll = ref(null);
    const handleCloseAll = () => {
      videoVisibleAll.value = false;
      // console.log(videoPlayOneRef.value.serial);
      qyvideoAll.value.destroy();
    };

    // 人工开闸
    const onOpenGate = (index, row) => {
      const param = reactive({
        deviceCode: row.device_code,
      });
      openGate(param).then((res) => {
        if (res.code == 0) {
          ElMessage.success("人工开闸成功");
        } else {
          ElMessage.error("人工开闸失败");
        }
      });
    };

    // 订阅主题
    const onAddTopic = (index, row) => {
      const param = reactive({
        deviceCode: row.device_code,
      });
      addTopic(param).then((res) => {
        console.log(res);
        if (res.code == 0) {
          ElMessage.success("订阅主题成功");
        } else {
          ElMessage.error(res.msg);
        }
      });
    };

    // 删除操作
    const handleDelete = (index, row) => {
      // 二次确认删除
      ElMessageBox.confirm("确定要删除吗？", "提示", {
        type: "warning",
      })
        .then(() => {
          let edit_device = reactive({
            is_del: "1",
            id: row.id,
          });
          editParkDevice(edit_device)
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

    const play = (index, row) => {
      var p = reactive({ deviceId: row.device_code });
      playVideo(p).then((res) => {
        if (0 == res.code) {
          //window.open("http://localhost:8081/video/toVideo.do?deviceCode=" + row.device_code)
          window.open(
            "https://zhtc.aldwxa.top/api/video/toVideo.do?deviceCode=" + row.device_code
          );
        }
      });
    };

    // 表格编辑时弹窗和保存
    const editVisible = ref(false);
    const viewVisible = ref(false);
    const ppVisible = ref(false);

    let idx = "";
    var timer = "";
    const handleEdit = (index, row, type) => {
      result.street_list = [];
      result.park_list = [];
      getAreaList();
      // getStreetList();
      // getRoadList();
      getDeviceType();
      getGateType();
      if (type) {
        result.dialogT = "新增";
        result.addOrEdit = true;
        idx = "";
        form.device_code = "";

        form.device_type = "";
        form.channel_id = "";
        form.channel_name = "";
        form.serialNo = "";
        form.area_id = "";
        form.street_id = "";
        form.park_id = "";
        form.longitude = "";
        form.coordinate = "";
        form.latitude = "";
        form.id = "";
        form.passageway = "";
      } else {
        result.dialogT = "编辑";
        result.addOrEdit = false;
        idx = row.id;
        Object.keys(form).forEach((item) => {
          form[item] = row[item];
        });
        form.coordinate = form.latitude + "," + form.longitude;

        console.log("form");
        console.log(form);
        getStreetList("c");
        getParkList("c");
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

    const rEdit = (index, row) => {
      console.log(row.id);
      router.push({ path: "/equipmentedit", query: { id: row.id } });
    };

    const saveEdit = () => {
      if (
        form.device_code == null ||
        form.device_code == "" ||
        form.channel_id == null ||
        form.channel_id == "" ||
        form.channel_name == null ||
        form.channel_name == "" ||
        form.serialNo == null ||
        form.serialNo == "" ||
        form.device_type == null ||
        form.device_type == "" ||
        form.area_id == null ||
        form.area_id == "" ||
        form.street_id == null ||
        form.street_id == "" ||
        form.park_id == null ||
        form.park_id == "" ||
        form.coordinate == null ||
        form.coordinate == ""
      ) {
        ElMessage.success("参数不可为空！");
        return false;
      }

      form.longitude = form.coordinate.split(",")[1];
      form.latitude = form.coordinate.split(",")[0];
      if (!idx) {
        addParkDevice(form)
          .then((res) => {
            if (res.code === 0) {
              ElMessage.success(res.msg);
            } else {
              ElMessage.error(res.msg);
            }
          })
          .then(() => {
            query.pageIndex = 1;
            getData();
          });
      } else {
        editParkDevice(form)
          .then((res) => {
            if (res.code === 0) {
              ElMessage.success(res.msg);
              getData();
            } else {
              ElMessage.error(res.msg);
            }
          })
          // .then(() => {
          //   getData();
          // });
      }

      editVisible.value = false;
    };

    return {
      rtsplist: [],
      rtspData,
      query,
      timer,
      result,
      tableData,
      pageTotal,
      editVisible,
      viewVisible,
      form,
      zfCode,
      map,
      videoVisible,
      videoVisibleAll,
      cancleEdit,
      queryStreet,
      searchMaps,
      getAreaList,
      router,
      getStreetList,
      getParkList,
      queryPark,
      handleSearch,
      handleStop,
      handlePageChange,
      handleDelete,
      handleEdit,
      getData,
      handleView,
      handleVideo,
      handleVideoAll,
      onOpenGate,
      onAddTopic,
      saveEdit,
      rEdit,
      multipleSelection: [],
      dialogImageUrl: "../../../../src/assets/img/no-pic.jpg",
      ppVisible: false,
      play,
      handleClose,
      handleCloseAll,
    };
  },
  // data(){
  //   return {
  //     routerAlive:true
  //   }
  // },
  watch: {
    // 利用watch方法检测路由变化：
    $route: function (to, from) {
      // 拿到目标参数 to.query.id 去再次请求数据接口
      if (this.router.currentRoute.value.query.park_id != null) {
        this.getAreaList();
        this.query.area_id = parseInt(this.router.currentRoute.value.query.area_id);
        this.getStreetList("a");
        this.query.street_id = parseInt(this.router.currentRoute.value.query.street_id);
        this.getParkList("a");
        this.query.park_id = parseInt(this.router.currentRoute.value.query.park_id);
      }
      this.getData();
      // console.log(1111);
    },
  },
  methods: {
    //上传图片操作
    handleRemove(file, fileList) {
      console.log(file, fileList);
    },
    // handleClose() {
    //   this.videoVisible = false;
    //   setTimeout(() => {
    //     this.$refs.qyvideoOne.destroy();
    //   }, 500);
    // },
    //图片预览
    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url;
      this.ppVisible = true;
    },
    // routerRefresh(){
    //     this.routerAlive=false;
    //     this.$nextTick(()=>{
    //         this.routerAlive=true;
    //     });
    //   },

    handleSelectionChange(data) {
      this.selectedData = data;
    },
    handleCommand(command) {
      // this.$message("click on item " + command);
    },
    handleDeleteAll() {
      ElMessageBox.confirm("确定要删除吗？", "提示", {
        type: "warning",
      })
        .then(() => {
          var that = this;
          var val = this.selectedData;
          console.log(val);
          var ids = "";
          if (val) {
            val.forEach(function (item, index) {
              //alert(item.id);
              ids = ids + item.id + ",";
            });
            delParkDeviceAll({ deviceIds: ids }).then((res) => {
              ElMessage.success("删除成功");
              that.getData();
            });
          } else {
            ElMessage.warning(`请选择一条记录`);
          }
        })
        .catch(() => {});
    },
    bindPark() {
      var that = this;
      var val = this.selectedData;
      if (val) {
        val.forEach(function (item, index) {
          that.tableData.forEach(function (itemI, indexI) {
            if (item === itemI) {
              //that.tableData.splice(indexI, 1);
            }
          });
        });
        this.viewVisible = true;
        ElMessage.success("绑定成功");
        this.$refs.multipleTable.clearSelection();
      } else {
        ElMessage.warning(`请选择一条记录`);
      }
    },
    exportEquipment() {
      ElMessage.success("正在下载中·····");
      exportEquipmentData(this.query).then((res) => {
        const url = window.URL.createObjectURL(new Blob([res]));
        const link = document.createElement("a");
        link.href = url;
        link.setAttribute("download", "停车场设备管理.xls");
        document.body.appendChild(link);
        link.click();
      });
    },
  },
};
</script>

<style>
.self-modal {
  left: 201px;
  top: 70px;
}
</style>
