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
                  v-model="query.berthNoOrCode"
                  class="handle-input"
                  placeholder="输入查询内容"
              ></el-input>
            </el-form-item>
            <el-form-item label="" class="search-mb0">
              <el-select
                  clearable
                  v-model="query.areaId"
                  filterable
                  size="small"
                  placeholder="所有区域"
                  class="w100"
                  @change="getStreet"
              >
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
                  clearable
                  v-model="query.streetId"
                  filterable
                  size="small"
                  placeholder="所有街道"
                  class="w100"
                  @change="getRoad"
              >
                <el-option
                    v-for="item in form.streets"
                    :key="item.id"
                    :label="item.street_name"
                    :value="item.id"
                ></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="" class="search-mb0">
              <el-select
                  clearable
                  v-model="query.roadId"
                  filterable
                  size="small"
                  placeholder="所有路内"
                  class="w100"
              >
                <el-option
                    v-for="item in form.roads"
                    :key="item.id"
                    :label="item.road_name"
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
            >查询</el-button
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
        <el-table-column type="index" label="序号" width="55" align="center"> </el-table-column>
        <el-table-column
            prop="berth_no"
            align="center"
            width="300"
            label="设备编号">
          <template #default="scope">
            {{ scope.row.berth_no }}
          </template>
        </el-table-column>
        <el-table-column
            prop="device_code"
            align="center"
            width="200"
            label="对应泊位编号">
          <template #default="scope">
            {{ scope.row.device_code }}
          </template>
        </el-table-column>
        <el-table-column
            prop="area_name"
            align="center"
            label="路内">
          <template #default="scope">
            {{ scope.row.road_name }}
          </template>
        </el-table-column>
        <el-table-column
            prop="alarm_type"
            align="center"
            width="180"
            label="报警类型">
          <template #default="scope">
            {{ scope.row.alarm_type }}
          </template>
        </el-table-column>
        <el-table-column label="报警图片" align="center" width="100">
          <template #default="scope">
            <el-image
                class="table-td-thumb"
                hide-on-click-modal="true" preview-teleported="true"
                :src="imgurl(scope.row.file_url)"
                :preview-src-list="[imgurl(scope.row.file_url)]"
            >
            </el-image>
          </template>
        </el-table-column>
        <el-table-column
            prop="alarm_type"
            align="center"
            width="180"
            label="发生时间">
          <template #default="scope">
            {{ scope.row.time_occurrence }}
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
    <el-dialog title="填写拒绝原因" v-model="editVisible" width="35%">
      <el-form label-width="70px" size="small">
        <el-input v-model="form.name" type="textarea" :rows="6"></el-input>
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
  </div>
</template>

<script>
import {reactive, ref} from "vue";
import {ElMessage, ElMessageBox} from "element-plus";
import {queryAreaData, queryDeviceAlarm, queryRoadData, queryStreetData} from "../../api/index";
import Ueditor from "../../components/UE.vue";
import File_URL from "../../file_url";

export default {
  name: "equipmentwarn",
  components: {
    Ueditor,
  },
  data() {
    return {
       tableH:0,
      imgViewUrl: File_URL.file_hx_img_url,//图片回显路径
    };
  },
  setup() {
    const query = reactive({
      berthNoOrCode: "",
      areaId: "",
      streetId: "",
      roadId: "",
      checked: false,
      pageIndex: 1,
      pageSize: 15,
    });
    const tableData = ref([]);
    const pageTotal = ref(0);
    // 获取表格数据
    const getData = () => {
      queryDeviceAlarm(query).then((res) => {
        tableData.value = res.data.list;
        pageTotal.value = res.data.total;
      });
    };
    getData();

    //获取区域下拉框数据
    const queryArea = reactive({});
    const getArea = () => {
      queryAreaData(queryArea).then((res) => {
        form.areas = res.data;
      })
    }
    getArea();
    //获取街道下拉框数据
    let queryStreet = reactive({
      areaId: query.areaId
    });
    const getStreet = () => {
      queryStreet.areaId = query.areaId;
      form.roads =[];
      query.roadId = "";
      query.streetId = "";
      queryStreetData(queryStreet).then((res) => {
        form.streets = res.data;
      })
    }
    //获取路内下拉框数据
    const queryRoad = reactive({
      streetId: query.streetId
    });
    const getRoad = () => {
      queryRoad.streetId = query.streetId;
      query.roadId = "";
      queryRoadData(queryRoad).then((res) => {
        form.roads = res.data;
      })
    }

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
    const editVisible = ref(false);
    let form = reactive({
      areas: [],
      streets: [],
      roads: [],
    });
    let idx = -1;
    const handleEdit = (index, row) => {
      idx = index;
      Object.keys(form).forEach((item) => {
        form[item] = row[item];
      });

      editVisible.value = true;
    };

    const handleView = (index, row) => {
      ElMessageBox.confirm(
        "将解除车牌现有绑定者，重新绑定到申诉用户，确定审核通过吗？",
        "提示",
        {
          type: "warning",
        }
      )
        .then(() => {
          ElMessage.success("解除成功");
          that.tableData.splice(indexI, 1);
        })
        .catch(() => {
          ElMessage.info("取消解除");
        });
    };

    const saveEdit = () => {
      editVisible.value = false;
      ElMessage.success(`修改第 ${idx + 1} 行成功`);
      Object.keys(form).forEach((item) => {
        tableData.value[idx][item] = form[item];
      });
    };

    return {
      getArea,
      getStreet,
      getRoad,
      query,
      tableData,
      pageTotal,
      editVisible,
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
