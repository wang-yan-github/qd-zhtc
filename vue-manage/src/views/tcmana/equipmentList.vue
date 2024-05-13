<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
  <div>
    <div class="container">
      <div class="handle-box">
        <div class="left-panel">
          <el-button
            type="primary"
            size="small"
            icon="el-icon-plus"
            @click="handleEdit(0, null, true)"
            v-permission="'road_equipmentlist_add'"
            >添加
          </el-button>
          <el-button
            type="danger"
            size="small"
            icon="el-icon-delete"
            @click="handleDeleteAll()"
            v-permission="'road_equipmentlist_deleteAll'"
            >批量删除
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
              v-model="query.berth_code"
              placeholder="请输入泊位编号"
              class="handle-input mr10"
            ></el-input>
            <el-select
                    v-model="query.device_type"
                    filterable
                    size="small"
                    clearable
                    placeholder="设备类型"
                    class="w100"
            >
              <el-option
                  v-for="(item, i) in result.device_types"
                  :key="i"
                  :label="item.label"
                  :value="item.dc_value"
              ></el-option>
            </el-select>
            <span class="dispinline ml5"></span>
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
              @change="getRoadList('a')"
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
              v-model="query.road_id"
              filterable
              size="small"
              clearable
              placeholder="所有路内"
              class="w100"
            >
              <el-option value="">全部</el-option>
              <el-option
                v-for="(item, i) in result.query_road_list"
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
              >查询
            </el-button>
            <el-button size="small"  icon="el-icon-upload2" type="success" @click="exportEquipmentRoad" v-permission="'road_equipmentlist_excel'"
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
        <el-table-column
          type="selection"
          width="55"
          align="center"
        ></el-table-column>
        <el-table-column type="index" label="序号" width="55" align="center">
        </el-table-column>
        <!-- <el-table-column
            prop="device_type"
            label="设备类型1"
            width="100"
            align="center"
        >
          <template #default="scope">
            <p v-if="scope.row.device_type==1">视频杆</p>
            <p v-if="scope.row.device_type==2">电池视频杆</p>
            <p v-if="scope.row.device_type==3">地磁</p>
            <p v-if="scope.row.device_type==4">人工泊位</p>
            <p v-if="scope.row.device_type==5">高位视频</p>
          </template>
        </el-table-column> -->
        <el-table-column
          prop="device_type_name"
          label="设备类型"
          align="center"
          width="110"
        ></el-table-column>
        <el-table-column
          prop="device_code"
          label="设备编号"
          align="center"
          width="130"
        ></el-table-column>
        <el-table-column
          prop="berth_code"
          label="泊位编号"
          align="center"
          width="130"
        ></el-table-column>
        <el-table-column
          prop="street_name"
          label="街道"
          min-width="200"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="road_name"
          label="路内"
          min-width="200"
          align="center"
        ></el-table-column>
        <!--<el-table-column prop="longitude,latitude" label="坐标" width="180" align="center">-->
          <!--<template #default="scope">-->
            <!--{{ scope.row.latitude }},{{ scope.row.longitude }}-->
          <!--</template>-->
        <!--</el-table-column>-->
        <el-table-column prop="status" label="设备在线状态" width="130" align="center">
          <template #default="scope">
            <p v-if="scope.row.status == '1'" style="color: #67c23a">空闲</p>
            <p v-if="scope.row.status == '2'" style="color: #656d82">使用中</p>
            <p v-if="scope.row.status == '3'" style="color: #f56c6c">异常</p>
          </template>
        </el-table-column>

        <el-table-column label="状态" width="80" align="center">
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
              v-if="scope.row.is_use == '1'"
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
          <!--align="center"-->
          <!--width="150"-->
        <!--&gt;</el-table-column>-->
        <el-table-column
          prop="create_time"
          label="添加时间"
          align="center"
          width="170"
        ></el-table-column>

        <el-table-column label="操作" fixed="right" width="200" align="center">
          <template #default="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleEdit(scope.$index, scope.row, false)"
              v-permission="'road_equipmentlist_edit'"
              >编辑
            </el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-circle-close"
              @click="handleStop(scope.$index, scope.row, '0')"
              class="red"
              v-if="scope.row.is_use == '1'"
              v-permission="'road_equipmentlist_status'"
              >停用
            </el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-circle-check"
              @click="handleStop(scope.$index, scope.row, '1')"
              v-if="scope.row.is_use == '0'"
              v-permission="'road_equipmentlist_status'"
              >启用
            </el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              class="red"
              @click="handleDelete(scope.$index, scope.row)"
              v-permission="'road_equipmentlist_delete'"
              >删除
            </el-button>

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
      <el-form label-width="90px" size="small" :rules="formRules" :model="form" ref="formId">
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item
              label="设备编号"
              v-if="result.addOrEdit"
              prop="device_code"
            >
              <el-input
                v-model="form.device_code"
                type="textarea"
                :rows="5"
              ></el-input>
              <p class="color999 lh20">
                操作提示：各个设备编号请用英文逗号“,”间隔
              </p>
            </el-form-item>
            <el-form-item label="设备编号" prop="device_code" v-else>
              <el-input v-model="form.device_code"></el-input>
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
          <el-col :span="24" v-if="form.device_type == 5">
            <el-form-item label="设备型号" prop="device_model">
                <el-input
                  size="small"
                  v-model="form.device_model"
                  placeholder="请输入设备型号"
                ></el-input>
              </el-form-item>
          </el-col>
          <el-col :span="24" v-if="form.device_type == 5">
            <el-form-item label="泊位配置" prop="berth_number">
              <div class="berth-list">
                <div class="berth-item">
                  <div v-for="(formItem, index) in form.berth_number" class="berth-con" :key="index">
                    <el-input required placeholder="泊位编号" v-model="formItem.berth_code">
                      <template v-if="index != 0" v-slot:append>
                        <el-button type="danger" icon="el-icon-delete" @click="removeFormItem(index)"></el-button>
                      </template>
                    </el-input>
                  </div>
                </div>
                <el-button type="primary" size="small" @click="addFormItem" style="height: 32px;">添加</el-button>
              </div>

            </el-form-item>
          </el-col>

          <el-col :span="8">
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
              <!-- <p class="color999 lh20">操作提示：如没有您要的区域，请先添加区域！</p> -->
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="选择街道" prop="street_id">
              <el-select
                v-model="form.street_id"
                filterable
                size="small"
                placeholder="所有街道"
                class="w"
                @change="getRoadList('b')"
              >
                <el-option
                  v-for="(item, i) in result.street_list"
                  :key="i"
                  :label="item.street_name"
                  :value="item.id"
                ></el-option>
              </el-select>
              <!-- <p class="color999 lh20">操作提示：如没有您要的街道，请先添加街道！</p> -->
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="指定路内" prop="road_id">
              <el-select
                v-model="form.road_id"
                filterable
                size="small"
                placeholder="所有路内"
                class="w"
              >
                <el-option
                  v-for="(item, i) in result.road_list"
                  :key="i"
                  :label="item.road_name"
                  :value="item.id"
                ></el-option>
              </el-select>
              <!-- <p class="color999 lh20">操作提示：如没有您要的路内，请先添加路内！</p> -->
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="坐标范围" prop="coordinate">
              <el-input
                v-model="form.coordinate"
                placeholder=""
                disabled
              ></el-input>
              <p class="color999 lh20">
                操作提示：http://lbs.qq.com/tool/getpoint/index.html，以腾讯位置服务坐标拾取为准。
              </p>
            </el-form-item>
          </el-col>
          <!-- <el-col :span="12">
              <el-form-item>
                  <el-input v-model="form.name"
                  ><template v-slot:append
                  ><el-button>查询</el-button></template
                  ></el-input
                  >
              </el-form-item>
          </el-col> -->
          <el-col :span="24">
            <el-form-item label="路内查询">
              <el-input v-model="form.searchkey"
                ><template v-slot:append
                  ><el-button @click="searchMaps()">查询</el-button></template
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

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="cancleEdit">取 消</el-button>
          <el-button type="primary" @click="saveEdit()">确 定</el-button>
        </span>
      </template>
    </el-dialog>
    <el-dialog v-model="ppVisible">
      <img width="100%" :src="dialogImageUrl" alt="" />
    </el-dialog>
    <!-- 详情弹出框 -->
    <el-dialog title="绑定路内" v-model="viewVisible" width="40%">
      <el-form label-width="70px">
        <el-form-item label="设备类型">
          <el-select
            v-model="form.selvalue"
            filterable
            size="small"
            placeholder="所有区域"
            class="w"
          >
            <el-option
              v-for="item in form.czroptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="选择区域">
          <el-select
            v-model="form.selvalue"
            filterable
            size="small"
            placeholder="所有区域"
            class="w"
          >
            <el-option
              v-for="item in form.czroptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            ></el-option>
          </el-select>
          <span class="color999"
            >操作提示：如没有您要的区域，请先添加区域！</span
          >
        </el-form-item>
        <el-form-item label="选择街道">
          <el-select
            v-model="form.selvalue"
            filterable
            size="small"
            placeholder="所有区域"
            class="w"
          >
            <el-option
              v-for="item in form.czroptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            ></el-option>
          </el-select>
          <span class="color999"
            >操作提示：如没有您要的街道，请先添加街道！</span
          >
        </el-form-item>

        <el-form-item label="指定路内">
          <el-select
            v-model="form.selvalue"
            filterable
            size="small"
            placeholder="所有区域"
            class="w"
          >
            <el-option
              v-for="item in form.czroptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            ></el-option>
          </el-select>
          <span class="color999"
            >操作提示：如没有您要的路内，请先添加路内！</span
          >
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
import { ref, nextTick, reactive } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import {
  deviceList,
  addDevice,
  delDeviceAll,
  queryAreaData,
  queryStreetData,
  queryRoadData,
  dictData,
  editDevice,
  exportEquipmentRoadData,
  getDevice,
  delDevice,
} from "../../api/index";
import { useRouter } from "vue-router";

export default {
  name: "equipmentlist",
  components: {},
  data() {
    return {
      formRules: {
        device_code: [{ required: true, message: "必填项", trigger: "blur" }],
        device_type: [{ required: true, message: "必填项", trigger: "change" }],
        area_id: [{ required: true, message: "必填项", trigger: "change" }],
        street_id: [{ required: true, message: "必填项", trigger: "change" }],
        road_id: [{ required: true, message: "必填项", trigger: "change" }],
        coordinate: [{ required: true, message: "必填项", trigger: "blur" }],
        device_model: [{ required: true, message: "必填项", trigger: "blur" }],
        berth_number: [{ required: true, message: "必填项", trigger: "blur" }],
      },
    };
  },
  setup() {
    const router = useRouter();
    const formId = ref(null);
    const query = reactive({
      device_code: "",
      berth_code: "",
      device_type: "",
      status: "",
      area_id: "",
      street_id: "",
      road_id: "",
      pageIndex: 1,
      pageSize: 15,
    });

    const form = reactive({
      device_code: "",
      device_type: "",
      area_id: "",
      street_id: "",

      road_id: "",
      coordinate: "",
      longitude: "",
      latitude: "",
      id: "",
      searchkey: "",
      // 新增泊位配置
      device_model: "",
      berth_number: [
        {
          id: "",
          berth_code: "",
        },
      ], // 表单列表
    });

    let result = reactive({
      area_list: [],
      street_list: [],
      query_street_list: [],
      road_list: [],
      query_road_list: [],
      device_types: [],
      addOrEdit: true,
      dialogT: "",
    });
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
      result.road_list = [];
      result.query_road_list = [];
      if (type == "a") {
        queryStreet.areaId = query.area_id;
        query.road_id = "";
        query.street_id = "";
      } else if (type == "b") {
        queryStreet.areaId = form.area_id;
        form.road_id = "";
        form.street_id = "";
      } else {
        queryStreet.areaId = form.area_id;
      }

      //console.log(1111);

      queryStreetData(queryStreet).then((res) => {
        // console.log(res);
        if (type == "a") {
          result.query_street_list = res.data;
        } else {
          result.street_list = res.data;
        }

        //console.log(res.data);
      });
    };

    //获取路内下拉框数据
    const queryRoad = reactive({
      streetId: query.street_id,
    });
    const getRoadList = (type) => {
      if (type == "a") {
        queryRoad.streetId = query.street_id;
        query.road_id = "";
        // result.query_road_list=[];
      } else if (type == "b") {
        // result.road_list=[];
        queryRoad.streetId = form.street_id;
        form.road_id = "";
      } else {
        queryRoad.streetId = form.street_id;
      }

      queryRoadData(queryRoad).then((res) => {
        console.log(res.data);
        if (type == "a") {
          result.query_road_list = res.data;
        } else {
          result.road_list = res.data;
        }
      });
    };

    // 查询区域等级
    let dict = reactive({
      is_del: "0",
      dict_type: "deviceType",
    });
    const getDeviceType = () => {
      dictData(dict).then((res) => {
        console.log(res);
        result.device_types = res.data;
      });
    };
    getDeviceType();

    if (router.currentRoute.value.query.road_id != null) {
      query.area_id = parseInt(router.currentRoute.value.query.area_id);
      getStreetList("a");
      query.street_id = parseInt(router.currentRoute.value.query.street_id);
      getRoadList("a");
      query.road_id = parseInt(router.currentRoute.value.query.road_id);
    }

    const tableData = ref([]);
    const pageTotal = ref(0);
    // const router = useRouter();

    // 获取表格数据
    const getData = () => {
      console.log(query);
      deviceList(query).then((res) => {
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
          delDevice(edit_device)
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
          delDevice(edit_device)
            .then((res) => {
              console.log(res);
              ElMessage.success("删除成功");
            })
            .then(() => {
              getData();
            });
        })
        .catch(() => {});
    };

    // 表格编辑时弹窗和保存
    const editVisible = ref(false);
    const viewVisible = ref(false);
    const ppVisible = ref(false);

    let idx = "";
    var timer = "";
    const handleEdit = (index, row, type) => {
      result.street_list = [];
      result.road_list = [];
      getAreaList();
      // getStreetList();
      // getRoadList();
      // getDeviceType();
      if (type) {
        result.dialogT = "新增";
        result.addOrEdit = true;
        idx = "";
        form.device_code = "";

        form.device_type = "";
        form.area_id = "";
        form.street_id = "";
        form.road_id = "";
        form.longitude = "";
        form.coordinate = "";
        form.latitude = "";
        form.id = "";
        // 新增泊位配置
        form.device_model = "";
        form.berth_number = [
          {
            id: "",
            berth_code: "",
          },
        ];
      } else {
        result.addOrEdit = false;
        result.dialogT = "编辑";
        idx = row.id;
        // Object.keys(form).forEach((item) => {
        //   form[item] = row[item];
        // });
        getDevice(reactive({id: row.id})).then((res) => {
          // console.log(res)
          // form.value = res.data;
          // form.value.coordinate = form.value.latitude + "," + form.value.longitude;
          // console.log(form.value)
          Object.keys(form).forEach((item) => {
            form[item] = res.data[item];
          });
          form.coordinate = form.latitude + "," + form.longitude;
          if (form.berth_number.length == 0) {
            form.berth_number = [
              {
                id: "",
                berth_code: "",
              },
            ];
          }
        });
        getStreetList("c");
        getRoadList("c");
      }

      nextTick(() => {
        formId.value.clearValidate();
      });

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

      //router.push({ path: "/equipmentedit", query: { setid: 123456 } });
    };

    const handleView = (index, row) => {
      idx = index;
      Object.keys(form).forEach((item) => {
        form[item] = row[item];
      });
      viewVisible.value = true;
    };

    const saveEdit = () => {
      formId.value.validate((valid) => {
        if (valid) {
          form.longitude = form.coordinate.split(",")[1];
          form.latitude = form.coordinate.split(",")[0];
          if (!idx) {
            // var arry = form.device_code.split("\n").filter(str=>{return !!str});

            addDevice(reactive({ body: JSON.stringify(form) }))
                    .then((res) => {
                      if (res.code === 0) {
                        ElMessage.success(res.msg);
                        editVisible.value = false;
                      } else {
                        ElMessage.error(res.msg);
                      }
                    })
                    .then(() => {
                      query.pageIndex = 1;
                      getData();
                    });
          } else {
            editDevice(reactive({ body: JSON.stringify(form) }))
                    .then((res) => {
                      if (res.code === 0) {
                        ElMessage.success(res.msg);
                        editVisible.value = false;
                      } else {
                        ElMessage.error(res.msg);
                      }
                    })
                    .then(() => {
                      getData();
                    });
          }

        }
      });
      // if (
      //   form.device_code == null ||
      //   form.device_code == "" ||
      //   form.device_type == null ||
      //   form.device_type == "" ||
      //   form.area_id == null ||
      //   form.area_id == "" ||
      //   form.street_id == null ||
      //   form.street_id == "" ||
      //   form.road_id == null ||
      //   form.road_id == "" ||
      //   form.coordinate == null ||
      //   form.coordinate == ""
      // ) {
      //   ElMessage.success("参数不可为空！");
      //   return false;
      // }

      // editVisible.value = false;
    };

    //泊位配置 新增
    const addFormItem = () => {
      form.berth_number.push({ id: "", berth_code: ""});
    };
    //泊位配置 删除
    const removeFormItem = (index) =>{
      form.berth_number.splice(index, 1);
    };



    return {
      query,
      result,
      tableData,
      pageTotal,
      editVisible,
      viewVisible,
      form,
      cancleEdit,
      map,
      queryStreet,
      getStreetList,
      getAreaList,
      getRoadList,
      router,
      searchMaps,
      queryRoad,
      handleSearch,
      handleStop,
      handlePageChange,
      handleDelete,
      handleEdit,
      getData,
      handleView,
      saveEdit,
      formId,
      rEdit,
      multipleSelection: [],
      dialogImageUrl: "",
      ppVisible: false,
      addFormItem,
      removeFormItem,
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
      if (this.router.currentRoute.value.query.road_id != null) {
        this.getAreaList();
        this.query.area_id = parseInt(
          this.router.currentRoute.value.query.area_id
        );
        this.getStreetList("a");
        this.query.street_id = parseInt(
          this.router.currentRoute.value.query.street_id
        );
        this.getRoadList("a");
        this.query.road_id = parseInt(
          this.router.currentRoute.value.query.road_id
        );
      }
      this.getData();
      console.log(1111);
    },
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
      //this.$message("click on item " + command);
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
            delDeviceAll({ deviceIds: ids }).then((res) => {
              ElMessage.success("删除成功");
              that.getData();
            });
          } else {
            ElMessage.warning(`请选择一条记录`);
          }
        })
        .catch(() => {});
    },
    bindRoad() {
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
    exportEquipmentRoad() {
      ElMessage.success("正在下载中·····");
      exportEquipmentRoadData(this.query).then((res) => {
        const url = window.URL.createObjectURL(new Blob([res]));
        const link = document.createElement("a");
        link.href = url;
        link.setAttribute("download", "路内设备管理.xls");
        document.body.appendChild(link);
        link.click();
      });
    },
    // initMap() {
    //   var that = this;
    //   const timer = setInterval(() => {
    //     if (document.getElementById("has-map")) {
    //       const center = new window.TMap.LatLng(34.25639, 117.1996);
    //       const markersArray = [];
    //       console.log(document.getElementById("has-map"));
    //       // 初始化地图
    //       const map = new window.TMap.Map(document.getElementById("has-map"), {
    //         rotation: 0, // 设置地图旋转角度
    //         pitch: 0, // 设置俯仰角度（0~45）
    //         zoom: 16, // 设置地图缩放级别
    //         center: center, // 设置地图中心点坐标
    //       });
    //       // 创建信息窗口
    //       const info = new window.TMap.InfoWindow({
    //         map,
    //         position: map.getCenter(),
    //         offset: {x: 0, y: -32},
    //       });
    //       info.close();

    //       //点击地图事件
    //       map.on("click", (evt) => {
    //         //console.log(evt)
    //         info.open();
    //         info.setPosition(evt.latLng);
    //         var lat = evt.latLng.getLat().toFixed(6);
    //         var lng = evt.latLng.getLng().toFixed(6);
    //         info.setContent("当前坐标：" + lat + "," + lng);
    //         that.form.coordinate = lat + "," + lng;
    //       });
    //       clearInterval(timer);
    //     }
    //   }, 500);
    // },
  },
  // mounted() {
  //   this.initMap();
  // },
};
</script>
<style scoped>
.berth-list{
  display: flex;
}
.berth-item{
  width: 246px;
  margin-right: 14px;
}
.berth-item .berth-con{
  margin-bottom: 10px;
}

</style>
