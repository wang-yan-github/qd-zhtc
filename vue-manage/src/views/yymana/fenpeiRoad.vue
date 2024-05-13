<template>
  <div>
    <div class="container">
      <div class="handle-box">
        <div class="left-panel">
          <el-form inline label-width="80" class="lineH0">
            <el-form-item label="" class="search-mb0">
              <el-input
                @keyup.enter="handleSearch()"
                size="small"
                v-model="query.roadName"
                class="handle-input"
                placeholder="请输入路内/停车场名"
              ></el-input>
            </el-form-item>
            <el-form-item label="" class="search-mb0">
              <el-select
                v-model="query.areaId"
                filterable
                size="small"
                placeholder="所有区域"
                class="w100"
                @change="getStreet"
              >
                <el-option value="">全部</el-option>
                <el-option
                  v-for="item in form.areas"
                  :key="item.id"
                  :label="item.area_name"
                  :value="item.id"
                ></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="" class="search-mb0">
              <el-select
                v-model="query.streetId"
                filterable
                size="small"
                placeholder="所有街道"
                class="w100"
                @change="getRoad"
              >
                <el-option value="">全部</el-option>
                <el-option
                  v-for="item in form.streets"
                  :key="item.id"
                  :label="item.street_name"
                  :value="item.id"
                ></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="" class="search-mb0">
              <el-button
                size="small"
                type="primary"
                icon="el-icon-search"
                @click="handleSearch"
                >查询
              </el-button>
            </el-form-item>
            <el-form-item label="" class="search-mb0">
              <el-button
                v-if="parking_type == 0"
                type="success"
                size="small"
                icon="el-icon-s-operation"
                @click="bindRoad('')"
                v-permission="'road_fenpeiroad_fpxjy'"
                >分配巡检员
              </el-button>
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
        @row-click="handleRowClick"
        :row-key="getRowKeys"
        @selection-change="handleSelectionChange"
      >
        <el-table-column
          v-if="parking_type == 0"
          type="selection"
          width="55"
          :reserve-selection="true"
          align="center"
        ></el-table-column>
        <el-table-column label="序号" width="55" align="center">
          <template #default="scope">
            {{ scope.$index + 1 }}
          </template>
        </el-table-column>
        <el-table-column prop="name" label="所属街道" align="center">
          <template #default="scope">
            {{ scope.row.street_name }}
          </template>
        </el-table-column>
        <el-table-column
          v-if="parking_type == 0"
          prop="name"
          label="路内名称"
          align="center"
        >
          <template #default="scope">
            {{ scope.row.road_name }}
          </template>
        </el-table-column>
        <el-table-column
          v-if="parking_type == 1"
          prop="name"
          label="停车场名称"
          align="center"
        >
          <template #default="scope">
            {{ scope.row.park_name }}
          </template>
        </el-table-column>
        <el-table-column
          v-if="parking_type == 0"
          label="泊位数量"
          align="center"
          width="100"
        >
          <template #default="scope">
            {{ scope.row.berth_num }}
          </template>
        </el-table-column>
        <el-table-column
          v-if="parking_type == 1"
          label="泊位总数"
          align="center"
          width="100"
        >
          <template #default="scope">
            {{ scope.row.park_num }}
          </template>
        </el-table-column>
        <el-table-column label="分配人数" align="center" width="100">
          <template #default="scope">
            {{ scope.row.inspectManageListSize }}
          </template>
        </el-table-column>
        <el-table-column
          label="负责巡检员"
          prop="name"
          align="center"
          v-if="parking_type == 0"
        >
          <template #default="scope">
            <el-tag
              effect="dark"
              size="small"
              v-for="(item, index) in scope.row.inspectManageList"
              :key="index"
              class="mar5 mb5"
              >{{ item }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          label="负责收费员"
          prop="name"
          align="center"
          v-if="parking_type == 1"
        >
          <template #default="scope">
            <el-tag
              effect="dark"
              size="small"
              v-for="(item, index) in scope.row.inspectManageList"
              :key="index"
              class="mar5 mb5"
              >{{ item }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" align="center">
          <template #default="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-s-operation"
              @click="handleEdit(scope.$index, scope.row)"
              v-permission="['road_fenpeiroad_jc','park_fenpeipark_jc']"
              >解除
            </el-button>
            <el-button
              size="mini"
              type="text"
              v-if="parking_type == 1"
              icon="el-icon-s-operation"
              @click="bindRoad(scope.row)"
              v-permission="'park_fenpeipark_fpsfy'"
              >分配收费员
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

    <!-- 编辑弹出框 -->
    <el-dialog title="解绑" v-model="editVisible" width="40%">
      <el-table
        :data="tableInspectorData"
        border
        class="table"
        ref="multipleEditTable"
        header-cell-class-name="table-header"
        @selection-change="handleEditSelectionChange"
        @row-click="handleRowEditClick"
      >
        <el-table-column
          type="selection"
          width="55"
          align="center"
        ></el-table-column>
        <el-table-column type="index" label="序号" width="55" align="center">
          <!--<template #default="scope">-->
            <!--{{ (query.pageIndex - 1) * query.pageSize + scope.$index + 1 }}-->
          <!--</template>-->
        </el-table-column>
        <el-table-column prop="name" label="姓名" width="130">
          <template #default="scope">
            {{ scope.row.name }}
          </template>
        </el-table-column>
        <el-table-column prop="phone" label="手机号" width="130">
          <template #default="scope">
            {{ scope.row.phone }}
          </template>
        </el-table-column>

        <el-table-column label="时间" align="center">
          <template #default="scope">
            {{ scope.row.create_time }}
          </template>
        </el-table-column>
      </el-table>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="editVisible = false">取 消</el-button>
          <el-button type="primary" @click="handDealRelieve">解 绑</el-button>
        </span>
      </template>
    </el-dialog>

    <el-dialog v-model="ppVisible">
      <img width="100%" :src="dialogImageUrl" alt="" />
    </el-dialog>

    <el-dialog title="分配" v-model="viewVisible" width="538px">
      <div class="handle-title mgb20">
        <i class="el-icon-location"></i>{{ form.formTitle }}
      </div>
      <el-form label-width="70px" size="small">
        <el-form-item label="指定时间">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-time-picker
                placeholder="开时时分"
                class="w"
                format="HH:mm"
                value-format="HH:mm"
                v-model="form.startTime"
              >
              </el-time-picker>
            </el-col>
            <el-col :span="12">
              <el-time-picker
                placeholder="结束时分"
                class="w"
                format="HH:mm"
                value-format="HH:mm"
                v-model="form.endTime"
              >
              </el-time-picker>
            </el-col>
          </el-row>
        </el-form-item>
        <el-form-item label="指定巡检">
          <el-row :gutter="20">
            <el-col :span="24">
              <el-select
                v-model="formDealSectionInspector.inspectId"
                filterable
                size="small"
                placeholder="所有巡检员"
                class="w"
              >
                <el-option
                  v-for="item in form.inspectManages"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                ></el-option>
              </el-select>
            </el-col>
          </el-row>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="viewVisible = false">取 消</el-button>
          <el-button type="primary" @click="saveDealSectionInspector"
            >确 定</el-button
          >
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { reactive, ref } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import {
  dealInspectorList,
  dealRelieve,
  dealSectionInspector,
  listInspector,
  queryAllocatedSection,
  queryAreaData,
  queryRoadData,
  queryStreetData,
} from "../../api/index";
import Ueditor from "../../components/UE.vue";

export default {
  name: "equipmentwarn",
  data() {
    return {
      tableH: 0,
    };
  },
  components: {
    Ueditor,
  },
  setup() {
    const query = reactive({
      roadName: "",
      areaId: "",
      streetId: "",
      roadId: "",
      checked: false,
      pageIndex: 1,
      pageSize: 15,
    });
    const tableData = ref([]);
    const tableInspectorData = ref([]);
    const pageTotal = ref(0);
    const parking_type = ref(0);
    // 获取表格数据
    const getData = () => {
      queryAllocatedSection(query).then((res) => {
        tableData.value = res.data.page.list;
        pageTotal.value = res.data.page.total;
        parking_type.value = res.data.parking_type;
      });
    };
    getData();

    //巡检员列表
    const getInspectorData = (id) => {
      listInspector({ id: id }).then((res) => {
        tableInspectorData.value = res.data;
      });
    };

    //获取巡检员下拉框数据
    const getInspectManageSelectList = () => {
      dealInspectorList().then((res) => {
        form.inspectManages = res.data;
      });
    };
    getInspectManageSelectList();
    //获取区域下拉框数据
    const queryArea = reactive({});
    const getArea = () => {
      queryAreaData(queryArea).then((res) => {
        form.areas = res.data;
      });
    };
    getArea();
    //获取街道下拉框数据
    let queryStreet = reactive({
      areaId: query.areaId,
    });
    const getStreet = () => {
      queryStreet.areaId = query.areaId;
      query.roadId = "";
      query.streetId = "";
      queryStreetData(queryStreet).then((res) => {
        form.streets = res.data;
      });
    };
    //获取路内下拉框数据
    const queryRoad = reactive({
      streetId: query.streetId,
    });
    const getRoad = () => {
      queryRoad.streetId = query.streetId;
      queryRoadData(queryRoad).then((res) => {
        form.roads = res.data;
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
    const dialogT = "新增";

    // 表格编辑时弹窗和保存
    const viewVisible = ref(false);
    const editVisible = ref(false);
    const saveDealSectionInspector = () => {
      formDealSectionInspector.startTime = form.startTime;
      formDealSectionInspector.endTime = form.endTime;
      if (
        formDealSectionInspector.startTime &&
        formDealSectionInspector.endTime &&
        formDealSectionInspector.inspectId
      ) {
        dealSectionInspector(formDealSectionInspector).then((res) => {
          form.startTime = "";
          form.endTime = "";
          formDealSectionInspector.inspectId = "";
          viewVisible.value = false;
          ElMessage.success("保存成功");
          getData();
        });
      } else {
        ElMessage.error("请指定时间和指定巡检员");
      }
    };
    const saveDdealRelieve = () => {
      dealRelieve(formDealRelieve).then((res) => {
        form.startTime = "";
        form.endTime = "";
        formDealSectionInspector.inspectId = "";
        editVisible.value = false;
        ElMessage.success("解除成功");
        getData();
      });
    };

    let form = reactive({
      areas: [],
      streets: [],
      roads: [],
      inspectManages: [],
      formTitle: "",
      startTime: "",
      endTime: "",
    });
    let formDealSectionInspector = reactive({
      allocatedSectionIds: "",
      allocatedSectionId: "",
      inspectId: "",
      startTime: "",
      endTime: "",
    });
    let formDealRelieve = reactive({
      allocatedSectionId: "",
      inspectIds: "",
    });

    const dataFormat = (time) => {
      var dateTime = time;
      var dstr = dateTime + " ";
      if (dstr.search("GMT") != -1) {
        var Year = dateTime.getFullYear();
        var month =
          dateTime.getMonth() + 1 >= 10
            ? dateTime.getMonth() + 1
            : "0" + (dateTime.getMonth() + 1);
        var day =
          dateTime.getDate() >= 10
            ? dateTime.getDate()
            : "0" + dateTime.getDate();
        var hours =
          dateTime.getHours() < 10
            ? "0" + dateTime.getHours()
            : dateTime.getHours();
        var minutes =
          dateTime.getMinutes() < 10
            ? "0" + dateTime.getMinutes()
            : dateTime.getMinutes();
        var seconds =
          dateTime.getSeconds() < 10
            ? "0" + dateTime.getSeconds()
            : dateTime.getSeconds();
        return hours + ":" + minutes;
      }
    };

    return {
      formDealSectionInspector,
      query,
      tableData,
      tableInspectorData,
      pageTotal,
      parking_type,
      viewVisible,
      editVisible,
      form,
      formDealRelieve,
      dialogT,
      getData,
      getArea,
      getInspectManageSelectList,
      getInspectorData,
      getStreet,
      getRoad,
      saveDealSectionInspector,
      saveDdealRelieve,
      handleSearch,
      handlePageChange,
      handleDelete,
    };
  },
  created() {
    let h = document.documentElement.clientHeight || document.body.clientHeight;
    this.tableH = h - 308 + "px";
  },
  methods: {
    handleRowClick(row, column, event) {
      //给操作列设置不触发选中
      //如果有编辑删除按钮使用了模板函数，可以根据传递的column参数,判断里边的
      //label值，来判断是否触发选中
      if (column && column.label == "操作") {
        return;
      }
      this.$refs.multipleTable.toggleRowSelection(row);
    },
    handleSelectionChange(data) {
      this.selectedData = data;
    },
    getRowKeys(row) {
      // id 是后台传递的每行信息唯一标识
      return row.id;
    },
    handleRowEditClick(row) {
      this.$refs.multipleEditTable.toggleRowSelection(row);
    },
    handleEditSelectionChange(data) {
      this.editSelectedData = data;
    },
    handleEdit(index, row) {
      this.formDealSectionInspector.allocatedSectionId = row.id;
      this.getInspectorData(row.id);
      this.editVisible = true;
    },
    handDealRelieve() {
      var val = this.editSelectedData;
      if (val != undefined && val.length != 0) {
        var inspectIds = "";
        val.forEach(function (item, index) {
          inspectIds += item.id + ",";
        });
        this.formDealRelieve.allocatedSectionId =
          this.formDealSectionInspector.allocatedSectionId;
        this.formDealRelieve.inspectIds = inspectIds;
        this.saveDdealRelieve();
      } else {
        ElMessage.error(`请选择一条记录`);
      }
    },
    bindRoad(row) {
      var that = this;
      var idx = 0;
      var street_name = "";
      var road_name = "";
      var park_name = "";
      var val;
      if (that.parking_type == 0) {
        val = that.selectedData;
        if (val == undefined || val.length == 0) {
          ElMessage.error(`请选择一条记录`);
          return;
        }
        that.formDealSectionInspector.allocatedSectionIds = "";
        val.forEach(function (item, index) {
          idx = index;
          street_name = item.street_name;
          road_name = item.road_name;
          park_name = item.park_name;
          that.formDealSectionInspector.allocatedSectionIds += item.id + ",";
        });
        if (idx == 0) {
          that.form.formTitle = street_name + " > " + road_name;
        } else {
          that.form.formTitle = "选择了" + (idx + 1) + "个路内";
        }
      } else if (that.parking_type == 1) {
        val = row;
        that.formDealSectionInspector.allocatedSectionIds = "";
        street_name = row.street_name;
        road_name = row.road_name;
        park_name = row.park_name;
        that.formDealSectionInspector.allocatedSectionIds += row.id;
        that.form.formTitle = street_name + " > " + park_name;
      }
      that.viewVisible = true;
      that.getInspectManageSelectList();
    },
  },
};
</script>
